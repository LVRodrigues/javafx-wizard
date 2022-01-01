# javafx-wizard

Exemplo de aplicativo utilizando Java FX para criar um aplicativo Desktop no modelo de Wizard, que coletará informações do usuário e no final, executará uma operação.

Esse exemplo utiliza:

* [OpenJDK (Java)](https://openjdk.java.net/)
* [Maven](https://maven.apache.org/)
* [Java FX](https://openjfx.io/)
* [Scene Builder](https://gluonhq.com/products/scene-builder/)

Há um esforço para desenvolver as classes principais de gerenciamento do Wizard e das páginas de navegação.

## Páginas

As páginas devem ser arquivos de leioute Java FX (*.fxml), que possuam uma classe de controle que
que herde de **br.com.spiderbot.wizard.pages.Page**.

Em **br.com.spiderbot.wizard.Wizard**, o método **initialize** pode ser utilizado para carregar
as as páginas. A ordem de apresentação será definida pela ordem de registro delas.

## Navegação

