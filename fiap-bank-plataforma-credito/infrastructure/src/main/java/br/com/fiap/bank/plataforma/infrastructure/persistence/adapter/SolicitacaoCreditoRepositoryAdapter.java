package br.com.fiap.bank.plataforma.infrastructure.persistence.adapter;

import br.com.fiap.bank.plataforma.domain.model.SolicitacaoCredito;
import br.com.fiap.bank.plataforma.domain.repository.SolicitacaoCreditoRepository;
import br.com.fiap.bank.plataforma.infrastructure.persistence.entity.SolicitacaoCreditoJpaEntity;
import br.com.fiap.bank.plataforma.infrastructure.persistence.repository.SpringDataSolicitacaoCreditoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SolicitacaoCreditoRepositoryAdapter implements SolicitacaoCreditoRepository {

    private final SpringDataSolicitacaoCreditoRepository springDataRepository;

    @Override
    public SolicitacaoCredito salvar(SolicitacaoCredito solicitacao) {
        SolicitacaoCreditoJpaEntity entity = toEntity(solicitacao);
        SolicitacaoCreditoJpaEntity saved = springDataRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<SolicitacaoCredito> buscarPorId(String id) {
        return springDataRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<SolicitacaoCredito> buscarPorCpfCliente(String cpfCliente) {
        return springDataRepository.findByCpfClienteOrderByDataSolicitacaoDesc(cpfCliente)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<SolicitacaoCredito> listarTodasOrdenadasPorData() {
        return springDataRepository.findAllByOrderByDataSolicitacaoDesc()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deletarPorId(String id) {
        springDataRepository.deleteById(id);
    }

    @Override
    public long contar() {
        return springDataRepository.count();
    }

    private SolicitacaoCreditoJpaEntity toEntity(SolicitacaoCredito domain) {
        return new SolicitacaoCreditoJpaEntity(
                domain.getId(),
                domain.getCpfCliente(),
                domain.getNomeCliente(),
                domain.getTipoCredito(),
                domain.getValorSolicitado(),
                domain.getQuantidadeParcelas(),
                domain.getRendaMensalDeclarada(),
                domain.getStatus(),
                domain.getScoreBureau(),
                domain.getScrRestricoes(),
                domain.getParecerAnalise(),
                domain.getDataSolicitacao(),
                domain.getDataFinalizacao()
        );
    }

    private SolicitacaoCredito toDomain(SolicitacaoCreditoJpaEntity entity) {
        return new SolicitacaoCredito(
                entity.getId(),
                entity.getCpfCliente(),
                entity.getNomeCliente(),
                entity.getTipoCredito(),
                entity.getValorSolicitado(),
                entity.getQuantidadeParcelas(),
                entity.getRendaMensalDeclarada(),
                entity.getStatus(),
                entity.getScoreBureau(),
                entity.getScrRestricoes(),
                entity.getParecerAnalise(),
                entity.getDataSolicitacao(),
                entity.getDataFinalizacao()
        );
    }
}
