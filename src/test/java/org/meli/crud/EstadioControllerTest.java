package org.meli.crud;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.meli.crud.controller.EstadioController;
import org.meli.crud.dto.EstadioDTO;
import org.meli.crud.service.ClubeService;
import org.meli.crud.service.EstadioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(EstadioController.class)
public class EstadioControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstadioService estadioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateEstadio_Success() throws Exception {
        EstadioDTO estadioDTO = new EstadioDTO();
        estadioDTO.setNome("Estadio 1");

        mockMvc.perform(post("/estadio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estadioDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Estádio cadastrado com sucesso!"));
    }

    @Test
    void testCreateEstadio_InvalidData() throws Exception {
        EstadioDTO estadioDTO = new EstadioDTO();

        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos"))
                .when(estadioService).createEstadio(any(EstadioDTO.class));

        mockMvc.perform(post("/estadio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estadioDTO)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testUpdateEstadio_Success() throws Exception {
        EstadioDTO estadioDTO = new EstadioDTO();
        estadioDTO.setNome("Estadio 1");

        mockMvc.perform(put("/estadio/atualizar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estadioDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Estádio atualizado com sucesso!"));
    }

    @Test
    void testUpdateEstadio_NotFound() throws Exception {
        EstadioDTO estadioDTO = new EstadioDTO();
        estadioDTO.setNome("Estadio 1");

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Estádio não encontrado"))
                .when(estadioService).updateEstadio(any(EstadioDTO.class), eq(1L));

        mockMvc.perform(put("/estadio/atualizar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estadioDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetEstadio_Success() throws Exception {
        EstadioDTO estadioDTO = new EstadioDTO();
        estadioDTO.setNome("Estadio 1");

        when(estadioService.getEstadio(1L)).thenReturn(estadioDTO);

        mockMvc.perform(get("/estadio/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetEstadio_NotFound() throws Exception {
        when(estadioService.getEstadio(1L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Estádio não encontrado"));

        mockMvc.perform(get("/estadio/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllEstadios_Success() throws Exception {
        EstadioDTO estadio1 = new EstadioDTO();
        estadio1.setNome("Estadio 1");

        EstadioDTO estadio2 = new EstadioDTO();
        estadio2.setNome("Estadio 2");

        List<EstadioDTO> lista = Arrays.asList(estadio1, estadio2);

        when(estadioService.getAllEstadios()).thenReturn(lista);

        mockMvc.perform(get("/estadio/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }


}
