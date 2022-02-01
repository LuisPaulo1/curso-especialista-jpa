package com.algaworks.ecommerce.jpql;

import java.util.List;

import javax.persistence.TypedQuery;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.algaworks.ecommerce.EntityManagerTest;

public class FuncoesTest extends EntityManagerTest {

	@Test
	public void aplicarFuncaoAgregacao() {
		
	    // avg, count, min, max, sum
	
		//String jpql = "select avg(p.total) from Pedido p";
		//String jpql = "select count(*) from Pedido p";
		//String jpql = "select min(p.total) from Pedido p";
		//String jpql = "select max(p.total) from Pedido p";
		String jpql = "select sum(p.total) from Pedido p";
	
	    TypedQuery<Number> typedQuery = entityManager.createQuery(jpql, Number.class);
	
	    List<Number> lista = typedQuery.getResultList();
	    
	    Assertions.assertFalse(lista.isEmpty());
	
	    lista.forEach(obj -> System.out.println(obj));
	}
	
	//@Test
	public void aplicarFuncaoNativas() {
		
	    String jpql = "select function('dayname', p.dataCriacao) from Pedido p " +
	            " where function('acima_media_faturamento', p.total) = 1";
	
	    TypedQuery<String> typedQuery = entityManager.createQuery(jpql, String.class);
	
	    List<String> lista = typedQuery.getResultList();
	    Assertions.assertFalse(lista.isEmpty());
	
	    lista.forEach(obj -> System.out.println(obj));
	}

	@Test
	public void aplicarFuncaoColecao() {
		String jpql = "select size(p.itens) from Pedido p where size(p.itens) > 1";
	
		TypedQuery<Integer> typedQuery = entityManager.createQuery(jpql, Integer.class);
	
		List<Integer> lista = typedQuery.getResultList();
		Assertions.assertFalse(lista.isEmpty());
	
		lista.forEach(size -> System.out.println(size));
	}

	@Test
	public void aplicarFuncaoNumero() {

		String jpql = "select abs(p.total), mod(p.id, 2), sqrt(p.total) from Pedido p where abs(p.total) > 1000";

		TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);

		List<Object[]> lista = typedQuery.getResultList();
		Assertions.assertFalse(lista.isEmpty());

		lista.forEach(arr -> System.out.println(arr[0] + " | " + arr[1] + " | " + arr[2]));
	}

	@Test
	public void aplicarFuncaoData() {
		// TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		// current_date (data atual), current_time (hora atual), current_timestamp (data e hora atual)
		// year(p.dataCriacao), month(p.dataCriacao), day(p.dataCriacao)

		// String jpql = "select current_date, current_time, current_timestamp from
		// Pedido p";
		// String jpql = "select year(p.dataCriacao), month(p.dataCriacao),
		// day(p.dataCriacao) from Pedido p";
		String jpql = "select hour(p.dataCriacao), minute(p.dataCriacao), second(p.dataCriacao) from Pedido p";
		// String jpql = "select hour(p.dataCriacao), minute(p.dataCriacao),
		// second(p.dataCriacao) from Pedido p where hour(p.dataCriacao) > 18";

		TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);

		List<Object[]> lista = typedQuery.getResultList();
		Assertions.assertFalse(lista.isEmpty());

		lista.forEach(arr -> System.out.println(arr[0] + " | " + arr[1] + " | " + arr[2]));
	}

	@Test
	public void aplicarFuncaoString() {
		// concat, length, locate, substring, lower, upper, trim

		// String jpql = "select c.nome, concat('Categoria: ', c.nome) from Categoria c";
		// String jpql = "select c.nome, length(c.nome) from Categoria c";
		// String jpql = "select c.nome, locate('a', c.nome) from Categoria c"; //retorna o índice da primeira ocorrência encontrada, o valor do índice inicia em 1. Retorna zero se não encontrar.
		// String jpql = "select c.nome, substring(c.nome, 1, 3) from Categoria c"; //o valor do índice inicia em 1
		// String jpql = "select c.nome, lower(c.nome) from Categoria c";
		// String jpql = "select c.nome, upper(c.nome) from Categoria c";
		// String jpql = "select c.nome, trim(c.nome) from Categoria c";
		// String jpql = "select c.nome, length(c.nome) from Categoria c where
		// length(c.nome) > 10";
		String jpql = "select c.nome, length(c.nome) from Categoria c where substring(c.nome, 1, 1) = 'N'";

		TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);

		List<Object[]> lista = typedQuery.getResultList();
		Assertions.assertFalse(lista.isEmpty());

		lista.forEach(arr -> System.out.println(arr[0] + " - " + arr[1]));
	}
}