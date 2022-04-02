package io.github.lvrodrigues.wizard.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
* Controller Java FX para apresentar uma página do Wizard.
*
* @author $Author$
* @author $Committer$
* $Branch$
*/
public abstract class Page {

    /**
     * Mapa de informações compartilhadas entre as páginas.
     */
    private static final Map<String, String> datas = new HashMap<>();
 
    /**
     * Recupera o título da página para apresentar.
     * @return Título da Página.
     */
    public abstract String getTitle();

    /**
     * Controle de estado de navegação.
     * @return true O Wizard pode navegar para a página anterior.
     * @return false O Wizard não pode navegar para a página anterior.
     */
    public boolean canDoPrevious() {
        return true;
    }

    /**
     * Controle de estado de navagação.
     * @return true O Wizard pode navegar para a próxima página.
     * @return false O Wizard não pode navegar para a próxima página.
     */
    public boolean canDoNext() {
        return true;
    }

    /**
     * Evento disparado sempre antes de ocultar uma página durante a navegação
     * do Wizard.
     * <p>
     * Esse evento salva as informações coletadas no Wizard em um arquivo
     * XML.
     */
    public void onHide() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            
            Element root = document.createElement("config");
            document.appendChild(root);

            datas.forEach((k, v) -> {
                Element item = document.createElement(k);
                item.setTextContent(v);
                root.appendChild(item);
            });

            try (FileOutputStream stream = new FileOutputStream(getFile())) {
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(stream);
                transformer.transform(source, result);
            }
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Falha ao persistir a configuração.");
            alert.setContentText(e.getLocalizedMessage());
            alert.showAndWait();        
        }
    }

    /**
     * Evento disparado sempre antes de apresentar a página durante
     * a navegação do Wizard.
     * <p>
     * Esse evento carrega as informações salvas pelo Wizard em um arquivo
     * XML.
     */
    public void onShow() {
        File file = getFile();
        if (!file.exists()) {
            return;
        }
        datas.clear();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            try (InputStream stream = new FileInputStream(file)) {
                Document document = builder.parse(stream);
                Element root = document.getDocumentElement();
                for (int i = 0; i < root.getChildNodes().getLength(); i++) {
                    Node node = root.getChildNodes().item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String key   = element.getNodeName();
                        String value = element.getTextContent();
                        datas.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Falha ao recuperar a configuração.");
            alert.setContentText(e.getLocalizedMessage());
            alert.showAndWait();            
        }
    }

    /**
     * Recupera o conjunto de informações compartilhadas entre as páginas.
     * @return Conjunto de informações no padrão chave e valor.
     */
    public Map<String, String> datas() {
        return datas;
    }

    /**
     * Recupera o arquivo de persistência de informações.
     * @return Arquivo XML de persistência de informações.
     */
    private File getFile() {
        String home = System.getProperty("user.home");
        Path path   = Path.of(home, ".spiderbot");
        if (!path.toFile().exists()) {
            path.toFile().mkdirs();
        }
        return new File(path.toFile(), "javafx-wizard.xml");
    }
}
