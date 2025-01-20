package org.meli.crud.repository;

import org.meli.crud.model.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {
    boolean existsById(Long id);
    Optional<Partida> findTopByTimeCasaIdOrderByDataHoraPartidaDesc(Long idTimeCasa);
    Optional<Partida> findTopByTimeVisitanteIdOrderByDataHoraPartidaDesc(Long idTimeVisitante);
    Optional<Partida> findTopByEstadioIdOrderByDataHoraPartidaDesc(Long estadioId);

}