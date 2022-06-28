# Descrição da API

## Configurações iniciais

### XML
No arquivo pom.xml temos as configurações do projeto, onde estão a conexão com o apache, as dependências e os plug-ins.

### Aplication Properties
No arquivo aplication.properties temos as configurações de acesso ao banco de dados, onde temos a url de acesso, username, senha e a linguagem que conversará com o banco. Também temos uma correção de bug e uma configuração que, ao se modificar uma classe na aplicação, essa classe se altere no banco também.

## Classes de Modelo

- Jogador: 
Definimos essa classe como uma tabela no banco e seus atributos como colunas. Nela temos os atributos pedidos para o jogador, seus construtores, getters e setters e método toString. Para realizarmos o projeto de maneira correta, utilizamos a notação @Id para definirmos a chave primária, @Column para as colunas da tabela, @JsonFormat para definirmos como deverá ser feita a entrada dos dados e @OneToMany para fazermos a ligação entre as classes. Para alguns atributos é pedido que se limite o tamanho das variáveis, para isso escrevemos length = N dentro da notação @Column. Para se auto-incrementar a chave-primária, utilizamos a notação @GeneratedValue.

- Pagamento:
Semelhante a classe Jogador, o Pagamento é definido como uma tabela e seus atributos como colunas. Nele temos seus atributos, construtores, getters e setter e método toString. Para o devido funcinamento, temos as mesmas notações @Id e @Column para a chave primária e colunas. Para a associação com a classe Jogador, utilizamos a notação @ManyToOne e @JoinColumn onde definimos o cod_jogador como chave estrangeira da classe Pagamento. Para não deixarmos um atributo ser nulo, escrevemos nullable = false na notação @JoinClumn. Para se auto-incrementar a chave-primária, utilizamos a notação @GeneratedValue. A notação @JsonIgnore é para corrigir um erro de inserção de um pagamento onde a chave estrangeira ficava em looping infinito.

## Classes Repositório

Nelas temos todos os métodos paara buscar e recuperarmos dados das tabelas(classes) dentro do banco de dados.

- JogadorRepository:
No caso do jogador, temos buscas por nome e por email que contenham a palavra fornecida pelo usuário.

- PagamentoRepository:
No caso do pagamento, temos buscas por valores de salários maiores do que o usuário fornece e busca por ano que o salário foi pago.

## Classes Controle

Nelas que temos os métodos GET, POST, DELETE, PUT etc onde podemos utilizar os métodos dos repositórios criados.

- JogadorController:
Para o jogador temos as opções de listagem de jogadores geral, por filtragem de nome ou email(repositório) ou por cod, inserção de jogadores, atualização de jogadores já existentes pelo cod e remoção de jogadores geral ou por cod.

- PagamentoController:
Para os pagamentos temos as opções de listagem de pagamento geral, por filtragem de ano ou valor > (repositório) ou por cod, inserção de pagamentos com o cod do jogador, atualização de pagamentos por cod e remoção de pagamentos geral ou por cod.

## Testes

Para os testes foi usado a IDE pgAdmin para verificarmos o que está acontecendo no banco. Ao rodarmos a aplicação no Maven, as tabelas já foram criadas automaticamente no banco pagamentosfut criado, com suas colunas e chaves primárias e secundárias já definidas por causa das notações @OneToMany, @ManyToOne e @JoinColumn. Para inserirmos dados nesse banco utilizamos a Talend API Tester para todos os métodos das classes controle.