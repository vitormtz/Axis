package org.gvs.axis.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import org.gvs.axis.enums.StatusReserva;

/**
 *
 * @author vitor
 */
@Data
public class ReservaResponse {

    private Long id;
    private Long ambienteId;
    private String nomeAmbiente;
    private LocalDateTime data;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFim;
    private StatusReserva status;
    private String nomeUsuario;
    private BigDecimal valorTotal;
}
