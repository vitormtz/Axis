/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.gvs.axis.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

/**
 *
 * @author vitor
 */
@Data
public class AtualizarReservaDTO {

    private LocalDate data;
    private LocalTime horaInicio;
    private LocalTime horaFim;
}
