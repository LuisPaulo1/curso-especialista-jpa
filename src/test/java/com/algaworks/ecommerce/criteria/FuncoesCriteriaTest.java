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
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.Pagamento;
import com.algaworks.ecommerce.model.PagamentoBoleto;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.StatusPedido;

public class FuncoesCriteriaTest extends EntityManagerTest {
	
    @Test
    public void aplicarFuncaoAgregacao() {
    	
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.multiselect(
                criteriaBuilder.count(root.get("id")),
                criteriaBuilder.avg(root.get("total")),
                criteriaBuilder.sum(root.get("total")),
                criteriaBuilder.min(root.get("total")),
                criteriaBuilder.max(root.get("total"))
        );

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(
                "count: " + arr[0]
                        + ", avg: " + arr[1]
                        + ", sum: " + arr[2]
                        + ", min: " + arr[3]
                        + ", max: " + arr[4]));
    }
	
    @Test
    public void aplicarFuncaoNativas() {
    	
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.multiselect(
                root.get("id"),
                criteriaBuilder.function("dayname", String.class, root.get("dataCriacao"))
        );

        criteriaQuery.where(criteriaBuilder.isTrue(
                criteriaBuilder.function(
                        "acima_media_faturamento", Boolean.class, root.get("total"))));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(
                arr[0] + ", dayname: " + arr[1]));
    }
	
    @Test
    public void aplicarFuncaoColecao() {
    	
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.multiselect(
                root.get("id"),
                criteriaBuilder.size(root.get("itens"))
        );

        criteriaQuery.where(criteriaBuilder.greaterThan(
                criteriaBuilder.size(root.get("itens")), 1));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(
                arr[0]
                        + ", size: " + arr[1]));
    }
	
    @Test
    public void aplicarFuncaoNumero() {
    	
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.multiselect(
                root.get("id"),
                criteriaBuilder.abs(criteriaBuilder.prod(root.get("id"), -1)),
                criteriaBuilder.mod(root.get("id"), 2),
                criteriaBuilder.sqrt(root.get("total"))
        );

        criteriaQuery.where(criteriaBuilder.greaterThan(criteriaBuilder.sqrt(root.get("total")), 10.0));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
        
        lista.forEach(arr -> System.out.println(
                arr[0]
                        + ", abs: " + arr[1]
                        + ", mod: " + arr[2]
                        + ", sqrt: " + arr[3]));
        
    }
	
    @Test
    public void aplicarFuncaoData() {
        // current_date, current_time, current_timestamp

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        Join<Pedido, Pagamento> joinPagamento = root.join("pagamento");
        Join<Pedido, PagamentoBoleto> joinPagamentoBoleto = criteriaBuilder.treat(joinPagamento, PagamentoBoleto.class); // Join usado em herança

        criteriaQuery.multiselect(
                root.get("id"),
                criteriaBuilder.currentDate(),
                criteriaBuilder.currentTime(),
                criteriaBuilder.currentTimestamp()
        );

        criteriaQuery.where(
                criteriaBuilder.between(criteriaBuilder.currentDate(),
                        root.get("dataCriacao").as(java.sql.Date.class),
                        joinPagamentoBoleto.get("dataVencimento").as(java.sql.Date.class)),
                criteriaBuilder.equal(root.get("status"), StatusPedido.AGUARDANDO)
        );

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(
                arr[0]
                        + ", current_date: " + arr[1]
                        + ", current_time: " + arr[2]
                        + ", current_timestamp: " + arr[3]));
    }

    @Test
    public void aplicarFuncaoString() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Cliente> root = criteriaQuery.from(Cliente.class);

        criteriaQuery.multiselect(
                root.get("nome"),
                criteriaBuilder.concat("Nome do cliente: ", root.get("nome")),
                criteriaBuilder.length(root.get("nome")),
                criteriaBuilder.locate(root.get("nome"), "a"),
                criteriaBuilder.substring(root.get("nome"), 1, 2),
                criteriaBuilder.lower(root.get("nome")),
                criteriaBuilder.upper(root.get("nome")),
                criteriaBuilder.trim(root.get("nome"))
        );

        criteriaQuery.where(criteriaBuilder.equal(
                criteriaBuilder.substring(root.get("nome"), 1, 1), "M"));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(
                arr[0]
                        + ", concat: " + arr[1]
                        + ", length: " + arr[2]
                        + ", locate: " + arr[3]
                        + ", substring: " + arr[4]
                        + ", lower: " + arr[5]
                        + ", upper: " + arr[6]
                        + ", trim: |" + arr[7] + "|"));
    }
}