package ca2.greenhouse.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import ca2.greenhouse.model.Category;

/**
 * DAO for working with Category entities.
 * Includes helpers so the parsers and loaders can reuse or create categories.
 */
@ApplicationScoped
public class CategoryDAO {

    @PersistenceContext
    EntityManager em;

    // Look up a category by its code. Returns null if it doesn't exist.
    public Category findByCode(String code) {
        List<Category> list = em.createQuery(
                "SELECT c FROM Category c WHERE c.code = :code",
                Category.class)
                .setParameter("code", code)
                .getResultList();

        return list.isEmpty() ? null : list.get(0);
    }

    // Used by the parsers: find a category by code, or create it if missing.
    @Transactional
    public Category findOrCreateByCode(String code, String description) {

        Category existing = findByCode(code);
        if (existing != null) {
            // If a category already exists but has no description yet, update it.
            if (description != null &&
                (existing.getDescription() == null || existing.getDescription().isEmpty())) {
                existing.setDescription(description);
            }
            return existing;
        }

        // Create a new category
        Category c = new Category();
        c.setCode(code);
        c.setDescription(description);

        em.persist(c);
        return c;
    }

    // Loads category codes + descriptions from categories.txt into the Category table
    @Transactional
    public int loadFromFile() {
        int count = 0;
        System.out.println("DEBUG: loadFromFile() called");

        try {
            // File must be inside src/main/resources
            InputStream in = getClass().getClassLoader()
                    .getResourceAsStream("categories.txt");

            if (in == null) {
                System.out.println("DEBUG: categories.txt not found in resources");
                return 0;
            }

            System.out.println("DEBUG: categories.txt found, starting read...");

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            boolean first = true;  // skip header row

            while ((line = reader.readLine()) != null) {

                if (first) {  
                    first = false; 
                    continue;  
                }

                // Expecting code + description separated by a tab
                String[] parts = line.split("\t");
                if (parts.length < 2) {
                    continue;
                }

                String code = parts[0].trim();
                String description = parts[1].trim();

                if (code.isEmpty()) continue;

                Category existing = findByCode(code);
                if (existing != null) {
                    // Overwrite the existing description with the cleaned EFDB description
                    existing.setDescription(description);
                } else {
                    // Create a new category
                    Category c = new Category();
                    c.setCode(code);
                    c.setDescription(description);
                    em.persist(c);
                }

                count++;
            }

            reader.close();
            System.out.println("DEBUG: loaded " + count + " categories from file");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    // Returns how many categories are stored in the database
    public long countAll() {
        return em.createQuery("SELECT COUNT(c) FROM Category c", Long.class)
                 .getSingleResult();
    }
}