package org.meli.crud.service;

import org.meli.crud.dto.ClubeDTO;
import org.meli.crud.exception.ConflitosDadosException;
import org.meli.crud.exception.ErrosDeBaseDeDados;
import org.meli.crud.model.Clube;
import org.meli.crud.repository.ClubeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClubeService {
    private final ClubeRepository clubeRepository;

    public ClubeService(ClubeRepository clubeRepository) {
        this.clubeRepository = clubeRepository;
    }

    public void createClube(ClubeDTO clubeDTO) {
        validarNome(clubeDTO.getNome());
        validarEstado(clubeDTO.getEstado());
        validarDataFundacao(clubeDTO.getFundacao());

        if(!clubeRepository.existsByNomeAndEstado(clubeDTO.getNome(), clubeDTO.getEstado())) {
            Clube clube = new Clube();
            clube.setNome(clubeDTO.getNome());
            clube.setEstado(clubeDTO.getEstado());
            clube.setAtivo(clubeDTO.isAtivo());
            clube.setFundacao(clubeDTO.getFundacao());
            this.clubeRepository.save(clube);
        }
        else{
            throw new ResponseStatusException(HttpStatus.CONFLICT , "Já existe um clube com as mesmas características cadastrado.");
        }
    }

    public void updateClube(ClubeDTO clubeDTO, Long id) {
        validarNome(clubeDTO.getNome());
        validarEstado(clubeDTO.getEstado());
        validarDataFundacao(clubeDTO.getFundacao());

        if(clubeRepository.existsById(id)) {
            Clube clube = clubeRepository.findById(id).get();

            if(!clubeDTO.getNome().equals(clube.getNome()) && !clubeDTO.getNome().isBlank()) {
                clube.setNome(clubeDTO.getNome());
            }
            if(!clubeDTO.getEstado().equals(clube.getEstado()) && !clubeDTO.getEstado().isBlank()) {
                clube.setEstado(clubeDTO.getEstado());
            }

            if(!clubeDTO.getFundacao().equals(clube.getFundacao())) {
                clube.setFundacao(clubeDTO.getFundacao());
            }

            if(clubeRepository.existsByNomeAndEstado(clubeDTO.getNome(), clubeDTO.getEstado()) ) {
                clubeRepository.save(clube);
            }
            else{
                if(clube.getId().equals(id)){
                    clubeRepository.save(clube);
                }
                else{
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um clube cadastrado com essas características.");
                }
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND , "O clube não foi encontrado na base de dados.");
        }
    }

    public void deactivateClube(Long id) {
        if(clubeRepository.existsById(id)) {
            Clube clube = clubeRepository.findById(id).get();
            clube.setAtivo(false);
            clubeRepository.save(clube);
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND , "O clube não foi encontrado na base de dados.");
        }
    }

    public ClubeDTO getClube(Long id) {
        if(clubeRepository.existsById(id)) {
            Clube clube = clubeRepository.findById(id).get();
            ClubeDTO clubeDTO = new ClubeDTO();
            clubeDTO.setNome(clube.getNome());
            clubeDTO.setEstado(clube.getEstado());
            clubeDTO.setFundacao(clube.getFundacao());
            clubeDTO.setAtivo(clube.isAtivo());
            return clubeDTO;
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND , "O clube não foi encontrado na base de dados.");
        }


    }

    public List<ClubeDTO> getAllClubes() {
        List<Clube> clubes = clubeRepository.findAll();
        List<ClubeDTO> clubeDTOs = new ArrayList<>();
        if (this.clubeRepository.findAll().toArray().length > 0) {
            for(Clube clube : clubes) {
                ClubeDTO clubeDTO = new ClubeDTO();
                clubeDTO.setNome(clube.getNome());
                clubeDTO.setEstado(clube.getEstado());
                clubeDTO.setFundacao(clube.getFundacao());
                clubeDTO.setAtivo(clube.isAtivo());
                clubeDTOs.add(clubeDTO);
            }
        }
        return clubeDTOs;

    }


    //MÉTODOS DE VALIDACAO
    public void validarNome(String nome) {
        if(nome == null || nome.length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome deve ser preenchido contendo ao menos 3 caracteres.");
        }
    }

    public void validarEstado(String estado) {
        List<String> estados = List.of(
                "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
        );

        boolean isSiglaExistente = estados.contains(estado.toUpperCase());

        if (!isSiglaExistente && estado.length() != 2){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O estado deve conter duas letras e deve ser um estado válido, como SP.");
        }
    }

    public void validarDataFundacao(LocalDate dataFundacao) {
        if(dataFundacao == null || dataFundacao.isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data deve ser menor ou igual a data de hoje.");
        }
    }

}
