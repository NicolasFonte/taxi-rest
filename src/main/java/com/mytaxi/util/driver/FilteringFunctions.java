package com.mytaxi.util.driver;

import com.mytaxi.domainobject.DriverDO;
import java.util.Objects;
import java.util.function.Predicate;

public class FilteringFunctions
{

    public static Predicate<DriverDO> hasCarDetails(String manufacturer, Integer seatCount, Boolean convertible, Double minimumRating, String engineType)
    {
        return d -> Objects.nonNull(d.getSelectedCar())
            && (manufacturer == null || d.getSelectedCar().getManufacturer().getName().equals(manufacturer))
            && (seatCount == null || d.getSelectedCar().getSeatCount() == seatCount)
            && (convertible == null || d.getSelectedCar().isConvertible() == convertible)
            && (minimumRating == null || d.getSelectedCar().getRating() >= minimumRating)
            && (engineType == null || d.getSelectedCar().getEngineType().toString().equalsIgnoreCase(engineType));
    }

}
