package org.meli.crud.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.meli.crud.dto.EstadioDTO;
import org.meli.crud.service.EstadioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estadio")
public class EstadioController {

    @Autowired
    private EstadioService estadioService;


    @Operation(summary = "Criar estádio", description = "Este endpoint cria um estádio.")
    @ApiResponse(responseCode = "201", description = "Estádio cadastrado com sucesso!")
    @PostMapping
    public ResponseEntity<String> createEstadio(@RequestBody EstadioDTO estadioDTO) {
        this.estadioService.createEstadio(estadioDTO);
        return new ResponseEntity<>("Estádio cadastrado com sucesso!", HttpStatus.CREATED);
    }
    @Operation(summary = "Alterar estádio", description = "Este endpoint altera um estádio.")
    @ApiResponse(responseCode = "200", description = "Estádio atualizado com sucesso!")
    @PutMapping("atualizar/{id}")
    public ResponseEntity<String> updateEstadio(@RequestBody EstadioDTO estadioDTO, @PathVariable Long id) {
        this.estadioService.updateEstadio(estadioDTO, id);
        return new ResponseEntity<>("Estádio atualizado com sucesso!", HttpStatus.OK);
    }

    @Operation(summary = "Retornar estádio", description = "Este endpoint retorna um estádio.")
    @ApiResponse(responseCode = "200")
    @GetMapping("/{id}")
    public ResponseEntity<EstadioDTO> getEstadio(@RequestBody @PathVariable Long id) {
        EstadioDTO estadioDTO = this.estadioService.getEstadio(Long.valueOf(id));
        return new ResponseEntity<>(estadioDTO, HttpStatus.OK);
    }

    @Operation(summary = "Retornar todos os estádios", description = "Este endpoint retorna uma listagem de estádios.")
    @ApiResponse(responseCode = "200")
    @GetMapping("/listar")
    public ResponseEntity<List<EstadioDTO>> getAllEstadios() {
        List<EstadioDTO> lista = this.estadioService.getAllEstadios() ;
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

}
