package org.meli.crud.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ClubeDTO {
    private String nome;
    private String estado;
    private LocalDate fundacao;
    private boolean ativo;
}