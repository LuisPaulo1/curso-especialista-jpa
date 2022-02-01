package com.algaworks.ecommerce.conhecendoentitymanager;

import org.junit.jupiter.api.Test;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Produto;

public class CachePrimeiroNivelTest extends EntityManagerTest {

    @Test
    public void verificarCache() {
    	
        Produto produto = entityManager.find(Produto.class, 1);
        
        System.out.println(produto.getNome());

        System.out.println("------------------------------");

        //entityManager.close();
        //entityManager = entityManagerFactory.createEntityManager();

        Produto produtoResgatado = entityManager.find(Produto.class, produto.getId());
        System.out.println(produtoResgatado.getNome());
    }
}