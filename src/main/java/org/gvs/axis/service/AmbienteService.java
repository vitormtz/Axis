package org.gvs.axis.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.gvs.axis.dto.BuscaAmbienteDTO;
import org.gvs.axis.model.Ambiente;
import org.gvs.axis.repository.AmbienteRepository;
import org.springframework.stereotype.Service;

/**
 *
 * @author vitor
 */
@Service
@RequiredArgsConstructor
public class AmbienteService {

    private final AmbienteRepository ambienteRepository;

    public List<Ambiente> buscarAmbientesDisponiveis(BuscaAmbienteDTO busca) {
        // Converter data e hora para LocalDateTime
        LocalDateTime horaInicio = LocalDateTime.of(busca.getData(), busca.getHoraInicial());
        LocalDateTime horaFim = LocalDateTime.of(busca.getData(), busca.getHoraFinal());

        // Usar a query existente do AmbienteRepository que já tem toda a lógica necessária
        return ambienteRepository.findAmbientesDisponiveis(
                horaInicio,
                horaFim,
                busca.getCapacidade(),
                busca.getComodidades());
    }
}
