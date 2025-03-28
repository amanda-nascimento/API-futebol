package org.meli.crud;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.meli.crud.model.Clube;
import org.meli.crud.repository.ClubeRepository;
import org.meli.crud.service.ClubeService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
public class ClubeServiceTest {

    @InjectMocks
    ClubeService clubeService;

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

}
