package org.meli.crud.model;
//Jakarta conjunto de especificações e APIs para o desenvolvimento de aplicações em Java
import jakarta.persistence.*;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

@Table(name = "clube")
public class Clube {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String nome;

    @Column(length = 2)
    private String estado;

    @Column()
    private LocalDate fundacao;

    @Column()
    private boolean ativo;
}
