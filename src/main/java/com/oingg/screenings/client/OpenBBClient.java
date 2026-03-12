package com.oingg.screenings.client;

import com.oingg.screenings.dto.openbb.OpenBBGrowthInDTO;
import com.oingg.screenings.dto.openbb.OpenBBMetricsInDTO;
import com.oingg.screenings.dto.openbb.OpenBBResponse;
import com.oingg.screenings.dto.openbb.OpenBBQuoteInDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class OpenBBClient {

    private final WebClient webClient;

    public OpenBBClient(WebClient.Builder webClientBuilder, @Value("${openbb.api.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    /**
     * 獲取即時行情數據 (Quote)，並自動映射到 DTO。
     */
    public Mono<OpenBBQuoteInDTO> getEquityQuote(String symbol) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/equity/price/quote")
                        .queryParam("symbol", symbol)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<OpenBBResponse<OpenBBQuoteInDTO>>() {})
                .map(response -> {
                    if (response.getResults() == null || response.getResults().isEmpty()) {
                        throw new RuntimeException("No results found for symbol: " + symbol);
                    }
                    return response.getResults().get(0);
                });
    }

    /**
     * 獲取基礎指標數據 (Fundamental Metrics)，並自動映射到 DTO。
     */
    public Mono<OpenBBMetricsInDTO> getFundamentalMetrics(String symbol) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/equity/fundamental/metrics")
                        .queryParam("symbol", symbol)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<OpenBBResponse<OpenBBMetricsInDTO>>() {})
                .map(response -> {
                    if (response.getResults() == null || response.getResults().isEmpty()) {
                        throw new RuntimeException("No fundamental metrics found for symbol: " + symbol);
                    }
                    return response.getResults().get(0);
                });
    }

    /**
     * 獲取分析師預期數據 (Estimates Consensus)，並自動映射到 DTO。
     */
    public Mono<OpenBBGrowthInDTO> getEstimatesConsensus(String symbol) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/equity/estimates/consensus")
                        .queryParam("symbol", symbol)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<OpenBBResponse<OpenBBGrowthInDTO>>() {})
                .map(response -> {
                    if (response.getResults() == null || response.getResults().isEmpty()) {
                        throw new RuntimeException("No consensus estimates found for symbol: " + symbol);
                    }
                    return response.getResults().get(0);
                });
    }
}
