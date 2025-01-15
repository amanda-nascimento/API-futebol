package org.meli.crud.service;

import org.meli.crud.dto.ClubeDTO;
import org.meli.crud.exception.ConflitosDadosException;
import org.meli.crud.exception.ErrosDeBaseDeDados;
import org.meli.crud.model.Clube;
import org.meli.crud.repository.ClubeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClubeService {
    private final ClubeRepository clubeRepository;

    public ClubeService(ClubeRepository clubeRepository) {
        this.clubeRepository = clubeRepository;
    }

    public void createClube(ClubeDTO clubeDTO) {
        //o controller lida com o DTO [clubeDTO] (entrada de dados da requisição)
        //o service transforma esse DTO em uma entidade (Clube) antes de salvar no banco de dados
        Clube clube = new Clube();

        clube.setNome(clubeDTO.getNome());
        clube.setEstado(clubeDTO.getEstado());
        clube.setAtivo(clubeDTO.isAtivo());
        clube.setFundacao(clubeDTO.getFundacao());

        // Salvar o clube no banco
        if(!clubeRepository.existsByNomeAndEstado(clubeDTO.getNome(), clubeDTO.getEstado())) {
            this.clubeRepository.save(clube);
        }
        else{
            throw new ConflitosDadosException("Já existe um clube com as mesmas características cadastrado.");
        }
    }

    public void updateClube(ClubeDTO clubeDTO, Long id) {
        if(clubeRepository.existsById(id)) {
            //recuperar objeto
            Clube clube = clubeRepository.findById(id).get();

            //PARA REFLEXAO E REFATORACAO: se eu tentar mudar os dados que nao sejam nome e estado, ele dará conflito porque ele apontará para ele mesmo no banco e nao permitira a atualizacao.
            if(clubeRepository.existsByNomeAndEstado(clubeDTO.getNome(), clubeDTO.getEstado()) && clubeRepository.existsById(id)) {
                if(!clubeDTO.getNome().equals(clube.getNome()) && !clubeDTO.getNome().isBlank()) {
                    clube.setNome(clubeDTO.getNome());
                }
                if(!clubeDTO.getEstado().equals(clube.getEstado()) && !clubeDTO.getEstado().isBlank()) {
                    clube.setEstado(clubeDTO.getEstado());
                }

                if(!clubeDTO.getFundacao().equals(clube.getFundacao())) {
                    clube.setFundacao(clubeDTO.getFundacao());
                }
                clubeRepository.save(clube);
            }
            else{
                throw new ConflitosDadosException("Já existe um clube com as mesmas características cadastrado.");
            }
        }
        else{
            throw new ErrosDeBaseDeDados("O clube indicado nao existe na base de dados");
        }
    }

    public boolean deactivateClube(Long id) {
        if(clubeRepository.existsById(id)) {
            Clube clube = clubeRepository.findById(id).get();
            clube.setAtivo(false);
            clubeRepository.save(clube);
            return true;
        }
        return false;
    }

    public ClubeDTO getClube(Long id) {

        Clube clube = clubeRepository.findById(id).get();
        ClubeDTO clubeDTO = new ClubeDTO();
        clubeDTO.setNome(clube.getNome());
        clubeDTO.setEstado(clube.getEstado());
        clubeDTO.setFundacao(clube.getFundacao());
        clubeDTO.setAtivo(clube.isAtivo());

        return clubeDTO;
    }




    //MÉTODOS DE VALIDACAO

    public Boolean validarNome(String nome) {
        return nome != null && nome.length() > 3;
    }

    public boolean validarEstado(String estado) {
        List<String> estados = List.of(
                "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
        );

        boolean isSiglaExistente = estados.contains(estado.toUpperCase());

        return isSiglaExistente && estado.length() == 2;
    }

    public Boolean validarDataFundacao(LocalDate dataFundacao) {
        return dataFundacao != null && !dataFundacao.isAfter(LocalDate.now());
    }
}
