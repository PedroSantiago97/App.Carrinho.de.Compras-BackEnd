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

@Table(name ="chart")
@Entity(name = "chart")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chart {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	 @Column(name = "user_id") // Mapeia para a coluna no banco
	 private UUID userId;      // Campo Java em camelCase
	    
	 @Column(name = "qtd_itens")
	 private int qtdItens;
	    
	 @Column(name = "total_value")
	 private Double totalValue;
	
	public Chart(UUID user_id, Double total_value, int qtd_itens) {
		this.userId = user_id;
		this.qtdItens = qtd_itens;
		this.totalValue = total_value;
	}
	
	

}
