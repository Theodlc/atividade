# Resumo do Projeto HonkaiFX CRUD

## 1. Visão geral

O projeto é uma aplicação desktop JavaFX para cadastrar personagens ou itens do
universo de Honkai. A solução utiliza MVC, FXML, interfaces, Generics,
validação, injeção de dependência, fábrica de Controllers, Service, DAO e banco
de dados PostgreSQL.

O sistema permite:

- cadastrar registros;
- listar os registros na tabela;
- selecionar um registro;
- atualizar um registro existente;
- excluir um registro;
- validar os dados antes do cadastro e da atualização;
- apresentar mensagens de sucesso ou erro ao usuário;
- armazenar os dados no PostgreSQL;
- abrir a interface por um atalho, sem utilizar o terminal.

## 2. O que já existia no projeto original

O projeto original já possuía uma estrutura inicial organizada em pacotes:

- `controller`;
- `main`;
- `model.dao`;
- `model.dto`;
- `service`;
- `util`;
- `validator`;
- arquivos de interface em FXML e CSS.

Também já existiam:

- `MainController` com eventos dos botões;
- `Main` para abrir a aplicação JavaFX;
- `HonkaiDTO` com os campos `id`, `nome`, `elemento`, `raridade` e `efeito`;
- interfaces `IHonkaiDAO` e `IHonkaiService`;
- classes `HonkaiDAO` e `HonkaiService`;
- interface genérica `Validador<T>`;
- interface `IHonkaiValidador`;
- classe `HonkaiValidador`;
- uma validação inicial de campo obrigatório;
- `DialogUtil` para exibir caixas de diálogo;
- `main.fxml`, `style.css` e `icon.png`;
- configuração de conexão indicando PostgreSQL em
  `localhost:5432/bd_honkai`.

Entretanto, a persistência original ainda não estava pronta. Os métodos do DAO
possuíam comentários `TODO` e lançavam `UnsupportedOperationException`. A
atualização, exclusão e listagem ainda não executavam SQL real.

## 3. Validação e requisitos da atividade

### Interface genérica `Validador<T>`

A interface foi mantida e documentada com os três métodos obrigatórios:

```java
boolean validar(T valorAtual);
String getMensagemErro();
T getValor();
```

O tipo genérico `T` permite criar validadores para `String`, `Integer`, datas ou
qualquer outro tipo sem precisar duplicar a interface.

### `CamposObrigatoriosValidador`

Foi criada a classe com o nome exigido pela atividade. Ela implementa
`Validador<String>` e verifica se o valor:

- não é `null`;
- não está vazio;
- não contém apenas espaços.

### `TamanhoMinimoValidador`

Foi criada uma segunda implementação de `Validador<String>`. Ela verifica se o
texto possui a quantidade mínima de caracteres definida no construtor.

Atualmente as regras utilizadas são:

- Nome obrigatório;
- Elemento obrigatório;
- Raridade obrigatória;
- Efeito obrigatório;
- Nome com pelo menos 3 caracteres;
- Efeito com pelo menos 10 caracteres.

### `HonkaiValidador`

`HonkaiValidador` implementa `IHonkaiValidador` e reúne as regras do cadastro em
uma lista genérica:

```java
List<Validador<String>> validadores = new ArrayList<>();
```

Os validadores são executados obrigatoriamente por um `foreach`. Durante o
percurso, a classe utiliza somente os métodos da interface:

```java
for (Validador<String> validador : validadores) {
    if (!validador.validar(validador.getValor())) {
        throw new Exception(validador.getMensagemErro());
    }
}
```

Isso permite incluir novas regras de validação sem modificar os validadores que
já funcionam.

## 4. Controller e injeção de dependência

O `MainController` recebe suas dependências pelo construtor:

- `IHonkaiService`;
- `IHonkaiValidador`.

O Controller não instancia diretamente `HonkaiService` nem `HonkaiValidador`.
A validação é feita pela interface `IHonkaiValidador` tanto no método de
cadastro quanto no método de atualização.

Depois de cada operação bem-sucedida, a tabela é recarregada para mostrar os
dados atuais do banco.

