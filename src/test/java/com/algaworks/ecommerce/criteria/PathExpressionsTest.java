package com.algaworks.ecommerce.criteria;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.ItemPedido;
import com.algaworks.ecommerce.model.Pedido;

public class PathExpressionsTest extends EntityManagerTest {

	@Test
	public void buscarPedidosComProdutosDeIDIgual1Exercicio() {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
		Root<Pedido> root = criteriaQuery.from(Pedido.class);
		Join<Pedido, ItemPedido> JoinItemPedido = root.join("itens");
		
		criteriaQuery.select(root);

		criteriaQuery.where(criteriaBuilder.equal(JoinItemPedido.get("produto").get("id"), 1));
		
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
		List<Pedido> lista = typedQuery.getResultList();

		Assertions.assertFalse(lista.isEmpty());	
	}

	@Test
	public void usarPathExpression() {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
		Root<Pedido> root = criteriaQuery.from(Pedido.class);

		criteriaQuery.select(root);

		criteriaQuery.where(criteriaBuilder.like(root.get("cliente").get("nome"), "M%"));

		TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
		List<Pedido> lista = typedQuery.getResultList();

		Assertions.assertFalse(lista.isEmpty());

	}
}