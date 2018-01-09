package com.mytaxi.controller;

import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainobject.ManufacturerDO;
import com.mytaxi.domainvalue.EngineType;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.service.driver.DriverService;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser
public class DriverControllerTest
{

    private MockMvc mockMvc;

    @Mock
    private DriverService driverService;


    @Before
    public void setUp()
    {
        DriverController driverController = new DriverController(driverService);
        mockMvc = standaloneSetup(driverController).build();
    }


    @Test
    public void findDriversByCarDetails() throws Exception
    {
        CarDO carDO = new CarDO("RTY7777", new ManufacturerDO("Ford"));
        carDO.setAvailable(true);
        carDO.setEngineType(EngineType.GAS);
        carDO.setConvertible(true);
        carDO.setRating(3.0);
        carDO.setSeatCount(5);

        DriverDO driverDO1 = new DriverDO("user1", "pass1");
        driverDO1.setSelectedCar(carDO);
        driverDO1.setOnlineStatus(OnlineStatus.ONLINE);

        when(driverService.find(OnlineStatus.ONLINE)).thenReturn(Collections.singletonList(driverDO1));

        mockMvc.perform(get("/v1/drivers/filter?onlineStatus=ONLINE&manufacturer=Ford&seatCount=5&convertible=true&minimumRating=3.0&engineType=GAS"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$[0].username", is("user1")))
            .andExpect(jsonPath("$[0].password", is("pass1")));

    }


    @Test
    public void testResourceCanSelectCar() throws Exception
    {
        mockMvc.perform(put("/v1/drivers/1/select?licensePlate=RTY7777"))
            .andExpect(status().isOk());

        verify(driverService).selectCar(eq(1L), eq("RTY7777"));
        verifyNoMoreInteractions(driverService);
    }


    @Test
    public void testResourceCanReleaseCar() throws Exception
    {
        mockMvc.perform(put("/v1/drivers/1/release"))
            .andExpect(status().isOk());

        verify(driverService, only()).releaseCar(1L);
    }

}