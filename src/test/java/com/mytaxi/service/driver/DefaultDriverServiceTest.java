package com.mytaxi.service.driver;

import com.mytaxi.dataaccessobject.DriverRepository;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainobject.ManufacturerDO;
import com.mytaxi.exception.CarAlreadyInUseException;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.car.CarService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.matchers.Null;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultDriverServiceTest
{
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private CarService carService;

    private DefaultDriverService driverService;


    @Before
    public void setUp()
    {
        driverService = new DefaultDriverService(driverRepository, carService);
    }


    @Test
    public void testDriverCanBeFoundInRepository() throws Exception
    {
        DriverDO driver = new DriverDO("driverX", "passX");
        when(driverRepository.findOne(1L)).thenReturn(driver);

        DriverDO driverLoaded = driverService.find(1L);

        assertThat(driverLoaded)
            .isNotNull()
            .extracting("username", "password")
            .contains("driverX", "passX");
    }


    @Test
    public void testDriverNotFoundThrowInvalidEntityException() throws Exception
    {
        thrown.expect(EntityNotFoundException.class);
        thrown.expectMessage("Could not find entity with id: 1");

        driverService.find(1L);

    }


    @Test
    public void testCarCanBeSelectedByDriver() throws Exception
    {
        CarDO car = new CarDO("OLD1111", new ManufacturerDO("Ford"));
        DriverDO driverDO = mock(DriverDO.class);
        when(carService.findByLicensePlate("OLD1111")).thenReturn(car);
        when(driverRepository.findOne(1L)).thenReturn(driverDO);

        driverService.selectCar(1L, "OLD1111");

        verify(driverDO, only()).setSelectedCar(car);
    }


    @Test
    public void testCarInUseCannotBeSelected() throws Exception
    {
        CarDO car = new CarDO("OLD1111", new ManufacturerDO("Ford"));
        car.setAvailable(false);
        DriverDO driverDO = mock(DriverDO.class);
        when(carService.findByLicensePlate("OLD1111")).thenReturn(car);
        when(driverRepository.findOne(1L)).thenReturn(driverDO);

        thrown.expect(CarAlreadyInUseException.class);
        thrown.expectMessage("Car already in use!");

        driverService.selectCar(1L, "OLD1111");

    }


    @Test
    public void testCarCanBeReleased() throws Exception
    {
        CarDO car = mock(CarDO.class);
        DriverDO driverDO = mock(DriverDO.class);
        when(driverDO.getSelectedCar()).thenReturn(car);
        when(driverRepository.findOne(1L)).thenReturn(driverDO);

        driverService.releaseCar(1L);

        verify(car, only()).setAvailable(true);
        verify(driverDO).setSelectedCar(isNull(CarDO.class));
    }


    @Test
    public void testDriverCanBeCreated() throws Exception
    {
        DriverDO driver = new DriverDO("userX", "passX");

        driverService.create(driver);

        verify(driverRepository, only()).save(driver);
    }


    @Test
    public void testDriverCreateThrowsConstraintViolationExceptionIfEmptyUserName() throws Exception
    {
        DriverDO driver = new DriverDO("", "passX");
        when(driverRepository.save(driver)).thenThrow(new DataIntegrityViolationException("error"));

        thrown.expect(ConstraintsViolationException.class);
        thrown.expectMessage("error");

        driverService.create(driver);

    }


}