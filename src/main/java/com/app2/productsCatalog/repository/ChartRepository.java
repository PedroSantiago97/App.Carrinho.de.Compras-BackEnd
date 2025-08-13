package com.app2.productsCatalog.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app2.productsCatalog.domain.chart.Chart;

public interface ChartRepository extends JpaRepository<Chart, UUID>{
	
	interface UserSummary{
		String getUserName();
		UUID getUserId();
		Integer getTotalItems();
		Double getTotalValue();
	}
	@Query(value = """
			SELECT
				u.login as userName,
				c.user_id as userId,
				SUM(c.qtd_itens) as totalItems,
				SUM(c.total_value) as totalValue
			FROM chart c
			JOIN users u ON c.user_id = u.id
			GROUP BY c.user_id, u.login
			""",
			nativeQuery = true)
	List<UserSummary> getUserPurchaseSummary();
}
