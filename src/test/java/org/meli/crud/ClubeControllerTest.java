package org.meli.crud;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.meli.crud.controller.ClubeController;
import org.meli.crud.dto.ClubeDTO;
import org.meli.crud.service.ClubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ClubeController.class)
public class ClubeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClubeService clubeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateClube_Success() throws Exception {
        ClubeDTO clubeDTO = new ClubeDTO();
        clubeDTO.setFundacao(LocalDate.of(2020, 1, 1));
        clubeDTO.setNome("Santos");
        clubeDTO.setEstado("SP");

        doNothing().when(clubeService).createClube(any(ClubeDTO.class));

        mockMvc.perform(post("/clube")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clubeDTO)))
                .andExpect(status().isCreated()).andExpect(content().string("O clube foi criado com sucesso!"));
    }

    @Test
    public void testCreateClube_Error() throws Exception {
        ClubeDTO clubeDTO = new ClubeDTO();
        clubeDTO.setFundacao(LocalDate.of(2020, 1, 1));
        clubeDTO.setNome(null);

        doThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Dados inválidos!"))
                .when(clubeService).createClube(any(ClubeDTO.class));

        mockMvc.perform(post("/clube")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clubeDTO)))
                        .andExpect(status().isInternalServerError());
    }

    @Test
    void testUpdateClube_Success() throws Exception {
        ClubeDTO clubeDTO = new ClubeDTO();
        clubeDTO.setFundacao(LocalDate.of(2020, 1, 1));
        clubeDTO.setNome("Santos");
        clubeDTO.setEstado("SP");

        mockMvc.perform(put("/clube/atualizar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clubeDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("O clube foi atualizado com sucesso!"));
    }

    @Test
    void testUpdateClube_NotFound() throws Exception {
        ClubeDTO clubeDTO = new ClubeDTO();
        clubeDTO.setFundacao(LocalDate.of(2020, 1, 1));
        clubeDTO.setNome("Santos");
        clubeDTO.setEstado("SP");


        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "O clube não foi encontrado na base de dados."))
                .when(clubeService).updateClube(any(ClubeDTO.class), eq(1L));

        mockMvc.perform(put("/clube/atualizar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clubeDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetClube_Success() throws Exception {
        ClubeDTO clubeDTO = new ClubeDTO();
        clubeDTO.setFundacao(LocalDate.of(2020, 1, 1));
        clubeDTO.setNome("SP");
        clubeDTO.setEstado("SP");

        when(clubeService.getClube(1L)).thenReturn(clubeDTO);

        mockMvc.perform(get("/clube/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("SP"))
                .andExpect(jsonPath("$.estado").value("SP"));
    }

    @Test
    void testGetClube_NotFound() throws Exception {
        when(clubeService.getClube(1000L)).thenThrow(
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado"));

        mockMvc.perform(get("/clube/1000"))
                .andExpect(status().isNotFound());
    }


    @Test
    void testDeactivateClube_Success() throws Exception {
        Long id = 16L;
        doNothing().when(clubeService).deactivateClube(id);

        mockMvc.perform(delete("/clube/inativar/{id}", id))
                .andExpect(status().isNoContent())
                .andExpect(content().string("O clube foi inativado com sucesso!"));
    }

    @Test
    void testDeactivateClube_NotFound() throws Exception {
        Long id = 1000L;

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado"))
                .when(clubeService).deactivateClube(id);

        mockMvc.perform(delete("/clube/inativar/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Clube não encontrado"));
    }

    @Test
    void testGetAllClube_Success() throws Exception {
        List<ClubeDTO> clubes = new ArrayList<>();
        ClubeDTO clube1 = new ClubeDTO();
        clube1.setNome("Santos");
        clube1.setEstado("SP");
        clube1.setFundacao(LocalDate.of(2000, 1, 1));
        clube1.setAtivo(true);

        clubes.add(clube1);

        when(clubeService.getAllClubes()).thenReturn(clubes);

        mockMvc.perform(get("/clube/listar/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllClube_InternalServerError() throws Exception {
        when(clubeService.getAllClubes()).thenThrow(new RuntimeException("Erro ao buscar clubes"));

        mockMvc.perform(get("/clube/listar/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Erro interno no servidor: Erro ao buscar clubes"));
    }


}
