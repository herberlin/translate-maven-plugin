package de.herberlin.translate;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class AndroidWalker implements FileWalker {

    private Translator translator;
    private File source;
    private File baseDirectory;
    private Log log;

    public static final String DIRNAME="values-";

    @Override
    public void init(Translator translator, File source, Log log) throws MojoExecutionException {
        this.translator = translator;
        this.source = source;
        if (!source.exists()) {
            throw new MojoExecutionException("Source: " + source + " does not exist.");
        }
        this.baseDirectory = source.getParentFile().getParentFile();
        if (!baseDirectory.exists() || !baseDirectory.canWrite()) {
            throw new MojoExecutionException("Cant write: " + baseDirectory);
        }
        this.log = log;
    }

    @Override
    public void translate(String language) throws MojoExecutionException {
        try {
            File targetDir = new File(baseDirectory, DIRNAME + language);
            if (!targetDir.exists()) {
                targetDir.mkdir();
            }
            File targetFile = new File(targetDir, source.getName());
            boolean targetFileIsNew = false;
            if (!targetFile.exists()) {
                targetFileIsNew = targetFile.createNewFile();
            }
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document sourceDoc = builder.parse(source);
            Document targetDoc;
            if (targetFileIsNew) {
                targetDoc = builder.newDocument();
                Element element = targetDoc.createElement("resources");
                targetDoc.appendChild(element);
            } else {
                targetDoc =  builder.parse(targetFile);
            }

            process(sourceDoc, targetDoc);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(targetDoc);
            StreamResult result = new StreamResult(targetFile);
            transformer.transform(source, result);
        } catch (Exception e) {
            log.error("Error translating to " + language, e);
            throw new MojoExecutionException("Error translating to " + language, e);
        }
    }

    private void process(Document source, Document target) {
        NodeList nodeList = source.getDocumentElement().getElementsByTagName("string");
        for (int i = 0; i< nodeList.getLength() ; i++) {
            Node node = nodeList.item(i);
            String nodeName = node.getAttributes().getNamedItem("name").getNodeValue();
            Node translatableNode = node.getAttributes().getNamedItem("translatable");
            if (translatableNode != null && "false".equals(translatableNode.getNodeValue())) {
                log.debug("Node " + nodeName +" not translatable");
                continue;
            }
            log.debug("Processing: "+ node.getTextContent());
        }

    }
}
