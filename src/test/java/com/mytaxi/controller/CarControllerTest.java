package com.mytaxi.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytaxi.controller.mapper.CarMapper;
import com.mytaxi.dataaccessobject.ManufacturerRepository;
import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.ManufacturerDO;
import com.mytaxi.service.car.CarService;
import java.io.IOException;
import java.nio.charset.Charset;
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
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser
public class CarControllerTest
{

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Mock
    private CarService carService;

    @Mock
    private ManufacturerRepository manufacturerRepository;


    @Before
    public void setUp()
    {
        CarController carController = new CarController(carService, manufacturerRepository);
        mockMvc = standaloneSetup(carController).build();
    }


    @Test
    public void testCarFoundResource() throws Exception
    {
        CarDO carDO = new CarDO("OLD4556", new ManufacturerDO("Ford"));
        when(carService.findCar(1L)).thenReturn(carDO);

        mockMvc.perform(get("/v1/cars/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.licensePlate", is("OLD4556")))
            .andExpect(jsonPath("$.manufacturer", is("Ford")));

        verify(carService, only()).findCar(1L);
        verifyNoMoreInteractions(carService);

    }


    @Test
    public void testCreateCarResource() throws Exception
    {
        CarDTO carDTO = CarDTO.builder()
            .licensePlate("OLD4444")
            .manufacturer("Test")
            .convertible(true)
            .build();
        ManufacturerDO manufacturerDO = new ManufacturerDO("Test");
        when(manufacturerRepository.findByName("Test")).thenReturn(manufacturerDO);
        when(carService.create(anyObject())).thenReturn(CarMapper.makeCarDO(carDTO, manufacturerDO));

        mockMvc.perform(post("/v1/cars")
            .contentType(APPLICATION_JSON_UTF8)
            .content(convertObjectToJsonBytes(carDTO)).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.licensePlate", is("OLD4444")))
            .andExpect(jsonPath("$.manufacturer", is("Test")));

        verify(carService, only()).create(anyObject());
        verifyNoMoreInteractions(carService);
    }


    public byte[] convertObjectToJsonBytes(Object object) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

}