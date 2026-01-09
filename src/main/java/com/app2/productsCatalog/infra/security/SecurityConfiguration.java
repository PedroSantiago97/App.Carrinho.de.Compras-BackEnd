package com.app2.productsCatalog.infra.security;


import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Autowired
	SecurityFilter securityFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http 
			.csrf(csrf -> csrf.disable())
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/swagger-ui/**").permitAll()
					.requestMatchers("/v3/api-docs/**").permitAll()
	                .requestMatchers("/swagger-ui.html").permitAll()
	                .requestMatchers("/swagger-resources/**").permitAll()
	                .requestMatchers("/webjars/**").permitAll()
	                .requestMatchers("/configuration/**").permitAll()
					.requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
					.requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
					.requestMatchers(HttpMethod.POST, "/product/add").hasRole("ADMIN")
					.requestMatchers(HttpMethod.GET, "/product").permitAll()
					.requestMatchers(HttpMethod.POST,"/product/chart/add").permitAll()
					.requestMatchers(HttpMethod.GET, "/product/clients").hasRole("ADMIN")
					
					.anyRequest().authenticated())
			.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
			
		
		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		
		CorsConfiguration configuration = new CorsConfiguration();
		
		configuration.setAllowedOriginPatterns(Arrays.asList(
				"localhost:*",
				"http://127.0.0.1:5500",
				"/swagger-ui/**",
                "/swagger-ui.html",
                "/v3/api-docs/**",
                "/v3/api-docs.yaml",
                "/swagger-resources/**",
                "/webjars/**"
				));
		configuration.setAllowedMethods(Arrays.asList(
				"GET","POST","PUT","DELETE","OPTIONS"
				));
		configuration.setAllowedHeaders(Arrays.asList(
				"*"
				));
		configuration.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
