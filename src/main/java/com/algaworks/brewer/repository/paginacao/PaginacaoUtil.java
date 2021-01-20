package com.algaworks.brewer.repository.paginacao;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PaginacaoUtil<T> {
	
	public TypedQuery<T> preparar(EntityManager manager, CriteriaBuilder criteriaBuilder, Root<T> root, CriteriaQuery<T> criteriaQuery, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
		
		TypedQuery<T> typedQuery = manager.createQuery(criteriaQuery);
		
		typedQuery.setFirstResult(primeiroRegistro);
		typedQuery.setMaxResults(totalRegistrosPorPagina);
				
		System.out.println(pageable.getSort());
		
		if(!pageable.getSort().isEmpty()) {
			Sort sort = pageable.getSort();
			Sort.Order order = sort.iterator().next();
			String property = order.getProperty();
			criteriaQuery.orderBy(order.isAscending() ? criteriaBuilder.asc(root.get(property)) : criteriaBuilder.desc(root.get(property)));
		}
		
		return typedQuery;
	}
	
	
}
