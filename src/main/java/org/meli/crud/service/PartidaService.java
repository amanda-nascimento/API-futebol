package org.meli.crud.service;

import org.meli.crud.ENUM.TimeVencedor;
import org.meli.crud.dto.PartidaDTO;
import org.meli.crud.model.Clube;
import org.meli.crud.model.Estadio;
import org.meli.crud.model.Partida;
import org.meli.crud.repository.ClubeRepository;
import org.meli.crud.repository.EstadioRepository;
import org.meli.crud.repository.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PartidaService {
    @Autowired
    private ClubeRepository clubeRepository;
    @Autowired
    private EstadioRepository estadioRepository;
    @Autowired
    private PartidaRepository partidaRepository;

    public void createPartida(PartidaDTO partidaDTO) {
        validarTimes(partidaDTO.getIdTimeCasa(), partidaDTO.getIdTimeVisitante());
        validarEstadio(partidaDTO.getIdEstadio());
        validarResultado(partidaDTO.getSaldoGolsTimeCasa(), partidaDTO.getSaldoGolsTimeVisitante());
        verificarDataHora(partidaDTO.getDataHoraPartida());
        verificarConflitoAgenda(partidaDTO.getIdTimeCasa(), partidaDTO.getIdTimeVisitante(), partidaDTO.getDataHoraPartida());
        verificarDisponibilidadeEstadio(partidaDTO.getIdEstadio(), partidaDTO.getDataHoraPartida());

        Partida partida = new Partida();
        Clube timeCasa = clubeRepository.findById(partidaDTO.getIdTimeCasa()).get();
        Clube timeVisitante = clubeRepository.findById(partidaDTO.getIdTimeVisitante()).get();
        Estadio estadio = estadioRepository.findById(partidaDTO.getIdEstadio()).get();

        if(partidaDTO.getResultado() == null){
            if(partidaDTO.getSaldoGolsTimeCasa() > partidaDTO.getSaldoGolsTimeVisitante()){
                partida.setResultado(TimeVencedor.TIME_CASA);
            } else if (partidaDTO.getSaldoGolsTimeCasa() == partidaDTO.getSaldoGolsTimeVisitante()) {
                partida.setResultado(TimeVencedor.EMPATE);
            }else
            {
                partida.setResultado(TimeVencedor.TIME_VISITANTE);
            }
        }
        else{
            partida.setResultado(partidaDTO.getResultado());
        }

        partida.setTimeCasa(timeCasa);
        partida.setTimeVisitante(timeVisitante);
        partida.setEstadio(estadio);
        partida.setDataHoraPartida(partidaDTO.getDataHoraPartida());
        partida.setSaldoGolTimeCasa(partidaDTO.getSaldoGolsTimeCasa());
        partida.setSaldoGolTimeVisitante(partidaDTO.getSaldoGolsTimeVisitante());
        this.partidaRepository.save(partida);
    }

    public void validarTimes(Long idTimeCasa, Long idTimeVisitante) {
        if(!clubeRepository.existsById(idTimeCasa)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O Time da Casa nao foi encontrado na base de dados.");
        }
        if(!clubeRepository.existsById(idTimeVisitante)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O Time Visitante nao foi encontrado na base de dados.");
        }
        if(idTimeVisitante.equals(idTimeCasa)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Os dois times nao podem ser iguais.");
        }
    }

    public void validarEstadio(Long idEstadio) {
        if(!estadioRepository.existsById(idEstadio)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O estádio nao foi encontrado na base de dados.");
        }
    }

    public void validarResultado(Integer saldoGolTimeCasa, Integer saldoGolTimeVisitante) {
        if(saldoGolTimeCasa == null && saldoGolTimeVisitante == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O saldo de gol nao pode ser nulo. Preencha com um valor igual ou maior que zero.");
        }
    }

    public void verificarDataHora(LocalDateTime dataHora) {
        if(dataHora == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data e hora da partida nao pode ser nula.");
        }
    }


    public void verificarConflitoAgenda(Long idTimeCasa, Long idTimeVisitante, LocalDateTime dataHora) {
        Optional<Partida> ultimaPartidaTimeCasa = this.partidaRepository.findTopByTimeCasaIdOrderByDataHoraPartidaDesc(idTimeCasa);
        Optional<Partida> ultimaPartidaTimeVisitante = this.partidaRepository.findTopByTimeVisitanteIdOrderByDataHoraPartidaDesc(idTimeVisitante);


        if (ultimaPartidaTimeCasa.isPresent() && ultimaPartidaTimeVisitante.isPresent()) {
            LocalDateTime ultimaDataCasa = ultimaPartidaTimeCasa.get().getDataHoraPartida();
            LocalDateTime ultimaDataVisitante = ultimaPartidaTimeVisitante.get().getDataHoraPartida();

            Duration durationTimeCasa = Duration.between(ultimaDataCasa, dataHora);
            Duration durationTimeVisitante = Duration.between(ultimaDataVisitante, dataHora);

            if (durationTimeCasa.abs().toHours() < 48 && durationTimeVisitante.abs().toHours() < 48) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "A data e hora da partida precisam ser maior que 48 horas em relação à última partida jogada pelos times envolvidos.");
            }
        }
    }
    //VERIFICAR A DISPONIBILIDADE DO ESTADIO 
    public void verificarDisponibilidadeEstadio(Long idEstadio, LocalDateTime dataHoraPartida) {
        Optional<Partida> ultimaPartidaEstadio = this.partidaRepository.findTopByEstadioIdOrderByDataHoraPartidaDesc(idEstadio);
        if (ultimaPartidaEstadio.isPresent()) {
            LocalDateTime dataAlocacao = ultimaPartidaEstadio.get().getDataHoraPartida();

            if (dataAlocacao == dataHoraPartida) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "A data e hora da partida precisam ser maior que 48 horas em relação à última partida jogada pelos times envolvidos.");
            }
        }
    }



}
