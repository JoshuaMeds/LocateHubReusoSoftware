package com.locatehub.demo.repository;

import com.locatehub.demo.model.Ativo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtivoRepository extends JpaRepository<Ativo, Long> {
}
