package org.gvs.axis.enums;

/**
 *
 * @author vitor
 */
public enum TipoComodidade {
    PROJETOR("Projetor"),
    TV("TV"),
    AR_CONDICIONADO("Ar Condicionado"),
    QUADRO_BRANCO("Quadro Branco"),
    CAFE("Café");

    private String descricao;

    TipoComodidade(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
