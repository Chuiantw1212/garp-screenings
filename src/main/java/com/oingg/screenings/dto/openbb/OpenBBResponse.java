package com.oingg.screenings.dto.openbb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * A generic and comprehensive wrapper for OpenBB API responses.
 * This class maps the common structure of OpenBB's JSON output, including metadata.
 * @param <T> The type of the data contained within the "results" list.
 */
@Setter
@Getter
public class OpenBBResponse<T> {
    private String id;
    private List<T> results;
    private String provider;
    private String warnings;
    private Object chart;
}
