package com.algaworks.ecommerce.jpql;

import java.util.List;

import javax.persistence.TypedQuery;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Categoria;

public class PaginacaoJPQLTest extends EntityManagerTest {

    @Test
    public void paginarResultados() {
        String jpql = "select c from Categoria c order by c.nome";

        TypedQuery<Categoria> typedQuery = entityManager.createQuery(jpql, Categoria.class);

        // FIRST_RESULT = MAX_RESULTS * (pagina - 1)
        typedQuery.setFirstResult(0);
        typedQuery.setMaxResults(2); //limita o número de resultados

        List<Categoria> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(c -> System.out.println(c.getId() + ", " + c.getNome()));
    }
}