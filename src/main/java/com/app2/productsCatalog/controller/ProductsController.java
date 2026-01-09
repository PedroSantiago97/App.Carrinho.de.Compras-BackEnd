package com.app2.productsCatalog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app2.productsCatalog.domain.chart.Chart;
import com.app2.productsCatalog.domain.chart.CreateChartDTO;
import com.app2.productsCatalog.domain.products.CreateProductDTO;
import com.app2.productsCatalog.domain.products.Products;
import com.app2.productsCatalog.repository.ChartRepository.UserSummary;
import com.app2.productsCatalog.service.ProductsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controlador REST para operações relacionadas a produtos e carrinho de compras.
 * Disponibiliza endpoints para gerenciamento do catálogo de produtos, 
 * adição de itens ao carrinho e consultas de clientes.
 * 
 * @author SeuNome
 * @since 1.0
 * @version 1.0
 * 
 * @see ProductsService
 * @see Products
 * @see Chart
 */
@RestController
@RequestMapping("/product")
@Tag(
    name = "Produtos e Carrinho", 
    description = "Endpoints para gerenciamento de produtos do catálogo e operações do carrinho de compras"
)
public class ProductsController {
    
    @Autowired
    private ProductsService service;
    
    /**
     * Cria um novo produto no catálogo.
     * Permite a adição de produtos ao sistema para posterior venda.
     * 
     * @param data DTO contendo informações do produto a ser criado
     * @return ResponseEntity com o produto criado ou mensagem de erro
     */
    @PostMapping("/add")
    @Operation(
        summary = "Criar novo produto",
        description = """
            Adiciona um novo produto ao catálogo do sistema.
            
            Requer autenticação com role **ADMIN**.
            
            Validações:
            - Nome não pode estar vazio
            - Preço deve ser maior que zero
            - URL da imagem deve ser válida (opcional)
            
            Exemplo de uso:
            ```json
            {
              "name": "Smartphone XYZ",
              "image_url": "https://exemplo.com/foto.jpg",
              "price": 1999.99
            }
            ```
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Produto criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Products.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos fornecidos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = """
                    {
                      "timestamp": "2024-01-15T10:30:00",
                      "status": 400,
                      "error": "Bad Request",
                      "message": "Nome do produto é obrigatório"
                    }
                    """)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Não autenticado - token JWT inválido ou ausente"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Acesso negado - requer permissão de ADMIN"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Conflito - produto com mesmo nome já existe"
        )
    })
    public ResponseEntity createProduct(
            @Parameter(
                description = "Dados do produto a ser criado",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = CreateProductDTO.class)
                )
            )
            @RequestBody @Valid CreateProductDTO data) {
        return service.addProduct(data);
    }
    
    /**
     * Lista todos os produtos disponíveis no catálogo.
     * Retorna uma lista completa dos produtos cadastrados no sistema.
     * 
     * @return Lista de produtos ordenados por nome
     */
    @GetMapping
    @Operation(
        summary = "Listar todos os produtos",
        description = """
            Retorna todos os produtos disponíveis no catálogo.
            
            Acesso público - não requer autenticação.
            
            A lista é retornada ordenada por nome do produto.
            
            Para acesso paginado ou filtrado, considere implementar parâmetros adicionais.
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de produtos recuperada com sucesso",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(
                    schema = @Schema(implementation = Products.class)
                )
            )
        ),
        @ApiResponse(
            responseCode = "204",
            description = "Nenhum produto encontrado"
        )
    })
    public List<Products> produtcsReview() {
        return service.showProducts();
    }
    
    /**
     * Adiciona um produto ao carrinho de compras.
     * Permite que usuários adicionem itens ao seu carrinho.
     * 
     * @param data DTO contendo informações do item do carrinho
     * @return ResponseEntity indicando sucesso da operação
     */
    @PostMapping("/chart/add")
    @Operation(
        summary = "Adicionar produto ao carrinho",
        description = """
            Adiciona um produto ao carrinho de compras do usuário autenticado.
            
            Requer autenticação com role **USER** ou **ADMIN**.
            
            O sistema automaticamente:
            1. Valida a existência do produto
            2. Calcula o valor total baseado na quantidade
            3. Associa ao usuário logado
            4. Atualiza o estoque (se aplicável)
            
            Exemplo de uso:
            ```json
            {
              "nome": "Smartphone XYZ",
              "total_value": 1999.99,
              "qtd_itens": 1
            }
            ```
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Produto adicionado ao carrinho com sucesso"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos ou produto não encontrado",
            content = @Content(
                schema = @Schema(example = """
                    {
                      "error": "Produto 'Smartphone XYZ' não encontrado"
                    }
                    """)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Não autenticado"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Acesso negado - requer autenticação"
        ),
        @ApiResponse(
            responseCode = "422",
            description = "Quantidade solicitada maior que estoque disponível"
        )
    })
    public ResponseEntity addChart(
            @Parameter(
                description = "Dados do item a ser adicionado ao carrinho",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = CreateChartDTO.class)
                )
            )
            @RequestBody @Valid CreateChartDTO data) {
        return service.addProductsInChart(data);
    }
    
    /**
     * Obtém resumo das compras dos clientes.
     * Retorna dados agregados das compras realizadas pelos usuários.
     * 
     * @return Lista de resumos de usuários com estatísticas de compras
     */
    @GetMapping("/clients")
    @Operation(
        summary = "Obter resumo de clientes",
        description = """
            Retorna um resumo das compras realizadas por todos os clientes.
            
            Requer autenticação com role **ADMIN**.
            
            Dados retornados por cliente:
            - Nome do usuário
            - ID do usuário
            - Total de itens comprados
            - Valor total gasto
            
            Uso típico: Relatórios administrativos e análise de vendas.
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Resumo de clientes recuperado com sucesso",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(
                    schema = @Schema(implementation = UserSummary.class)
                )
            )
        ),
        @ApiResponse(
            responseCode = "204",
            description = "Nenhum cliente com compras encontrado"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Não autenticado"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Acesso negado - requer permissão de ADMIN"
        )
    })
    public List<UserSummary> clientsReview(){
        return service.showClients();
    }
}