package org.meli.crud.controller;
import org.meli.crud.dto.ClubeDTO;
import org.meli.crud.model.Clube;
import org.meli.crud.service.ClubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//define que é uma controller
@RestController
//Define a minha URI da  controller
@RequestMapping("/clube")
public class ClubeController {
    @Autowired
    private ClubeService clubeService;

    @PostMapping
    public ResponseEntity<String> createClube(@RequestBody ClubeDTO clube) {
       if (!this.clubeService.validarNome(clube.getNome())){
           return new ResponseEntity<>("O nome do clube é obrigatorio e precisa ter mais de 2 letras!", HttpStatus.BAD_REQUEST);
       }
        this.clubeService.createClube(clube);
        return new ResponseEntity<>("O clube foi criado com sucesso!", HttpStatus.CREATED);
    }

    @PutMapping ("update/{id}")
    public ResponseEntity<String> updateClube(@RequestBody ClubeDTO clube, @PathVariable String id) {
        if (!this.clubeService.validarNome(clube.getNome())){
            return new ResponseEntity<>("O nome do clube precisa ter ao menos duas 2 letras!", HttpStatus.BAD_REQUEST);
        }
        //NOTA: adicionar uma validacao para caso a data de fundacao seja atualizda para uma data apos alguma partida
        else if (!this.clubeService.validarEstado(clube.getEstado())) {
           return new ResponseEntity<>("A sigla do estado deve ser uma sigla válida!", HttpStatus.BAD_REQUEST);
       } else if (!this.clubeService.validarDataFundacao(clube.getFundacao())) {
           return new ResponseEntity<>("A data da fundacao precisa ser menor que a data de hoje!", HttpStatus.BAD_REQUEST);
       } else{
            this.clubeService.updateClube(clube, Long.valueOf(id));
            return new ResponseEntity<>("O clube foi atualizado com sucesso!", HttpStatus.OK);
       }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteClube(@PathVariable String id) {
        return new ResponseEntity<>("O clube foi atualizado com sucesso!", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public String getMessage(@PathVariable Long id) {
        return "ID: " + id;
    }
}


