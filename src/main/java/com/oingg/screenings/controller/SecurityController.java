package com.oingg.screenings.controller;

import com.oingg.screenings.dto.PegyView;
//import com.oingg.screenings.service.BondService; // 導入 BondService
import com.oingg.screenings.service.EquityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/securities")
public class SecurityController {

    private final EquityService equityService;
//    private final BondService bondService; // 新增 BondService 欄位

    // 透過建構子注入 EquityService 和 BondService
    public SecurityController(EquityService equityService) {
        this.equityService = equityService;
//        this.bondService = bondService; // 初始化 BondService
    }

    /**
     * 根據股票代碼查詢完整的 PEGY 視圖
     * GET /api/v1/securities/AAPL/pegy
     */
    @GetMapping("/{symbol}/pegy")
    public Mono<PegyView> getPegyView(@PathVariable String symbol) {
        return equityService.getPegyView(symbol);
    }

    // 您可以在這裡新增呼叫 bondService 的 API 端點
    // 例如:
    // @GetMapping("/bonds/{cusip}")
    // public Mono<BondView> getBondDetails(@PathVariable String cusip) {
    //     return bondService.getBondDetails(cusip);
    // }
}
