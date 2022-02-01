
package com.algaworks.ecommerce.relacionamentos;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Categoria;

public class AutoRelacionamentoTest extends EntityManagerTest {

    @Test
    public void verificarRelacionamento() {
        Categoria categoriaPai = new Categoria();
        categoriaPai.setNome("Futebol");

        Categoria categoria = new Categoria();
        categoria.setNome("Uniformes");
        categoria.setCategoriaPai(categoriaPai);


        entityManager.getTransaction().begin();
        entityManager.persist(categoriaPai);
        entityManager.persist(categoria);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Categoria categoriaVerificacao = entityManager.find(Categoria.class, categoria.getId());
        Assertions.assertNotNull(categoriaVerificacao.getCategoriaPai());

        Categoria categoriaPaiVerificacao = entityManager.find(Categoria.class, categoriaPai.getId());
        Assertions.assertFalse(categoriaPaiVerificacao.getCategorias().isEmpty());
    }
}