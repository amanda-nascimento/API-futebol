package org.meli.crud.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.meli.crud.ENUM.TimeVencedor;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter

@Table(name = "partida")
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataHoraPartida;

    @ManyToOne
    @JoinColumn(name = "id_estadio", nullable = false)
    private Estadio estadio;

    @ManyToOne
    @JoinColumn(name = "id_time_casa", nullable = false)
    private Clube timeCasa;

    @ManyToOne
    @JoinColumn(name = "id_time_visitante", nullable = false)
    private Clube timeVisitante;

    @Column(length = 3, nullable = false)
    private int saldoGolTimeCasa;

    @Column(length = 3, nullable = false)
    private int saldoGolTimeVisitante;

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private TimeVencedor resultado;
}
