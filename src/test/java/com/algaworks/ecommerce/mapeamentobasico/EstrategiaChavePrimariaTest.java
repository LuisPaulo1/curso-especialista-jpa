package com.algaworks.ecommerce.mapeamentobasico;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Categoria;

public class EstrategiaChavePrimariaTest extends EntityManagerTest {

    @Test
    public void testarEstrategiaAuto() {
        Categoria categoria = new Categoria();
        categoria.setNome("Natação");

        entityManager.getTransaction().begin();
        entityManager.persist(categoria);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Categoria categoriaVerificacao = entityManager.find(Categoria.class, categoria.getId());
        Assertions.assertNotNull(categoriaVerificacao);
    }
}