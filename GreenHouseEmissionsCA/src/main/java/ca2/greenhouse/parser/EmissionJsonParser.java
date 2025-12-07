package ca2.greenhouse.parser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import ca2.greenhouse.model.Emission;

@ApplicationScoped
public class EmissionJsonParser {

    // parse the JSON file and build Emission entities directly
    public List<Emission> parseJsonEmissions() {

        List<Emission> results = new ArrayList<>();

        try {
            JSONParser parser = new JSONParser();

            // load the JSON file from resources
            InputStream is = getClass().getClassLoader()
                    .getResourceAsStream("GreenhouseGasEmissions2025.json");
            if (is == null) {
                System.out.println("GreenhouseGasEmissions2025.json not found on classpath");
                return results;
            }

            JSONObject root = (JSONObject) parser.parse(new InputStreamReader(is));
            JSONArray emissions = (JSONArray) root.get("Emissions");

            // loop over each JSON row
            for (Object o : emissions) {
                JSONObject row = (JSONObject) o;

                // only keep values > 0
                Number valNumber = (Number) row.get("Value");
                if (valNumber == null) {
                    continue;
                }
                double value = valNumber.doubleValue();
                if (value <= 0) {
                    continue;
                }

                String category = (String) row.get("Category");
                String gasUnits = (String) row.get("Gas Units");

                // create Emission entity directly
                Emission e = new Emission();
                // JSON file is for 2023
                e.setYear(2023);
                e.setScenario("Actual 2023");
                e.setCategoryCode(category);
                // for now, still using gas units as a placeholder description
                e.setCategoryDescription(gasUnits);
                e.setValue(value);
                e.setApproved(false);
                e.setApprovedBy(null);

                results.add(e);
            }

            System.out.println("Parsed JSON emissions count: " + results.size());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }
}