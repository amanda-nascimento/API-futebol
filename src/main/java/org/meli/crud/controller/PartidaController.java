package org.meli.crud.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.meli.crud.dto.ClubeDTO;
import org.meli.crud.dto.EstadioDTO;
import org.meli.crud.dto.PartidaDTO;
import org.meli.crud.service.PartidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("partidas")
public class PartidaController {

    @Autowired
    private PartidaService partidaService;

    @Operation(summary = "Criar partida", description = "Este endpoint cria uma partida com os clubes envolvidos.")
    @ApiResponse(responseCode = "201")
    @PostMapping
    public ResponseEntity<String> createPartida(@RequestBody PartidaDTO  partidaDTO) {
        this.partidaService.createPartida(partidaDTO);
        return new ResponseEntity<>("Partida cadastrado com sucesso!", HttpStatus.CREATED);
    }

    @Operation(summary = "Atualizar partida", description = "Este endpoint atualiza uma partida com os clubes envolvidos.")
    @ApiResponse(responseCode = "200")
    @PutMapping("atualizar/{id}")
    public ResponseEntity<String> updatePartida(@RequestBody PartidaDTO  partidaDTO , @PathVariable Long id) {
        this.partidaService.updatePartida(partidaDTO, Long.valueOf(id));
        return new ResponseEntity<>("Partida atualizada com sucesso!", HttpStatus.OK);
    }

    @Operation(summary = "Retornar partida", description = "Este endpoint retorna uma partida.")
    @ApiResponse(responseCode = "200")
    @GetMapping("/{id}")
    public ResponseEntity<PartidaDTO> getPartida(@PathVariable Long id) {
        PartidaDTO partida = this.partidaService.getPartida(Long.valueOf(id));
        return new ResponseEntity<>(partida, HttpStatus.OK);
    }

    @Operation(summary = "Listar partidas", description = "Este endpoint lista todas as  partidas.")
    @ApiResponse(responseCode = "200")
    @GetMapping("/listar")
    public ResponseEntity<List<PartidaDTO>> getAllClube() {
        List<PartidaDTO> lista = this.partidaService.getAllPartidas() ;
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(summary = "Excluir partida", description = "Este endpoint exclui uma partida com os clubes envolvidos.")
    @ApiResponse(responseCode = "204")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> deactivatePartida(@PathVariable Long id) {
        this.partidaService.deactivatePartida(Long.valueOf(id));
        return new ResponseEntity<>("A partida foi excluida com sucesso!", HttpStatus.NO_CONTENT);
    }

}
