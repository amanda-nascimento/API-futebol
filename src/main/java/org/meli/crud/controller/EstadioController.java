package org.meli.crud.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.meli.crud.dto.EstadioDTO;
import org.meli.crud.service.EstadioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estadio")
@Api(value = "Estadio Controller", description = "Gerencia os estádios") // Define a API para o Swagger
public class EstadioController {

    @Autowired
    private EstadioService estadioService;

    @ApiOperation(value = "Cria um novo estádio", notes = "Adiciona um novo estádio ao sistema")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Estádio cadastrado com sucesso"),
            @ApiResponse(code = 400, message = "Dados inválidos para criação do estádio")
    })
    @PostMapping
    public ResponseEntity<String> createEstadio(@RequestBody EstadioDTO estadioDTO) {
        this.estadioService.createEstadio(estadioDTO);
        return new ResponseEntity<>("Estádio cadastrado com sucesso!", HttpStatus.CREATED);
    }

    @ApiOperation(value = "Atualiza um estádio", notes = "Atualiza os dados de um estádio existente pelo ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Estádio atualizado com sucesso"),
            @ApiResponse(code = 404, message = "Estádio não encontrado"),
            @ApiResponse(code = 400, message = "Dados inválidos para atualização")
    })
    @PutMapping("atualizar/{id}")
    public ResponseEntity<String> updateEstadio(@RequestBody EstadioDTO estadioDTO, @PathVariable Long id) {
        this.estadioService.updateEstadio(estadioDTO, id);
        return new ResponseEntity<>("Estádio atualizado com sucesso!", HttpStatus.OK);
    }

    @ApiOperation(value = "Busca um estádio pelo ID", notes = "Retorna os dados de um estádio cadastrado pelo ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Estádio encontrado"),
            @ApiResponse(code = 404, message = "Estádio não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EstadioDTO> getEstadio(@PathVariable Long id) {
        EstadioDTO estadioDTO = this.estadioService.getEstadio(id);
        return new ResponseEntity<>(estadioDTO, HttpStatus.OK);
    }

    @ApiOperation(value = "Lista todos os estádios", notes = "Retorna uma lista de todos os estádios cadastrados")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista de estádios retornada com sucesso")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<EstadioDTO>> getAllEstadios() {
        List<EstadioDTO> lista = this.estadioService.getAllEstadios();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }
}
