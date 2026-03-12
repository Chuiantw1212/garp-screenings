package com.oingg.screenings.service;

import com.oingg.screenings.client.OpenBBClient;
import com.oingg.screenings.dto.openbb.OpenBBMetricsInDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MetricsService {

    private final OpenBBClient openBBClient;

    /**
     * 透過建構子注入 OpenBBClient。
     */
    public MetricsService(OpenBBClient openBBClient) {
        this.openBBClient = openBBClient;
    }

    /**
     * 獲取財務指標數據。
     * 快取 'metrics'，預計存活時間為 24 小時。
     */
    @Cacheable(value = "metrics", key = "#symbol") // TODO: 後續設定專用的 24 小時過期 cacheManager
    public Mono<OpenBBMetricsInDTO> fetchMetrics(String symbol) {
        System.out.println("Cache miss for metrics: " + symbol + ". Fetching from OpenBB API.");
        return openBBClient.getFundamentalMetrics(symbol);
    }
}
