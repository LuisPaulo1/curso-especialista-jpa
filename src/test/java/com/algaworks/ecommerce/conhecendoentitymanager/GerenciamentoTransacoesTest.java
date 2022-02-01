package com.algaworks.ecommerce.conhecendoentitymanager;

import org.junit.jupiter.api.Test;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.StatusPedido;

public class GerenciamentoTransacoesTest extends EntityManagerTest {

    @Test
    public void abrirFecharCancelarTransacao() {
        try {
            entityManager.getTransaction().begin();
            metodoDeNegocio();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    private void metodoDeNegocio() {
        Pedido pedido = entityManager.find(Pedido.class, 1);
        pedido.setStatus(StatusPedido.PAGO);

        if (!pedido.getStatus().equals(StatusPedido.PAGO)) {
            throw new RuntimeException("Pedido ainda não foi pago.");
        }
    }
}