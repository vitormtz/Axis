/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package org.gvs.axis.enums;

/**
 *
 * @author vitor
 */
public enum StatusReserva {
    PENDENTE("Pendente"),
    CONFIRMADA("Confirmada"),
    CANCELADA("Cancelada"),
    CONCLUIDA("Concluída");

    private String descricao;

    StatusReserva(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
