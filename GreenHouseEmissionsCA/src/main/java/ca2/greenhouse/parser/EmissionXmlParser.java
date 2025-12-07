package ca2.greenhouse.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ca2.greenhouse.model.Emission;

@ApplicationScoped 
public class EmissionXmlParser {

    // parse the XML file and build Emission entities directly
    public List<Emission> parseXmlEmissions() {

        List<Emission> results = new ArrayList<>();

        try {
            // load the XML file from resources
            InputStream in = getClass().getClassLoader()
                    .getResourceAsStream("GreenhouseEmissions2025.xml");

            if (in == null) {
                System.out.println("XML file not found on classpath");
                return results;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(in);
            doc.getDocumentElement().normalize();

            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

            // each row is inside a <Row> element
            NodeList nodes = doc.getElementsByTagName("Row");

            for (int i = 0; i < nodes.getLength(); i++) {

                Node node = nodes.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) node;

                    // <Category__1_3>, <Year>, <Scenario>, <Gas___Units>, <Value>
                    String yearText      = getTagText(elem, "Year");
                    String scenario      = getTagText(elem, "Scenario");
                    String code          = getTagText(elem, "Category__1_3");
                    String description   = getTagText(elem, "Gas___Units");
                    String valueText     = getTagText(elem, "Value");

                    if (yearText == null || scenario == null || valueText == null) {
                        continue;
                    }

                    int year = Integer.parseInt(yearText.trim());
                    double value = Double.parseDouble(valueText.trim());

                    // keep only 2023 + WEM + value > 0
                    if (year != 2023) continue;
                    if (!"WEM".equalsIgnoreCase(scenario.trim())) continue;
                    if (value <= 0) continue;

                    // build Emission entity directly
                    Emission e = new Emission();
                    e.setYear(year);
                    e.setScenario(scenario);
                    e.setCategoryCode(code);
                    e.setCategoryDescription(description);
                    e.setValue(value);
                    e.setApproved(false);
                    e.setApprovedBy(null);

                    results.add(e);
                }
            }

            System.out.println("Parsed XML emissions count: " + results.size());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    // helper method to read tag text
    private String getTagText(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list == null || list.getLength() == 0) return null;
        Node node = list.item(0);
        return node.getTextContent();
    }
}