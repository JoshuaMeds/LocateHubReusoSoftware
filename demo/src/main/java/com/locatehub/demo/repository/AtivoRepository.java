package com.locatehub.demo.repository;

import com.locatehub.demo.model.Ativo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AtivoRepository extends JpaRepository<Ativo, Long> {
    List<Ativo> findByDonoId(Long donoId);
}
