package org.meli.crud;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.meli.crud.model.Pet;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private Pet maple = new Pet("Maple", "Cachorro");
    private Pet pepper = new Pet("Pepper", "Gato");
    private Pet momo = new Pet("Momo", "Cachorro");

    private List<Pet> petList = List.of(maple, momo, pepper);

    private Map<String, Object> expectedResponseMap = new HashMap<>();

    @Test
    void testGetPets() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(get("/pet").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        Map<String, List<Pet>> responseMap = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});

        List<Pet> listPetResponse = responseMap.get("Pets:");
        assertEquals( new HashSet<>(petList), new HashSet<>(listPetResponse));
        assertEquals(200, response.getStatus());

    }


    @Test
    void testGetPets_201() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(get("/pet").queryParam("status", "201").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertEquals(201, response.getStatus());

    }


    @Test
    void testGetPets_400() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(get("/pet").queryParam("status", "400").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertEquals(400, response.getStatus());

    }
    @Test
    void testGetPets_404() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(get("/pet").queryParam("status", "404").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertEquals(404, response.getStatus());

    }


    @Test
    void testGetPets_409() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(get("/pet").queryParam("status", "409").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertEquals(409, response.getStatus());

    }

    @Test
    void testGetPets_500() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(get("/pet").queryParam("status", "500").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertEquals(500, response.getStatus());

    }

    @Test
    void testCreatePet() throws Exception {

        Pet novoPet = new Pet("Maple", "Cachorro");

        MockHttpServletResponse response = mockMvc.perform(post("/pet").content(objectMapper.writeValueAsString(novoPet)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        String expectedResponse = objectMapper.writeValueAsString(maple);
        String responseContent = response.getContentAsString();

        assertEquals(201, response.getStatus());
        assertEquals(expectedResponse, responseContent);
    }

    @Test
    void testCreatePet_400() throws Exception {

        Pet novoPet = new Pet("Maple", "Cachorro");

        MockHttpServletResponse response = mockMvc.perform(post("/pet").queryParam("statusCode", "400").content(objectMapper.writeValueAsString(novoPet)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }
}