package org.meli.crud;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.meli.crud.ENUM.TimeVencedor;
import org.meli.crud.controller.PartidaController;
import org.meli.crud.dto.PartidaDTO;
import org.meli.crud.service.PartidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(PartidaController.class)
public class PartidaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartidaService partidaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreatePartida_Success() throws Exception {
        PartidaDTO partidaDTO = new PartidaDTO();
        partidaDTO.setDataHoraPartida(LocalDateTime.of(2026, 1, 1, 1, 1));
        partidaDTO.setIdEstadio(1L);
        partidaDTO.setIdTimeCasa(1L);
        partidaDTO.setIdTimeVisitante(2L);
        partidaDTO.setResultado(TimeVencedor.EMPATE);
        partidaDTO.setSaldoGolsTimeVisitante(1);
        partidaDTO.setSaldoGolsTimeCasa(1);

        mockMvc.perform(post("/partidas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partidaDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Partida cadastrada com sucesso!"));
    }

    @Test
    void testCreatePartida_InvalidData() throws Exception {
        PartidaDTO partidaDTO = new PartidaDTO();

        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos"))
                .when(partidaService).createPartida(any(PartidaDTO.class));

        mockMvc.perform(post("/partidas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partidaDTO)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testGetPartida_Success() throws Exception {
        PartidaDTO partidaDTO = new PartidaDTO();
        partidaDTO.setDataHoraPartida(LocalDateTime.of(2026, 1, 1, 1, 1));
        partidaDTO.setIdEstadio(1L);
        partidaDTO.setIdTimeCasa(1L);
        partidaDTO.setIdTimeVisitante(2L);
        partidaDTO.setResultado(TimeVencedor.EMPATE);
        partidaDTO.setSaldoGolsTimeVisitante(1);
        partidaDTO.setSaldoGolsTimeCasa(1);

        when(partidaService.getPartida(1L)).thenReturn(partidaDTO);

        mockMvc.perform(get("/partidas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dataHoraPartida").value("2026-01-01T01:01:00"));
    }

    @Test
    void testGetPartida_NotFound() throws Exception {
        when(partidaService.getPartida(1L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida não encontrada"));

        mockMvc.perform(get("/partidas/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllPartidas_Success() throws Exception {
        PartidaDTO partida1 = new PartidaDTO();
        partida1.setDataHoraPartida(LocalDateTime.of(2026, 1, 1, 1, 1));
        partida1.setIdEstadio(1L);
        partida1.setIdTimeCasa(1L);
        partida1.setIdTimeVisitante(2L);
        partida1.setResultado(TimeVencedor.EMPATE);
        partida1.setSaldoGolsTimeVisitante(1);
        partida1.setSaldoGolsTimeCasa(1);

        PartidaDTO partida2 = new PartidaDTO();
        partida2.setDataHoraPartida(LocalDateTime.of(2026, 2, 1, 1, 1));
        partida2.setIdEstadio(1L);
        partida2.setIdTimeCasa(1L);
        partida2.setIdTimeVisitante(2L);
        partida2.setResultado(TimeVencedor.EMPATE);
        partida2.setSaldoGolsTimeVisitante(1);
        partida2.setSaldoGolsTimeCasa(1);

        List<PartidaDTO> lista = Arrays.asList(partida1, partida2);

        when(partidaService.getAllPartidas()).thenReturn(lista);

        mockMvc.perform(get("/partidas/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testDeletePartida_Success() throws Exception {
        mockMvc.perform(delete("/partidas/excluir/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("A partida foi excluída com sucesso!"));
    }

    @Test
    void testDeletePartida_NotFound() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida não encontrada"))
                .when(partidaService).deactivatePartida(1L);

        mockMvc.perform(delete("/partidas/excluir/1"))
                .andExpect(status().isNotFound());
    }

}
