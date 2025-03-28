package org.meli.crud.controller;

import org.meli.crud.dto.RetrospectoDTO;
import org.meli.crud.service.RetrospectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/retrospecto")
public class RetrospectoController {
    @Autowired
    RetrospectoService retrospectoService;

    @GetMapping("/geral-por-clube/{id}")
    public ResponseEntity<RetrospectoDTO> getRetrospecto(@PathVariable Long id) {
        RetrospectoDTO retrospecto = this.retrospectoService.getRetrospectoPorClube(Long.valueOf(id));
        return new ResponseEntity<>(retrospecto, HttpStatus.OK);
    }
}
