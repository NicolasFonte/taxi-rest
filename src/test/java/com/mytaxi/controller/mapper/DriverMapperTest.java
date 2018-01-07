package com.mytaxi.controller.mapper;

import com.mytaxi.datatransferobject.DriverDTO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.GeoCoordinate;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class DriverMapperTest
{
    @Test
    public void testMakeDriverDOFromDTO() throws Exception
    {
        DriverDTO driverDTO = DriverDTO.newBuilder()
            .setUsername("user1")
            .setPassword("pass1")
            .createDriverDTO();

        DriverDO driverDO = DriverMapper.makeDriverDO(driverDTO);

        assertThat(driverDO)
            .isNotNull()
            .extracting("username", "password")
            .contains("user1", "pass1");
    }


    @Test
    public void testMakeDriverDTOFromDO() throws Exception
    {
        GeoCoordinate coordinate = new GeoCoordinate(50, 60);
        DriverDO driverDO = createDriverDO("user1", "pass1", 1L, coordinate);

        DriverDTO driverDTO = DriverMapper.makeDriverDTO(driverDO);

        assertThat(driverDTO)
            .isNotNull()
            .extracting("id", "username", "password", "coordinate")
            .contains(1L, "user1", "pass1", coordinate);

    }


    @Test
    public void testMakeDriverDTOListFromDOs() throws Exception
    {
        GeoCoordinate coordinate1 = new GeoCoordinate(50, 60);
        DriverDO driverDO1 = createDriverDO("user1", "pass1", 1L, coordinate1);

        GeoCoordinate coordinate2 = new GeoCoordinate(15, 24);
        DriverDO driverDO2 = createDriverDO("user2", "pass2", 2L, coordinate2);

        List<DriverDTO> driverDTOs = DriverMapper.makeDriverDTOList(Arrays.asList(driverDO1, driverDO2));
        assertThat(driverDTOs)
            .hasSize(2)
            .extracting("username", "password", "id", "coordinate")
            .contains(
                tuple("user1", "pass1", 1L, coordinate1),
                tuple("user2", "pass2", 2L, coordinate2));

    }


    private DriverDO createDriverDO(String user, String password, Long id, GeoCoordinate geoCoordinate)
    {
        DriverDO driverDO = new DriverDO(user, password);
        driverDO.setId(id);
        driverDO.setCoordinate(geoCoordinate);

        return driverDO;
    }

}