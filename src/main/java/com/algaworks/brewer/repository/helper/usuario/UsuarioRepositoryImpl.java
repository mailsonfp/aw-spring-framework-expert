package com.algaworks.brewer.repository.helper.usuario;

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
import javax.persistence.criteria.Subquery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.ObjectUtils;

import com.algaworks.brewer.model.Usuario;
import com.algaworks.brewer.model.UsuarioGrupo;
import com.algaworks.brewer.repository.filter.UsuarioFilter;

public class UsuarioRepositoryImpl implements UsuarioRepositoryQueries {
	
	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Optional<Usuario> buscarUsuarioAtivoPorEmail(String email) {
		return manager
				.createQuery("from Usuario where lower(email) = lower(:email) and ativo = true", Usuario.class)
				.setParameter("email", email).getResultList().stream().findFirst();
	}
	
	@Override
	public List<String> permissoes(Usuario usuario) {
		return manager.createQuery(
				"select distinct p.nome from Usuario u inner join u.grupos g inner join g.permissoes p where u = :usuario", String.class)
				.setParameter("usuario", usuario)
				.getResultList();
	}

	@Override
	public Page<Usuario> pesquisar(UsuarioFilter filtro, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<Usuario> query = criteriaBuilder.createQuery(Usuario.class);
		Root<Usuario> root = query.from(Usuario.class);
		
		query.where(adicionarFiltro(filtro, root, query));
		
		if(!pageable.getSort().isEmpty()) {
			Sort sort = pageable.getSort();
			Order order = sort.iterator().next();
			String prop = order.getProperty();
			
			query.orderBy(order.isAscending() ? criteriaBuilder.asc(root.get(prop)) : criteriaBuilder.desc(root.get(prop)));
		}
		
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
		
		TypedQuery<Usuario> queryRetorno = manager.createQuery(query);
		queryRetorno.setMaxResults(totalRegistrosPorPagina);
		queryRetorno.setFirstResult(primeiroRegistro);
		
		return new PageImpl<>(queryRetorno.getResultList(), pageable, total(filtro, query));
	}
	
	private Long total(UsuarioFilter filtro, CriteriaQuery<Usuario> queryUsuario) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<Object> query = criteriaBuilder.createQuery(Object.class);
		Root<Usuario> root = query.from(Usuario.class);
		
		query.select(criteriaBuilder.count(root.get("codigo"))).where(adicionarFiltro(filtro, root, queryUsuario));
		
		TypedQuery<Object> queryRetorno = manager.createQuery(query);
		
		return (Long) queryRetorno.getSingleResult();
	}
	
	private Predicate[] adicionarFiltro(UsuarioFilter filtro, Root<Usuario> root, CriteriaQuery<Usuario> queryUsuario) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder criteria = manager.getCriteriaBuilder();
		
		if (filtro != null) {			
			if (!ObjectUtils.isEmpty(filtro.getNome())) {			
				predicates.add(criteria.like(root.get("nome"), "%" + filtro.getNome() + "%"));
			}
			
			if(!ObjectUtils.isEmpty(filtro.getEmail())) {
				predicates.add(criteria.like(root.get("email"), "%" + filtro.getEmail() + "%"));
			}
			
			if(filtro.getGrupos()!= null && !filtro.getGrupos().isEmpty()) {
				Subquery<Long> subQuery = queryUsuario.subquery(Long.class);
				Root<UsuarioGrupo> rootUsuarioGrupo = subQuery.from(UsuarioGrupo.class);
				subQuery.select(criteria.literal(1L));
				subQuery.where(	criteria.equal(rootUsuarioGrupo.get("id").get("usuario"), root),
								rootUsuarioGrupo.get("id").get("grupo").in(filtro.getGrupos()));
				predicates.add(criteria.exists(subQuery));
			}
		}
		
		Predicate[] predArray = new Predicate[predicates.size()];
		return predicates.toArray(predArray);
	}
}
