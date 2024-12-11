package org.gvs.axis.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.gvs.axis.enums.TipoComodidade;
import org.gvs.axis.model.Ambiente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author vitor
 */
public interface AmbienteRepository extends JpaRepository<Ambiente, Long> {

    List<Ambiente> findAllByAtivoTrue();

    @Query("SELECT DISTINCT a FROM Ambiente a LEFT JOIN a.comodidades c "
            + "WHERE (:capacidadeMaxima IS NULL OR a.capacidade <= :capacidadeMaxima) "
            + "AND (:comodidades IS NULL OR c IN :comodidades) "
            + "AND a.ativo = true")
    List<Ambiente> findByFiltros(
            @Param("capacidadeMaxima") Integer capacidadeMaxima,
            @Param("comodidades") Set<TipoComodidade> comodidades);

    @Query("SELECT DISTINCT a FROM Ambiente a "
            + "WHERE a.id NOT IN ("
            + "    SELECT r.ambiente.id FROM Reserva r "
            + "    WHERE r.horaInicio <= :fim AND r.horaFim >= :inicio "
            + "    AND r.status NOT IN ('CANCELADA')"
            + ") "
            + "AND (:capacidadeMaxima IS NULL OR a.capacidade <= :capacidadeMaxima) "
            + "AND (COALESCE(:comodidades, NULL) IS NULL OR "
            + "    EXISTS (SELECT c FROM a.comodidades c WHERE c IN :comodidades)) "
            + "AND a.ativo = true")
    List<Ambiente> findAmbientesDisponiveis(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("capacidadeMaxima") Integer capacidadeMaxima,
            @Param("comodidades") Set<TipoComodidade> comodidades);

    @Query("SELECT COUNT(r) = 0 FROM Reserva r "
            + "WHERE r.ambiente.id = :ambienteId "
            + "AND r.status != 'CANCELADA' "
            + "AND r.horaInicio <= :fim "
            + "AND r.horaFim >= :inicio")
    boolean isAmbienteDisponivel(
            @Param("ambienteId") Long ambienteId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

    @Query("SELECT r.horaInicio, r.horaFim FROM Reserva r "
            + "WHERE r.ambiente.id = :ambienteId "
            + "AND r.status != 'CANCELADA' "
            + "AND r.horaInicio >= :dataInicio "
            + "AND r.horaInicio < :dataFim "
            + "ORDER BY r.horaInicio")
    List<Object[]> findHorariosOcupados(
            @Param("ambienteId") Long ambienteId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);
}
