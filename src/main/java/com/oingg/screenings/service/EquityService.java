package com.oingg.screenings.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oingg.screenings.client.OpenBBClient;
import com.oingg.screenings.dto.PegyView;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class EquityService {

    private final OpenBBClient openBBClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EquityService(OpenBBClient openBBClient) {
        this.openBBClient = openBBClient;
    }

    public Mono<PegyView> getPegyView(String symbol) {
        // 1. 並行發送三個 API 請求
        Mono<String> quoteMono = openBBClient.getEquityQuote(symbol);
        Mono<String> metricsMono = openBBClient.getFundamentalMetrics(symbol);
        Mono<String> estimatesMono = openBBClient.getEstimatesConsensus(symbol);

        // 2. 使用 Mono.zip 等待所有結果
        return Mono.zip(quoteMono, metricsMono, estimatesMono)
                .flatMap(tuple -> {
                    try {
                        // 3. 解析三個 API 的 JSON 回應
                        JsonNode quoteJson = objectMapper.readTree(tuple.getT1()).path("results").get(0);
                        JsonNode metricsJson = objectMapper.readTree(tuple.getT2()).path("results").get(0);
                        JsonNode estimatesJson = objectMapper.readTree(tuple.getT3()).path("results").get(0);

                        // 4. 提取所需欄位
                        BigDecimal lastPrice = new BigDecimal(quoteJson.path("p").asText()); // Last Price
                        BigDecimal change = new BigDecimal(quoteJson.path("c").asText());    // Change

                        BigDecimal peRatio = new BigDecimal(metricsJson.path("pe_ratio").asText());
                        // Dividend Yield (D) - OpenBB 回傳的是百分比，我們要轉成小數
                        BigDecimal dividendYield = new BigDecimal(metricsJson.path("dividend_yield").asText())
                                                        .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

                        // Consensus Growth Estimate (G) - 同樣轉成小數
                        BigDecimal growthEstimate = new BigDecimal(estimatesJson.path("growth").asText())
                                                        .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

                        // 5. 計算 PEGY
                        BigDecimal growthPlusYield = dividendYield.add(growthEstimate);
                        BigDecimal pegyRatio = BigDecimal.ZERO;
                        // 避免除以零的錯誤
                        if (growthPlusYield.compareTo(BigDecimal.ZERO) != 0) {
                            pegyRatio = peRatio.divide(growthPlusYield, 2, RoundingMode.HALF_UP);
                        }

                        // 6. 組合成 PegyView 物件
                        return Mono.just(new PegyView(
                            symbol,
                            lastPrice,
                            change,
                            peRatio,
                            dividendYield,
                            growthEstimate,
                            pegyRatio
                        ));
                    } catch (Exception e) {
                        // 處理 JSON 解析或欄位不存在的錯誤
                        return Mono.error(new RuntimeException("Failed to parse OpenBB API response for symbol: " + symbol, e));
                    }
                });
    }
}
