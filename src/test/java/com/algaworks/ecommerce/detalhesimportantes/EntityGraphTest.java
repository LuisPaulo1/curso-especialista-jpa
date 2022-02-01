package com.algaworks.ecommerce.detalhesimportantes;

import java.util.List;

import javax.persistence.EntityGraph;
import javax.persistence.Subgraph;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.Pedido;

public class EntityGraphTest extends EntityManagerTest {
	
    @Test
    public void buscarAtributosEssenciaisComNamedEntityGraph() {
        EntityGraph<?> entityGraph = entityManager
                .createEntityGraph("Pedido.dadosEssencias");
        entityGraph.addAttributeNodes("pagamento");

        TypedQuery<Pedido> typedQuery = entityManager
                .createQuery("select p from Pedido p", Pedido.class);
        typedQuery.setHint("javax.persistence.fetchgraph", entityGraph);
        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }
	
    @Test
    public void buscarAtributosEssenciaisDePedido02() {
    	
        EntityGraph<Pedido> entityGraph = entityManager.createEntityGraph(Pedido.class);
        entityGraph.addAttributeNodes("dataCriacao", "status", "total");

        Subgraph<Cliente> subgraphCliente = entityGraph.addSubgraph("cliente", Cliente.class);
        subgraphCliente.addAttributeNodes("nome", "cpf");

        TypedQuery<Pedido> typedQuery = entityManager
                .createQuery("select p from Pedido p", Pedido.class);
        typedQuery.setHint("javax.persistence.fetchgraph", entityGraph);
        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    public void buscarAtributosEssenciaisDePedido() {
    	
        EntityGraph<Pedido> entityGraph = entityManager.createEntityGraph(Pedido.class);
        entityGraph.addAttributeNodes("dataCriacao", "status", "total", "notaFiscal");
       
        /*
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.fetchgraph", entityGraph); // retorna os atributos informados no addAttributesNodes e ignora o restante mesmo se eles estiverem eager
        properties.put("javax.persistence.loadgraph", entityGraph); // retorna os atributos informados no addAttributesNodes e o restante é retornado de acordo com a configuração eager ou lazy
        Pedido pedido = entityManager.find(Pedido.class, 1, properties);
        Assertions.assertNotNull(pedido);
        */

        TypedQuery<Pedido> typedQuery = entityManager.createQuery("select p from Pedido p", Pedido.class);
        typedQuery.setHint("javax.persistence.fetchgraph", entityGraph);
        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
        
    }
}