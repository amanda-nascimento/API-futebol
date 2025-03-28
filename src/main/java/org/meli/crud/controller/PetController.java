package org.meli.crud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.meli.crud.model.Pet;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/pet")
public class PetController {

    Set<Pet> listaDePets = new HashSet();
    Pet pepper = new Pet("Pepper", "Gato");
    Pet maple = new Pet("Maple", "Cachorro");
    Pet momo = new Pet("Momo", "Cachorro");


    @GetMapping
    public ResponseEntity<Map<String, Object>> getPets(
            @RequestParam(required = false, defaultValue = "200") int status
    ) {

        listaDePets.add(maple);
        listaDePets.add(momo);
        listaDePets.add(pepper);

        Map<String, Object> response = new HashMap<>();
        response.put("Pets:", listaDePets);

        switch (status) {
            case 201:
                return new ResponseEntity<>(HttpStatus.CREATED);
            case 204:
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            case 404:
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            case 400:
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            case 409:
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            case 500:
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            default:
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

    }

    @Operation(summary = "Cria um novo pet", description = "Adiciona um novo pet à lista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pet criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<Pet> createPet(
            @RequestBody Pet pet,
            @RequestParam(value = "statusCode", defaultValue = "201") int statusCode) {

        switch (statusCode) {
            case 400:
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            case 500:
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            default:
                listaDePets.add(pet);
                return new ResponseEntity<>(pet, HttpStatus.CREATED);
        }
    }
}
