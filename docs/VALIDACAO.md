# Validação da AEP do 2º Bimestre

## Conferência
- Aderência: 8 requisitos funcionais cobertos no menu e nos DAOs.
- POO: classe abstrata Usuario, subclasses com @Override, interface Residuo e três implementações polimórficas.
- CRUD: criar, listar/buscar, atualizar e excluir pontos.
- Persistência: MySQL por JDBC; tabelas, chaves estrangeiras e transações em database/schema.sql.

## Roteiro de demonstração
1. Executar o script SQL no MySQL.
2. Configurar DB_URL, DB_USER e DB_PASSWORD e iniciar com mvn compile exec:java.
3. Cadastrar ponto e resíduo, associá-los, consultar por resíduo e exibir detalhes.
4. Atualizar e excluir o ponto, verificando suas associações.
5. Reiniciar e confirmar que os dados persistem.
6. Demonstrar a hierarquia Usuario e a interface Residuo.

## Verificação pendente
A equipe deve testar a execução e o CRUD em uma instância real do MySQL e garantir que os commits representem a participação verdadeira de seus integrantes.
