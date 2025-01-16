package org.meli.crud.service;

import org.meli.crud.dto.ClubeDTO;
import org.meli.crud.dto.EstadioDTO;
import org.meli.crud.model.Clube;
import org.meli.crud.model.Estadio;
import org.meli.crud.repository.EstadioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class EstadioService {
    private final EstadioRepository estadioRepository;

    public EstadioService(EstadioRepository estadioRepository) {
        this.estadioRepository = estadioRepository;
    }

    public void createEstadio(EstadioDTO estadioDTO) {
        if(!estadioRepository.existsByNome(estadioDTO.getNome())) {
            Estadio estadio = new Estadio();
            estadio.setNome(estadioDTO.getNome());
            this.estadioRepository.save(estadio);
        }
        else{
            throw new ResponseStatusException(HttpStatus.CONFLICT , "Já existe um estádio com o mesmo nome.");
        }
    }

    public void updateEstadio(EstadioDTO estadioDTO, Long id) {
        validarNome(estadioDTO.getNome());
        if(estadioRepository.existsById(id)) {
            Estadio estadio = estadioRepository.findById(id).get();
            estadio.setNome(estadioDTO.getNome());

            if(!estadioRepository.existsByNome(estadioDTO.getNome())) {
                estadioRepository.save(estadio);
            }
            else{
                if(estadio.getId().equals(id)){
                    estadioRepository.save(estadio);
                }
                else{
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um estádio cadastrado com essas características.");
                }
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND , "O estádio não foi encontrado na base de dados.");
        }
    }

    public EstadioDTO getEstadio(Long id) {
        if(estadioRepository.existsById(id)) {
            Estadio estadio = estadioRepository.findById(id).get();
            EstadioDTO estadioDTO = new EstadioDTO();
            estadioDTO.setNome(estadio.getNome());
            return estadioDTO;
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND , "O clube não foi encontrado na base de dados.");
        }
    }

    //MÉTODOS DE VALIDACAO
    public void validarNome(String nome) {
        if(nome == null || nome.length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do estádio deve ser preenchido contendo ao menos 3 caracteres.");
        }
    }

    public List<EstadioDTO> getAllEstadios() {
        List<Estadio> estadios = estadioRepository.findAll();
        List<EstadioDTO> estadiosDTOs = new ArrayList<>();
        if (this.estadioRepository.findAll().toArray().length > 0) {
            for(Estadio item : estadios) {
                EstadioDTO estadioDTO = new EstadioDTO();
                estadioDTO.setNome(item.getNome());
                estadiosDTOs.add(estadioDTO);
            }
        }
        return estadiosDTOs;

    }
}
