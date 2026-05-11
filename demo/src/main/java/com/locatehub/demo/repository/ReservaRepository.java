package com.locatehub.demo.repository;

import com.locatehub.demo.model.Reserva;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByLocatario_IdOrderByDataInicioDesc(Long locatarioId);

    List<Reserva> findByAtivo_IdOrderByDataInicioAsc(Long ativoId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select r from Reserva r
            where r.ativo.id = :ativoId
              and r.cancelada = false
              and r.dataInicio < :dataFim
              and r.dataFim > :dataInicio
            """)
    List<Reserva> findConflitos(
            @Param("ativoId") Long ativoId,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );
}
