package org.meli.crud;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.meli.crud.dto.EstadioDTO;
import org.meli.crud.model.Estadio;
import org.meli.crud.repository.EstadioRepository;
import org.meli.crud.service.EstadioService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EstadioServiceTest {
    @InjectMocks
    private EstadioService estadioService;

    @Mock
    private EstadioRepository estadioRepository;

    @Test
    void createEstadio_Success() {
        when(estadioRepository.existsByNome(anyString())).thenReturn(Boolean.FALSE);
        EstadioDTO estadioDTO = new EstadioDTO();
        String randomString = UUID.randomUUID().toString();
        estadioDTO.setNome(randomString);

        assertDoesNotThrow(() ->{
            estadioService.createEstadio(estadioDTO);
        });
    }

    @Test
    void createEstadio_ErrorWhenEstadioExists() {
        when(estadioRepository.existsByNome(anyString())).thenReturn(Boolean.TRUE);
        EstadioDTO estadioDTO = new EstadioDTO();
        String randomString = UUID.randomUUID().toString();
        estadioDTO.setNome(randomString);

        ResponseStatusException erro =  Assertions.assertThrows(ResponseStatusException.class, () -> {
            estadioService.createEstadio(estadioDTO);
        });

        assertEquals("Já existe um estádio com o mesmo nome.", erro.getReason());
    }


    @Test
    void updateEstadio_Success() {
        when(estadioRepository.existsByNome(anyString())).thenReturn(Boolean.FALSE);
        when(estadioRepository.findById(anyLong())).thenReturn(Optional.of(new Estadio()));
        EstadioDTO estadioDTO = new EstadioDTO();
        String randomString = UUID.randomUUID().toString();
        estadioDTO.setNome(randomString);


        assertDoesNotThrow(() ->{
            estadioService.updateEstadio(estadioDTO,2L);
        });

    }

    @Test
    void updateEstadio_ErrorWhenEstadioDoesNotExist() {
        when(estadioRepository.existsByNome(anyString())).thenReturn(Boolean.TRUE);
        EstadioDTO estadioDTO = new EstadioDTO();
        String randomString = UUID.randomUUID().toString();
        estadioDTO.setNome(randomString);


        ResponseStatusException erro =  Assertions.assertThrows(ResponseStatusException.class, () -> {
            estadioService.createEstadio(estadioDTO);
        });

        assertEquals("Já existe um estádio com o mesmo nome.", erro.getReason());
    }

    @Test
    void updateEstadio_ErrorWhenEstadioIsEmpty() {
        when(estadioRepository.existsByNome(anyString())).thenReturn(Boolean.FALSE);
        when(estadioRepository.findById(anyLong())).thenReturn(Optional.empty());
        EstadioDTO estadioDTO = new EstadioDTO();
        String randomString = UUID.randomUUID().toString();
        estadioDTO.setNome(randomString);


        ResponseStatusException erro = assertThrows(ResponseStatusException.class, () -> {
            estadioService.updateEstadio(estadioDTO, 1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, erro.getStatusCode());
        assertEquals("O estádio não foi encontrado na base de dados.", erro.getReason());
    }

    @Test
    void getEstadio_Success() {
        Estadio estadio = new Estadio();
        estadio.setNome("Estádio Teste");

        when(estadioRepository.existsById(1L)).thenReturn(true);
        when(estadioRepository.findById(1L)).thenReturn(Optional.of(estadio));

        EstadioDTO estadioDTO = estadioService.getEstadio(1L);

        assertNotNull(estadioDTO);

        verify(estadioRepository, times(1)).existsById(1L);
        verify(estadioRepository, times(1)).findById(1L);
    }

    @Test
    void getEstadio_ErrorIfNotFound() {
        when(estadioRepository.existsById(1L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            estadioService.getEstadio(1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("O estadio não foi encontrado na base de dados.", exception.getReason());

        verify(estadioRepository, times(1)).existsById(1L);
        verify(estadioRepository, never()).findById(anyLong());
    }

    @Test
    void getAllEstadios_Success() {
        when(estadioRepository.findAll()).thenReturn(List.of(new Estadio()));

        List<EstadioDTO> estadiosDTOs = estadioService.getAllEstadios();

        assertNotNull(estadiosDTOs);
        assertEquals(1, estadiosDTOs.size());
    }

    @Test
    void getAllEstadios_EmptyList() {
        when(estadioRepository.findAll()).thenReturn(new ArrayList<>());
        List<EstadioDTO> estadiosDTOs;

        assertDoesNotThrow(() ->{
            estadioService.getAllEstadios();
        });
    }

    @Test
    void validarNome_Success() {
        assertDoesNotThrow(() -> estadioService.validarNome("Maracanã"));
    }

    @Test
    void validarNome_ErrorIfNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            estadioService.validarNome(null);
        });
        assertEquals("400 BAD_REQUEST \"O nome do estádio deve ser preenchido contendo ao menos 3 caracteres.\"", exception.getMessage());
    }

    @Test
    void validarNome_ErrorIfLessThanFourCharacters() {
        ResponseStatusException exception1 = assertThrows(ResponseStatusException.class, () -> {
            estadioService.validarNome("A");
        });

        ResponseStatusException exception2 = assertThrows(ResponseStatusException.class, () -> {
            estadioService.validarNome("AB");
        });

        ResponseStatusException exception3 = assertThrows(ResponseStatusException.class, () -> {
            estadioService.validarNome("ABC");
        });

        assertEquals("400 BAD_REQUEST \"O nome do estádio deve ser preenchido contendo ao menos 3 caracteres.\"", exception1.getMessage());
        assertEquals("400 BAD_REQUEST \"O nome do estádio deve ser preenchido contendo ao menos 3 caracteres.\"", exception2.getMessage());
        assertEquals("400 BAD_REQUEST \"O nome do estádio deve ser preenchido contendo ao menos 3 caracteres.\"", exception3.getMessage());
    }

}
