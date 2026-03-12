package com.oingg.screenings.controller;

import com.oingg.screenings.dto.PegyViewOutDTO;
import com.oingg.screenings.service.SecurityAggregatorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/securities")
public class SecurityController {

    private final SecurityAggregatorService aggregatorService;

    // 透過建構子注入 SecurityAggregatorService
    public SecurityController(SecurityAggregatorService aggregatorService) {
        this.aggregatorService = aggregatorService;
    }

    /**
     * 根據股票代碼查詢完整的 PEGY 視圖
     * GET /api/v1/securities/AAPL/pegy
     */
    @GetMapping("/{symbol}/pegy")
    public Mono<PegyViewOutDTO> getPegyView(@PathVariable String symbol) {
        return aggregatorService.getPegyView(symbol);
    }
}
