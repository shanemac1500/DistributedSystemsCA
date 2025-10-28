package com.fitnesscentre.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "membershipPlan")
@Entity
public class MembershipPlan {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String description;
	private double totalCost;
	
	public MembershipPlan() {}
	
	@XmlElement
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	
	@XmlElement
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	
	public double getTotalCost() { return totalCost; }
	public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
	
	}


