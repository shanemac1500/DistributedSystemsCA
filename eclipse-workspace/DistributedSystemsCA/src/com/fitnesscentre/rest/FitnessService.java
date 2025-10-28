package com.fitnesscentre.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.fitnesscentre.DAO.MemberDAO;
import com.fitnesscentre.DAO.MembershipPlanDAO;
import com.fitnesscentre.DAO.PaymentDAO;
import com.fitnesscentre.model.Member;
import com.fitnesscentre.model.MembershipPlan;
import com.fitnesscentre.model.Payment;

@Path("/fitness")
public class FitnessService {
	
	@GET
	@Path("/hello")
	@Produces("text/plain")
	public String hello() {
		return "Hello from FitnessService!";
	}
	
	//Get all Membership Plans
	@GET
	@Path("/json/plans")
	@Produces("application/json")
	public List<MembershipPlan> getAllPlans(){
		MembershipPlanDAO dao = new MembershipPlanDAO();
		return dao.findAll();
	}
	
	//POST a new plan
	@POST
	@Path("/plan")
	@Consumes("application/json")
	@Produces("text/plain")
	public String addPlan(MembershipPlan plan) {
		MembershipPlanDAO dao = new MembershipPlanDAO();
		dao.persist(plan);
		return "Plan added: " + plan.getDescription();
	}
	
	//MEMBERS
	@GET
	@Path("/json/members")
	@Produces("application/json")
	public List<Member> getAllMembers() {
	    return new MemberDAO().findAll();
	}

	@POST
	@Path("/member")
	@Consumes("application/json")
	@Produces("text/plain")
	public String addMember(Member m) {
	    new MemberDAO().persist(m);
	    return "Member added: " + m.getName();
	}

}
