package com.mytaxi.service.car;

import com.mytaxi.dataaccessobject.CarRepository;
import com.mytaxi.dataaccessobject.ManufacturerRepository;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.ManufacturerDO;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class DefaultCarService implements CarService
{
    private static Logger LOG = LoggerFactory.getLogger(DefaultCarService.class);

    private final CarRepository carRepository;
    private final ManufacturerRepository manufacturerRepository;


    @Autowired
    public DefaultCarService(final CarRepository carRepository, final ManufacturerRepository manufacturerRepository)
    {
        this.carRepository = carRepository;
        this.manufacturerRepository = manufacturerRepository;
    }


    @Override
    public CarDO findCar(Long carId) throws EntityNotFoundException
    {
        CarDO car = carRepository.findOne(carId);
        if (car == null)
        {
            throw new EntityNotFoundException("Could not find entity with id: " + carId);
        }
        return car;
    }


    @Override
    public CarDO findByLicensePlate(String licensePlate) throws EntityNotFoundException
    {
        CarDO car = carRepository.findByLicensePlate(licensePlate);
        if (car == null)
        {
            throw new EntityNotFoundException("Could not find car with license plate: " + licensePlate);
        }
        return car;
    }


    @Override
    public CarDO create(CarDO carDO) throws ConstraintsViolationException
    {
        CarDO car;
        try
        {
            car = carRepository.save(carDO);
        }
        catch (DataIntegrityViolationException e)
        {
            LOG.warn("Constraint violation on car creation.", e);
            throw new ConstraintsViolationException(e.getMessage());
        }
        return car;
    }


    @Override
    public List<CarDO> findByManufacturer(String manufacturer) throws EntityNotFoundException
    {
        ManufacturerDO manufacturerDO = manufacturerRepository.findByName(manufacturer);
        if (manufacturerDO == null)
        {
            throw new EntityNotFoundException("Manufacturer " + manufacturer + " does not exists!");
        }

        return carRepository.findByManufacturer(manufacturerDO);
    }
}
