package io.github.lvrodrigues.wizard.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
* Controller Java FX para apresentar uma página do Wizard.
*
* @author $Author$
* @author $Committer$
* @branch $Branch$
*/
public abstract class AbstractPage {

    /**
     * Mapa de informações compartilhadas entre as páginas.
     */
    private static final Map<String, String> DATAS = new HashMap<>();

    /**
     * Título das caixas de alertas de Erro.
     */
    private static final String ERROR_TITLE = "Erro";
 
    /**
     * Recupera o título da página para apresentar.
     *
     * @return Título da Página.
     */
    public abstract String getTitle();

    /**
     * Controle de estado de navegação.
     *
     * @return
     *     - true O Wizard pode navegar para a página anterior.
     *     _ false O Wizard não pode navegar para a página anterior.
     */
    public boolean canDoPrevious() {
        return true;
    }

    /**
     * Controle de estado de navagação.
     *
     * @return 
     *     _ true O Wizard pode navegar para a próxima página.
     *     _ false O Wizard não pode navegar para a próxima página.
     */
    public boolean canDoNext() {
        return true;
    }

    /**
     * Evento disparado sempre antes de ocultar uma página durante a navegação
     * do Wizard.
     *
     * <p>Esse evento salva as informações coletadas no Wizard em um arquivo
     * XML.
     */
    public void onHide() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            
            Element root = document.createElement("config");
            document.appendChild(root);

            DATAS.forEach((k, v) -> {
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
        } catch (IOException | TransformerException | ParserConfigurationException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(ERROR_TITLE);
            alert.setHeaderText("Falha ao persistir a configuração.");
            alert.setContentText(e.getLocalizedMessage());
            alert.showAndWait();        
        }
    }

    /**
     * Evento disparado sempre antes de apresentar a página durante
     * a navegação do Wizard.
     * 
     * <p>Esse evento carrega as informações salvas pelo Wizard em um arquivo
     * XML.
     */
    public void onShow() {
        DATAS.clear();
        try {
            File file = getFile();

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
                        DATAS.put(key, value);
                    }
                }
            }
        } catch (IOException | SAXException | ParserConfigurationException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(ERROR_TITLE);
            alert.setHeaderText("Falha ao recuperar a configuração.");
            alert.setContentText(e.getLocalizedMessage());
            alert.showAndWait();            
        }
    }

    /**
     * Recupera o conjunto de informações compartilhadas entre as páginas.
     *
     * @return Conjunto de informações no padrão chave e valor.
     */
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public Map<String, String> datas() {
        return DATAS;
    }

    /**
     * Recupera o arquivo de persistência de informações.
     *
     * @return Arquivo XML de persistência de informações.
     * @throws FileNotFoundException Diretório de persistência de dados não encontrado.
     */
    private File getFile() throws FileNotFoundException {
        String home = System.getProperty("user.home");
        Path path   = Path.of(home, ".spiderbot");
        if (!path.toFile().exists() && !path.toFile().mkdirs()) {
            throw new FileNotFoundException("Erro ao criar o diretório para persistência de dados.");
        }
        return new File(path.toFile(), "javafx-wizard.xml");
    }
}
