package com.app2.productsCatalog.domain.chart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(
    description = "DTO (Data Transfer Object) para criação de um novo carrinho de compras",
    requiredProperties = {"nome", "total_value", "qtd_itens"}
)
public record CreateChartDTO(
    
    @Schema(
        description = "Nome ou identificador do carrinho",
        example = "Carrinho Principal",
        minLength = 3,
        maxLength = 100,
        required = true
    )
    @NotBlank(message = "O nome do carrinho é obrigatório")
    @jakarta.validation.constraints.Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    String nome,
    
    @Schema(
        description = "Valor total do carrinho em reais (R$)",
        example = "199.99",
        minimum = "0.0",
        required = true
    )
    @NotNull(message = "O valor total é obrigatório")
    @PositiveOrZero(message = "O valor total não pode ser negativo")
    Double total_value,
    
    @Schema(
        description = "Quantidade total de itens no carrinho",
        example = "5",
        minimum = "0",
        required = true
    )
    @NotNull(message = "A quantidade de itens é obrigatória")
    @Min(value = 0, message = "A quantidade de itens não pode ser negativa")
    int qtd_itens
) {

    // Método de validação customizada (opcional)
    @Schema(hidden = true)
    public boolean isValid() {
        return nome != null && !nome.trim().isEmpty() 
               && total_value != null && total_value >= 0 
               && qtd_itens >= 0;
    }
    
    // Método para converter em entidade Chart (opcional)
    @Schema(hidden = true)
    public Chart toEntity() {
        // Note: O Chart precisa de um UUID userId - você pode precisar ajustar
        // Este é um exemplo genérico
        return new Chart(
            null, // ID será gerado automaticamente
            java.util.UUID.randomUUID(), // Você precisa definir como obter o userId
            qtd_itens,
            total_value
        );
    }
}
