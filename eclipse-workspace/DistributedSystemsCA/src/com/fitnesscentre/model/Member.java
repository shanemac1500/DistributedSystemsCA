package com.fitnesscentre.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "member")        // allows JSON & XML
@XmlAccessorType(XmlAccessType.FIELD)   // JAXB reads fields directly
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto increments PK
    private int id;

    private String name;
    private String membershipId;
    private String phone;
    private String address;
    private String goal;

    //Many members can share one plan
    @ManyToOne(fetch = FetchType.EAGER)
    private MembershipPlan plan;   // member is linked to one plan

    // Constructors
    public Member() {}

    public Member(String name, String membershipId, String phone, String address, String goal, MembershipPlan plan) {
        this.name = name;
        this.membershipId = membershipId;
        this.phone = phone;
        this.address = address;
        this.goal = goal;
        this.plan = plan;
    }

    //Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public MembershipPlan getPlan() {
        return plan;
    }

    public void setPlan(MembershipPlan plan) {
        this.plan = plan;
    }
}