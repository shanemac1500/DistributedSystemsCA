package com.fitnesscentre.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "payment")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double amount;

    
    private String date;   

    //Each payment is linked to one member
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Member member;

    public Payment() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }
}