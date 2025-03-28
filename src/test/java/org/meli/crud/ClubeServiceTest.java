package org.meli.crud;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.meli.crud.dto.ClubeDTO;
import org.meli.crud.repository.ClubeRepository;
import org.meli.crud.service.ClubeService;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ClubeServiceTest {

    @InjectMocks
    ClubeService clubeService;

    @Mock
    private ClubeRepository clubeRepository;

    @Mock
    private ClubeDTO clubeDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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
    void testCreateClube_Success() {
        ClubeDTO clubeDTO = new ClubeDTO();
        String randomName = UUID.randomUUID().toString();
        clubeDTO.setNome(randomName);
        clubeDTO.setFundacao(LocalDate.of(2000, 1, 1));
        clubeDTO.setEstado("SP");

        Mockito.when(clubeRepository.existsByNomeAndEstado(randomName, "SP")).thenReturn(false);

        clubeService.createClube(clubeDTO);

//        ArgumentCaptor<Clube> clubeCaptor = ArgumentCaptor.forClass(Clube.class);
//
//        Clube clubeSalvo = clubeCaptor.getValue();
//        assertEquals(randomName, clubeSalvo.getNome());
//        assertEquals("SP", clubeSalvo.getEstado());
//        assertTrue(clubeSalvo.isAtivo());
//        assertEquals(LocalDate.of(2000, 1, 1), clubeSalvo.getFundacao());
    }


}
