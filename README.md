# Descarte Point — AEP 2º Bimestre

Unicesumar — ESOFT4SM-A — 2026.2

Equipe: Eduardo Roschel Giffoni, André Dias Gerez e João Victor Lara Vendrametto.

Aplicação Java de console para gerenciar pontos de descarte e resíduos, com persistência em MySQL por JDBC. Alinhada às ODS 11 e 12.

## Requisitos
- Java 17+
- Maven 3.9+
- MySQL 8+

## Execução
1. Execute `database/schema.sql` no MySQL.
2. Configure `DB_USER`, `DB_PASSWORD` e, opcionalmente, `DB_URL` no ambiente. O padrão usa localhost:3306, usuário root e senha vazia.
3. Na pasta raiz, execute `mvn compile exec:java`.
4. Use o menu para cadastrar pontos, resíduos, relacioná-los e consultar os resultados.

## Escopo
RF01: cadastrar pontos; RF02: cadastrar resíduos; RF03: associar ponto a resíduo; RF04: listar pontos; RF05: pesquisar por resíduo; RF06: atualizar ponto; RF07: excluir ponto e associações; RF08: ver detalhes.

## Orientação a objetos
A classe abstrata `Usuario` possui as subclasses `Administrador` e `UsuarioComum`, com polimorfismo por `@Override`. A interface `Residuo` possui implementações para resíduos eletrônicos, recicláveis e perigosos. Os dados operacionais são gravados em tabelas MySQL; não em listas temporárias.

## Atenção
Esta é uma aplicação acadêmica em terminal, não implementa autenticação. Não inclua senhas reais no código. Teste a conexão e o CRUD no seu MySQL antes da entrega. O histórico de commits dos integrantes deve refletir a participação real da equipe.
