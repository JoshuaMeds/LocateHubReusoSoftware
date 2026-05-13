package com.locatehub.demo.service;

import com.locatehub.demo.model.Avaliacao;
import com.locatehub.demo.model.Reserva;
import com.locatehub.demo.repository.AvaliacaoRepository;
import com.locatehub.demo.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final ReservaRepository reservaRepository;

    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository, ReservaRepository reservaRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.reservaRepository = reservaRepository;
    }

    public Avaliacao avaliarReserva(Long reservaId, Integer nota, String comentario) {
        Optional<Reserva> reservaOpt = reservaRepository.findById(reservaId);

        if (reservaOpt.isEmpty()) {
            throw new IllegalArgumentException("Reserva não encontrada.");
        }

        Reserva reserva = reservaOpt.get();

        // Valida se a reserva foi finalizada usando a lógica do outro dev
        if (reserva.isCancelada() || !LocalDate.now().isAfter(reserva.getDataFim())) {
            throw new IllegalStateException("Apenas reservas finalizadas podem ser avaliadas.");
        }

        if (avaliacaoRepository.findByReserva_Id(reservaId).isPresent()) {
            throw new IllegalStateException("Esta reserva já foi avaliada.");
        }

        if (nota < 1 || nota > 5) {
            throw new IllegalArgumentException("A nota deve ser entre 1 e 5.");
        }

        Avaliacao avaliacao = new Avaliacao(reserva, nota, comentario);
        return avaliacaoRepository.save(avaliacao);
    }
}