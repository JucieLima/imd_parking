Disciplina: LINGUAGEM DE PROGRAMAÇÃO II

Projeto IMD Parking

**Sobre o projeto**

O sistema de gerenciamento de estacionamentos é uma solução projetada para otimizar e facilitar as operações em um estabelecimento desse tipo. Com um design intuitivo e fluente, nosso objetivo é garantir que as tarefas relacionadas ao estacionamento sejam executadas de maneira eficiente.

Para alcançar esse objetivo, utilizamos as tecnologias JavaFX e MySQL. O JavaFX é uma biblioteca que nos permite criar interfaces gráficas interativas e atraentes, proporcionando uma experiência amigável para os usuários do sistema. O MySQL é um sistema de gerenciamento de banco de dados confiável e amplamente utilizado, que nos permite armazenar e manipular os dados relacionados aos veículos e clientes do estacionamento.

A conexão do sistema com o banco de dados é essencial para o funcionamento adequado das operações. Por meio dessa conexão, podemos cadastrar novos veículos, verificar quais veículos estão atualmente estacionados no pátio, cadastrar clientes e realizar outras operações relevantes para a gestão do estacionamento.

Ao cadastrar um veículo, o sistema armazena informações importantes, como modelo, placa e data de entrada. Esses dados são essenciais para rastrear e controlar o fluxo de veículos no estacionamento. Além disso, é possível verificar os veículos estacionados no pátio, o que permite uma visualização rápida e fácil da ocupação atual.

O sistema também oferece a funcionalidade de cadastrar clientes, o que possibilita a criação de perfis individuais e o armazenamento de informações pessoais relevantes. Isso pode incluir nome, telefone, e-mail e outras informações que facilitem a comunicação e a gestão de cada cliente.

Além das operações mencionadas, o sistema de gerenciamento de estacionamentos pode oferecer recursos adicionais, como a geração de relatórios de ocupação, histórico de entradas e saídas, controle de pagamentos e outras funcionalidades personalizadas, de acordo com as necessidades do estabelecimento.

Em resumo, o sistema de gerenciamento de estacionamentos que desenvolvemos é uma solução abrangente, projetada para oferecer máxima fluência e eficiência nas operações. Utilizando as tecnologias JavaFX e MySQL, proporcionamos uma interface intuitiva e uma conexão confiável com o banco de dados. Com a capacidade de cadastrar veículos, verificar os veículos estacionados, cadastrar clientes e realizar outras operações relevantes, nosso sistema é uma ferramenta valiosa para melhorar a gestão de estacionamentos.

**JavaFX**

O JavaFX é uma biblioteca de software desenvolvida pela Oracle para a criação de aplicativos de interface gráfica do usuário (GUI) em Java. Ele fornece um conjunto de ferramentas e componentes gráficos que permitem a criação de interfaces de usuário interativas e visualmente atraentes.

Em resumo, o JavaFX é uma biblioteca poderosa para o desenvolvimento de interfaces gráficas em Java. Com ele, os desenvolvedores podem criar aplicativos desktop e móveis modernos e atraentes, aproveitando a ampla gama de recursos e ferramentas disponíveis.

**MySQL**

O MySQL é um sistema de gerenciamento de banco de dados relacional (RDBMS) muito popular e amplamente utilizado. Ele é conhecido por sua facilidade de uso, desempenho confiável, escalabilidade e compatibilidade com várias plataformas. Ele suporta a linguagem de consulta estruturada (SQL) e oferece uma ampla gama de recursos.

**Conceitos Aplicados**

1. Classes e Objetos;

O sistema está sendo projetado usando a IDE IntelliJ IDEA 2022.1 (Edu), usando os conceitos de orientação a objetos.


1. Herança;

O conceito herança foi aplicado em diversas classes, veja um exemplo abaixo:


1. Classes Abstratas;

O conceito de classes abstratas foi aplicado na classe Person, esta classe não pode ser estanciada por nenhum objeto. Ela serve para guardar atributos que são comuns a outras classes que irão herdar esses atributos. Com isso, se houver alguma mudança em comum às classes que herdam de Person, essa mudança será efetuada apenas em uma classe.

4. Interfaces;

Nesse projeto estamos aplicando o padrão DAO(Data Access Object). Com isso, se faz necessário a aplicação de interfaces para a aplicação correta deste padrão. Veja abaixo uma interface presente no programa que aplica esse padrão.


5. Composição;

Um exemplo de composição usado nesse projeto pode ser verificado na relação entre as classes Client e Account. O cliente tem uma conta e esta não existe sem um cliente, portanto deve ser aplicado o conceito de composição para estabelecer essa relação. Veja mais detalhes no diagrama de classes.

6. Polimorfismo;

Usamos em um ponto do projeto o polimorfismo de sobrescrita, no qual iremos realizar uma busca por um veículo. Neste caso a busca pode ser feita de duas maneiras. A primeira é passando a **placa** e o **tipo** do veículo e a segunda o **objeto**. Em ambos os casos verificamos se o mesmo está cadastrado no sistema, se não estiver, realizamos imediatamente o cadastro.


7. Tratamento de Exceção;

O tratamento de exceção pode ser verificado em vários pontos do projeto, inclusive na classe acima mostrada.

8. Coleções;

Assim como o tratamento de exceção o uso de coleções também foi amplamente utilizado.

**Diagrama de Classes - Entidades**


**Diagrama de Classes - Controllers**
