package com.algaworks.ecommerce.operacaoemcascata;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Categoria;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.ItemPedido;
import com.algaworks.ecommerce.model.ItemPedidoId;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;
import com.algaworks.ecommerce.model.StatusPedido;

public class CascadeTypeMergeTest extends EntityManagerTest {
	
    //@Test
    public void atualizarProdutoComCategoria() {
        Produto produto = new Produto();
        produto.setId(1);
        produto.setDataUltimaAtualizacao(LocalDateTime.now());
        produto.setPreco(new BigDecimal(500));
        produto.setNome("Kindle");
        produto.setDescricao("Agora com iluminação embutida ajustável.");

        Categoria categoria = new Categoria();
        categoria.setId(2);
        categoria.setNome("Tablets");

        produto.setCategorias(Arrays.asList(categoria)); // CascadeType.MERGE

        entityManager.getTransaction().begin();
        entityManager.merge(produto);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Categoria categoriaVerificacao = entityManager.find(Categoria.class, categoria.getId());
        Assertions.assertEquals("Tablets", categoriaVerificacao.getNome());
    }

    //@Test
    public void atualizarPedidoComItens() {
        Cliente cliente = entityManager.find(Cliente.class, 1);
        Produto produto = entityManager.find(Produto.class, 1);

        Pedido pedido = new Pedido();
        pedido.setId(1);
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.AGUARDANDO);

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId(new ItemPedidoId());
        itemPedido.getId().setPedidoId(pedido.getId());
        itemPedido.getId().setProdutoId(produto.getId());
        itemPedido.setPedido(pedido);
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(3);
        itemPedido.setPrecoProduto(produto.getPreco());

        pedido.setItens(Arrays.asList(itemPedido)); // CascadeType.MERGE

        entityManager.getTransaction().begin();
        entityManager.merge(pedido);
        entityManager.getTransaction().commit();

        entityManager.clear();

        ItemPedido itemPedidoVerificacao = entityManager.find(ItemPedido.class, itemPedido.getId());
        Assertions.assertTrue(itemPedidoVerificacao.getQuantidade().equals(3));
    }

    //@Test
    public void atualizarItemPedidoComPedido() {
        Cliente cliente = entityManager.find(Cliente.class, 1);
        Produto produto = entityManager.find(Produto.class, 1);

        Pedido pedido = new Pedido();
        pedido.setId(1);
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.PAGO);

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId(new ItemPedidoId());
        itemPedido.getId().setPedidoId(pedido.getId());
        itemPedido.getId().setProdutoId(produto.getId());
        itemPedido.setPedido(pedido); // CascadeType.MERGE
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(5);
        itemPedido.setPrecoProduto(produto.getPreco());

        pedido.setItens(Arrays.asList(itemPedido));

        entityManager.getTransaction().begin();
        entityManager.merge(itemPedido);
        entityManager.getTransaction().commit();

        entityManager.clear();

        ItemPedido itemPedidoVerificacao = entityManager.find(ItemPedido.class, itemPedido.getId());
        Assertions.assertTrue(StatusPedido.PAGO.equals(itemPedidoVerificacao.getPedido().getStatus()));
    }
}