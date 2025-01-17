package org.meli.crud.repository;

import org.meli.crud.model.Clube;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubeRepository extends JpaRepository<Clube, Long> {
    boolean existsByNomeAndEstado(String nome, String estado);
    Optional<Clube> findById(Long id);
}
