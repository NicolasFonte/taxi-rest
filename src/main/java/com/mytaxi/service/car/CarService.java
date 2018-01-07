package com.mytaxi.service.car;

import com.mytaxi.domainobject.CarDO;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import java.util.List;

public interface CarService
{
    CarDO findCar(Long id) throws EntityNotFoundException;

    CarDO create(CarDO carDO) throws ConstraintsViolationException;

    List<CarDO> findByManufacturer(String manufacturer) throws EntityNotFoundException;
}
