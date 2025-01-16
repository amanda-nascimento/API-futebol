package org.meli.crud.repository;

import org.meli.crud.model.Estadio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadioRepository extends JpaRepository<Estadio, Long>  {
    boolean existsById(Long id);
    boolean existsByNome(String nome);

}
