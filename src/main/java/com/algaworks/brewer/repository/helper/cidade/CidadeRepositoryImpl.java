package com.algaworks.brewer.repository.helper.cidade;

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

import com.algaworks.brewer.model.Cidade;
import com.algaworks.brewer.repository.filter.CidadeFilter;

public class CidadeRepositoryImpl implements CidadeRepositoryQueries {
	
	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Page<Cidade> filtrar(CidadeFilter filtro, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<Cidade> query = criteriaBuilder.createQuery(Cidade.class);
		Root<Cidade> root = query.from(Cidade.class);										
		
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
		
		TypedQuery<Cidade> queryRetorno = manager.createQuery(query);
		queryRetorno.setMaxResults(totalRegistrosPorPagina);
		queryRetorno.setFirstResult(primeiroRegistro);
		
		return new PageImpl<>(queryRetorno.getResultList(), pageable, total(filtro));
	}
	
	private Long total(CidadeFilter filtro) {
		CriteriaBuilder criteria = manager.getCriteriaBuilder();
		CriteriaQuery<Object> query = criteria.createQuery(Object.class);
		Root<Cidade> root = query.from(Cidade.class);
		
		query.select(criteria.count(root.get("codigo"))).where(adicionarFiltro(filtro, root));
		
		TypedQuery<Object> queryRetorno = manager.createQuery(query);
		
		return (Long) queryRetorno.getSingleResult();
	}
	
	private Predicate[] adicionarFiltro(CidadeFilter filtro, Root<Cidade> root) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder criteria = manager.getCriteriaBuilder();
		
		if (filtro != null) {			
			if (!ObjectUtils.isEmpty(filtro.getNome())) {			
				predicates.add(criteria.like(root.get("nome"), "%" + filtro.getNome() + "%"));
			}
			
			if (filtro.getEstado() != null) {				
				predicates.add(criteria.equal(root.get("estado"), filtro.getEstado()));
			}		
		}
		
		Predicate[] predArray = new Predicate[predicates.size()];
		return predicates.toArray(predArray);
	}	
}
