package org.gvs.axis.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.gvs.axis.dto.request.ReservaRequest;
import org.gvs.axis.dto.response.ReservaResponse;
import org.gvs.axis.enums.StatusReserva;
import org.gvs.axis.model.Ambiente;
import org.gvs.axis.model.Reserva;
import org.gvs.axis.model.Usuario;
import org.gvs.axis.repository.AmbienteRepository;
import org.gvs.axis.repository.ReservaRepository;
import org.gvs.axis.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author vitor
 */
@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private AmbienteRepository ambienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public ReservaResponse criarReserva(ReservaRequest request, String email) {
        // Buscar usuário

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuário não encontrado"
        ));

        // Buscar ambiente
        Ambiente ambiente = ambienteRepository.findById(request.getAmbienteId())
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Ambiente não encontrado"
        ));

        // Calcular valor total
        double horasReservadas = ChronoUnit.MINUTES.between(
                request.getHoraInicio(),
                request.getHoraFim()
        ) / 60.0;

        BigDecimal valorTotal = BigDecimal.valueOf(ambiente.getValorHora().doubleValue() * horasReservadas);

        // Criar reserva
        Reserva reserva = new Reserva();
        reserva.setHoraFim(request.getHoraFim().plusMinutes(30));
        reserva.setHoraInicio(request.getHoraInicio());
        reserva.setStatus(StatusReserva.CONFIRMADA);
        reserva.setValorTotal(valorTotal);
        reserva.setAmbiente(ambiente);
        reserva.setUsuario(usuario);

        reserva = reservaRepository.save(reserva);

        return converterParaResponse(reserva);
    }

    @Transactional(readOnly = true)
    public List<ReservaResponse> buscarReservasDoUsuario(String username) {
        List<Reserva> reservas = reservaRepository.findByUsuarioNomeOrderByDataCriacaoDescHoraInicioDesc(username);
        return reservas.stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReservaResponse buscarReserva(Long id, String username) {
        Reserva reserva = reservaRepository.findByIdAndUsuarioNome(id, username)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Reserva não encontrada"
        ));
        return converterParaResponse(reserva);
    }

    @Transactional
    public ReservaResponse cancelarReserva(Long id, String username) {
        Reserva reserva = reservaRepository.findByIdAndUsuarioNome(id, username)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Reserva não encontrada"
        ));

        if (reserva.getDataCriacao().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Não é possível cancelar reservas passadas"
            );
        }

        if ("CANCELADA".equals(reserva.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Reserva já está cancelada"
            );
        }

        reserva.setStatus(StatusReserva.CANCELADA);
        reserva = reservaRepository.save(reserva);

        return converterParaResponse(reserva);
    }

    public boolean verificarDisponibilidade(Long ambienteId, LocalDateTime data,
            LocalTime horaInicio, LocalTime horaFim) {
        validarHorarioFuncionamento(horaInicio, horaFim);
        validarDataFutura(data);

        return reservaRepository.findConflitantes(ambienteId, data, horaInicio.atDate(data.toLocalDate()), horaFim.atDate(data.toLocalDate())).isEmpty();
    }

    public List<LocalTime> buscarHorariosDisponiveis(Long ambienteId, LocalDateTime data) {
        validarDataFutura(data);

        List<LocalTime> todosHorarios = gerarHorariosDisponiveis();
        List<Reserva> reservasExistentes = reservaRepository
                .findByAmbienteIdAndDataCriacao(ambienteId, data);

        return todosHorarios.stream()
                .filter(horario -> !existeConflito(horario, reservasExistentes))
                .collect(Collectors.toList());
    }

    private void validarHorarioFuncionamento(LocalTime horaInicio, LocalTime horaFim) {
        LocalTime abertura = LocalTime.of(7, 0);
        LocalTime fechamento = LocalTime.of(19, 0);

        if (horaInicio.isBefore(abertura) || horaFim.isAfter(fechamento)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Horário fora do período de funcionamento (07:00 às 19:00)"
            );
        }

        if (horaInicio.isAfter(horaFim) || horaInicio.equals(horaFim)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Hora de início deve ser anterior à hora de fim"
            );
        }
    }

    private void validarDataFutura(LocalDateTime data) {
        if (data.isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Não é possível fazer reservas para datas passadas"
            );
        }

        if (data.isAfter(LocalDateTime.now().plusMonths(3))) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Reservas limitadas a 3 meses de antecedência"
            );
        }
    }

    private List<LocalTime> gerarHorariosDisponiveis() {
        List<LocalTime> horarios = new ArrayList<>();
        LocalTime horario = LocalTime.of(7, 0);

        while (horario.isBefore(LocalTime.of(19, 0))) {
            horarios.add(horario);
            horario = horario.plusMinutes(30);
        }

        return horarios;
    }

    private boolean existeConflito(LocalTime horario, List<Reserva> reservas) {
        return reservas.stream().anyMatch(reserva
                -> horario.isAfter(reserva.getHoraInicio().toLocalTime())
                && horario.isBefore(reserva.getHoraFim().toLocalTime())
        );
    }

    private ReservaResponse converterParaResponse(Reserva reserva) {
        ReservaResponse response = new ReservaResponse();
        response.setId(reserva.getId());
        response.setAmbienteId(reserva.getAmbiente().getId());
        response.setNomeAmbiente(reserva.getAmbiente().getNome());
        response.setData(reserva.getDataCriacao());
        response.setHoraInicio(reserva.getHoraInicio());
        response.setHoraFim(reserva.getHoraFim());
        response.setStatus(reserva.getStatus());
        response.setNomeUsuario(reserva.getUsuario().getNome());
        response.setValorTotal(reserva.getValorTotal());
        return response;
    }

    @Scheduled(fixedRate = 1800000) // 30 minutos em milissegundos
    @Transactional
    public void verificarReservasExpiradas() {
        try {
            reservaRepository.atualizarStatusReservasExpiradas(LocalDateTime.now());
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.FAILED_DEPENDENCY,
                    "Erro ao verificar reservas expiradas");
        }
    }
}
