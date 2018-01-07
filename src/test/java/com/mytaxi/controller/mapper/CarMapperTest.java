package com.mytaxi.controller.mapper;

import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.ManufacturerDO;
import com.mytaxi.domainvalue.EngineType;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

public class CarMapperTest
{
    private static final String LICENSE_PLATE1 = "NUW9845";
    private static final String LICENSE_PLATE2 = "OLD4582";
    private static final String MANUFACTURER_NAME = "Ford";


    @Test
    public void testCarDomainObjectCanBeGeneratedFromTransferObject() throws Exception
    {
        CarDTO carToMap = CarDTO.builder()
            .manufacturer(MANUFACTURER_NAME)
            .licensePlate(LICENSE_PLATE1)
            .build();

        CarDO car = CarMapper.makeCarDO(carToMap);

        assertThat(car)
            .isNotNull()
            .extracting("manufacturer.name", "licensePlate")
            .contains(MANUFACTURER_NAME, LICENSE_PLATE1);
    }


    @Test
    public void testCarTransferObjectCanBeGeneratedFromDomainObject() throws Exception
    {
        CarDO carDO = createCarDO(LICENSE_PLATE1, MANUFACTURER_NAME, true, EngineType.GAS, 3.0, 5);

        CarDTO carDTO = CarMapper.makeCarDTO(carDO);

        assertThat(carDTO)
            .isNotNull()
            .extracting("licensePlate", "seatCount", "convertible", "rating", "engineType", "manufacturer")
            .contains(LICENSE_PLATE1, 5, true, 3.0, EngineType.GAS, MANUFACTURER_NAME);
    }


    @Test
    public void testDTOCarsCanBeGeneratedFromDOs() throws Exception
    {
        CarDO carDO1 = createCarDO(LICENSE_PLATE1, MANUFACTURER_NAME, true, EngineType.GAS, 3.0, 5);
        CarDO carDO2 = createCarDO(LICENSE_PLATE2, MANUFACTURER_NAME, false, EngineType.DIESEL, 4.0, 7);

        List<CarDTO> carDTOs = CarMapper.makeCarDTOList(Arrays.asList(carDO1, carDO2));

        assertThat(carDTOs)
            .hasSize(2)
            .extracting("licensePlate", "seatCount", "convertible", "rating", "engineType", "manufacturer")
            .contains(
                tuple(LICENSE_PLATE1, 5, true, 3.0, EngineType.GAS, MANUFACTURER_NAME),
                tuple(LICENSE_PLATE2, 7, false, 4.0, EngineType.DIESEL, MANUFACTURER_NAME)
            );
    }


    private CarDO createCarDO(String licensePlate, String manufacturer, boolean convertible, EngineType engineType, double rating, int seatCount)
    {
        CarDO carDO = new CarDO(licensePlate, new ManufacturerDO(manufacturer));
        carDO.setConvertible(convertible);
        carDO.setEngineType(engineType);
        carDO.setRating(rating);
        carDO.setSeatCount(seatCount);
        return carDO;
    }

}