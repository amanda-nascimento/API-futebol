package org.meli.crud;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.meli.crud.dto.ClubeDTO;
import org.meli.crud.model.Clube;
import org.meli.crud.repository.ClubeRepository;
import org.meli.crud.service.ClubeService;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClubeServiceTest {

    @InjectMocks
    ClubeService clubeService;

    @Mock
    private ClubeRepository clubeRepository;

    @Mock
    private ClubeDTO clubeDTO;


    @Test
    void validaNomeClube_Success() throws Exception {
        String nome = "Santos";
        Assertions.assertDoesNotThrow(() -> {
            clubeService.validarNome(nome);
        });
    }

    @Test
    void validaNomeClube_Error() throws Exception{
        String nome = "Sa";
        ResponseStatusException erro = assertThrows(ResponseStatusException.class, () -> {
            clubeService.validarNome(nome);
        }) ;
        assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());
        assertEquals("O nome deve ser preenchido contendo ao menos 3 caracteres.", erro.getReason());
    }

    @Test
    void validarEstado_Success() {
        String estado = "SP";
        Assertions.assertDoesNotThrow(() -> clubeService.validarEstado(estado));
    }

    @Test
    void validarEstado_Error_2LetrasInvalido() {
        String estado = "SPP";
        ResponseStatusException erro = assertThrows(ResponseStatusException.class, () -> {
            clubeService.validarEstado(estado);
        });
        assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());
        assertEquals("O estado deve conter duas letras e deve ser um estado válido, como SP.", erro.getReason());
    }

    @Test
    void validarEstado_Error_1Letra() {
        String estado = "S";
        ResponseStatusException erro = assertThrows(ResponseStatusException.class, () -> {
            clubeService.validarEstado(estado);
        });
        assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());
        assertEquals("O estado deve conter duas letras e deve ser um estado válido, como SP.", erro.getReason());
    }

    @Test
    void validarEstado_Error_Invalido() {
        String estado = "XY";
        ResponseStatusException erro = assertThrows(ResponseStatusException.class, () -> {
            clubeService.validarEstado(estado);
        });
        assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());
        assertEquals("O estado deve conter duas letras e deve ser um estado válido, como SP.", erro.getReason());
    }

    @Test
    void createClube_Success() {
        ClubeDTO clubeTeste = new ClubeDTO();
        String randomString = UUID.randomUUID().toString();
        clubeTeste.setNome(randomString);
        clubeTeste.setEstado("SP");
        clubeTeste.setFundacao(LocalDate.of(1895, 11, 15));
        clubeTeste.setAtivo(true);

        when(clubeRepository.existsByNomeAndEstado(anyString(), anyString())).thenReturn(false);

        clubeService.createClube(clubeTeste);

        verify(clubeRepository, times(1)).save(any(Clube.class));
    }

    @Test
    void createClube_Error() {
        ClubeDTO clubeTeste = new ClubeDTO();
        String randomString = UUID.randomUUID().toString();
        clubeTeste.setNome(randomString);
        clubeTeste.setEstado("SP");
        clubeTeste.setFundacao(LocalDate.of(1895, 11, 15));
        clubeTeste.setAtivo(true);

        when(clubeRepository.existsByNomeAndEstado(anyString(), anyString())).thenReturn(true);
        ResponseStatusException erro = assertThrows(ResponseStatusException.class, () -> {
            clubeService.createClube(clubeTeste);
        });
        assertEquals("É esperado o nome, data de fundacao e status do clube.", erro.getReason());
    }

    @Test
    void updateClube_ErrorClubeWithoutName() {
        ClubeDTO clubeTeste = new ClubeDTO();
        clubeTeste.setEstado("SP");
        clubeTeste.setFundacao(LocalDate.of(1895, 11, 15));
        clubeTeste.setAtivo(true);

        ResponseStatusException erro = assertThrows(ResponseStatusException.class, () -> {
            clubeService.updateClube(clubeTeste, 9223372036854775807L);
        });
        assertEquals("O nome deve ser preenchido contendo ao menos 3 caracteres.", erro.getReason());
    }

    @Test
    void updateClube_Success() {
        ClubeDTO clubeTeste = new ClubeDTO();
        String randomString = UUID.randomUUID().toString();
        clubeTeste.setNome(randomString);
        clubeTeste.setEstado("SP");
        clubeTeste.setFundacao(LocalDate.of(1895, 11, 15));

        Clube clube = new Clube();
        clube.setNome(randomString);
        clube.setEstado("SP");
        clube.setFundacao(LocalDate.of(1895, 11, 15));
        clube.setAtivo(true);


        when(clubeRepository.findById(anyLong())).thenReturn(Optional.of(clube));
        when(clubeRepository.save(any(Clube.class))).thenReturn(clube);

        assertDoesNotThrow(() ->{
            clubeService.updateClube(clubeTeste, 12L);
        });

        verify(clubeRepository, times(1)).save(any(Clube.class));
    }

    @Test
    void updateClube_Error() {
        ClubeDTO clubeTeste = new ClubeDTO();
        String randomString = UUID.randomUUID().toString();
        clubeTeste.setNome(randomString);
        clubeTeste.setEstado("SP");
        clubeTeste.setFundacao(LocalDate.of(1895, 11, 15));

        when(clubeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            clubeService.updateClube(clubeTeste, 12L);
        });

    }

    @Test
    void deleteClube_Success() {

        when(clubeRepository.existsById(anyLong())).thenReturn(true);
        when(clubeRepository.findById(anyLong())).thenReturn(Optional.of(new Clube()));

        assertDoesNotThrow(() ->{
            clubeService.deactivateClube(12L);
        });
    }

    @Test
    void deleteClube_ErrorIfClubeNotFound() {
        when(clubeRepository.existsById(anyLong())).thenReturn(true);
        when(clubeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            clubeService.deactivateClube(12L);
        });
    }

    @Test
    void deleteClube_ErrorIfClubeNotExists() {
        when(clubeRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> {
            clubeService.deactivateClube(12L);
        });
    }

    @Test
    void getClube_Success() {

        when(clubeRepository.existsById(anyLong())).thenReturn(true);
        when(clubeRepository.findById(anyLong())).thenReturn(Optional.of(new Clube()));

        assertDoesNotThrow(() ->{
            clubeService.getClube(12L);
        });
    }

    @Test
    void getClube_ErrorIfClubeNotFound() {
        when(clubeRepository.existsById(anyLong())).thenReturn(true);
        when(clubeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            clubeService.getClube(12L);
        });
    }

    @Test
    void getClube_ErrorIfClubeNotExists() {
        when(clubeRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> {
            clubeService.getClube(12L);
        });
    }




    @Test
    void getAllClubes_Success() {
        when(clubeRepository.findAll()).thenReturn(List.of(new Clube()));

        assertDoesNotThrow(() ->{
            clubeService.getAllClubes();
        });
    }

    @Test
    void getAllClubes_ErrorIfClubesNotFound() {
        when(clubeRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ResponseStatusException.class, () -> {
            clubeService.getAllClubes();
        });
    }

}
