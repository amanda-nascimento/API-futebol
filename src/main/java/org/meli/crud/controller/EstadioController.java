package org.meli.crud.controller;

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

    @PostMapping
    public ResponseEntity<String> createEstadio(@RequestBody EstadioDTO estadioDTO) {
        this.estadioService.createEstadio(estadioDTO);
        return new ResponseEntity<>("Estádio cadastrado com sucesso!", HttpStatus.CREATED);
    }

    @PutMapping("atualizar/{id}")
    public ResponseEntity<String> updateEstadio(@RequestBody EstadioDTO estadioDTO, @PathVariable Long id) {
        this.estadioService.updateEstadio(estadioDTO, id);
        return new ResponseEntity<>("Estádio atualizado com sucesso!", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadioDTO> getEstadio(@RequestBody @PathVariable Long id) {
        EstadioDTO estadioDTO = this.estadioService.getEstadio(Long.valueOf(id));
        return new ResponseEntity<>(estadioDTO, HttpStatus.OK);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<EstadioDTO>> getAllEstadios() {
        List<EstadioDTO> lista = this.estadioService.getAllEstadios() ;
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

//    @DeleteMapping("inativar/{id}")
//    public ResponseEntity<String> deactivateClube(@PathVariable String id) {
//        this.estadioService.deleteEstadio(Long.valueOf(id));
//        return new ResponseEntity<>("O estádio foi excluído com sucesso!", HttpStatus.NO_CONTENT);
//    }
}
