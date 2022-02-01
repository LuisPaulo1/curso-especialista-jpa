package com.algaworks.ecommerce.criteria;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.junit.jupiter.api.Test;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Categoria;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.ItemPedido;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;

public class GroupByCriteriaTest extends EntityManagerTest {
	
    @Test
    public void condicionarAgrupamentoComHaving() {
//         Total de vendas dentre as categorias que mais vendem.
//        String jpql = "select cat.nome, sum(ip.precoProduto) from ItemPedido ip " +
//                " join ip.produto pro join pro.categorias cat " +
//                " group by cat.id " +
//                " having sum(ip.precoProduto) > 100 ";

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<ItemPedido> root = criteriaQuery.from(ItemPedido.class);
        Join<ItemPedido, Produto> joinProduto = root.join("produto");
        Join<Produto, Categoria> joinProdutoCategoria = joinProduto.join("categorias");

        criteriaQuery.multiselect(
                joinProdutoCategoria.get("nome"),
                criteriaBuilder.sum(root.get("precoProduto")),
                criteriaBuilder.avg(root.get("precoProduto"))
        );

        criteriaQuery.groupBy(joinProdutoCategoria.get("id"));

        criteriaQuery.having(criteriaBuilder.greaterThan(
                criteriaBuilder.avg(
                        root.get("precoProduto")).as(BigDecimal.class),
                new BigDecimal(700)));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Object[]> lista = typedQuery.getResultList();

        lista.forEach(arr -> System.out.println(
                "Nome categoria: " + arr[0]
                        + ", SUM: " + arr[1]
                        + ", AVG: " + arr[2]));
    }

	@Test
	public void agruparResultadoComFuncoes() {
//	         Total de vendas por mês.
//	        String jpql = "select concat(year(p.dataCriacao), '/', function('monthname', p.dataCriacao)), sum(p.total) " +
//	                " from Pedido p " +
//	                " group by year(p.dataCriacao), month(p.dataCriacao) ";

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
		Root<Pedido> root = criteriaQuery.from(Pedido.class);

		Expression<Integer> anoCriacaoPedido = criteriaBuilder.function("year", Integer.class, root.get("dataCriacao"));
		Expression<Integer> mesCriacaoPedido = criteriaBuilder.function("month", Integer.class, root.get("dataCriacao"));
		Expression<String> nomeMesCriacaoPedido = criteriaBuilder.function("monthname", String.class, root.get("dataCriacao"));

		Expression<String> anoMesConcat = criteriaBuilder
				.concat(criteriaBuilder.concat(anoCriacaoPedido.as(String.class), "/"), nomeMesCriacaoPedido);

		criteriaQuery.multiselect(anoMesConcat, criteriaBuilder.sum(root.get("total")));

		criteriaQuery.groupBy(anoCriacaoPedido, mesCriacaoPedido);

		TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
		List<Object[]> lista = typedQuery.getResultList();

		lista.forEach(arr -> System.out.println("Ano/Mês: " + arr[0] + ", Sum: " + arr[1]));
	}

	@Test
	public void agruparResultado03Exercicio() {

		// Total de vendas por cliente
		// String jpql = "select c.nome, sum(ip.precoProduto) from ItemPedido ip "
		// + "join ip.pedido p join p.cliente c "
		// + "group by c.id";

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
		Root<ItemPedido> root = criteriaQuery.from(ItemPedido.class);
		Join<ItemPedido, Pedido> joinPedido = root.join("pedido");
		Join<Pedido, Cliente> joinPedidoCliente = joinPedido.join("cliente");

		criteriaQuery.multiselect(joinPedidoCliente.get("nome"), criteriaBuilder.sum(root.get("precoProduto")));

		criteriaQuery.groupBy(joinPedidoCliente.get("id"));

		TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
		List<Object[]> lista = typedQuery.getResultList();

		lista.forEach(arr -> System.out.println("Nome Cliente: " + arr[0] + ", Sum: " + arr[1]));

	}

	@Test
	public void agruparResultado02() {
//        Total de vendas por categoria.
//        String jpql = "select c.nome, sum(ip.precoProduto) from ItemPedido ip " +
//                " join ip.produto pro join pro.categorias c " +
//                " group by c.id";

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
		Root<ItemPedido> root = criteriaQuery.from(ItemPedido.class);
		Join<ItemPedido, Produto> joinProduto = root.join("produto");
		Join<Produto, Categoria> joinProdutoCategoria = joinProduto.join("categorias");

		criteriaQuery.multiselect(joinProdutoCategoria.get("nome"), criteriaBuilder.sum(root.get("precoProduto")));

		criteriaQuery.groupBy(joinProdutoCategoria.get("id"));

		TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
		List<Object[]> lista = typedQuery.getResultList();

		lista.forEach(arr -> System.out.println("Nome categoria: " + arr[0] + ", Sum: " + arr[1]));

	}

	@Test
	public void agruparResultado01() {
//        Quantidade de produtos por categoria.
//        String jpql = "select c.nome, count(p.id) from Categoria c join c.produtos p group by c.id";

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
		Root<Categoria> root = criteriaQuery.from(Categoria.class);
		Join<Categoria, Produto> joinProduto = root.join("produtos", JoinType.LEFT);

		criteriaQuery.multiselect(root.get("nome"), criteriaBuilder.count(joinProduto.get("id")));

		criteriaQuery.groupBy(root.get("id"));

		TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
		List<Object[]> lista = typedQuery.getResultList();

		lista.forEach(arr -> System.out.println("Nome: " + arr[0] + ", Count: " + arr[1]));
	}
}