package com.locatehub.demo.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.locatehub.demo.model.Reserva;

import jakarta.persistence.LockModeType;

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

    @Query("""
            select coalesce(sum(r.valorTotal), 0)
            from Reserva r
            where r.locatario.id = :locatarioId
              and r.cancelada = false
            """)
    BigDecimal somarGastoTotalLocatario(@Param("locatarioId") Long locatarioId);

    @Query("""
            select coalesce(sum(r.valorTotal), 0)
            from Reserva r
            where r.locatario.id = :locatarioId
              and r.cancelada = false
              and r.dataInicio between :inicio and :fim
            """)
    BigDecimal somarGastoNoPeriodoLocatario(
            @Param("locatarioId") Long locatarioId,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );

    @Query("""
            select coalesce(sum(r.valorTotal), 0)
            from Reserva r
            where r.ativo.donoId = :locadorId
              and r.cancelada = false
              and r.dataFim < :hoje
            """)
    BigDecimal somarReceitaRealizadaLocador(
            @Param("locadorId") Long locadorId,
            @Param("hoje") LocalDate hoje
    );

    @Query("""
            select coalesce(sum(r.valorTotal), 0)
            from Reserva r
            where r.ativo.donoId = :locadorId
              and r.cancelada = false
              and r.dataFim >= :hoje
            """)
    BigDecimal somarReceitaProjetadaLocador(
            @Param("locadorId") Long locadorId,
            @Param("hoje") LocalDate hoje
    );

    @Query("""
            select r from Reserva r
            where r.ativo.donoId = :locadorId
            order by r.dataInicio desc
            """)
    List<Reserva> findByLocadorId(@Param("locadorId") Long locadorId);
}
