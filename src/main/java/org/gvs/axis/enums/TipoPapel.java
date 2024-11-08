/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package org.gvs.axis.enums;

/**
 *
 * @author vitor
 */
public enum TipoPapel {
    ADMIN("Administrador"),
    USUARIO("Usuário");

    private String descricao;

    TipoPapel(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
