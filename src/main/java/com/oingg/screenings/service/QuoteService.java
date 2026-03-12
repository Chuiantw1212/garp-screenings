package com.oingg.screenings.service;

import com.oingg.screenings.client.OpenBBClient;
import com.oingg.screenings.dto.openbb.OpenBBQuoteInDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class QuoteService {

    private final OpenBBClient openBBClient;

    /**
     * 透過建構子注入 OpenBBClient。
     * Spring IoC 容器會自動找到 OpenBBClient 的實例並傳入。
     * @param openBBClient 用於與 OpenBB API 溝通的客戶端。
     */
    public QuoteService(OpenBBClient openBBClient) {
        this.openBBClient = openBBClient;
    }

    /**
     * 獲取即時行情資料。
     * 這個方法被設定為可快取的。在呼叫此方法前，Spring 會先檢查 'quotes' 快取中
     * 是否已有鍵值為 'symbol' 的資料。如果有，則直接回傳快取結果；否則，
     * 執行此方法，並將結果存入快取。
     *
     * @param symbol 股票代碼。
     * @return 包含即時行情資料的 Mono 物件。
     */
    @Cacheable(value = "quotes", key = "#symbol") // TODO: 後續設定專用的 1 分鐘過期 cacheManager
    public Mono<OpenBBQuoteInDTO> fetchQuote(String symbol) {
        System.out.println("Cache miss for quote: " + symbol + ". Fetching from OpenBB API.");
        return openBBClient.getEquityQuote(symbol);
    }
}
