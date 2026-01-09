package com.app2.productsCatalog.domain.products;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "DTO para criação de produto")
public record CreateProductDTO(
    
    @Schema(
        description = "Nome do produto (3-100 caracteres)",
        example = "Smartphone Galaxy S23",
        required = true,
        minLength = 3,
        maxLength = 100
    )
    @NotBlank
    String name,
    
    @Schema(
        description = "URL da imagem",
        example = "https://exemplo.com/imagem.jpg"
    )
    String image_url,
    
    @Schema(
        description = "Preço (maior que zero)",
        example = "1999.99",
        required = true
    )
    @NotNull
    @Positive
    Double price
) {}