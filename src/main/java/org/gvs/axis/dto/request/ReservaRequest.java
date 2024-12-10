package org.gvs.axis.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import org.gvs.axis.enums.StatusReserva;

/**
 *
 * @author vitor
 */
@Data
public class ReservaRequest {

    private Long ambienteId;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFim;
    private StatusReserva status;
    private BigDecimal valorTotal;
    private Long usuarioId;
}
