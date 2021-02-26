package com.algaworks.brewer.repository.helper.cliente;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.ObjectUtils;

import com.algaworks.brewer.model.Cliente;
import com.algaworks.brewer.model.embeddable.Endereco;
import com.algaworks.brewer.repository.filter.ClienteFilter;
import com.algaworks.brewer.repository.paginacao.PaginacaoUtil;

public class ClienteRepositoryImpl implements ClienteRepositoryQueries {
	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	PaginacaoUtil<Cliente> paginacaoCliente;
	
	@Override
	public Page<Cliente> filtrar(ClienteFilter filtro, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<Cliente> query = criteriaBuilder.createQuery(Cliente.class);
		Root<Cliente> root = query.from(Cliente.class);
		
		Join<Cliente, Endereco> enderecoCliente = root.join("endereco");
		enderecoCliente.fetch("cidade", JoinType.LEFT);
		//enderecoCliente.fetch("cidade.estado", JoinType.LEFT);
	
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
		
		TypedQuery<Cliente> queryRetorno = manager.createQuery(query);
		queryRetorno.setMaxResults(totalRegistrosPorPagina);
		queryRetorno.setFirstResult(primeiroRegistro);
		
		return new PageImpl<>(queryRetorno.getResultList(), pageable, total(filtro));
	}
	
	private Long total(ClienteFilter filtro) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<Object> query = criteriaBuilder.createQuery(Object.class);
		Root<Cliente> root = query.from(Cliente.class);			
		
		query.select(criteriaBuilder.count(root.get("codigo"))).where(adicionarFiltro(filtro, root));
		
		TypedQuery<Object> queryRetorno = manager.createQuery(query);
		
		return (Long) queryRetorno.getSingleResult();
	}
	
	private Predicate[] adicionarFiltro(ClienteFilter filtro, Root<Cliente> root) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder criteria = manager.getCriteriaBuilder();
		
		if (filtro != null) {			
			if (!ObjectUtils.isEmpty(filtro.getNome())) {			
				predicates.add(criteria.like(root.get("nome"), "%" + filtro.getNome() + "%"));
			}
			
			if (!ObjectUtils.isEmpty(filtro.getCpfOuCnpj())) {			
				predicates.add(criteria.equal(root.get("cpfOuCnpj"), filtro.getCpfOuCnpj()));
			}
		}
		
		Predicate[] predArray = new Predicate[predicates.size()];
		return predicates.toArray(predArray);
	}
}
