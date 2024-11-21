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
