package com.algaworks.ecommerce.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;

import com.algaworks.ecommerce.dto.ProdutoDTO;
import com.algaworks.ecommerce.listener.GenericoListener;
import com.algaworks.ecommerce.model.converter.BooleanToSimNaoConverter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NamedNativeQueries({
    @NamedNativeQuery(name = "produto_loja.listar",
            query = "select id, nome, descricao, data_criacao, data_ultima_atualizacao, preco, foto " +
                    " from produto_loja", resultClass = Produto.class),
    @NamedNativeQuery(name = "ecm_produto.listar",
            query = "select * from ecm_produto", resultSetMapping = "ecm_produto.Produto")
})
@SqlResultSetMappings({
    @SqlResultSetMapping(name = "produto_loja.Produto",
            entities = { @EntityResult(entityClass = Produto.class) }),
    @SqlResultSetMapping(name = "ecm_produto.Produto",
    entities = { @EntityResult(entityClass = Produto.class,
            fields = {
                    @FieldResult(name = "id", column = "prd_id"),
                    @FieldResult(name = "nome", column = "prd_nome"),
                    @FieldResult(name = "descricao", column = "prd_descricao"),
                    @FieldResult(name = "preco", column = "prd_preco"),
                    @FieldResult(name = "foto", column = "prd_foto"),
                    @FieldResult(name = "dataCriacao", column = "prd_data_criacao"),
                    @FieldResult(name = "dataUltimaAtualizacao", column = "prd_data_ultima_atualizacao")
            }) }),
    @SqlResultSetMapping(name = "ecm_produto.ProdutoDTO",
    classes = {
            @ConstructorResult(targetClass = ProdutoDTO.class,
                    columns = {
                            @ColumnResult(name = "prd_id", type = Integer.class),
                            @ColumnResult(name = "prd_nome", type = String.class)
                    })
    })
})
@NamedQueries({
    @NamedQuery(name = "Produto.listar", query = "select p from Produto p"),
    @NamedQuery(name = "Produto.listarPorCategoria", query = "select p from Produto p where exists (select 1 from Categoria c2 join c2.produtos p2 where p2 = p and c2.id = :categoria)")
})
@EntityListeners({ GenericoListener.class })
@Entity
@Table(name = "produto",
		uniqueConstraints = { @UniqueConstraint(name = "unq_nome", columnNames = { "nome" }) },
		indexes = { @Index(name = "idx_nome", columnList = "nome") })
public class Produto extends EntidadeBaseInteger {
		
    @NotBlank
    @Column(length = 100, nullable = false)
    private String nome;

    @Lob
    private String descricao;

    @Positive
    private BigDecimal preco;

    @Lob
    private byte[] foto;
    
    @Convert(converter = BooleanToSimNaoConverter.class)
    @NotNull
    @Column(length = 3, nullable = false)
    private Boolean ativo = Boolean.FALSE;
    
    @PastOrPresent
    @NotNull
    @Column(name = "data_criacao", updatable = false, nullable = false)
    private LocalDateTime dataCriacao;
    
    @PastOrPresent
    @Column(name = "data_ultima_atualizacao", insertable = false)
    private LocalDateTime dataUltimaAtualizacao;

    @ManyToMany
    @JoinTable(name = "produto_categoria",
            joinColumns = @JoinColumn(name = "produto_id", nullable = false,
                    foreignKey = @ForeignKey(name = "fk_produto_categoria_produto")),
            inverseJoinColumns = @JoinColumn(name = "categoria_id", nullable = false,
                    foreignKey = @ForeignKey(name = "fk_produto_categoria_categoria")))
    private List<Categoria> categorias;

    @OneToOne(mappedBy = "produto")
    private Estoque estoque;

    @ElementCollection
    @CollectionTable(name = "produto_tag",
            joinColumns = @JoinColumn(name = "produto_id", nullable = false,
                    foreignKey = @ForeignKey(name = "fk_produto_tag_produto")))
    @Column(name = "tag", length = 50, nullable = false)
    private List<String> tags;

    @ElementCollection
    @CollectionTable(name = "produto_atributo",
            joinColumns = @JoinColumn(name = "produto_id", nullable = false,
                    foreignKey = @ForeignKey(name = "fk_produto_atributo_produto")))
    private List<Atributo> atributos;    
}    