package org.gvs.axis.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.gvs.axis.enums.TipoComodidade;
import org.gvs.axis.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author vitor
 */
public interface SalaRepository extends JpaRepository<Sala, Long> {

    @Query("SELECT DISTINCT s FROM Sala s LEFT JOIN s.comodidades c "
            + "WHERE (:capacidadeMinima IS NULL OR s.capacidade >= :capacidadeMinima) "
            + "AND (:comodidades IS NULL OR c IN :comodidades) "
            + "AND s.ativo = true")
    List<Sala> findByFiltros(
            @Param("capacidadeMinima") Integer capacidadeMinima,
            @Param("comodidades") Set<TipoComodidade> comodidades);

    @Query("SELECT DISTINCT s FROM Sala s "
            + "WHERE s.id NOT IN ("
            + "    SELECT r.sala.id FROM Reserva r "
            + "    WHERE r.horaInicio <= :fim AND r.horaFim >= :inicio "
            + "    AND r.status NOT IN ('CANCELADA')"
            + ") "
            + "AND (:capacidadeMinima IS NULL OR s.capacidade >= :capacidadeMinima) "
            + "AND (COALESCE(:comodidades, NULL) IS NULL OR "
            + "    EXISTS (SELECT c FROM s.comodidades c WHERE c IN :comodidades)) "
            + "AND s.ativo = true")
    List<Sala> findSalasDisponiveis(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("capacidadeMinima") Integer capacidadeMinima,
            @Param("comodidades") Set<TipoComodidade> comodidades);

    @Query("SELECT COUNT(r) = 0 FROM Reserva r "
            + "WHERE r.sala.id = :salaId "
            + "AND r.status != 'CANCELADA' "
            + "AND r.horaInicio <= :fim "
            + "AND r.horaFim >= :inicio")
    boolean isSalaDisponivel(
            @Param("salaId") Long salaId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

    @Query("SELECT r.horaInicio, r.horaFim FROM Reserva r "
            + "WHERE r.sala.id = :salaId "
            + "AND r.status != 'CANCELADA' "
            + "AND r.horaInicio >= :dataInicio "
            + "AND r.horaInicio < :dataFim "
            + "ORDER BY r.horaInicio")
    List<Object[]> findHorariosOcupados(
            @Param("salaId") Long salaId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);
}
