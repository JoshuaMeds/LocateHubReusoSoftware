package com.locatehub.demo.repository;

import com.locatehub.demo.model.ListaDesejos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ListaDesejosRepository extends JpaRepository<ListaDesejos, Long> {

    List<ListaDesejos> findByLocatario_Id(Long locatarioId);

    boolean existsByLocatario_IdAndAtivo_Id(Long locatarioId, Long ativoId);

    Optional<ListaDesejos> findByLocatario_IdAndAtivo_Id(Long locatarioId, Long ativoId);
}
