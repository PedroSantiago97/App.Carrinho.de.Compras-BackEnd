package com.app2.productsCatalog.domain.products;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Schema(
    description = "Entidade que representa um produto no catálogo",
    requiredProperties = {"name", "price"}
)
@Entity(name = "products")
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Products {
    
    @Schema(
        description = "ID único do produto (UUID)",
        example = "550e8400-e29b-41d4-a716-446655440000",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Schema(
        description = "Nome do produto",
        example = "Smartphone XYZ",
        minLength = 2,
        maxLength = 200,
        required = true
    )
    @Column(nullable = false, length = 200)
    private String name;
    
    @Schema(
        description = "URL da imagem do produto",
        example = "https://exemplo.com/imagens/smartphone-xyz.jpg",
        maxLength = 500
    )
    @Column(name = "image_url", length = 500)
    private String image_url;
    
    @Schema(
        description = "Preço do produto em reais (R$)",
        example = "1299.99",
        minimum = "0.0",
        required = true
    )
    @Column(nullable = false, precision = 10, scale = 2)
    private Double price;
    
    // Construtor SEM @Schema - não é permitido em construtores
    public Products(String name, String image_url, Double price) {
        this.name = name;
        this.image_url = image_url;
        this.price = price;
    }
}