## 5. Fábrica de Controllers

Foi criada a classe `ControllerFactory`, que implementa:

```java
Callback<Class<?>, Object>
```

Ela recebe as dependências configuradas pelo `Main` e cria o `MainController`
com essas dependências. O `FXMLLoader` utiliza essa fábrica por meio de
`setControllerFactory`.

## 6. Configuração no `Main`

O `Main` funciona como ponto de composição da aplicação. Ele cria:

1. `HonkaiDAO` como implementação de `IHonkaiDAO`;
2. `HonkaiService` como implementação de `IHonkaiService`;
3. `HonkaiValidador` como implementação de `IHonkaiValidador`;
4. `ControllerFactory` com o Service e o Validador;
5. `FXMLLoader` configurado com a fábrica.

A janela é aberta maximizada, pode ser redimensionada e possui rolagem para
manter os controles acessíveis em telas menores.

## 7. Persistência PostgreSQL

A implementação temporária em memória foi substituída por persistência JDBC no
PostgreSQL.

Configuração utilizada em `Database.java`:

```text
Servidor: localhost
Porta: 5432
Banco: bd_honkai
Usuário: postgres
Senha: postgres
```

O `HonkaiDAO` executa SQL real utilizando `PreparedStatement`:

- `INSERT` para cadastrar;
- `SELECT` para listar;
- `UPDATE` para atualizar;
- `DELETE` para excluir.

A tabela é criada automaticamente quando a aplicação consegue acessar o banco:

```sql
CREATE TABLE IF NOT EXISTS honkai (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    elemento VARCHAR(30) NOT NULL,
    raridade VARCHAR(30) NOT NULL,
    efeito TEXT NOT NULL
);
```

O `INSERT` utiliza `RETURN_GENERATED_KEYS` para recuperar o ID criado pelo
PostgreSQL. Todas as conexões, comandos e resultados utilizam
`try-with-resources`, garantindo o fechamento automático dos recursos.

Quando o servidor não está disponível, a aplicação apresenta uma mensagem
explicando que é necessário iniciar o PostgreSQL e conferir a configuração.

Importante: o servidor PostgreSQL não estava ativo durante a última verificação,
pois a porta `5432` estava fechada. O código e o driver foram testados até a
tentativa de conexão, mas o funcionamento real do SQL depende do servidor estar
iniciado e do banco `bd_honkai` existir.

## 8. Nova interface gráfica

A interface foi redesenhada para ficar mais próxima da atmosfera visual de
Honkai: Star Rail, sem utilizar um logotipo oficial baixado.

Foram adicionados:

- fundo espacial original;
- trilhos orbitais dourados;
- nebulosas azuis e violetas;
- cartões translúcidos;
- detalhes em dourado e azul-ciano;
- cabeçalho no estilo “Astral Express / Data Bank”;
- melhor hierarquia de títulos e campos;
- estados visuais de foco, hover, clique e seleção;
- tabela redesenhada;
- mensagem visual para tabela vazia;
- indicação da conexão PostgreSQL no rodapé;
- suporte a redimensionamento e rolagem.

O arquivo de fundo criado para o projeto é:

```text
src/resources/com/template/astral-archive-bg.png
```

Ele foi gerado como uma arte espacial original, sem texto, logotipo, marca
d'água, personagens ou elementos oficiais do jogo.

## 9. Maven e dependências

Foi criado um `pom.xml` para tornar a compilação reproduzível. O projeto utiliza:

- Java 17;
- JavaFX Controls 17.0.12;
- JavaFX FXML 17.0.12;
- PostgreSQL JDBC 42.7.7;
- Maven Compiler Plugin;
- JavaFX Maven Plugin.

Com Java e Maven instalados, o projeto também pode ser executado com:

```powershell
mvn clean javafx:run
```

## 10. Executável e atalhos

Foi gerado um aplicativo Windows autossuficiente em:

```text
app/HonkaiDataBank/HonkaiDataBank.exe
```

O diretório `app` contém:

- executável gráfico;
- runtime Java próprio;
- bibliotecas JavaFX;
- driver PostgreSQL;
- classes e recursos do projeto.

