package com.fitnesscentre.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.util.List;

@XmlRootElement(name = "membershipPlan")
@Entity
public class MembershipPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;
    private double totalCost;

    // Reverse relationship: one plan can have many members
    @OneToMany(mappedBy = "plan", fetch = FetchType.LAZY)
    private List<Member> members;

    public MembershipPlan() {}

    @XmlElement
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @XmlElement
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @XmlElement
    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
    
    @XmlTransient
    public List<Member> getMembers() { return members; }
    public void setMembers(List<Member> members) { this.members = members; }
}