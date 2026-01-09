package com.app2.productsCatalog.domain.chart;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Entidade que representa um carrinho de compras no sistema")
@Table(name ="chart")
@Entity(name = "chart")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chart {
	
	@Schema(
		description = "ID único do carrinho (UUID)",
		example = "123e4567-e89b-12d3-a456-426614174000",
		accessMode = Schema.AccessMode.READ_ONLY
	)
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Schema(
		description = "ID do usuário proprietário do carrinho",
		example = "987e6543-e21b-45d3-b456-426614174999",
		required = true
	)
	@Column(name = "user_id")
	private UUID userId;
	
	@Schema(
		description = "Quantidade total de itens no carrinho",
		example = "5",
		minimum = "0"
	)
	@Column(name = "qtd_itens")
	private int qtdItens;
	
	@Schema(
		description = "Valor total do carrinho em reais (R$)",
		example = "299.90",
		minimum = "0.0"
	)
	@Column(name = "total_value")
	private Double totalValue;
	

	public Chart(UUID user_id, Double total_value, int qtd_itens) {
		this.userId = user_id;
		this.qtdItens = qtd_itens;
		this.totalValue = total_value;
	}
}
