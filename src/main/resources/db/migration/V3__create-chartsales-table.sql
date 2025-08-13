CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE chart (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	user_id UUID NOT NULL,
	qtd_itens INT NOT NULL,
	total_value DECIMAL(10,2) NOT NULL CHECK (total_value > 0)
);