Como o executável inclui o runtime, não é necessário ter Java ou Maven no
`PATH` para abri-lo.

Na raiz do projeto foram criados:

- `Abrir Honkai Data Bank.lnk`: atalho direto para o executável;
- `Abrir Honkai Data Bank.vbs`: launcher portátil que localiza o executável por
  caminho relativo, útil quando a pasta do projeto é movida.

O executável é gráfico e não abre uma janela de terminal.

## 11. Aplicação dos princípios SOLID

### SRP — Princípio da Responsabilidade Única

- Controller controla a tela;
- Validador executa as validações;
- Service coordena os casos de uso;
- DAO executa a persistência;
- Database abre conexões;
- Factory cria Controllers e injeta dependências.

### OCP — Princípio Aberto/Fechado

Novos validadores podem ser criados implementando `Validador<T>`. As classes de
validação existentes não precisam ser modificadas.

### LSP — Princípio da Substituição de Liskov

Implementações de `IHonkaiDAO`, `IHonkaiService` e `IHonkaiValidador` podem ser
substituídas por outras que respeitem os mesmos contratos.

### ISP — Princípio da Segregação de Interfaces

As interfaces são pequenas e específicas para suas responsabilidades.

### DIP — Princípio da Inversão de Dependência

Controller e Service dependem de interfaces. As implementações concretas são
criadas fora dessas classes e fornecidas pelo `Main` e pela fábrica.

## 12. Verificações realizadas

Durante o desenvolvimento foram realizadas as seguintes verificações:

- validação do XML do FXML;
- conferência dos IDs e eventos entre FXML e Controller;
- compilação completa com Maven;
- resultado final de compilação: `BUILD SUCCESS`;
- inicialização real da interface JavaFX;
- teste do cadastro, atualização e exclusão na implementação temporária em
  memória antes da migração para PostgreSQL;
- teste das mensagens de campo obrigatório e tamanho mínimo;
- teste do executável empacotado;
- teste do atalho `.lnk`;
- confirmação de que o driver PostgreSQL está dentro do aplicativo;
- confirmação de que o executável tenta acessar o servidor configurado.

## 13. Arquivos mais importantes

```text
src/java/com/template/main/Main.java
src/java/com/template/main/Launcher.java
src/java/com/template/controller/MainController.java
src/java/com/template/controller/ControllerFactory.java
src/java/com/template/model/dto/HonkaiDTO.java
src/java/com/template/model/dao/Database.java
src/java/com/template/model/dao/IHonkaiDAO.java
src/java/com/template/model/dao/HonkaiDAO.java
src/java/com/template/service/IHonkaiService.java
src/java/com/template/service/HonkaiService.java
src/java/com/template/validator/Validador.java
src/java/com/template/validator/IHonkaiValidador.java
src/java/com/template/validator/HonkaiValidador.java
src/java/com/template/validator/CamposObrigatoriosValidador.java
src/java/com/template/validator/TamanhoMinimoValidador.java
src/resources/com/template/main.fxml
src/resources/com/template/style.css
src/resources/com/template/icon.png
src/resources/com/template/astral-archive-bg.png
pom.xml
Abrir Honkai Data Bank.lnk
Abrir Honkai Data Bank.vbs
app/HonkaiDataBank/HonkaiDataBank.exe
```

## 14. Como demonstrar o sistema

1. Inicie o PostgreSQL.
2. Confirme que existe o banco `bd_honkai`.
3. Abra `Abrir Honkai Data Bank.lnk`.
4. Tente cadastrar com campos vazios para demonstrar a validação obrigatória.
5. Informe um nome muito curto ou efeito com menos de 10 caracteres.
6. Cadastre um registro válido.
7. Feche e abra o programa para demonstrar que o registro permaneceu no banco.
8. Selecione o registro na tabela e atualize algum campo.
9. Selecione o registro e realize a exclusão.
10. Durante a explicação, mostre que o Controller recebe interfaces e que as
    implementações são criadas no `Main` e fornecidas pela `ControllerFactory`.
