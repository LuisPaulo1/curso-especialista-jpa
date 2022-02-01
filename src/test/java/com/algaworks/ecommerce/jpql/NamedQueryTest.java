package com.algaworks.ecommerce.jpql;

import java.util.List;

import javax.persistence.TypedQuery;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;

public class NamedQueryTest extends EntityManagerTest {
	
    @Test
    public void executarConsultaArquivoXMLEspecificoProduto() {
    	TypedQuery<Produto> typedQuery = entityManager
                .createNamedQuery("Produto.todos", Produto.class);

        List<Produto> lista = typedQuery.getResultList();

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    public void executarConsultaArquivoXMLEspecificoPedido() {
        TypedQuery<Pedido> typedQuery = entityManager
                .createNamedQuery("Pedido.todos", Pedido.class);

        List<Pedido> lista = typedQuery.getResultList();

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    public void executarConsultaArquivoXML() {
        TypedQuery<Pedido> typedQuery = entityManager
                .createNamedQuery("Pedido.listar", Pedido.class);

        List<Pedido> lista = typedQuery.getResultList();

        Assertions.assertFalse(lista.isEmpty());
    }
	
	@Test
	public void executarConsulta1() {
		
	    TypedQuery<Produto> typedQuery = entityManager.createNamedQuery("Produto.listar", Produto.class);  
	    
	    List<Produto> lista = typedQuery.getResultList();
	
	    Assertions.assertFalse(lista.isEmpty());
	}
	
	@Test
	public void executarConsulta2() {
		
	    TypedQuery<Produto> typedQuery = entityManager.createNamedQuery("Produto.listarPorCategoria", Produto.class);
	    
	    typedQuery.setParameter("categoria", 2);
	
	    List<Produto> lista = typedQuery.getResultList();
	
	    Assertions.assertFalse(lista.isEmpty());
	}
}