package org.meli.crud.controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.meli.crud.dto.ClubeDTO;
import org.meli.crud.repository.ClubeRepository;
import org.meli.crud.service.ClubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Api(value = "Clube Controller", description = "Gerencia os clubes de futebol")
@RestController
public class ClubeController {
    @Autowired
    private ClubeService clubeService;
    @Autowired
    private ClubeRepository clubeRepository;

    @ApiOperation(value = "Cria um clube")
    @PostMapping
    public ResponseEntity<String> createClube(@RequestBody ClubeDTO clubeDTO) {
        this.clubeService.createClube(clubeDTO);
        return new ResponseEntity<>("O clube foi criado com sucesso!", HttpStatus.CREATED);
    }

    @ApiOperation(value = "Atualiza um clube", notes = "Retorna 200")
    @PutMapping("atualizar/{id}")
    public ResponseEntity<String> updateClube(@RequestBody ClubeDTO clube, @PathVariable String id) {
        this.clubeService.updateClube(clube, Long.valueOf(id));
        return new ResponseEntity<>("O clube foi atualizado com sucesso!", HttpStatus.OK);

    }

    @ApiOperation(value = "Inativa um clube", notes = "Retorna 204 no content")
    @DeleteMapping("inativar/{id}")
    public ResponseEntity<String> deactivateClube(@PathVariable String id) {
        this.clubeService.deactivateClube(Long.valueOf(id));
        return new ResponseEntity<>("O clube foi inativado com sucesso!", HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Retorna um glube", notes = "Retorna um clubes cadastrado")
    @GetMapping("/{id}")
    public ResponseEntity<ClubeDTO> getClube(@PathVariable String id) {
        ClubeDTO clubeDTO = this.clubeService.getClube(Long.valueOf(id));
        return new ResponseEntity<>(clubeDTO, HttpStatus.OK);
    }

    @ApiOperation(value = "Lista todos os clubes", notes = "Retorna uma lista de clubes cadastrados")
    @GetMapping("/listar/")
    public ResponseEntity<List<ClubeDTO>> getAllClube() {
        List<ClubeDTO> listaClubes = this.clubeService.getAllClubes() ;
        return new ResponseEntity<>(listaClubes, HttpStatus.OK);
    }
}


