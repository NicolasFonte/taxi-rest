package com.mytaxi.controller;

import com.mytaxi.controller.mapper.DriverMapper;
import com.mytaxi.datatransferobject.DriverDTO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.CarAlreadyInUseException;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.driver.DriverService;
import com.mytaxi.util.driver.FilteringFunctions;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * All operations with a driver will be routed by this controller.
 * <p/>
 */
@RestController
@RequestMapping("v1/drivers")
public class DriverController
{

    private final DriverService driverService;


    @Autowired
    public DriverController(final DriverService driverService)
    {
        this.driverService = driverService;
    }


    @GetMapping("/{driverId}")
    public DriverDTO getDriver(@Valid @PathVariable long driverId) throws EntityNotFoundException
    {
        return DriverMapper.makeDriverDTO(driverService.find(driverId));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverDTO createDriver(@Valid @RequestBody DriverDTO driverDTO) throws ConstraintsViolationException
    {
        DriverDO driverDO = DriverMapper.makeDriverDO(driverDTO);
        return DriverMapper.makeDriverDTO(driverService.create(driverDO));
    }


    @DeleteMapping("/{driverId}")
    public void deleteDriver(@Valid @PathVariable long driverId) throws EntityNotFoundException
    {
        driverService.delete(driverId);
    }


    @PutMapping("/{driverId}")
    public void updateLocation(
        @Valid @PathVariable long driverId, @RequestParam double longitude, @RequestParam double latitude)
        throws ConstraintsViolationException, EntityNotFoundException
    {
        driverService.updateLocation(driverId, longitude, latitude);
    }


    @GetMapping
    public List<DriverDTO> findDrivers(@RequestParam OnlineStatus onlineStatus)
        throws ConstraintsViolationException, EntityNotFoundException
    {
        return DriverMapper.makeDriverDTOList(driverService.find(onlineStatus));
    }


    /**
     * Filter Drivers according car details.
     * Car details are optionally queried
     */
    @GetMapping("/filter")
    public List<DriverDTO> findDriversByCarDetails(
        @RequestParam OnlineStatus onlineStatus,
        @RequestParam(required = false) String manufacturer,
        @RequestParam(required = false) Integer seatCount,
        @RequestParam(required = false) Boolean convertible,
        @RequestParam(required = false) Double minimumRating,
        @RequestParam(required = false) String engineType)
        throws ConstraintsViolationException, EntityNotFoundException
    {
        List<DriverDO> driversByManufacturer = driverService.find(onlineStatus)
            .stream()
            .filter(FilteringFunctions.hasCarDetails(manufacturer, seatCount, convertible, minimumRating, engineType))
            .collect(Collectors.toList());

        return DriverMapper.makeDriverDTOList(driversByManufacturer);
    }


    @PutMapping("/{driverId}/select")
    public void selectCar(@Valid @PathVariable long driverId, @Valid @RequestParam String licensePlate) throws EntityNotFoundException, CarAlreadyInUseException
    {
        driverService.selectCar(driverId, licensePlate);
    }


    @PutMapping("/{driverId}/release")
    public void releaseCar(@Valid @PathVariable long driverId) throws EntityNotFoundException
    {
        driverService.releaseCar(driverId);
    }

}
