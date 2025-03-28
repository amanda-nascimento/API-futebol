package org.meli.crud.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.meli.crud.dto.PartidaDTO;
import org.meli.crud.service.PartidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("partidas")
@Api(value = "Partida Controller", description = "Gerencia as partidas de futebol") // Define a API para o Swagger
public class PartidaController {

    @Autowired
    private PartidaService partidaService;

    @ApiOperation(value = "Cria uma nova partida", notes = "Adiciona uma nova partida ao sistema")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Partida cadastrada com sucesso"),
            @ApiResponse(code = 400, message = "Dados inválidos para criação da partida")
    })
    @PostMapping
    public ResponseEntity<String> createPartida(@RequestBody PartidaDTO partidaDTO) {
        this.partidaService.createPartida(partidaDTO);
        return new ResponseEntity<>("Partida cadastrada com sucesso!", HttpStatus.CREATED);
    }

    @ApiOperation(value = "Atualiza uma partida", notes = "Atualiza os dados de uma partida existente pelo ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Partida atualizada com sucesso"),
            @ApiResponse(code = 404, message = "Partida não encontrada"),
            @ApiResponse(code = 400, message = "Dados inválidos para atualização")
    })
    @PutMapping("atualizar/{id}")
    public ResponseEntity<String> updatePartida(@RequestBody PartidaDTO partidaDTO, @PathVariable Long id) {
        this.partidaService.updatePartida(partidaDTO, Long.valueOf(id));
        return new ResponseEntity<>("Partida atualizada com sucesso!", HttpStatus.OK);
    }

    @ApiOperation(value = "Busca uma partida pelo ID", notes = "Retorna os dados de uma partida cadastrada pelo ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Partida encontrada"),
            @ApiResponse(code = 404, message = "Partida não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PartidaDTO> getPartida(@PathVariable Long id) {
        PartidaDTO partida = this.partidaService.getPartida(Long.valueOf(id));
        return new ResponseEntity<>(partida, HttpStatus.OK);
    }

    @ApiOperation(value = "Lista todas as partidas", notes = "Retorna uma lista de todas as partidas cadastradas")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista de partidas retornada com sucesso")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<PartidaDTO>> getAllClube() {
        List<PartidaDTO> lista = this.partidaService.getAllPartidas();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @ApiOperation(value = "Exclui uma partida", notes = "Remove uma partida do sistema pelo ID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Partida excluída com sucesso"),
            @ApiResponse(code = 404, message = "Partida não encontrada")
    })
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> deactivatePartida(@PathVariable Long id) {
        this.partidaService.deactivatePartida(Long.valueOf(id));
        return new ResponseEntity<>("A partida foi excluída com sucesso!", HttpStatus.NO_CONTENT);
    }
}
