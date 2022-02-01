package com.algaworks.ecommerce.consultasnativas;

import java.util.List;

import javax.persistence.Query;

import org.junit.jupiter.api.Test;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;

public class ViewTest extends EntityManagerTest {

    @SuppressWarnings("unchecked")
	@Test
    public void executarView() {
        Query query = entityManager.createNativeQuery(
                "select cli.id, cli.nome, sum(ped.total) " +
                        " from pedido ped " +
                        " join view_clientes_acima_media cli on cli.id = ped.cliente_id " +
                        " group by ped.cliente_id");

        List<Object[]> lista = query.getResultList();

        lista.stream().forEach(arr -> System.out.println(
                String.format("Cliente => ID: %s, Nome: %s, Total: %s", arr)));
    }

    @SuppressWarnings("unchecked")
	@Test
    public void executarViewRetornandoCliente() {
        Query query = entityManager.createNativeQuery(
                "select * from view_clientes_acima_media", Cliente.class);

        List<Cliente> lista = query.getResultList();

        lista.stream().forEach(obj -> System.out.println(
                String.format("Cliente => ID: %s, Nome: %s", obj.getId(), obj.getNome())));
    }
}