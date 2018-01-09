package com.mytaxi.controller.mapper;

import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.ManufacturerDO;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CarMapper
{
    public static CarDO makeCarDO(CarDTO carDTO, ManufacturerDO manufacturerDO)
    {
        return new CarDO(carDTO.getLicensePlate(), manufacturerDO);
    }


    public static CarDTO makeCarDTO(CarDO carDO)
    {
        return CarDTO.builder()
            .licensePlate(carDO.getLicensePlate())
            .convertible(carDO.isConvertible())
            .manufacturer(carDO.getManufacturer().getName())
            .rating(carDO.getRating())
            .engineType(carDO.getEngineType().toString())
            .seatCount(carDO.getSeatCount())
            .build();
    }


    public static List<CarDTO> makeCarDTOList(Collection<CarDO> cars)
    {
        return cars.stream()
            .map(CarMapper::makeCarDTO)
            .collect(Collectors.toList());
    }
}
