### Controle de ponto

Sistema simples de controle de ponto

#### Rodando o sistema

A seguintes variáveis de ambiente são necessárias para subir a aplicação:

    DB_PASSWORD=<password-postgres>
    DB_USER=<postgres-user>
    DB_HOST=<postgres-host>
    DB_PORT=<postgres-port>
    DB_NAME=<nomedo-banco>
    JWT_SECRET=<assinatura-token>

#### Tecnologias

- Maven
- Spring boot
- Hibernate
- PostgreSQL
- Flyway
- Spring Security

#### Subindo aplicação com docker

Da raiz do projeto, digite o comando:

    docker-compose up