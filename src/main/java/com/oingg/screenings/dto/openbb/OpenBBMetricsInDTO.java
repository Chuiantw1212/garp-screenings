package com.oingg.screenings.dto.openbb;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * DTO for OpenBB's /equity/fundamental/metrics endpoint.
 * Maps key financial metrics.
 */
public record OpenBBMetricsInDTO( // <-- Renamed
    @JsonProperty("pe_ratio")
    BigDecimal peRatio,

    @JsonProperty("dividend_yield")
    BigDecimal dividendYield
) {}
