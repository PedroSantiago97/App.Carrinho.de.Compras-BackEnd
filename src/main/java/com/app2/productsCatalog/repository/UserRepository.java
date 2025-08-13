package com.app2.productsCatalog.repository;



import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import com.app2.productsCatalog.domain.user.User;

public interface UserRepository extends JpaRepository<User, String>{
	
	UserDetails findByLogin(String login);
	
	
	
}
