package com.algaworks.ecommerce.detalhesimportantes;

import org.junit.jupiter.api.Test;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;

public class ValidacaoTest extends EntityManagerTest {

    @Test
    public void validarCliente() {
        entityManager.getTransaction().begin();

        Cliente cliente = new Cliente();
        entityManager.merge(cliente);

        entityManager.getTransaction().commit();
    }
}