package com.app2.productsCatalog.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object (DTO) para resposta de login bem-sucedido.
 * Contém o token JWT gerado após autenticação válida do usuário.
 * 
 * Este DTO é retornado pelo endpoint de login (/auth/login) quando as
 * credenciais do usuário são validadas com sucesso. O token JWT deve
 * ser utilizado em requisições subsequentes para acessar endpoints protegidos.
 * 
 * @author PedroSantiago97
 * @since 1.0
 * @version 1.0
 * 
 * @see AuthenticationController#login(AuthenticationDTO)
 * @see TokenService#generateToken(User)
 */
@Schema(
    name = "LoginResponseDTO",
    description = """
        Objeto de transferência de dados para resposta de autenticação bem-sucedida.
        
        ### Funcionalidade:
        Contém o token JWT que deve ser utilizado para autenticar requisições
        subsequentes à API. Este token tem tempo de expiração e contém as
        informações do usuário autenticado.
        
        ### Uso do token:
        ```http
        GET /api/protected-endpoint
        Authorization: Bearer {token}
        ```
        
        ### Características do token:
        - Formato: JSON Web Token (JWT)
        - Algoritmo: HS256
        - Tempo de expiração: 2 horas
        - Contém: ID do usuário, login e role
        - Assinatura: usando chave secreta do servidor
        
        ### Exemplo de resposta:
        ```json
        {
          "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
        }
        ```
        """,
    example = """
        {
          "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
        }
        """
)
public record LoginResponseDTO(
    
    @Schema(
        description = """
            Token JWT (JSON Web Token) para autenticação.
            
            Este token deve ser incluído no cabeçalho Authorization
            de todas as requisições a endpoints protegidos.
            
            ### Formato do cabeçalho:
            ```
            Authorization: Bearer {token}
            ```
            
            ### Estrutura do token (se decodificado):
            ```json
            {
              "sub": "123e4567-e89b-12d3-a456-426614174000",
              "login": "usuario@email.com",
              "role": "USER",
              "iat": 1678901234,
              "exp": 1678908434
            }
            ```
            
            ### Campos incluídos:
            - **sub**: ID do usuário (subject)
            - **login**: Login do usuário
            - **role**: Perfil do usuário (USER ou ADMIN)
            - **iat**: Timestamp de emissão (issued at)
            - **exp**: Timestamp de expiração (2 horas após emissão)
            
            ### Validação:
            - Token é assinado pelo servidor usando chave secreta
            - Expira após 2 horas, requerendo novo login
            - Invalidação em caso de logout ou alteração de credenciais
            """,
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String token) {
}