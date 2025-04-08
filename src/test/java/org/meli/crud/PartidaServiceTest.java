package org.meli.crud;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.meli.crud.ENUM.TimeVencedor;
import org.meli.crud.dto.PartidaDTO;
import org.meli.crud.model.Clube;
import org.meli.crud.model.Estadio;
import org.meli.crud.model.Partida;
import org.meli.crud.repository.ClubeRepository;
import org.meli.crud.repository.EstadioRepository;
import org.meli.crud.repository.PartidaRepository;
import org.meli.crud.service.PartidaService;
import org.meli.crud.service.RetrospectoService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PartidaServiceTest {

    @Mock
    private ClubeRepository clubeRepository;

    @Mock
    private EstadioRepository estadioRepository;

    @Mock
    private PartidaRepository partidaRepository;

    @Mock
    private RetrospectoService retrospectoService;

    @InjectMocks
    private PartidaService partidaService;

    @Test
    void testCreatePartida_Success() {
        PartidaDTO partidaDTO = new PartidaDTO();

        partidaDTO.setDataHoraPartida(LocalDateTime.now());
        partidaDTO.setSaldoGolsTimeCasa(1);
        partidaDTO.setResultado(TimeVencedor.EMPATE);
        partidaDTO.setSaldoGolsTimeVisitante(1);
        partidaDTO.setIdEstadio(1L);
        partidaDTO.setIdTimeCasa(8L);
        partidaDTO.setIdTimeVisitante(9L);
        Clube clube = new Clube();
        clube.setFundacao(LocalDate.of(2020, 1, 1));
        clube.setEstado("SP");
        clube.setNome(UUID.randomUUID().toString());
        clube.setId(12L);
        clube.setAtivo(true);

        Estadio estadio = new Estadio();
        String randomString = UUID.randomUUID().toString();
        estadio.setNome(randomString);


        when(clubeRepository.findById(anyLong())).thenReturn(Optional.of(clube));
        when(clubeRepository.existsById(anyLong())).thenReturn(true);
        when(estadioRepository.existsById(anyLong())).thenReturn(true);
        when(estadioRepository.findById(anyLong())).thenReturn(Optional.of(estadio));

        assertDoesNotThrow(() -> partidaService.createPartida(partidaDTO));
        verify(partidaRepository, times(1)).save(any(Partida.class));
    }


    @Test
    void testCreatePartida_ErrorWhenClubeNotFound_ThrowsException() {
        PartidaDTO partidaDTO = new PartidaDTO();

        partidaDTO.setDataHoraPartida(LocalDateTime.now());
        partidaDTO.setSaldoGolsTimeCasa(1);
        partidaDTO.setResultado(TimeVencedor.EMPATE);
        partidaDTO.setSaldoGolsTimeVisitante(1);
        partidaDTO.setIdEstadio(1L);
        partidaDTO.setIdTimeCasa(8L);
        partidaDTO.setIdTimeVisitante(9L);

        when(clubeRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.createPartida(partidaDTO);
        });
    }

    @Test
    void testCreatePartida_WhenDataHoraPartidaIsNull_ThrowsException() {
        PartidaDTO partidaInvalida = new PartidaDTO();
        partidaInvalida.setIdTimeVisitante(12L);
        partidaInvalida.setIdTimeCasa(13L);
        partidaInvalida.setIdEstadio(2L);
        partidaInvalida.setDataHoraPartida(null);

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.createPartida(partidaInvalida);
        });

        verify(partidaRepository, never()).save(any(Partida.class));
    }

    @Test
    void testCreatePartida_ErrorWhenEstadioNotFound_ThrowsException() {
        PartidaDTO partidaDTO = new PartidaDTO();

        partidaDTO.setDataHoraPartida(LocalDateTime.now());
        partidaDTO.setSaldoGolsTimeCasa(1);
        partidaDTO.setResultado(TimeVencedor.EMPATE);
        partidaDTO.setSaldoGolsTimeVisitante(1);
        partidaDTO.setIdEstadio(1L);
        partidaDTO.setIdTimeCasa(8L);
        partidaDTO.setIdTimeVisitante(9L);

        when(estadioRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.createPartida(partidaDTO);
        });
    }


    @Test
    void testUpdatePartida_Success() {
        Long id = 8L;

        Partida partidaExistente = new Partida();
        PartidaDTO partidaDTO = new PartidaDTO();
        partidaDTO.setDataHoraPartida(LocalDateTime.now());
        partidaDTO.setSaldoGolsTimeCasa(1);
        partidaDTO.setResultado(TimeVencedor.EMPATE);
        partidaDTO.setSaldoGolsTimeVisitante(1);
        partidaDTO.setIdEstadio(1L);
        partidaDTO.setIdTimeCasa(12L);
        partidaDTO.setIdTimeVisitante(13L);

        //clube 1
        Clube timeVisitante = new Clube();
        timeVisitante.setNome(UUID.randomUUID().toString());
        timeVisitante.setEstado("SP");
        timeVisitante.setFundacao(LocalDate.of(1895, 11, 15));
        timeVisitante.setAtivo(true);
        timeVisitante.setId(12L);

        //clube 2
        Clube timeCasa = new Clube();
        timeCasa.setNome(UUID.randomUUID().toString());
        timeCasa.setEstado("RJ");
        timeCasa.setFundacao(LocalDate.of(1895, 11, 15));
        timeCasa.setAtivo(true);
        timeCasa.setId(13L);

        when(partidaRepository.findById(anyLong())).thenReturn(Optional.of(partidaExistente));
        when(partidaRepository.existsById(anyLong())).thenReturn(true);
        when(estadioRepository.existsById(anyLong())).thenReturn(true);
        when(clubeRepository.existsById(anyLong())).thenReturn(true);
        when(clubeRepository.findById(anyLong())).thenReturn(Optional.of(timeCasa));
        when(clubeRepository.findById(anyLong())).thenReturn(Optional.of(timeVisitante));
        when(estadioRepository.findById(anyLong())).thenReturn(Optional.of(new Estadio()));

        Assertions.assertDoesNotThrow(() -> {
            partidaService.updatePartida(partidaDTO, id);
        });

        verify(partidaRepository).save(any(Partida.class));
    }

    @Test
    void testUpdatePartida_ErrorWhenClubeNotFound_ThrowsException() {
        Long id = 8L;

        PartidaDTO partidaDTO = new PartidaDTO();
        partidaDTO.setDataHoraPartida(LocalDateTime.now());
        partidaDTO.setSaldoGolsTimeCasa(1);
        partidaDTO.setResultado(TimeVencedor.EMPATE);
        partidaDTO.setSaldoGolsTimeVisitante(1);
        partidaDTO.setIdEstadio(1L);
        partidaDTO.setIdTimeCasa(17L);
        partidaDTO.setIdTimeVisitante(18L);

        when(clubeRepository.findById(anyLong())).thenReturn(Optional.empty());


        Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.updatePartida(partidaDTO, id);
        });

        verify(partidaRepository, never()).save(any(Partida.class));
    }

    @Test
    void deactivatePartida_WhenPartidaNotExists_ThrowsException() {
        Long idInexistente = 999L;
        when(partidaRepository.findById(idInexistente)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.deactivatePartida(idInexistente);
        });

        verify(partidaRepository, never()).delete(any(Partida.class));
    }

}
