package com.locatehub.demo.repository;

import com.locatehub.demo.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    Optional<Avaliacao> findByReserva_Id(Long reservaId);
}