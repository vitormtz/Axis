package org.gvs.axis.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.gvs.axis.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author vitor
 */
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("SELECT CASE WHEN COUNT(r) = 0 THEN true ELSE false END FROM Reserva r "
            + "WHERE r.sala.id = :salaId AND r.status != 'CANCELADA' AND "
            + "((r.horaInicio <= :fim AND r.horaFim >= :inicio))")
    boolean isSalaDisponivel(
            @Param("salaId") Long salaId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

    @Query("SELECT new list(r.horaInicio, r.horaFim) FROM Reserva r "
            + "WHERE r.sala.id = :salaId AND r.status != 'CANCELADA' AND "
            + "r.horaInicio >= :dataInicio AND r.horaInicio < :dataFim "
            + "ORDER BY r.horaInicio")
    List<LocalDateTime[]> findHorariosOcupados(
            @Param("salaId") Long salaId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);
}
