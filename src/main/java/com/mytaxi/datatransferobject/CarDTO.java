package com.mytaxi.datatransferobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mytaxi.domainvalue.EngineType;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CarDTO
{
    @JsonIgnore
    private Long id;

    @NotNull(message = "License Plate cannot be null!")
    private String licensePlate;

    private int seatCount;

    private boolean convertible;

    private double rating;

    private String engineType;

    private String manufacturer;


    private CarDTO()
    {
    }


    private CarDTO(
        String licensePlate, int seatCount, boolean convertible, double rating,
        String engineType, String manufacturer)
    {
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
        this.convertible = convertible;
        this.rating = rating;
        this.engineType = engineType;
        this.manufacturer = manufacturer;
    }


}
