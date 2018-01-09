package com.mytaxi.service.car;

import com.mytaxi.dataaccessobject.CarRepository;
import com.mytaxi.dataaccessobject.ManufacturerRepository;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.ManufacturerDO;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultCarServiceTest
{

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private CarRepository carRepository;

    @Mock
    private ManufacturerRepository manufacturerRepository;

    private DefaultCarService defaultCarService;


    @Before
    public void setUp()
    {
        defaultCarService = new DefaultCarService(carRepository, manufacturerRepository);
    }


    @Test
    public void testCarCanBeFoundInRepository() throws Exception
    {
        CarDO car = new CarDO("OLD4567", new ManufacturerDO("Ford"));
        when(carRepository.findOne(1L)).thenReturn(car);

        CarDO carLoaded = defaultCarService.findCar(1L);

        assertThat(carLoaded)
            .isNotNull()
            .extracting("licensePlate", "manufacturer.name")
            .contains("OLD4567", "Ford");
    }


    @Test
    public void testCarCanBeFoundByLicensePlateInRepository() throws Exception
    {
        CarDO car = new CarDO("OLD4567", new ManufacturerDO("Ford"));
        when(carRepository.findByLicensePlate("OLD4567")).thenReturn(car);

        CarDO carLoaded = defaultCarService.findByLicensePlate("OLD4567");

        assertThat(carLoaded)
            .isNotNull()
            .extracting("licensePlate", "manufacturer.name")
            .contains("OLD4567", "Ford");
    }


    @Test
    public void testCarNotFoundThrowsEntityNotFoundException() throws Exception
    {
        thrown.expect(EntityNotFoundException.class);
        thrown.expectMessage("Could not find entity with id: 1");

        defaultCarService.findCar(1L);
    }


    @Test
    public void testCarNotFoundByLicensePlateThrowsEntityNotFoundException() throws Exception
    {
        thrown.expect(EntityNotFoundException.class);
        thrown.expectMessage("Could not find car with license plate: invalid");

        defaultCarService.findByLicensePlate("invalid");
    }


    @Test
    public void testCarCanBeCreated() throws Exception
    {
        CarDO car = new CarDO("OLD4567", new ManufacturerDO("Ford"));

        defaultCarService.create(car);

        verify(carRepository, only()).save(car);
    }


    @Test
    public void testCarCreateThrowsConstraintViolationExceptionIfDuplicateLicensePlate() throws Exception
    {
        CarDO car = new CarDO("OLD4567", new ManufacturerDO("Ford"));
        when(carRepository.save(car)).thenThrow(new DataIntegrityViolationException("error"));

        thrown.expect(ConstraintsViolationException.class);
        thrown.expectMessage("error");

        defaultCarService.create(car);

    }


    @Test
    public void testCarsCanBeFindByManufacturer() throws Exception
    {
        ManufacturerDO manufacturerDO = new ManufacturerDO("Volks");
        CarDO carDO = new CarDO("OLD4556", manufacturerDO);
        when(manufacturerRepository.findByName("Volks")).thenReturn(manufacturerDO);
        when(carRepository.findByManufacturer(manufacturerDO)).thenReturn(Collections.singletonList(carDO));

        List<CarDO> cars = defaultCarService.findByManufacturer("Volks");

        assertThat(cars)
            .hasSize(1)
            .extracting("licensePlate", "manufacturer.name")
            .contains(tuple("OLD4556", "Volks"));

    }


    @Test
    public void testFindByManufacturerThrowsExceptionIfManufacturerDoesntExists() throws Exception
    {
        when(manufacturerRepository.findByName("Volks")).thenReturn(null);

        thrown.expect(EntityNotFoundException.class);
        thrown.expectMessage("Manufacturer Volks does not exists!");

        defaultCarService.findByManufacturer("Volks");
    }

}