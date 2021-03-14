package com.algaworks.brewer.repository.helper.venda;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.ObjectUtils;

import com.algaworks.brewer.dto.VendaMes;
import com.algaworks.brewer.dto.VendaOrigem;
import com.algaworks.brewer.model.Venda;
import com.algaworks.brewer.model.enums.StatusVenda;
import com.algaworks.brewer.model.enums.TipoPessoa;
import com.algaworks.brewer.repository.filter.VendaFilter;

public class VendaRepositoryImpl implements VendaRepositoryQueries {
	
	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Page<Venda> filtrar(VendaFilter filtro, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<Venda> query = criteriaBuilder.createQuery(Venda.class);
		Root<Venda> root = query.from(Venda.class);	
		
		query.where(adicionarFiltro(filtro, root));
				
		if(!pageable.getSort().isEmpty()) {
			Sort sort = pageable.getSort();
			Order order = sort.iterator().next();
			String prop = order.getProperty();
			
			query.orderBy(order.isAscending() ? criteriaBuilder.asc(root.get(prop)) : criteriaBuilder.desc(root.get(prop)));
		}
		
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
		
		TypedQuery<Venda> queryRetorno = manager.createQuery(query);
		queryRetorno.setMaxResults(totalRegistrosPorPagina);
		queryRetorno.setFirstResult(primeiroRegistro);
		
		return new PageImpl<>(queryRetorno.getResultList(), pageable, total(filtro));
	}
	
	@Override
	public BigDecimal valorTotalNoAno() {
		Optional<BigDecimal> optional = Optional.ofNullable(
				manager.createQuery("select sum(valorTotal) from Venda where year(dataCriacao) = :ano and status = :status", BigDecimal.class)
					.setParameter("ano", Year.now().getValue())
					.setParameter("status", StatusVenda.EMITIDA)
					.getSingleResult());
		return optional.orElse(BigDecimal.ZERO);
	}
	
	@Override
	public BigDecimal valorTotalNoMes() {
		Optional<BigDecimal> optional = Optional.ofNullable(
				manager.createQuery("select sum(valorTotal) from Venda where month(dataCriacao) = :mes and status = :status", BigDecimal.class)
					.setParameter("mes", MonthDay.now().getMonthValue())
					.setParameter("status", StatusVenda.EMITIDA)
					.getSingleResult());
		return optional.orElse(BigDecimal.ZERO);
	}
	
	@Override
	public BigDecimal valorTicketMedioNoAno() {
		Optional<BigDecimal> optional = Optional.ofNullable(
				manager.createQuery("select sum(valorTotal)/count(*) from Venda where year(dataCriacao) = :ano and status = :status", BigDecimal.class)
					.setParameter("ano", Year.now().getValue())
					.setParameter("status", StatusVenda.EMITIDA)
					.getSingleResult());
		return optional.orElse(BigDecimal.ZERO);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<VendaMes> totalPorMes() {
		List<VendaMes> vendasMes = manager.createNamedQuery("Vendas.totalPorMes").getResultList();
		
		LocalDate hoje = LocalDate.now();
		for (int i = 1; i <= 6; i++) {
			String mesIdeal = String.format("%d/%02d", hoje.getYear(), hoje.getMonthValue());
			
			boolean possuiMes = vendasMes.stream().filter(v -> v.getMes().equals(mesIdeal)).findAny().isPresent();
			if (!possuiMes) {
				vendasMes.add(i - 1, new VendaMes(mesIdeal, 0));
			}
			
			hoje = hoje.minusMonths(1);
		}
		
		return vendasMes;
	}
	
	@Override
	public List<VendaOrigem> totalPorOrigem() {
		List<VendaOrigem> vendasNacionalidade = manager.createNamedQuery("Vendas.porOrigem", VendaOrigem.class).getResultList();
		
		LocalDate now = LocalDate.now();
		for (int i = 1; i <= 6; i++) {
			String mesIdeal = String.format("%d/%02d", now.getYear(), now.getMonth().getValue());
			
			boolean possuiMes = vendasNacionalidade.stream().filter(v -> v.getMes().equals(mesIdeal)).findAny().isPresent();
			if (!possuiMes) {
				vendasNacionalidade.add(i - 1, new VendaOrigem(mesIdeal, 0, 0));
			}
			
			now = now.minusMonths(1);
		}
		
		return vendasNacionalidade;
	}
	
	private Predicate[] adicionarFiltro(VendaFilter filtro, Root<Venda> root) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder criteria = manager.getCriteriaBuilder();
		
		if (filtro != null) {
			if (!ObjectUtils.isEmpty(filtro.getCodigo())) {
				predicates.add(criteria.equal(root.get("codigo"), filtro.getCodigo()));
			}
			
			if (filtro.getStatus() != null) {
				predicates.add(criteria.equal(root.get("status"), filtro.getStatus()));
			}
			
			if (filtro.getDesde() != null) {
				LocalDateTime desde = LocalDateTime.of(filtro.getDesde(), LocalTime.of(0, 0));
				predicates.add(criteria.greaterThan(root.get("dataCriacao"), desde));
			}
			
			if (filtro.getAte() != null) {
				LocalDateTime ate = LocalDateTime.of(filtro.getAte(), LocalTime.of(23, 59));
				predicates.add(criteria.lessThan(root.get("dataCriacao"), ate));
			}
			
			if (filtro.getValorMinimo() != null) {
				predicates.add(criteria.ge(root.get("valorTotal"), filtro.getValorMinimo()));
			}
			
			if (filtro.getValorMaximo() != null) {
				predicates.add(criteria.le(root.get("valorTotal"), filtro.getValorMaximo()));
			}
			
			if (!ObjectUtils.isEmpty(filtro.getNomeCliente())) {
				predicates.add(criteria.like(root.get("cliente").get("nome"), "%" + filtro.getNomeCliente() + "%"));
			}
			
			if (!ObjectUtils.isEmpty(filtro.getCpfOuCnpjCliente())) {
				predicates.add(criteria.equal(root.get("cliente").get("cpfOuCnpj"), TipoPessoa.removerFormatacao(filtro.getCpfOuCnpjCliente())));
			}
		}
		
		Predicate[] predArray = new Predicate[predicates.size()];
		return predicates.toArray(predArray);
	}
	
	private Long total(VendaFilter filtro) {
		CriteriaBuilder criteria = manager.getCriteriaBuilder();
		CriteriaQuery<Object> query = criteria.createQuery(Object.class);
		Root<Venda> root = query.from(Venda.class);
		
		query.select(criteria.count(root.get("codigo"))).where(adicionarFiltro(filtro, root));
		
		TypedQuery<Object> queryRetorno = manager.createQuery(query);
		
		return (Long) queryRetorno.getSingleResult();
	}
}
