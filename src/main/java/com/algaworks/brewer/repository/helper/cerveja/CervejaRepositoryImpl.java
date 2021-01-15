package com.algaworks.brewer.repository.helper.cerveja;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.ObjectUtils;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.repository.filter.CervejaFilter;

public class CervejaRepositoryImpl implements CervejaRepositoryQueries {
	
	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<Cerveja> filtrar(CervejaFilter filtro) {
		CriteriaBuilder criteria = manager.getCriteriaBuilder();
		CriteriaQuery<Cerveja> query = criteria.createQuery(Cerveja.class);
		Root<Cerveja> root = query.from(Cerveja.class);
		
		var predicates = new ArrayList<Predicate>();
		
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
		
		query.where(predicates.toArray(new Predicate[0]));
			
		return manager.createQuery(query).getResultList();
	}
	
	private boolean isEstiloPresente(CervejaFilter filtro) {
		return filtro.getEstilo() != null && filtro.getEstilo().getCodigo() != null;
	}
}
