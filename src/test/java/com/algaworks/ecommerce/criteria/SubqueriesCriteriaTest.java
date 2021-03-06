
package com.algaworks.ecommerce.criteria;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Categoria;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.ItemPedido;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;

public class SubqueriesCriteriaTest extends EntityManagerTest {	
	
    @Test
    public void pesquisarComAllExercicio() {
//        Todos os produtos que sempre foram vendidos pelo mesmo pre?o.
//        String jpql = "select distinct p from ItemPedido ip join ip.produto p where " +
//                " ip.precoProduto = ALL " +
//                " (select precoProduto from ItemPedido where produto = p and id <> ip.id)";

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<ItemPedido> root = criteriaQuery.from(ItemPedido.class);

        criteriaQuery.select(root.get("produto"));
		criteriaQuery.distinct(true);

        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        subquery.select(subqueryRoot.get("precoProduto"));
        subquery.where(
                criteriaBuilder.equal(subqueryRoot.get("produto"), root.get("produto")),
                criteriaBuilder.notEqual(subqueryRoot, root)
        );

        criteriaQuery.where(
                criteriaBuilder.equal(
                        root.get("precoProduto"), criteriaBuilder.all(subquery))
        );

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(obj -> System.out.println("ID: " + obj.getId()));
    }
	
    @Test
    public void pesquisarComAny02() {
//        Todos os produtos que j? foram vendidos por um preco diferente do atual
//        String jpql = "select p from Produto p " +
//                " where p.preco <> ANY (select precoProduto from ItemPedido where produto = p)";

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.select(root);

        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        subquery.select(subqueryRoot.get("precoProduto"));
        subquery.where(criteriaBuilder.equal(subqueryRoot.get("produto"), root));

        criteriaQuery.where(
                criteriaBuilder.notEqual(
                        root.get("preco"), criteriaBuilder.any(subquery))
        );

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(obj -> System.out.println("ID: " + obj.getId()));

    }

    @Test
    public void pesquisarComAny01() {
//        Todos os produtos que j? foram vendidos, pelo menos, uma vez pelo pre?o atual.
//        String jpql = "select p from Produto p " +
//                " where p.preco = ANY (select precoProduto from ItemPedido where produto = p)";

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.select(root);

        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        subquery.select(subqueryRoot.get("precoProduto"));
        subquery.where(criteriaBuilder.equal(subqueryRoot.get("produto"), root));

        criteriaQuery.where(
                criteriaBuilder.equal(
                        root.get("preco"), criteriaBuilder.any(subquery))
        );

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(obj -> System.out.println("ID: " + obj.getId()));

    }
	
    @Test
    public void pesquisarComAll02() {
//        Todos os produtos n?o foram vendidos mais depois que encareceram
//        String jpql = "select p from Produto p where " +
//                " p.preco > ALL (select precoProduto from ItemPedido where produto = p)";
//                " and exists (select 1 from ItemPedido where produto = p)";

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.select(root);

        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        subquery.select(subqueryRoot.get("precoProduto"));
        subquery.where(criteriaBuilder.equal(subqueryRoot.get("produto"), root));

        criteriaQuery.where(
                criteriaBuilder.greaterThan(
                        root.get("preco"), criteriaBuilder.all(subquery)),
                criteriaBuilder.exists(subquery)
        );

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(obj -> System.out.println("ID: " + obj.getId()));
    }

    @Test
    public void pesquisarComAll01() {
//        Todos os produtos que SEMPRE foram vendidos pelo preco atual.
//        String jpql = "select p from Produto p where " +
//                " p.preco = ALL (select precoProduto from ItemPedido where produto = p)";

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.select(root);

        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        subquery.select(subqueryRoot.get("precoProduto"));
        subquery.where(criteriaBuilder.equal(subqueryRoot.get("produto"), root));

        criteriaQuery.where(criteriaBuilder.equal(
                root.get("preco"), criteriaBuilder.all(subquery)));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(obj -> System.out.println("ID: " + obj.getId()));
    }	
	
	
    @Test
    public void perquisarComExistsExercicio() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.select(root);

        Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        subquery.select(criteriaBuilder.literal(1));
        subquery.where(
                criteriaBuilder.equal(subqueryRoot.get("produto"), root),
                criteriaBuilder.notEqual(
                        subqueryRoot.get("precoProduto"), root.get("preco"))
        );

