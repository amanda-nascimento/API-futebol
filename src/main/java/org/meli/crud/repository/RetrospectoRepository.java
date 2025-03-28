package org.meli.crud.repository;

import org.meli.crud.model.Clube;
import org.meli.crud.model.Partida;
import org.meli.crud.model.Retrospecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RetrospectoRepository extends JpaRepository<Retrospecto, Long> {
    boolean existsById(Long id);
    Optional<Retrospecto> findRetrospectoByIdClubeIs(Long id);

}