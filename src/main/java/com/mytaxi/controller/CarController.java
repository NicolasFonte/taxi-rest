package com.mytaxi.controller;

import com.mytaxi.controller.mapper.CarMapper;
import com.mytaxi.dataaccessobject.ManufacturerRepository;
import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.ManufacturerDO;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.car.CarService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * All operations with a car will be routed by this controller.
 * <p/>
 */
@RestController
@RequestMapping("v1/cars")
public class CarController
{
    private final CarService carService;
    private final ManufacturerRepository manufacturerRepository;


    @Autowired
    public CarController(
        final CarService carService,
        final ManufacturerRepository manufacturerRepository)
    {
        this.carService = carService;
        this.manufacturerRepository = manufacturerRepository;
    }


    @GetMapping("/{carId}")
    public CarDTO findCar(@Valid @PathVariable long carId) throws EntityNotFoundException
    {
        return CarMapper.makeCarDTO(carService.findCar(carId));
    }


    @GetMapping("/manufacturer/{manufacturer}")
    public List<CarDTO> findCarsByManufacturer(@PathVariable String manufacturer) throws EntityNotFoundException
    {
        return CarMapper.makeCarDTOList(carService.findByManufacturer(manufacturer));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarDTO createCar(@Valid @RequestBody CarDTO carDTO) throws ConstraintsViolationException
    {
        ManufacturerDO manufacturer = manufacturerRepository.findByName(carDTO.getManufacturer());
        CarDO carDO = CarMapper.makeCarDO(carDTO, manufacturer);

        return CarMapper.makeCarDTO(carService.create(carDO));
    }

}
