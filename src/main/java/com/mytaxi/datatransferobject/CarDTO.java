package com.mytaxi.datatransferobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mytaxi.domainvalue.EngineType;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
public class CarDTO
{
    @JsonIgnore
    private Long id;

    @NotNull(message = "License Plate cannot be null!")
    private String licensePlate;

    private int seatCount;

    private boolean convertible;

    private double rating;

    private EngineType engineType;

    private String manufacturer;
}
