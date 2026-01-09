package com.app2.productsCatalog.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enumeração que representa os perfis (roles) de usuário no sistema.
 * Define os níveis de acesso e permissões disponíveis para cada tipo de usuário.
 * 
 * Esta enumeração é utilizada para controle de autorização através do Spring Security,
 * permitindo restringir ou liberar acesso a endpoints baseados no perfil do usuário.
 * 
 * @author SeuNome
 * @since 1.0
 * @version 1.0
 * 
 * @see PedroSantiago97
 * @see org.springframework.security.core.authority.SimpleGrantedAuthority
 */
@Schema(
    name = "UserRole",
    description = """
        Enumeração que define os perfis de usuário disponíveis no sistema.
        
        ### Perfis disponíveis:
        
        #### ADMIN
        - **Descrição**: Usuário administrador com acesso completo ao sistema
        - **Permissões**: 
          - Todas as permissões de USER
          - Gerenciamento de produtos (criar, editar, excluir)
          - Acesso a relatórios e dados de todos os usuários
          - Configurações do sistema
        
        #### USER  
        - **Descrição**: Usuário comum com acesso básico ao sistema
        - **Permissões**:
          - Visualizar catálogo de produtos
          - Gerenciar próprio carrinho de compras
          - Finalizar pedidos
          - Acessar histórico próprio de compras
        
        ### Hierarquia de permissões:
        ```
        ADMIN
        └── USER (herda todas as permissões)
        ```
        
        ### Uso no Spring Security:
        ```java
        // Em controllers para restringir acesso
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity criarProduto() { ... }
        
        @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
        public ResponseEntity verCarrinho() { ... }
        ```
        
        ### Valores armazenados no banco de dados:
        - Como texto (String) utilizando @Enumerated(EnumType.STRING)
        - Valores: "admin", "user"
        """,
    example = "USER"
)
public enum UserRole {
    
    /**
     * Perfil de administrador do sistema.
     * Possui acesso completo a todas as funcionalidades.
     * 
     * ### Permissões incluídas:
     * - ROLE_ADMIN
     * - ROLE_USER (herdado automaticamente)
     * 
     * ### Casos de uso típicos:
     * - Gerenciamento de catálogo de produtos
     * - Visualização de relatórios de vendas
     * - Administração de usuários
     * - Configurações do sistema
     */
    @Schema(
        description = """
            Perfil de administrador com acesso completo ao sistema.
            
            Inclui automaticamente todas as permissões de USER.
            """,
        example = "admin"
    )
    ADMIN("admin"),
    
    /**
     * Perfil de usuário comum do sistema.
     * Possui acesso às funcionalidades básicas para clientes.
     * 
     * ### Permissões:
     * - ROLE_USER
     * 
     * ### Casos de uso típicos:
     * - Navegação no catálogo de produtos
     * - Adição de itens ao carrinho
     * - Finalização de pedidos
     * - Acompanhamento de pedidos próprios
     */
    @Schema(
        description = """
            Perfil de usuário comum com acesso às funcionalidades básicas.
            """,
        example = "user"
    )
    USER("user");
    
    /**
     * Representação textual do perfil, utilizada para persistência
     * no banco de dados e serialização em APIs.
     */
    private String role;
    
    /**
     * Construtor da enumeração UserRole.
     * 
     * @param role Representação textual do perfil (ex: "admin", "user")
     */
    UserRole(String role) {
        this.role = role;
    }
    
    /**
     * Retorna a representação textual do perfil de usuário.
     * Este valor é utilizado para:
     * - Persistência no banco de dados
     * - Serialização em respostas JSON
     * - Comparações e validações
     * 
     * @return String representando o perfil (ex: "admin", "user")
     */
    @Schema(
        description = "Representação textual do perfil para persistência e serialização",
        example = "user",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    public String getRole() {
        return role;
    }
    
    /**
     * Método utilitário para converter uma string em um valor UserRole.
     * Útil para processamento de dados vindos de formulários ou APIs.
     * 
     * @param role String representando o perfil (case-insensitive)
     * @return UserRole correspondente
     * @throws IllegalArgumentException se a string não corresponder a nenhum perfil válido
     */
    @Schema(hidden = true)
    public static UserRole fromString(String role) {
        if (role == null) {
            return USER; // Valor padrão
        }
        
        String normalizedRole = role.trim().toLowerCase();
        
        for (UserRole userRole : UserRole.values()) {
            if (userRole.getRole().equals(normalizedRole)) {
                return userRole;
            }
        }
        
        // Tenta pelo nome do enum também (ADMIN, USER)
        try {
            return UserRole.valueOf(role.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                String.format("Perfil '%s' não é válido. Use: %s", 
                    role, 
                    String.join(", ", 
                        java.util.Arrays.stream(UserRole.values())
                            .map(UserRole::getRole)
                            .toArray(String[]::new)
                    )
                )
            );
        }
    }
    
    
}