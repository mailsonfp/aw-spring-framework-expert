package com.algaworks.brewer.repository.helper.venda;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

import com.algaworks.brewer.model.Venda;
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
