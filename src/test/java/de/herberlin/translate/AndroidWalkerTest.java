package de.herberlin.translate;

import de.herberlin.translate.impl.AndroidWalker;
import de.herberlin.translate.impl.DummyTranslator;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static de.herberlin.translate.impl.AndroidWalker.ATTR_UPDATED;
import static de.herberlin.translate.impl.AndroidWalker.TRANSLATABLE;

public class AndroidWalkerTest {
    Translator translator = new DummyTranslator();
    File source = new File("target/data/android/values/strings.xml");
    Log log = new Log.MavenLogger(new SystemStreamLog());

    @Before
    public void setUp() throws IOException {
        File target = new File("target/data");
        File source = new File("data");
        FileUtils.deleteDirectory(target);
        FileUtils.copyDirectory(source, target);
    }

    @Test
    public void translateDe() throws MojoExecutionException, ParserConfigurationException, IOException, SAXException {
        List<String> existingItems = Arrays.asList("warning", "service_unavailable", "kph");
        File sourceFile = new File("target/data/android/values/strings.xml");
        File deFile = new File("target/data/android/values-de/strings.xml");
        AndroidWalker testObj = new AndroidWalker();
        testObj.init(translator, source, log);
        testObj.translate("de");
        Assert.assertTrue(deFile.exists());

        Document sourceDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(sourceFile);
        Document deDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(deFile);
        Map<String, Node> sourceMap = createMap(sourceDoc);
        Map<String, Node> deMap = createMap(deDoc);
        for (String key : sourceMap.keySet()) {
            Node node = sourceMap.get(key);
            if (node.getAttributes().getNamedItem(TRANSLATABLE) == null) {
                boolean updated = node.getAttributes().getNamedItem(ATTR_UPDATED) != null;
                Assert.assertTrue("Key not found: " + key, deMap.containsKey(key));
                if (existingItems.contains(key) && ! updated) {
                    Assert.assertEquals("Already existing:", "existing", deMap.get(key).getTextContent());
                } else {
                    Assert.assertEquals("No translation:", sourceMap.get(key).getTextContent() + "-de", deMap.get(key).getTextContent());
                }
            } else {
                Assert.assertFalse("Must not be here: " + key, deMap.containsKey(key));
            }
        }
    }

    @Test
    public void translateRu() throws MojoExecutionException, ParserConfigurationException, IOException, SAXException {
        File sourceFile = new File("target/data/android/values/strings.xml");
        File ruFile = new File("target/data/android/values-ru/strings.xml");
        AndroidWalker testObj = new AndroidWalker();
        testObj.init(translator, source, log);
        testObj.translate("ru");
        Assert.assertTrue(ruFile.exists());

        Document sourceDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(sourceFile);
        Document ruDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(ruFile);
        Map<String, Node> sourceMap = createMap(sourceDoc);
        Map<String, Node> ruMap = createMap(ruDoc);
        for (String key : sourceMap.keySet()) {
            Node node = sourceMap.get(key);
            if (node.getAttributes().getNamedItem(TRANSLATABLE) == null) {
                Assert.assertTrue("Key not found: " + key, ruMap.containsKey(key));
                Assert.assertEquals("No translation:", sourceMap.get(key).getTextContent() + "-ru", ruMap.get(key).getTextContent());
            } else {
                Assert.assertFalse("Must not be here: " + key, ruMap.containsKey(key));
            }
        }
    }


    private Map<String, Node> createMap(Document target) {
        Map<String, Node> result = new HashMap<>();
        NodeList list = target.getElementsByTagName("string");
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            String nodeName = node.getAttributes().getNamedItem("name").getNodeValue();
            result.put(nodeName, node);
        }
        return result;
    }


    @Test
    public void testGetLocaleName() {
        String[] tags = new String[]{"ru","ceb","zh", "zh-rCN"};
        for ( String s : tags) {
            Locale locale = Locale.forLanguageTag(s);
            System.out.println(locale);
            System.out.println(locale.getDisplayLanguage(Locale.ENGLISH));

        }
    }
}