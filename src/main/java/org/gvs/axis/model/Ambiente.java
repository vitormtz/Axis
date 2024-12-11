package org.gvs.axis.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gvs.axis.enums.TipoComodidade;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 *
 * @author vitor
 */
@Entity
@Table(name = "ambientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ambiente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(columnDefinition = "TEXT")
    private String imagem_nome;

    @Column(nullable = false)
    private Integer capacidade;

    @Column(name = "valor_hora", nullable = false)
    private BigDecimal valorHora;

    @Column(name = "ativo")
    @Builder.Default
    private Boolean ativo = true;

    @ElementCollection(targetClass = TipoComodidade.class)
    @CollectionTable(name = "comodidades_ambiente",
            joinColumns = @JoinColumn(name = "ambiente_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "comodidade", nullable = false)
    private Set<TipoComodidade> comodidades;

    @Column(name = "data_criacao")
    @CreationTimestamp
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    @UpdateTimestamp
    private LocalDateTime dataAtualizacao;

    @JsonManagedReference
    @OneToMany(mappedBy = "ambiente")
    private List<Reserva> reservas;
}
