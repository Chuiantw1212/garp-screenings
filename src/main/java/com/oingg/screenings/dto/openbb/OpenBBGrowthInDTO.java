package com.oingg.screenings.dto.openbb;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * DTO for OpenBB's /equity/estimates/consensus endpoint.
 * Maps the consensus growth estimate.
 */
public record OpenBBGrowthInDTO( // <-- Renamed
    @JsonProperty("growth")
    BigDecimal growthEstimate
) {}
