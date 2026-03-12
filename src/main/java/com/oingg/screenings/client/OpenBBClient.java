package com.oingg.screenings.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class OpenBBClient {

    private final WebClient webClient;

    // 透過建構子注入 WebClient，並從 application.properties 讀取 base-url
    public OpenBBClient(WebClient.Builder webClientBuilder, @Value("${openbb.api.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    /**
     * 獲取即時行情數據 (Quote)
     */
    public Mono<String> getEquityQuote(String symbol) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/equity/price/quote")
                        .queryParam("symbol", symbol)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    /**
     * 獲取基礎指標數據 (Fundamental Metrics)
     */
    public Mono<String> getFundamentalMetrics(String symbol) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/equity/fundamental/metrics")
                        .queryParam("symbol", symbol)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    /**
     * 獲取分析師預期數據 (Estimates Consensus)
     */
    public Mono<String> getEstimatesConsensus(String symbol) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/equity/estimates/consensus")
                        .queryParam("symbol", symbol)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }
}
