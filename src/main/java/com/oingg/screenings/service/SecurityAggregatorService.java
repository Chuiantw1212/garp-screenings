package com.oingg.screenings.service;

import com.oingg.screenings.dto.PegyViewOutDTO;
import com.oingg.screenings.dto.openbb.OpenBBGrowthInDTO;
import com.oingg.screenings.dto.openbb.OpenBBMetricsInDTO;
import com.oingg.screenings.dto.openbb.OpenBBQuoteInDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class SecurityAggregatorService {

    private final QuoteService quoteService;
    private final MetricsService metricsService;
    private final GrowthService growthService;

    /**
     * 透過建構子注入所有專業化的子服務。
     */
    public SecurityAggregatorService(QuoteService quoteService, MetricsService metricsService, GrowthService growthService) {
        this.quoteService = quoteService;
        this.metricsService = metricsService;
        this.growthService = growthService;
    }

    /**
     * 獲取完整的 PEGY 視圖。
     * 這個方法會協調多個子服務，並行地獲取資料，然後將結果組合成一個 PegyViewOutDTO。
     */
    public Mono<PegyViewOutDTO> getPegyView(String symbol) {
        // 1. 並行呼叫三個專業化子服務
        Mono<OpenBBQuoteInDTO> quoteMono = quoteService.fetchQuote(symbol);
        Mono<OpenBBMetricsInDTO> metricsMono = metricsService.fetchMetrics(symbol);
        Mono<OpenBBGrowthInDTO> growthMono = growthService.fetchGrowth(symbol);

        // 2. 使用 Mono.zip 等待所有結果
        return Mono.zip(quoteMono, metricsMono, growthMono)
            .map(tuple -> {
                OpenBBQuoteInDTO quote = tuple.getT1();
                OpenBBMetricsInDTO metrics = tuple.getT2();
                OpenBBGrowthInDTO growth = tuple.getT3();

                // 3. 組合 DTO 並執行最終計算
                return buildPegyView(symbol, quote, metrics, growth);
            });
    }

    /**
     * 根據從各個服務獲取的資料，建立 PegyViewOutDTO 物件並執行計算。
     */
    private PegyViewOutDTO buildPegyView(String symbol, OpenBBQuoteInDTO quote, OpenBBMetricsInDTO metrics, OpenBBGrowthInDTO growth) {
        // 從 DTO 中提取所需欄位
        BigDecimal lastPrice = BigDecimal.valueOf(quote.getLastPrice());
        BigDecimal change = BigDecimal.valueOf(quote.getOpen() - quote.getPrevClose());

        BigDecimal peRatio = metrics.peRatio();
        // Dividend Yield (D) - OpenBB 回傳的是百分比，我們要轉成小數
        BigDecimal dividendYield = metrics.dividendYield().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

        // Consensus Growth Estimate (G) - 同樣轉成小數
        BigDecimal growthEstimate = growth.growthEstimate().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

        // 計算 PEGY
        BigDecimal growthPlusYield = dividendYield.add(growthEstimate);
        BigDecimal pegyRatio = BigDecimal.ZERO;
        // 避免除以零的錯誤
        if (growthPlusYield.compareTo(BigDecimal.ZERO) != 0) {
            pegyRatio = peRatio.divide(growthPlusYield, 2, RoundingMode.HALF_UP);
        }

        // 組合成 PegyViewOutDTO 物件
        return new PegyViewOutDTO(
            symbol,
            lastPrice,
            change,
            peRatio,
            dividendYield,
            growthEstimate,
            pegyRatio
        );
    }
}
