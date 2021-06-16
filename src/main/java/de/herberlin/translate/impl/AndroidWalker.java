package de.herberlin.translate.impl;

import de.herberlin.translate.FileWalker;
import de.herberlin.translate.Translator;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AndroidWalker implements FileWalker {

    private Translator translator;
    private File source;
    private File baseDirectory;
    private Log log;

    private static final String DIRNAME = "values-";
    private static final String STRING_TAG = "string";

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
            String languageDisplayName = getLanguageDisplayName(language);
            log.info("Translating to: " + language + ", " + languageDisplayName);
            File targetDir = new File(baseDirectory, DIRNAME + language);
            if (!targetDir.exists()) {
                targetDir.mkdir();
            }
            File targetFile = new File(targetDir, source.getName());
            boolean targetFileIsNew = false;
            if (!targetFile.exists()) {
                targetFileIsNew = targetFile.createNewFile();
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document sourceDoc = builder.parse(source);
            Document targetDoc;
            if (targetFileIsNew) {
                targetDoc = builder.newDocument();
                Element element = targetDoc.createElement("resources");
                targetDoc.appendChild(element);
                Comment comment = targetDoc.createComment("Language: " + languageDisplayName);
                element.appendChild(comment);
            } else {
                targetDoc = builder.parse(targetFile);
            }

            process(sourceDoc, targetDoc, language);

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

    private void process(Document source, Document target, String language) throws MojoExecutionException {
        Map<String, Node> targetNodeMap = createMap(target);
        NodeList sourceNodeList = source.getDocumentElement().getElementsByTagName(STRING_TAG);
        for (int i = 0; i < sourceNodeList.getLength(); i++) {
            Node sourceNode = sourceNodeList.item(i);
            String nodeName = sourceNode.getAttributes().getNamedItem("name").getNodeValue();
            Node translatableNode = sourceNode.getAttributes().getNamedItem("translatable");
            if (translatableNode != null && "false".equals(translatableNode.getNodeValue())) {
                log.debug("Node: " + nodeName + " not translatable");
                continue;
            }
            Node targetNode = targetNodeMap.get(nodeName);
            if (targetNode != null) {
                log.debug("Node: " + nodeName + " is already translated.");
                continue;
            }
            targetNode = target.createElement(STRING_TAG);
            setAllAttributes(sourceNode, targetNode);
            StringBuilder st = new StringBuilder();
            writeChildrenAsText(st, sourceNode);
            String translated = sanitize(translator.translate(st.toString(), language));
            targetNode.setTextContent(translated);
            log.debug("Translating: " + st + " -> " + translated);

            target.getDocumentElement().appendChild(targetNode);

        }

    }

    private String sanitize(String translated) {
        return translated.replaceAll("<[\\w:/]*>", "");
    }

    private void writeChildrenAsText(StringBuilder result, Node sourceNode) {
        NodeList list = sourceNode.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            switch (node.getNodeType()) {
                case Node.TEXT_NODE:
                    result.append(node.getTextContent());
                    break;
                case Node.ELEMENT_NODE:
                    result.append("<" + node.getNodeName() + ">");
                    writeChildrenAsText(result, node);
                    result.append("</" + node.getNodeName() + ">");
                    break;
            }

        }
    }

    private void setAllAttributes(Node sourceNode, Node targetNode) {
        NamedNodeMap sourceAttribs = sourceNode.getAttributes();
        NamedNodeMap targetAttribs = targetNode.getAttributes();
        for (int i = 0; i < sourceAttribs.getLength(); i++) {
            Node node = sourceAttribs.item(i);
            Attr attr = targetNode.getOwnerDocument().createAttribute(node.getNodeName());
            attr.setValue(node.getNodeValue());
            targetAttribs.setNamedItem(attr);
        }
    }

    private Map<String, Node> createMap(Document target) {
        Map<String, Node> result = new HashMap<>();
        NodeList list = target.getElementsByTagName(STRING_TAG);
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            String nodeName = node.getAttributes().getNamedItem("name").getNodeValue();
            result.put(nodeName, node);
        }
        return result;
    }

    private String getLanguageDisplayName(String languageCode) {
        String s = languageCode;
        Locale locale = Locale.forLanguageTag(languageCode);
        if (locale != null) {
            s = locale.getDisplayLanguage(Locale.ENGLISH);
            if (s == null || s.length() == 0) {
                s = languageCode;
            }
        }
        return s;

    }
}
