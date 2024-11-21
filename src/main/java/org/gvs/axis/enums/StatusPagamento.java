package org.gvs.axis.enums;

/**
 *
 * @author vitor
 */
public enum StatusPagamento {
    PENDENTE("Pendente"),
    CONCLUIDO("Concluído"),
    FALHA("Falha"),
    REEMBOLSADO("Reembolsado");

    private String descricao;

    StatusPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
