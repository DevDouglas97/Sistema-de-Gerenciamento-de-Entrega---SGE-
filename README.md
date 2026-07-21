# Sistema de Gerenciamento de Rotas e Entregas

Um sistema desktop desenvolvido em Java (Swing) para controle, monitoramento e gerenciamento de rotas de entrega, pedidos e endereços. O projeto utiliza o padrão de arquitetura MVC (Model-View-Controller) e persistência em banco de dados MySQL.

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

---

## Tecnologias Utilizadas

- **Linguagem:** Java (JDK 8 ou superior)
- **Interface Gráfica:** Java Swing / NetBeans GUI Builder
- **Padrão de Arquitetura:** MVC (Model-View-Controller) e DAO (Data Access Object)
- **Banco de Dados:** MySQL
- **Driver de Conexão:** MySQL Connector/J

---

## Como Executar o Projeto

**Pré-requisitos:**
  
- Java JDK 8 ou superior instalado.
- Servidor MySQL em execução.
- IDE recomendada: NetBeans, IntelliJ IDEA ou Eclipse.

## Autor:
Desenvolvido por Douglas Soares Paz.

