package org.gvs.axis.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.gvs.axis.dto.BuscaAmbienteDTO;
import org.gvs.axis.enums.TipoComodidade;
import org.gvs.axis.model.Ambiente;
import org.gvs.axis.service.AmbienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author vitor
 */
@RestController
@RequestMapping("/api/ambientes")
@RequiredArgsConstructor
public class AmbienteController {

    private final AmbienteService ambienteService;

    @GetMapping("/buscar")
    public ResponseEntity<List<Ambiente>> buscarAmbientes(
            @RequestParam LocalDate data,
            @RequestParam LocalTime horaInicial,
            @RequestParam LocalTime horaFinal,
            @RequestParam(required = false) Integer capacidade,
            @RequestParam(required = false) Set<TipoComodidade> comodidades) {

        BuscaAmbienteDTO busca = BuscaAmbienteDTO.builder()
                .data(data)
                .horaInicial(horaInicial)
                .horaFinal(horaFinal)
                .capacidade(capacidade)
                .comodidades(comodidades)
                .build();

        return ResponseEntity.ok(ambienteService.buscarAmbientesDisponiveis(busca));
    }
}
