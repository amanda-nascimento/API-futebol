package org.meli.crud.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.meli.crud.dto.PartidaDTO;
import org.meli.crud.service.PartidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/partidas")
public class PartidaController {

    @Autowired
    private PartidaService partidaService;

    @Operation(summary = "Cria uma nova partida", description = "Adiciona uma nova partida ao sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Partida cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para criação da partida")
    })
    @PostMapping
    public ResponseEntity<String> createPartida(@RequestBody PartidaDTO partidaDTO) {
        this.partidaService.createPartida(partidaDTO);
        return new ResponseEntity<>("Partida cadastrada com sucesso!", HttpStatus.CREATED);
    }

    @Operation(summary = "Atualiza uma partida", description = "Atualiza os dados de uma partida existente pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partida atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Partida não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização")
    })
    @PutMapping("atualizar/{id}")
    public ResponseEntity<String> updatePartida(@RequestBody PartidaDTO partidaDTO, @PathVariable Long id) {
        this.partidaService.updatePartida(partidaDTO, Long.valueOf(id));
        return new ResponseEntity<>("Partida atualizada com sucesso!", HttpStatus.OK);
    }

    @Operation(summary = "Busca uma partida pelo ID", description = "Retorna os dados de uma partida cadastrada pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partida encontrada"),
            @ApiResponse(responseCode = "404", description = "Partida não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PartidaDTO> getPartida(@PathVariable Long id) {
        PartidaDTO partida = this.partidaService.getPartida(Long.valueOf(id));
        return new ResponseEntity<>(partida, HttpStatus.OK);
    }

    @Operation(summary = "Lista todas as partidas", description = "Retorna uma lista de todas as partidas cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de partidas retornada com sucesso")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<PartidaDTO>> getAllClube() {
        List<PartidaDTO> lista = this.partidaService.getAllPartidas();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(summary = "Exclui uma partida", description = "Remove uma partida do sistema pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Partida excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Partida não encontrada")
    })
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> deactivatePartida(@PathVariable Long id) {
        this.partidaService.deactivatePartida(Long.valueOf(id));
        return new ResponseEntity<>("A partida foi excluída com sucesso!", HttpStatus.NO_CONTENT);
    }
}
