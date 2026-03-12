package com.oingg.screenings.dto.openbb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OpenBBQuoteInDTO { // <-- Renamed
    private String symbol;
    
    @JsonProperty("asset_type")
    private String assetType;
    
    private String name;
    private String exchange;
    
    private Double bid;
    
    @JsonProperty("bid_size")
    private Integer bidSize;
    
    private Double ask;
    
    @JsonProperty("ask_size")
    private Integer askSize;
    
    @JsonProperty("last_price")
    private Double lastPrice;
    
    private Double open;
    private Double high;
    private Double low;
    private Long volume;
    
    @JsonProperty("prev_close")
    private Double prevClose;
    
    @JsonProperty("year_high")
    private Double yearHigh;
    
    @JsonProperty("year_low")
    private Double yearLow;
    
    @JsonProperty("ma_50d")
    private Double ma50d;
    
    @JsonProperty("ma_200d")
    private Double ma200d;
    
    @JsonProperty("volume_average")
    private Long volumeAverage;
    
    @JsonProperty("volume_average_10d")
    private Long volumeAverage10d;
    
    private String currency;
}
