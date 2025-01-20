package org.meli.crud.controller;

import org.meli.crud.dto.EstadioDTO;
import org.meli.crud.dto.PartidaDTO;
import org.meli.crud.service.PartidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
