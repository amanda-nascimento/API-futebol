package org.meli.crud.dto;

import lombok.Getter;
import lombok.Setter;
import org.meli.crud.ENUM.TimeVencedor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter

public class PartidaDTO {

    private Long idTimeCasa;
    private Long idTimeVisitante;
    private Long idEstadio;
    private LocalDateTime dataHoraPartida;
    private int saldoGolsTimeCasa;
    private int saldoGolsTimeVisitante;
    private TimeVencedor resultado;

}
