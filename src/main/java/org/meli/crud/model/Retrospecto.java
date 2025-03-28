package org.meli.crud.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor

@Table(name = "retrospecto")
public class Retrospecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_clube", nullable = false)
    private Long idClube;

    @Column(name = "vitorias", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int vitorias;

    @Column(name = "empates", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int empates;

    @Column(name = "derrotas", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int derrotas;

    @Column(name = "gols_feitos", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int golsFeitos;

    @Column(name = "gols_sofridos", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int golsSofridos;
}
