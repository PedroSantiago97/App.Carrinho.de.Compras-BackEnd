CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE products (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	name TEXT NOT NULL,
	image_url TEXT,
	price DECIMAL(10,2) NOT NULL CHECK (price > 0)
	
);