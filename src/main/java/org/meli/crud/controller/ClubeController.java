package org.meli.crud.controller;
import org.meli.crud.dto.ClubeDTO;
import org.meli.crud.repository.ClubeRepository;
import org.meli.crud.service.ClubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//define que é uma controller
@RestController
//Define a minha URI da  controller
@RequestMapping("/clube")
public class ClubeController {
    @Autowired
    private ClubeService clubeService;
    @Autowired
    private ClubeRepository clubeRepository;

    @PostMapping
    public ResponseEntity<String> createClube(@RequestBody ClubeDTO clubeDTO) {
        this.clubeService.createClube(clubeDTO);
        return new ResponseEntity<>("O clube foi criado com sucesso!", HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<String> updateClube(@RequestBody ClubeDTO clube, @PathVariable String id) {
        this.clubeService.updateClube(clube, Long.valueOf(id));
        return new ResponseEntity<>("O clube foi atualizado com sucesso!", HttpStatus.OK);

    }

    @DeleteMapping("inativar/{id}")
    public ResponseEntity<String> deactivateClube(@PathVariable String id) {
        this.clubeService.deactivateClube(Long.valueOf(id));
        return new ResponseEntity<>("O clube foi inativado com sucesso!", HttpStatus.NO_CONTENT);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubeDTO> getClube(@PathVariable String id) {
        ClubeDTO clubeDTO = this.clubeService.getClube(Long.valueOf(id));
        return new ResponseEntity<>(clubeDTO, HttpStatus.OK);

    }

    @GetMapping("/")
    public ResponseEntity<List<ClubeDTO>> getAllClube() {
        List<ClubeDTO> listaClubes = this.clubeService.getAllClubes() ;
        return new ResponseEntity<>(listaClubes, HttpStatus.OK);
    }
}


