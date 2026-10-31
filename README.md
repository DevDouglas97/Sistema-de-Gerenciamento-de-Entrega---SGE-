# Sistema de Gerenciamento de Rotas e Entregas (SGE)

Um sistema desktop desenvolvido em **Java (Swing)** para controle, monitoramento e gerenciamento de rotas de entrega, pedidos e endereços. O projeto utiliza o padrão de arquitetura **MVC (Model-View-Controller)** com **DAO (Data Access Object)** e persistência em **MySQL**.

---

## Funcionalidades Principais

- **Gerenciamento de Rotas:**
  - Visualização detalhada das rotas de entrega vinculadas a clientes e pedidos.
  - Atualização do status da rota ("Pendente", "Em Rota", "Entregue", "Cancelado").
  - Renderização visual e colorida do status na tabela (JTable).
  - Filtro por status para facilitar a navegação nas entregas.
- **Módulo de Endereços e Pedidos:**
  - Associação automática entre os endereços de entrega cadastrados e os pedidos do cliente.
- **Painel Administrativo (Dashboard):**
  - Navegação entre os painéis com atualização dinâmica de telas.
  - Menu adaptado ao perfil do usuário logado.
- **Segurança:**
  - Senhas armazenadas com hash **PBKDF2WithHmacSHA256** (com sal aleatório).
  - Credenciais do banco fora do código-fonte.
  - Migração automática de senhas antigas em texto puro no primeiro login.

---

## Tecnologias Utilizadas

- **Linguagem:** Java 21 (compatível com JDK 21+)
- **Interface Gráfica:** Java Swing / NetBeans GUI Builder
- **Padrão de Arquitetura:** MVC (Model-View-Controller) e DAO (Data Access Object)
- **Banco de Dados:** MySQL
- **Driver de Conexão:** MySQL Connector/J 8.0.21
- **Testes:** JUnit 4 + H2 (banco em memória)
- **Build:** Apache Ant (projeto NetBeans)

---

## Estrutura do Projeto

```
sge/
├── src/
│   ├── sge/SGE.java              # Classe principal
│   ├── Telas/                    # TelaLogin e Dashboard
│   ├── Panel*/                   # Telas de cada módulo
│   ├── Controller/               # Regras de aplicação
│   ├── DAO/                      # Acesso ao banco
│   ├── Model/                    # Entidades
│   ├── Util/                     # Configuração, conexão, senha e permissões
│   └── resource/                 # Logos e imagens
├── test/                         # Testes automatizados (JUnit + H2)
├── database/schema.sql           # Criação do banco e usuário inicial
├── lib/                          # Dependências (driver MySQL, JUnit, H2)
├── config.properties.example     # Modelo de configuração
└── nbproject/                    # Configuração do projeto NetBeans/Ant
```

---

## Como Executar

### Pré-requisitos

- Java JDK 21 ou superior.
- Servidor MySQL em execução (5.7+ ou 8.0).
- NetBeans (recomendado), IntelliJ IDEA ou Eclipse.
- Apache Ant (para build/teste pela linha de comando).

### 1. Criar o banco de dados

```bash
mysql -u root -p < database/schema.sql
```

Isso cria o banco `sge`, todas as tabelas e o usuário inicial:

- **Login:** `admin`
- **Senha:** `admin123`

> Troque essa senha após o primeiro acesso.

### 2. Configurar a conexão

Copie o arquivo de exemplo e ajuste os valores:

```bash
cp config.properties.example config.properties
```

```properties
db.url=jdbc:mysql://localhost:3306/sge?useTimezone=true&serverTimezone=UTC
db.usuario=root
db.senha=sua_senha
db.driver=com.mysql.cj.jdbc.Driver
```

O arquivo `config.properties` **não é versionado** (está no `.gitignore`).

Também é possível configurar por variáveis de ambiente:

```bash
export SGE_DB_URL="jdbc:mysql://localhost:3306/sge?serverTimezone=UTC"
export SGE_DB_USUARIO="root"
export SGE_DB_SENHA="sua_senha"
export SGE_DB_DRIVER="com.mysql.cj.jdbc.Driver"
```

Ou por propriedades da JVM:

```bash
ant run -Ddb.usuario=root -Ddb.senha=sua_senha
```

**Ordem de precedência:** propriedade da JVM > variável de ambiente > `config.properties` > valor padrão.

### 3. Compilar e executar

Pela IDE (NetBeans): abra o projeto e execute `sge.SGE`.

Pela linha de comando:

```bash
ant clean jar
java -cp "build/classes:lib/*" sge.SGE
```

---

## Testes Automatizados

A suíte usa **JUnit 4** e um banco **H2 em memória** no modo de compatibilidade MySQL, portanto **não é necessário ter o MySQL ligado** para rodar os testes.

```bash
ant test
```

Os testes cobrem:

- Configuração e precedência de credenciais.
- Hash/verificação de senhas e migração de senhas legadas.
- Regras de permissão por perfil.
- Modelos (IDs de relacionamento e valores padrão).
- Validação do `RotasController`.
- DAOs de clientes, endereços, produtos, expedições, rotas e usuários (INSERT, UPDATE, SELECT, DELETE e vínculos por ID).
- Abertura de conexão via configuração externa.

---

## Notas sobre a Arquitetura

- **Relacionamentos por ID:** as telas enviam o ID das entidades selecionadas (cliente, motorista, produto, endereço), e os DAOs priorizam esse ID. O fallback por nome foi mantido para compatibilidade com dados/chamadas antigas.
- **Controle de acesso:** a regra fica em `Util/PermissaoUtil` e o Dashboard esconde os módulos que o perfil não pode acessar.
- **Transações:** a gravação de expedições e seus itens usa `commit`/`rollback`.

---

## Segurança

- As credenciais do banco não ficam mais no código-fonte.
- Senhas são gravadas como hash PBKDF2 (nunca em texto puro).
- **Importante:** versões anteriores do repositório continham uma senha de banco em texto puro no histórico do Git. Recomenda-se **alterar a senha desse usuário MySQL** e, se necessário, limpar o histórico.
- O usuário inicial `admin/admin123` deve ser alterado após o primeiro acesso.

---

## Autor

Desenvolvido por Douglas Soares Paz.
