package br.com.fiap.bank.plataforma.infrastructure.persistence.entity;

import br.com.fiap.bank.plataforma.domain.model.StatusSolicitacao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_solicitacoes_credito")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoCreditoJpaEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String cpfCliente;

    @Column(nullable = false)
    private String nomeCliente;

    @Column(nullable = false)
    private String tipoCredito;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorSolicitado;

    @Column(nullable = false)
    private Integer quantidadeParcelas;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal rendaMensalDeclarada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSolicitacao status;

    private Integer scoreBureau;

    private Boolean scrRestricoes;

    @Column(length = 1000)
    private String parecerAnalise;

    @Column(nullable = false)
    private LocalDateTime dataSolicitacao;

    private LocalDateTime dataFinalizacao;
}
