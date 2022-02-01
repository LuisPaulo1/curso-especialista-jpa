package com.algaworks.ecommerce.conhecendoentitymanager;

import org.junit.jupiter.api.Test;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Categoria;

public class EstadosECicloDeVidaTest extends EntityManagerTest {

    @Test
    public void analisarEstados() {
    	
        Categoria categoriaNovo = new Categoria(); //estado Transient
        categoriaNovo.setNome("Eletrônicos");

        Categoria categoriaGerenciadaMerge = entityManager.merge(categoriaNovo); // o retorno passa a ser gerenciada - estado Managed
        categoriaGerenciadaMerge.getId();

        Categoria categoriaGerenciada = entityManager.find(Categoria.class, 1); // estado Managed

        entityManager.remove(categoriaGerenciada); // estado Removed
        
        entityManager.persist(categoriaGerenciada); // estado Managed

        entityManager.detach(categoriaGerenciada);  // estado Detached
    }

}