        criteriaQuery.where(criteriaBuilder.exists(subquery));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(obj -> System.out.println("ID: " + obj.getId()));
    }
	
	@Test
	public void pesquisarComInExercicio() {
		//Todos os pedidos que tem algum produto da categoria de ID = 2
		
        // String jpql = "select p from Pedido p where p.id in " +
        //        " (select p2.id from ItemPedido i2 " +
        //        "      join i2.pedido p2 join i2.produto pro2 join pro2.categorias c2 where c2.id = 2)";
        
        //String jpql = "select p from Pedido p join p.itens ip join ip.produto pro join pro.categorias cat where cat.id = 2";
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
		Root<Pedido> root = criteriaQuery.from(Pedido.class);		
		
		criteriaQuery.select(root);
		
		Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
		Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);		
		Join<ItemPedido, Produto> subqueryJoinProduto = subqueryRoot.join("produto");
		Join<Produto, Categoria> subqueryJoinProdutoCategoria  = subqueryJoinProduto.join("categorias");
		subquery.select(subqueryRoot.get("id").get("pedidoId"));
		subquery.where(criteriaBuilder.equal(subqueryJoinProdutoCategoria .get("id"), 2));
		
		criteriaQuery.where(root.get("id").in(subquery));
		
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);

		List<Pedido> lista = typedQuery.getResultList();
		Assertions.assertFalse(lista.isEmpty());

		lista.forEach(obj -> System.out.println("ID: " + obj.getId()));
	}
		
	@Test
	public void pesquisarComSubqueryExercicio() {
		//Todos os clientes que fizeram mais de dois pedidos
		// String jpql = "select c from Cliente c where 2 < (select count(p) from Pedido p where p.cliente  = c)";
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
		Root<Cliente> root = criteriaQuery.from(Cliente.class);
		
		criteriaQuery.select(root);
		
		Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
		Root<Pedido> subqueryRoot = subquery.from(Pedido.class);
		subquery.select(criteriaBuilder.count(subqueryRoot));
		subquery.where(criteriaBuilder.equal(subqueryRoot.get("cliente"), root));
		
		criteriaQuery.where(criteriaBuilder.greaterThan(subquery, 2L));
		
		TypedQuery<Cliente> typedQuery = entityManager.createQuery(criteriaQuery);
		
		List<Cliente> lista = typedQuery.getResultList();
		Assertions.assertFalse(lista.isEmpty());

		lista.forEach(obj -> System.out.println("ID: " + obj.getId() + ", Nome: " + obj.getNome()));
	}

	@Test
	public void pesquisarComExists() {
//	        Todos os produtos que j? foram vendidos.
//	        String jpql = "select p from Produto p where exists " +
//	                " (select 1 from ItemPedido ip2 join ip2.produto p2 where p2 = p)";

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
		Root<Produto> root = criteriaQuery.from(Produto.class);

		criteriaQuery.select(root);

		Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
		Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
		subquery.select(criteriaBuilder.literal(1));
		subquery.where(criteriaBuilder.equal(subqueryRoot.get("produto"), root));

		criteriaQuery.where(criteriaBuilder.exists(subquery));

		TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);

		List<Produto> lista = typedQuery.getResultList();
		Assertions.assertFalse(lista.isEmpty());

		lista.forEach(obj -> System.out.println("ID: " + obj.getId()));
	}

	@Test
	public void pesquisarComIN() {
//        String jpql = "select p from Pedido p where p.id in " +
//                " (select p2.id from ItemPedido i2 " +
//                "      join i2.pedido p2 join i2.produto pro2 where pro2.preco > 100)";

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
		Root<Pedido> root = criteriaQuery.from(Pedido.class);

		criteriaQuery.select(root);

		Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
		Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
		Join<ItemPedido, Pedido> subqueryJoinPedido = subqueryRoot.join("pedido");
		Join<ItemPedido, Produto> subqueryJoinProduto = subqueryRoot.join("produto");
		subquery.select(subqueryJoinPedido.get("id"));
		subquery.where(criteriaBuilder.greaterThan(subqueryJoinProduto.get("preco"), new BigDecimal(100)));

		criteriaQuery.where(root.get("id").in(subquery));

		TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);

		List<Pedido> lista = typedQuery.getResultList();
		Assertions.assertFalse(lista.isEmpty());

		lista.forEach(obj -> System.out.println("ID: " + obj.getId()));
	}

	@Test
	public void pesquisarSubqueries03() {
//        Bons clientes.
//        String jpql = "select c from Cliente c where " +
//                " 1300 < (select sum(p.total) from Pedido p where p.cliente = c)";

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
		Root<Cliente> root = criteriaQuery.from(Cliente.class);

		criteriaQuery.select(root);

		Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
		Root<Pedido> subqueryRoot = subquery.from(Pedido.class);
		subquery.select(criteriaBuilder.sum(subqueryRoot.get("total")));
		subquery.where(criteriaBuilder.equal(subqueryRoot.get("cliente"), root));

		criteriaQuery.where(criteriaBuilder.greaterThan(subquery, new BigDecimal(1300)));

		TypedQuery<Cliente> typedQuery = entityManager.createQuery(criteriaQuery);

		List<Cliente> lista = typedQuery.getResultList();
		Assertions.assertFalse(lista.isEmpty());

		lista.forEach(obj -> System.out.println("ID: " + obj.getId() + ", Nome: " + obj.getNome()));
	}

	@Test
	public void pesquisarSubqueries02() {
//         Todos os pedidos acima da m?dia de vendas
//        String jpql = "select p from Pedido p where " +
//                " p.total > (select avg(total) from Pedido)";

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
		Root<Pedido> root = criteriaQuery.from(Pedido.class);

		criteriaQuery.select(root);

		Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
		Root<Pedido> subqueryRoot = subquery.from(Pedido.class);
		subquery.select(criteriaBuilder.avg(subqueryRoot.get("total")).as(BigDecimal.class));

		criteriaQuery.where(criteriaBuilder.greaterThan(root.get("total"), subquery));

		TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);

		List<Pedido> lista = typedQuery.getResultList();
		Assertions.assertFalse(lista.isEmpty());

		lista.forEach(obj -> System.out.println("ID: " + obj.getId() + ", Total: " + obj.getTotal()));
	}

	@Test
	public void pesquisarSubqueries01() {
//         O produto ou os produtos mais caros da base.
//        String jpql = "select p from Produto p where " +
//                " p.preco = (select max(preco) from Produto)";

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
		Root<Produto> root = criteriaQuery.from(Produto.class);

		criteriaQuery.select(root);

		Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
		Root<Produto> subqueryRoot = subquery.from(Produto.class);
		subquery.select(criteriaBuilder.max(subqueryRoot.get("preco")));

		criteriaQuery.where(criteriaBuilder.equal(root.get("preco"), subquery));

		TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);

		List<Produto> lista = typedQuery.getResultList();
		Assertions.assertFalse(lista.isEmpty());

		lista.forEach(obj -> System.out
				.println("ID: " + obj.getId() + ", Nome: " + obj.getNome() + ", Pre?o: " + obj.getPreco()));
	}

}