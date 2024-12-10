package org.gvs.axis.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.gvs.axis.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author vitor
 */
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuarioNomeOrderByDataCriacaoDescHoraInicioDesc(String nome);

    Optional<Reserva> findByIdAndUsuarioNome(Long id, String nome);

    List<Reserva> findByAmbienteIdAndDataCriacao(Long ambiente, LocalDateTime dataCriacao);

    @Query("SELECT CASE WHEN COUNT(r) = 0 THEN true ELSE false END FROM Reserva r "
            + "WHERE r.ambiente.id = :ambienteId AND r.status != 'CANCELADA' AND "
            + "((r.horaInicio <= :fim AND r.horaFim >= :inicio))")
    boolean isAmbienteDisponivel(
            @Param("ambienteId") Long ambienteId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

    @Query("SELECT new list(r.horaInicio, r.horaFim) FROM Reserva r "
            + "WHERE r.ambiente.id = :ambienteId AND r.status != 'CANCELADA' AND "
            + "r.horaInicio >= :dataInicio AND r.horaInicio < :dataFim "
            + "ORDER BY r.horaInicio")
    List<LocalDateTime[]> findHorariosOcupados(
            @Param("ambienteId") Long ambienteId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);

    @Query("""
    SELECT r FROM Reserva r 
    WHERE r.ambiente.id = :ambienteId 
    AND r.dataCriacao = :data 
    AND r.status != 'CANCELADA'
    AND ((r.horaInicio <= :horaFim AND r.horaFim >= :horaInicio)
    OR (r.horaInicio >= :horaInicio AND r.horaInicio < :horaFim))
    """)
    List<Reserva> findConflitantes(
            @Param("ambienteId") Long ambienteId,
            @Param("data") LocalDateTime data,
            @Param("horaInicio") LocalDateTime horaInicio,
            @Param("horaFim") LocalDateTime horaFim
    );
}
