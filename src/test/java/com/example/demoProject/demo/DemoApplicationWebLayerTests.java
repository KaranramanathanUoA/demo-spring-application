package com.example.demoProject.demo;

import com.example.demoProject.demo.api.CarController;
import com.example.demoProject.demo.model.Car;
import com.example.demoProject.demo.service.CarService;
import com.example.demoProject.demo.utils.CarNotFoundException;
import com.example.demoProject.demo.utils.CarRecordAlreadyExistsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.util.NestedServletException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CarController.class)
@ActiveProfiles("test")
public class DemoApplicationWebLayerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;

	@MockBean
	private CarService carService;

	@Test
	public void TestGetAllCarsReturnsOkWithListOfVehicles() throws Exception {
		List<Car> cars = new ArrayList<>();
		Car car1 = new Car(11, "Grey", "Nissan Mazda");
		cars.add(car1);

		//Mocking Car Service using Mockito
		Mockito.when(carService.getAllCars()).thenReturn(cars);

		mockMvc.perform(MockMvcRequestBuilders.get("/cars").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].vehicleId", is(11)))
				.andExpect(jsonPath("$[0].color", is("Grey")))
				.andExpect(jsonPath("$[0].model", is("Nissan Mazda")));
	}

	@Test
	public void TestGetCarReturnsOkWithCorrectVehicleId() throws Exception {
		Car car = new Car(3, "red", "Jeep Compass");

		//Mocking Car Service using Mockito
		Mockito.when(carService.getCar(3)).thenReturn(car);

		mockMvc.perform(MockMvcRequestBuilders.get("/cars/3").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.vehicleId", is(3)))
				.andExpect(jsonPath("$.color", is("red")))
				.andExpect(jsonPath("$.model", is("Jeep Compass")));
	}

	@Test(expected = CarNotFoundException.class)
	public void TestGetCarReturnsCarNotFoundExceptionWithIncorrectVehicleId() throws Exception {
		/* Passing -1 as the vehicleId should throw an exception as	negative numbers are not accepted as primary key
		   in the database where we are storing the car record */
		Mockito.when(carService.getCar(-1)).thenThrow(new CarNotFoundException("Car with the provided " +
				"vehicleId does not exist!"));
		try {
			ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/cars/-1/")
					.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is5xxServerError());
		}
		catch(NestedServletException e) {
			throw (Exception) e.getCause();
		}
	}

	@Test
	public void TestAddCarReturnsOkWithCorrectParameters() throws Exception {
		Car newCar = new Car(15, "Black", "Rolls Royce Phantom");

		Mockito.when(carService.addCar(Mockito.any(Car.class))).thenReturn(newCar);

		MockHttpServletRequestBuilder builder = post("/cars")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8").content(this.mapper.writeValueAsString(newCar));

		mockMvc.perform(builder).andExpect(status().isOk())
				.andExpect(jsonPath("$.vehicleId", is(15)))
				.andExpect(MockMvcResultMatchers.content().string(this.mapper.writeValueAsString(newCar)));
	}

	@Test
	public void TestAddCarReturnsErrorWhenMissingModelParameterIsPassed() throws Exception {
		// Create new vehicle with empty 'make' field
		Car newCar = new Car(150, "Grey", "");

		String carJsonString = this.mapper.writeValueAsString(newCar);

		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/cars")
				.contentType(MediaType.APPLICATION_JSON).content(carJsonString)).andExpect(status().isBadRequest());

		// @Valid annotation in controller will cause exception to be thrown
		assertEquals(MethodArgumentNotValidException.class,
				resultActions.andReturn().getResolvedException().getClass());
		assertTrue(resultActions.andReturn().getResolvedException().getMessage().contains("'model' field was empty"));
	}

	@Test
	public void TestAddCarReturnsErrorWhenMissingColorParameterIsPassed() throws Exception {
		// Create new vehicle with empty 'make' field
		Car newCar = new Car(150, "", "Cadillac Dream");

		String carJsonString = this.mapper.writeValueAsString(newCar);

		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/cars")
				.contentType(MediaType.APPLICATION_JSON).content(carJsonString)).andExpect(status().isBadRequest());

		// @Valid annotation in controller will cause exception to be thrown
		assertEquals(MethodArgumentNotValidException.class,
				resultActions.andReturn().getResolvedException().getClass());
		assertTrue(resultActions.andReturn().getResolvedException().getMessage().contains("'color' field was empty"));
	}
}