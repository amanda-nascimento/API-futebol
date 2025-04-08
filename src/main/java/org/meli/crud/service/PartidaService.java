package org.meli.crud.service;

import org.meli.crud.ENUM.TimeVencedor;
import org.meli.crud.dto.EstadioDTO;
import org.meli.crud.dto.PartidaDTO;
import org.meli.crud.model.Clube;
import org.meli.crud.model.Estadio;
import org.meli.crud.model.Partida;
import org.meli.crud.model.Retrospecto;
import org.meli.crud.repository.ClubeRepository;
import org.meli.crud.repository.EstadioRepository;
import org.meli.crud.repository.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Autowired
    private RetrospectoService retrospectoService;

    public void createPartida(PartidaDTO partidaDTO) {
        validarPartidaDTO(partidaDTO);

        Clube timeCasa = clubeRepository.findById(partidaDTO.getIdTimeCasa())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Time casa não encontrado."));

        Clube timeVisitante = clubeRepository.findById(partidaDTO.getIdTimeVisitante())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Time visitante não encontrado."));

        Estadio estadio = estadioRepository.findById(partidaDTO.getIdEstadio())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estádio não encontrado."));

        Partida partida = construirPartida(partidaDTO, timeCasa, timeVisitante, estadio);

        partidaRepository.save(partida);
        retrospectoService.recordRetrospecto(partida);
    }

    private void validarPartidaDTO(PartidaDTO partidaDTO) {
        if (partidaDTO.getIdTimeVisitante() == null || partidaDTO.getIdTimeVisitante() == 0 ||
                partidaDTO.getIdTimeCasa() == null || partidaDTO.getIdTimeCasa() == 0 ||
                partidaDTO.getResultado() == null || partidaDTO.getDataHoraPartida() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Os dados mínimos são necessários para criar uma partida. Campos esperados: idTimeVisitante, idTimeCasa, resultado, dataHoraPartida.");
        }

        validarTimes(partidaDTO.getIdTimeCasa(), partidaDTO.getIdTimeVisitante());
        validarEstadio(partidaDTO.getIdEstadio());
        validarResultado(partidaDTO.getSaldoGolsTimeCasa(), partidaDTO.getSaldoGolsTimeVisitante());
        verificarDataHora(partidaDTO.getDataHoraPartida());
        verificarConflitoAgenda(partidaDTO.getIdTimeCasa(), partidaDTO.getIdTimeVisitante(), partidaDTO.getDataHoraPartida());
        verificarDisponibilidadeEstadio(partidaDTO.getIdEstadio(), partidaDTO.getDataHoraPartida());
        validarDataPartidaEdataFundacaoClube(partidaDTO.getDataHoraPartida(), partidaDTO.getIdTimeCasa(), partidaDTO.getIdTimeVisitante());
    }

    private Partida construirPartida(PartidaDTO partidaDTO, Clube timeCasa, Clube timeVisitante, Estadio estadio) {
        Partida partida = new Partida();

        partida.setTimeCasa(timeCasa);
        partida.setTimeVisitante(timeVisitante);
        partida.setEstadio(estadio);
        partida.setDataHoraPartida(partidaDTO.getDataHoraPartida());
        partida.setSaldoGolTimeCasa(partidaDTO.getSaldoGolsTimeCasa());
        partida.setSaldoGolTimeVisitante(partidaDTO.getSaldoGolsTimeVisitante());

        partida.setResultado(definirResultado(partidaDTO));

        return partida;
    }

    private TimeVencedor definirResultado(PartidaDTO partidaDTO) {
        if (partidaDTO.getResultado() != null) {
            return partidaDTO.getResultado();
        }

        return (partidaDTO.getSaldoGolsTimeCasa() > partidaDTO.getSaldoGolsTimeVisitante()) ? TimeVencedor.TIME_CASA :
                (partidaDTO.getSaldoGolsTimeCasa() < partidaDTO.getSaldoGolsTimeVisitante()) ? TimeVencedor.TIME_VISITANTE :
                        TimeVencedor.EMPATE;
    }


    public void updatePartida(PartidaDTO partidaDTO, Long id) {
        validarPartidaDTO(partidaDTO);

        Partida partida = partidaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida não encontrada com o ID informado."));

        Clube timeCasa = clubeRepository.findById(partidaDTO.getIdTimeCasa())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time da casa não encontrado."));
        Clube timeVisitante = clubeRepository.findById(partidaDTO.getIdTimeVisitante())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time visitante não encontrado."));
        Estadio estadio = estadioRepository.findById(partidaDTO.getIdEstadio())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estádio não encontrado."));

        partida.setTimeCasa(timeCasa);
        partida.setTimeVisitante(timeVisitante);
        partida.setEstadio(estadio);
        partida.setDataHoraPartida(partidaDTO.getDataHoraPartida());
        partida.setSaldoGolTimeCasa(partidaDTO.getSaldoGolsTimeCasa());
        partida.setSaldoGolTimeVisitante(partidaDTO.getSaldoGolsTimeVisitante());
        partida.setResultado(partidaDTO.getResultado());

        partidaRepository.save(partida);
        retrospectoService.recordRetrospecto(partida);
    }


    public PartidaDTO getPartida(Long id) {
        Partida partida = partidaRepository.findById(id).orElse(null);
        if (partida == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A partida nao foi encontrada.");
        }

        PartidaDTO partidaDTO = new PartidaDTO();

        Clube timeCasa = clubeRepository.findById(partida.getTimeCasa().getId()).orElse(null);
        Clube timeVisitante = clubeRepository.findById(partida.getTimeVisitante().getId()).orElse(null);
        Estadio estadio = estadioRepository.findById(partida.getEstadio().getId()).orElse(null);

        partidaDTO.setIdTimeVisitante(timeVisitante != null ? timeVisitante.getId() : null);
        partidaDTO.setIdTimeCasa(timeCasa != null ? timeCasa.getId() : null);
        partidaDTO.setIdEstadio(estadio != null ? estadio.getId() : null);
        partidaDTO.setDataHoraPartida(partida.getDataHoraPartida());
        partidaDTO.setSaldoGolsTimeCasa(partida.getSaldoGolTimeCasa());
        partidaDTO.setSaldoGolsTimeVisitante(partida.getSaldoGolTimeVisitante());
        partidaDTO.setResultado(partida.getResultado());

        return partidaDTO;
    }

    public List<PartidaDTO> getAllPartidas() {
        List<Partida> partidas = partidaRepository.findAll();
        List<PartidaDTO> partidasDTOs = new ArrayList<>();
        if (this.partidaRepository.findAll().toArray().length > 0) {
            for(Partida item : partidas) {
                PartidaDTO partidaDTO = new PartidaDTO();
                Clube timeCasa = clubeRepository.findById(item.getTimeCasa().getId()).orElse(null);
                Clube timeVisitante = clubeRepository.findById(item.getTimeVisitante().getId()).orElse(null);
                Estadio estadio = estadioRepository.findById(item.getEstadio().getId()).orElse(null);

                partidaDTO.setIdTimeVisitante(timeVisitante != null ? timeVisitante.getId() : null);
                partidaDTO.setIdTimeCasa(timeCasa != null ? timeCasa.getId() : null);
                partidaDTO.setIdEstadio(estadio != null ? estadio.getId() : null);
                partidaDTO.setDataHoraPartida(item.getDataHoraPartida());
                partidaDTO.setSaldoGolsTimeCasa(item.getSaldoGolTimeCasa());
                partidaDTO.setSaldoGolsTimeVisitante(item.getSaldoGolTimeVisitante());
                partidaDTO.setResultado(item.getResultado());
                partidasDTOs.add(partidaDTO);
            }
        }
        return partidasDTOs;

    }

    public void deactivatePartida(Long id) {
        if(partidaRepository.existsById(id)) {
            Partida partida = partidaRepository.findById(id).get();
            partidaRepository.delete(partida);
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND , "A partida não foi encontrado na base de dados.");
        }
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

        Clube timeCasa = clubeRepository.findById(idTimeCasa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time da casa não encontrado."));
        Clube timeVisitante = clubeRepository.findById(idTimeVisitante)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time visitante não encontrado."));

        if (!timeCasa.isAtivo() || !timeVisitante.isAtivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Os times envolvidos precisam estar ativos.");
        }
    }

    public void validarEstadio(Long idEstadio) {
        if(!estadioRepository.existsById(idEstadio)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O estádio nao foi encontrado na base de dados.");
        }
    }

    public void validarResultado(Integer saldoGolTimeCasa, Integer saldoGolTimeVisitante) {
        if(saldoGolTimeCasa == null || saldoGolTimeVisitante == null || saldoGolTimeCasa < 0 || saldoGolTimeVisitante < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O saldo de gol nao pode ser nulo ou negativo. Preencha com um valor igual ou maior que zero.");
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
            Duration durationTimeCasa = Duration.between(ultimaDataCasa, dataHora);
            LocalDateTime ultimaDataVisitante = ultimaPartidaTimeVisitante.get().getDataHoraPartida();
            Duration durationTimeVisitante = Duration.between(ultimaDataVisitante, dataHora);

            if (durationTimeCasa.toHours() < 48 || durationTimeVisitante.toHours() < 48) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "A data e hora da partida precisam ser maior que 48 horas em relação à última partida dos times.");
            }
        }
    }


    public void verificarDisponibilidadeEstadio(Long idEstadio, LocalDateTime dataHoraPartida) {
        Optional<Partida> ultimaPartidaEstadio = this.partidaRepository.findTopByEstadioIdOrderByDataHoraPartidaDesc(idEstadio);
        if (ultimaPartidaEstadio.isPresent()) {
            LocalDateTime dataAlocacao = ultimaPartidaEstadio.get().getDataHoraPartida();
            LocalDate dataAlocacaoDia = dataAlocacao.toLocalDate();
            LocalDate dataPartidaDia = dataHoraPartida.toLocalDate();

            if (dataAlocacaoDia.isEqual(dataPartidaDia)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "O estádio já está alocado para este dia.");
            }
        }
    }

    public void validarDataPartidaEdataFundacaoClube(LocalDateTime dataHoraPartida, Long idTimeCasa, Long idTimeVisitante) {
        Clube timeCasa = clubeRepository.findById(idTimeCasa).get();
        Clube timeVisitante = clubeRepository.findById(idTimeVisitante).get();

        LocalDate dataFundacaoCasa = timeCasa.getFundacao();
        LocalDate dataFundacaoVisitante = timeVisitante.getFundacao();
        LocalDate dataPartida = dataHoraPartida.toLocalDate();

        if (dataFundacaoCasa.isEqual(dataPartida) || dataFundacaoVisitante.isEqual(dataPartida)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A data da partida não pode ser a mesma em relação à data de fundação dos clubes.");
        }
    }



}
