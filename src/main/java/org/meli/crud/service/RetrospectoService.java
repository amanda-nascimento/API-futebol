package org.meli.crud.service;

import jakarta.transaction.Transactional;
import org.meli.crud.dto.RetrospectoDTO;
import org.meli.crud.model.Partida;
import org.meli.crud.model.Retrospecto;
import org.meli.crud.repository.ClubeRepository;
import org.meli.crud.repository.RetrospectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class RetrospectoService {

    @Autowired
    private RetrospectoRepository retrospectoRepository;
    @Autowired
    private ClubeRepository clubeRepository;

    @Transactional
    public void recordRetrospecto(Partida partida) {
        Optional<Retrospecto> retrospectoOptionalCasa = retrospectoRepository.findRetrospectoByIdClubeIs(partida.getTimeCasa().getId());
        Retrospecto retrospectoTimeCasa;

        if (!retrospectoOptionalCasa.isPresent()) {
            retrospectoTimeCasa = new Retrospecto();
            retrospectoTimeCasa.setIdClube(partida.getTimeCasa().getId());
        } else {
            retrospectoTimeCasa = retrospectoOptionalCasa.get();
        }

        switch (partida.getResultado()) {
            case TIME_CASA:
                retrospectoTimeCasa.setVitorias(retrospectoTimeCasa.getVitorias() + 1);
                break;
            case EMPATE:
                retrospectoTimeCasa.setEmpates(retrospectoTimeCasa.getEmpates() + 1);
                break;
            case TIME_VISITANTE:
                retrospectoTimeCasa.setDerrotas(retrospectoTimeCasa.getDerrotas() + 1);
                break;
        }

        retrospectoTimeCasa.setGolsFeitos(retrospectoTimeCasa.getGolsFeitos() + partida.getSaldoGolTimeCasa());
        retrospectoTimeCasa.setGolsSofridos(retrospectoTimeCasa.getGolsSofridos() + partida.getSaldoGolTimeVisitante());
        System.out.println("Gols feitos (Casa): " + retrospectoTimeCasa.getGolsFeitos());
        System.out.println("Gols sofridos (Casa): " + retrospectoTimeCasa.getGolsSofridos());

        retrospectoRepository.save(retrospectoTimeCasa); // Update ou Create


        Optional<Retrospecto> retrospectoOptionalVisitante = retrospectoRepository.findRetrospectoByIdClubeIs(partida.getTimeVisitante().getId());
        Retrospecto retrospectoTimeVisitante;

        if (!retrospectoOptionalVisitante.isPresent()) {
            retrospectoTimeVisitante = new Retrospecto();
            retrospectoTimeVisitante.setIdClube(partida.getTimeVisitante().getId());
        } else {
            retrospectoTimeVisitante = retrospectoOptionalVisitante.get();
        }

        switch (partida.getResultado()) {
            case TIME_VISITANTE:
                retrospectoTimeVisitante.setVitorias(retrospectoTimeVisitante.getVitorias() + 1);
                break;
            case EMPATE:
                retrospectoTimeVisitante.setEmpates(retrospectoTimeVisitante.getEmpates() + 1);
                break;
            case TIME_CASA:
                retrospectoTimeVisitante.setDerrotas(retrospectoTimeVisitante.getDerrotas() + 1);
                break;
        }

        retrospectoTimeVisitante.setGolsFeitos(retrospectoTimeVisitante.getGolsFeitos() + partida.getSaldoGolTimeVisitante());
        retrospectoTimeVisitante.setGolsSofridos(retrospectoTimeVisitante.getGolsSofridos() + partida.getSaldoGolTimeCasa());
        retrospectoRepository.save(retrospectoTimeVisitante); // Update ou Create
    }

    public RetrospectoDTO getRetrospectoPorClube(Long id){
        if(clubeRepository.existsById(id)){
            RetrospectoDTO retrospectoDTO = new RetrospectoDTO();
            Retrospecto retrospecto = retrospectoRepository.findRetrospectoByIdClubeIs(id).get();

            retrospectoDTO.setDerrotas(retrospecto.getDerrotas());
            retrospectoDTO.setEmpates(retrospecto.getEmpates());
            retrospectoDTO.setVitorias(retrospecto.getVitorias());
            retrospectoDTO.setGolsSofridos(retrospecto.getGolsSofridos());
            retrospectoDTO.setGolsFeitos(retrospecto.getGolsFeitos());
            return retrospectoDTO;
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nao foi encontrado retrospecto para o clube indicado.");
        }
    }
}
