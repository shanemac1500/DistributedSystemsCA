package com.fitnesscentre.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.fitnesscentre.DAO.MembershipPlanDAO;
import com.fitnesscentre.model.MembershipPlan;

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

}
