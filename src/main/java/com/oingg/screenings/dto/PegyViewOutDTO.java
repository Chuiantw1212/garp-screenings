package com.oingg.screenings.dto;

import java.math.BigDecimal;

/**
 * A complete view for PEGY analysis, combining data from multiple sources.
 * This is the final DTO sent to the client.
 *
 * @param symbol The stock symbol.
 * @param lastPrice The latest trading price.
 * @param change The price change.
 * @param peRatio Price-to-Earnings ratio.
 * @param dividendYield Dividend yield (D).
 * @param growthEstimate Consensus growth estimate for the next 3-5 years (G).
 * @param pegyRatio The calculated PEGY ratio (P/E / (G + D)).
 */
public record PegyViewOutDTO( // <-- Renamed
    String symbol,
    BigDecimal lastPrice,
    BigDecimal change,
    BigDecimal peRatio,
    BigDecimal dividendYield,
    BigDecimal growthEstimate,
    BigDecimal pegyRatio
) {}
