package com.algaworks.brewer.repository.helper.cerveja;

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

import com.algaworks.brewer.dto.CervejaDTO;
import com.algaworks.brewer.dto.ValorItensEstoque;
import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.repository.filter.CervejaFilter;

public class CervejaRepositoryImpl implements CervejaRepositoryQueries {
	
	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Page<Cerveja> filtrar(CervejaFilter filtro, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<Cerveja> query = criteriaBuilder.createQuery(Cerveja.class);
		Root<Cerveja> root = query.from(Cerveja.class);										
		
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
		
		TypedQuery<Cerveja> queryRetorno = manager.createQuery(query);
		queryRetorno.setMaxResults(totalRegistrosPorPagina);
		queryRetorno.setFirstResult(primeiroRegistro);
		
		return new PageImpl<>(queryRetorno.getResultList(), pageable, total(filtro));
	}
	
	@Override
	public List<CervejaDTO> buscarPorSkuOuNome(String skuOuNome) {
		String jpql = "select new com.algaworks.brewer.dto.CervejaDTO(codigo, sku, nome, origem, valor, foto) "
				+ "from Cerveja where lower(sku) like lower(:skuOuNome) or lower(nome) like lower(:skuOuNome)";
		
		List<CervejaDTO> cervejasFiltradas = manager.createQuery(jpql, CervejaDTO.class)
					.setParameter("skuOuNome", skuOuNome + "%")
					.getResultList();
		
		return cervejasFiltradas;
	}
	
	@Override
	public ValorItensEstoque valorItensEstoque() {
		String query = "select new com.algaworks.brewer.dto.ValorItensEstoque(sum(valor * quantidadeEstoque), sum(quantidadeEstoque)) from Cerveja";
		return manager.createQuery(query, ValorItensEstoque.class).getSingleResult();
	}
	
	private Predicate[] adicionarFiltro(CervejaFilter filtro, Root<Cerveja> root) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder criteria = manager.getCriteriaBuilder();
		
		if (filtro != null) {
			if (!ObjectUtils.isEmpty(filtro.getSku())) {				
				predicates.add(criteria.equal(root.get("sku"), filtro.getSku()));
			}
			
			if (!ObjectUtils.isEmpty(filtro.getNome())) {			
				predicates.add(criteria.like(root.get("nome"), "%" + filtro.getNome() + "%"));
			}

			if (isEstiloPresente(filtro)) {			
				predicates.add(criteria.equal(root.get("estilo"), filtro.getEstilo()));
			}

			if (filtro.getSabor() != null) {				
				predicates.add(criteria.equal(root.get("sabor"), filtro.getSabor()));
			}

			if (filtro.getOrigem() != null) {				
				predicates.add(criteria.equal(root.get("origem"), filtro.getOrigem()));
			}

			if (filtro.getValorDe() != null) {				
				predicates.add(criteria.greaterThanOrEqualTo(root.get("valor"), filtro.getValorDe()));
			}

			if (filtro.getValorAte() != null) {				
				predicates.add(criteria.lessThanOrEqualTo(root.get("valor"), filtro.getValorAte()));
			}
		}
		
		Predicate[] predArray = new Predicate[predicates.size()];
		return predicates.toArray(predArray);
	}
	
	private Long total(CervejaFilter filtro) {
		CriteriaBuilder criteria = manager.getCriteriaBuilder();
		CriteriaQuery<Object> query = criteria.createQuery(Object.class);
		Root<Cerveja> root = query.from(Cerveja.class);
		
		query.select(criteria.count(root.get("codigo"))).where(adicionarFiltro(filtro, root));
		
		TypedQuery<Object> queryRetorno = manager.createQuery(query);
		
		return (Long) queryRetorno.getSingleResult();
	}
	
	private boolean isEstiloPresente(CervejaFilter filtro) {
		return filtro.getEstilo() != null && filtro.getEstilo().getCodigo() != null;
	}	
}
