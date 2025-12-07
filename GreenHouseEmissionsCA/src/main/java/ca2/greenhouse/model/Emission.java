package ca2.greenhouse.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity // this marks the class as a table managed by Hibernate
public class Emission {

    @Id
    @GeneratedValue // auto-increments the id for each new row
    private int id;

    // stores the category code from JSON/XML (e.g. "1.A.1", etc)
    private String categoryCode;

    // stores the description for the category (loaded from data or API)
    private String categoryDescription;

    // numeric emission value
    private double value;

    // year of the emission (always 2023 for this CA)
    private int year;

    // "WEM", "Actual 2023", etc
    private String scenario;

    // approval flag so user can mark records as final
    private boolean approved;

    // username of the user who approved the record
    private String approvedBy;

    public Emission() {
        // empty constructor needed by Hibernate
    }

    public int getId() {
        return id;
    }

    public String getCategoryCode() {
        return categoryCode;
    }
    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }
    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public String getScenario() {
        return scenario;
    }
    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public boolean isApproved() {
        return approved;
    }
    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getApprovedBy() {
        return approvedBy;
    }
    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }
}