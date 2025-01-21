package org.meli.crud.controller;

import org.meli.crud.dto.EstadioDTO;
import org.meli.crud.dto.PartidaDTO;
import org.meli.crud.service.PartidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("partidas")
public class PartidaController {

    @Autowired
    private PartidaService partidaService;


    @PostMapping
    public ResponseEntity<String> createPartida(@RequestBody PartidaDTO  partidaDTO) {
        this.partidaService.createPartida(partidaDTO);
        return new ResponseEntity<>("Partida cadastrado com sucesso!", HttpStatus.CREATED);
    }

    @PutMapping("atualizar/{id}")
    public ResponseEntity<String> updatePartida(@RequestBody PartidaDTO  partidaDTO , @PathVariable Long id) {
        this.partidaService.updatePartida(partidaDTO, id);
        return new ResponseEntity<>("Partida atualizada com sucesso!", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getPartida(@RequestBody  @PathVariable Long id) {
        this.partidaService.getPartida(id);
        return new ResponseEntity<>("Partida atualizada com sucesso!", HttpStatus.CREATED);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<PartidaDTO>> getAllClube() {
        List<PartidaDTO> lista = this.partidaService.getAllPartidas() ;
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @DeleteMapping("excluir/{id}")
    public ResponseEntity<String> deactivatePartida(@PathVariable String id) {
        this.partidaService.deactivatePartida(Long.valueOf(id));
        return new ResponseEntity<>("O clube foi inativado com sucesso!", HttpStatus.NO_CONTENT);
    }

}
