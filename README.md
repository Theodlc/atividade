# HonkaiFX CRUD — MVC, FXML e SOLID

Aplicação JavaFX de cadastro de personagens/itens de Honkai. Os registros são
persistidos no PostgreSQL em `localhost:5432/bd_honkai`, utilizando usuário
`postgres` e senha `postgres`.

A interface usa um tema astral responsivo, com cartões translúcidos, tabela de
arquivo, estados de foco/seleção e fundo original em `astral-archive-bg.png`.
Em telas menores, a área passa a oferecer rolagem para manter todas as ações
acessíveis.

## Executar

### Pelo atalho, sem terminal

Dê duplo clique em `Abrir Honkai Data Bank.lnk`. O diretório `app` contém o
executável, JavaFX, driver PostgreSQL e runtime Java necessários. O arquivo
`Abrir Honkai Data Bank.vbs` é uma alternativa portátil caso a pasta inteira do
projeto seja movida para outro local.

O PostgreSQL precisa estar iniciado e o banco `bd_honkai` precisa existir. A
tabela `honkai` é criada automaticamente pela aplicação no primeiro acesso.

### Pelo Maven

Pré-requisitos: JDK 17 e Maven 3.9 ou superiores.

```powershell
mvn clean javafx:run
```

Também é possível abrir `teste.iml` no IntelliJ, configurar um JDK 17 e as
bibliotecas JavaFX, e executar `com.template.main.Main`.

## Onde cada requisito foi aplicado

- `Validador<T>`: contrato genérico com `validar`, `getMensagemErro` e `getValor`.
- `CamposObrigatoriosValidador`: verifica valores nulos, vazios ou só com espaços.
- `TamanhoMinimoValidador`: segunda regra, usada em Nome e Efeito.
- `HonkaiValidador`: cria `List<Validador<String>>` e executa cada regra com `foreach`.
- `IHonkaiValidador`: abstração recebida e usada pelo `MainController` tanto ao
  cadastrar quanto ao atualizar.
- `ControllerFactory`: recebe as dependências e cria `MainController` por injeção
  de construtor.
- `Main`: ponto de composição onde `HonkaiValidador`, DAO, Service e fábrica são
  instanciados e conectados.
- `Database`: configura e abre a conexão com o PostgreSQL.
- `HonkaiDAO`: executa `INSERT`, `SELECT`, `UPDATE` e `DELETE` com
  `PreparedStatement` e cria a tabela automaticamente.

## SOLID para explicar na avaliação

- **SRP:** Controller cuida da tela, validadores cuidam das regras, Service dos
  casos de uso e DAO dos dados.
- **OCP:** uma nova validação é criada implementando `Validador<T>` e adicionada à
  composição, sem alterar as classes de validação existentes.
- **LSP:** qualquer implementação de `IHonkaiValidador`, `IHonkaiService` ou
  `IHonkaiDAO` pode substituir a atual mantendo o contrato.
- **ISP:** as interfaces são pequenas e específicas para uma responsabilidade.
- **DIP:** Controller e Service dependem de interfaces; o `Main` fornece as
  implementações por meio da fábrica.

## Demonstração sugerida

1. Tente salvar com campos vazios e mostre a mensagem de obrigatoriedade.
2. Informe um Nome com menos de 3 caracteres ou Efeito com menos de 10.
3. Cadastre um item válido e observe a linha na tabela.
4. Selecione a linha, altere um valor e clique em Atualizar.
5. Selecione a linha e clique em Excluir.
