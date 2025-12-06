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

import ca2.greenhouse.model.EmissionRecord;

@ApplicationScoped   // like GreetingService in the Quarkus injection example
public class EmissionXmlParser {

    public List<EmissionRecord> parseXmlEmissions() {

        List<EmissionRecord> results = new ArrayList<>();

        try {
            // Load XML from src/main/resources (same idea as ReadUsers + labs)
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

            // Each emission is stored in a <Row> element
            NodeList nodes = doc.getElementsByTagName("Row");

            for (int i = 0; i < nodes.getLength(); i++) {

                Node node = nodes.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) node;

                    // Match the REAL tag names in the XML:
                    //  <Category__1_3>, <Year>, <Scenario>, <Gas___Units>, <Value>
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

                    // CA rules:
                    // - Year = 2023
                    // - Scenario = WEM (With Existing Measures)
                    // - Value > 0
                    if (year != 2023) continue;
                    if (!"WEM".equalsIgnoreCase(scenario.trim())) continue;
                    if (value <= 0) continue;

                    EmissionRecord rec = new EmissionRecord();
                    rec.setYear(year);
                    rec.setScenario(scenario);
                    rec.setCategoryCode(code);           // Category__1_3 text
                    rec.setCategoryDescription(description); // Gas___Units for now
                    rec.setValue(value);
                    rec.setApproved(false);
                    rec.setApprovedByUsername(null);
                    rec.setSourceType("XML");

                    results.add(rec);
                }
            }

            System.out.println("Parsed XML emissions count: " + results.size());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    // Helper like in ReadUsers
    private String getTagText(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list == null || list.getLength() == 0) return null;
        Node node = list.item(0);
        return node.getTextContent();
    }
}