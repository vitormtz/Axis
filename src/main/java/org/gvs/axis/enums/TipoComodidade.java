/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package org.gvs.axis.enums;

/**
 *
 * @author vitor
 */
public enum TipoComodidade {
    WIFI("Wi-Fi"),
    PROJETOR("Projetor"),
    TV("TV"),
    AR_CONDICIONADO("Ar Condicionado"),
    QUADRO_BRANCO("Quadro Branco"),
    CAFE("Café"),
    IMPRESSORA("Impressora"),
    ESTACIONAMENTO("Estacionamento");

    private String descricao;

    TipoComodidade(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
