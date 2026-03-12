package com.oingg.screenings.service;

import com.oingg.screenings.client.OpenBBClient;
import com.oingg.screenings.dto.openbb.OpenBBGrowthInDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GrowthService {

    private final OpenBBClient openBBClient;

    /**
     * 透過建構子注入 OpenBBClient。
     */
    public GrowthService(OpenBBClient openBBClient) {
        this.openBBClient = openBBClient;
    }

    /**
     * 獲取分析師預期成長數據。
     * 快取 'growth'，預計存活時間為 7 天。
     */
    @Cacheable(value = "growth", key = "#symbol") // TODO: 後續設定專用的 7 天過期 cacheManager
    public Mono<OpenBBGrowthInDTO> fetchGrowth(String symbol) {
        System.out.println("Cache miss for growth: " + symbol + ". Fetching from OpenBB API.");
        return openBBClient.getEstimatesConsensus(symbol);
    }
}
