package ca2.greenhouse.parser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import ca2.greenhouse.dao.CategoryDAO;
import ca2.greenhouse.model.Category;
import ca2.greenhouse.model.Emission;

/**
 * Parser for the JSON file with actual 2023 readings.
 * This converts the JSON rows into Emission entities.
 */
@ApplicationScoped
public class EmissionJsonParser {

    @Inject
    CategoryDAO categoryDAO;   // used to link emissions to Category rows

    public List<Emission> parseJsonEmissions() {

        List<Emission> results = new ArrayList<>();

        try {
            JSONParser parser = new JSONParser();

            // Load JSON file from src/main/resources
            InputStream is = getClass().getClassLoader()
                    .getResourceAsStream("GreenhouseGasEmissions2025.json");
            if (is == null) {
                System.out.println("GreenhouseGasEmissions2025.json not found on classpath");
                return results;
            }

            // Parse top-level object
            JSONObject root = (JSONObject) parser.parse(new InputStreamReader(is));
            JSONArray emissions = (JSONArray) root.get("Emissions");

            // Loop through each row in the JSON array
            for (Object o : emissions) {
                JSONObject row = (JSONObject) o;

                // Only keep entries with a valid numeric value > 0
                Number valNumber = (Number) row.get("Value");
                if (valNumber == null) {
                    continue;
                }
                double value = valNumber.doubleValue();
                if (value <= 0) {
                    continue;
                }

                // Extract the fields we care about
                String categoryCode = (String) row.get("Category");
                String gasUnits = (String) row.get("Gas Units");

                // Build a new Emission entity
                Emission e = new Emission();
                e.setYear(2023);                  // all JSON data is for 2023
                e.setScenario("Actual 2023");
                e.setCategoryCode(categoryCode);
                e.setCategoryDescription(gasUnits); 
                e.setValue(value);
                e.setApproved(false);
                e.setApprovedBy(null);

                // Create/lookup the Category in the DB and attach it
                if (categoryCode != null) {
                    Category cat = categoryDAO.findOrCreateByCode(categoryCode, gasUnits);
                    e.setCategory(cat);
                }

                results.add(e);
            }

            System.out.println("Parsed JSON emissions count: " + results.size());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }
}