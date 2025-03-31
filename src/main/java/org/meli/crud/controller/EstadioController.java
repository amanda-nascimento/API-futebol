package org.meli.crud.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Cria um novo estádio", description = "Adiciona um novo estádio ao sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estádio cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para criação do estádio")
    })
    @PostMapping
    public ResponseEntity<String> createEstadio(@RequestBody EstadioDTO estadioDTO) {
        this.estadioService.createEstadio(estadioDTO);
        return new ResponseEntity<>("Estádio cadastrado com sucesso!", HttpStatus.CREATED);
    }

    @Operation(summary = "Atualiza um estádio", description = "Atualiza os dados de um estádio existente pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estádio atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Estádio não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização")
    })
    @PutMapping("atualizar/{id}")
    public ResponseEntity<String> updateEstadio(@RequestBody EstadioDTO estadioDTO, @PathVariable Long id) {
        this.estadioService.updateEstadio(estadioDTO, id);
        return new ResponseEntity<>("Estádio atualizado com sucesso!", HttpStatus.OK);
    }

    @Operation(summary = "Busca um estádio pelo ID", description = "Retorna os dados de um estádio cadastrado pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estádio encontrado"),
            @ApiResponse(responseCode = "404", description = "Estádio não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EstadioDTO> getEstadio(@PathVariable Long id) {
        EstadioDTO estadioDTO = this.estadioService.getEstadio(id);
        return new ResponseEntity<>(estadioDTO, HttpStatus.OK);
    }

    @Operation(summary = "Lista todos os estádios", description = "Retorna uma lista de todos os estádios cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estádios retornada com sucesso")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<EstadioDTO>> getAllEstadios() {
        List<EstadioDTO> lista = this.estadioService.getAllEstadios();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }
}
