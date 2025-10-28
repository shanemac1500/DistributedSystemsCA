
package com.fitnesscentre.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "member")
@Entity
@Table(name = "member")
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String membershipId;
    private String phone;
    private String address;
    private String goal;

    @ManyToOne
    @JoinColumn(name = "plan_id")   // FK to MembershipPlan.id
    private MembershipPlan plan;

    public Member() {}

    // getters/setters (all fields)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMembershipId() { return membershipId; }
    public void setMembershipId(String membershipId) { this.membershipId = membershipId; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }

    public MembershipPlan getPlan() { return plan; }
    public void setPlan(MembershipPlan plan) { this.plan = plan; }
}