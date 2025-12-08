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

    // Parse the XML projection file and convert each valid row into an Emission entity
    public List<Emission> parseXmlEmissions() {

        List<Emission> results = new ArrayList<>();

        try {
            // Load XML from src/main/resources
            InputStream in = getClass().getClassLoader()
                    .getResourceAsStream("GreenhouseEmissions2025.xml");

            if (in == null) {
                System.out.println("XML file not found on classpath");
                return results;
            }

            //DOM setup
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(in);
            doc.getDocumentElement().normalize();

            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

            // All emission rows are stored inside <Row> tags
            NodeList nodes = doc.getElementsByTagName("Row");

            for (int i = 0; i < nodes.getLength(); i++) {

                Node node = nodes.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) node;

                    // Extract the tags we need from this <Row>
                    String yearText      = getTagText(elem, "Year");
                    String scenario      = getTagText(elem, "Scenario");
                    String code          = getTagText(elem, "Category__1_3");
                    String description   = getTagText(elem, "Gas___Units");
                    String valueText     = getTagText(elem, "Value");

                    // Skip rows missing critical fields
                    if (yearText == null || scenario == null || valueText == null) {
                        continue;
                    }

                    int year = Integer.parseInt(yearText.trim());
                    double value = Double.parseDouble(valueText.trim());

                    // Apply the CA rules: year must be 2023, scenario must be WEM, value > 0
                    if (year != 2023) continue;
                    if (!"WEM".equalsIgnoreCase(scenario.trim())) continue;
                    if (value <= 0) continue;

                    // Build a new Emission entity from this XML row
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

    // Helper method: returns the text inside a tag, or null if the tag is missing
    private String getTagText(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list == null || list.getLength() == 0) return null;
        Node node = list.item(0);
        return node.getTextContent();
    }
}