package org.meli.crud.controller;
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

@RestController
@RequestMapping("/clube")
public class ClubeController {
    @Autowired
    private ClubeService clubeService;
    @Autowired
    private ClubeRepository clubeRepository;

    @Operation(summary = "Cria clube", description = "Este endpoint cria um clube.")
    @ApiResponse(responseCode = "201")
    @PostMapping
    public ResponseEntity<String> createClube(@RequestBody ClubeDTO clubeDTO) {
        this.clubeService.createClube(clubeDTO);
        return new ResponseEntity<>("O clube foi criado com sucesso!", HttpStatus.CREATED);
    }

    @Operation(summary = "Altera clube", description = "Este endpoint altera um clube.")
    @ApiResponse(responseCode = "200", description = "O clube foi atualizado com sucesso!")
    @PutMapping("atualizar/{id}")
    public ResponseEntity<String> updateClube(@RequestBody ClubeDTO clube, @PathVariable String id) {
        this.clubeService.updateClube(clube, Long.valueOf(id));
        return new ResponseEntity<>("O clube foi atualizado com sucesso!", HttpStatus.OK);

    }

    @Operation(summary = "Desativa clube", description = "Este endpoint desativa um clube.")
    @ApiResponse(responseCode = "204", description = "O clube foi inativado com sucesso!")
    @DeleteMapping("inativar/{id}")
    public ResponseEntity<String> deactivateClube(@PathVariable String id) {
        this.clubeService.deactivateClube(Long.valueOf(id));
        return new ResponseEntity<>("O clube foi inativado com sucesso!", HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Retorna um clube", description = "Este endpoint retorna um clube de acordo com o id passado via url.")
    @ApiResponse(responseCode = "200", description = "Retorna o clube")
    @GetMapping("/{id}")
    public ResponseEntity<ClubeDTO> getClube(@PathVariable String id) {
        ClubeDTO clubeDTO = this.clubeService.getClube(Long.valueOf(id));
        return new ResponseEntity<>(clubeDTO, HttpStatus.OK);
    }

    @Operation(summary = "Retorno da lista de clubes", description = "Este endpoint retorna uma lista de clubes cadastrados.")
    @ApiResponse(responseCode = "200")
    @GetMapping("/listar/")
    public ResponseEntity<List<ClubeDTO>> getAllClube() {
        List<ClubeDTO> listaClubes = this.clubeService.getAllClubes() ;
        return new ResponseEntity<>(listaClubes, HttpStatus.OK);
    }
}


