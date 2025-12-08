package ca2.greenhouse.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Simple category entity to represent an emission category.
 * Each category has a code and a description, and many Emissions can point to one Category.
 */
@Entity
public class Category {

    @Id
    @GeneratedValue
    private int id;

    // e.g. "1.A.1", "2.B.3", etc.
    private String code;

    // Text description from the IPCC file (categories.txt)
    @Column(length = 2000)
    private String description;

    public Category() {
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}