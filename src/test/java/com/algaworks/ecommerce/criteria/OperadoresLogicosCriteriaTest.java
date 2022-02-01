package com.algaworks.ecommerce.criteria;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.StatusPedido;

public class OperadoresLogicosCriteriaTest extends EntityManagerTest {

    @Test
    public void usarOperadores() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.select(root);

        // select p from Pedido p where (status = 'PAGO' or status = 'AGUARDANDO') and total > 499

        criteriaQuery.where(
                criteriaBuilder.or(
                        criteriaBuilder.equal(root.get("status"), StatusPedido.AGUARDANDO),
                        criteriaBuilder.equal(root.get("status"), StatusPedido.PAGO)
                ),
                criteriaBuilder.greaterThan(root.get("total"), new BigDecimal(499))
        );

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", Total: " + p.getTotal()));
    }
}