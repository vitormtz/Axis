package org.gvs.axis.dto;

import java.time.LocalTime;
import java.util.List;
import lombok.Data;

/**
 *
 * @author vitor
 */
@Data
public class DisponibilidadeDTO {

    private Long salaId;
    private List<HorarioDisponivelDTO> horariosDisponiveis;

    @Data
    public static class HorarioDisponivelDTO {

        private LocalTime horaInicio;
        private LocalTime horaFim;
    }
}
