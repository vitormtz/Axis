package org.gvs.axis.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import org.gvs.axis.enums.TipoComodidade;

/**
 *
 * @author vitor
 */
@Data
@Builder
public class BuscaAmbienteDTO {

    private LocalDate data;
    private LocalTime horaInicial;
    private LocalTime horaFinal;
    private Integer capacidade;
    private Set<TipoComodidade> comodidades;
}
