package com.fitnesscentre.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import com.fitnesscentre.DAO.MemberDAO;
import com.fitnesscentre.DAO.MembershipPlanDAO;
import com.fitnesscentre.DAO.PaymentDAO;
import com.fitnesscentre.model.Member;
import com.fitnesscentre.model.MembershipPlan;
import com.fitnesscentre.model.Payment;


@Path("/fitness")
public class FitnessService {
	
	//Test Endpoint to confirm employment
	@GET
	@Path("/hello")
	@Produces("text/plain")
	public String hello() {
		return "Hello from FitnessService!";
	}
	
	//MEMBERSHIP PLANS
	// Get all Membership Plans (supports both JSON and XML)
	//Array helps JAXB produce collection for XML
	@GET
	@Path("/json/plans")
	@Produces({ "application/json", "application/xml" })
	public MembershipPlan[] getAllPlans() {
	    MembershipPlanDAO dao = new MembershipPlanDAO();
	    List<MembershipPlan> list = dao.findAll();
	    return list.toArray(new MembershipPlan[0]);
	}
	
	//Create a new plan
	//Accepts JSON or XML
	@POST
	@Path("/plan")
	@Consumes({"application/json","application/xml"})
	@Produces("text/plain")
	public String addPlan(MembershipPlan plan) {
		MembershipPlanDAO dao = new MembershipPlanDAO();
		dao.persist(plan);
		return "Plan added: " + plan.getDescription();
	}
	
	//MEMBERS
	//List all members as JSON
	@GET
	@Path("/json/members")
	@Produces("application/json")
	public List<Member> getAllMembers() {
	    return new MemberDAO().findAll();
	}
	
    //Create a member
	//If a plan id is included in the JSON (plan.id) the DAO will attach it
	@POST
	@Path("/member")
	@Consumes("application/json")
	@Produces("text/plain")
	public String addMember(Member m) {
	    new MemberDAO().persist(m);
	    return "Member added: " + m.getName();
	}
	
	// UPDATE member (PUT /member/{id})
	//Only updates fields that are present

	@PUT
	@Path("/member/{id}")
	@Consumes("application/json")
	@Produces("application/json")
	public Member updateMember(@PathParam("id") int id, Member incoming) {
	    MemberDAO dao = new MemberDAO();
	    Member existing = dao.findById(id);
	    if (existing == null) {
	        throw new WebApplicationException(
	            Response.status(Response.Status.NOT_FOUND)
	                    .entity("Member not found: " + id)
	                    .type("text/plain")
	                    .build()
	        );
	    }
        //Patch fields only if supplied
	    if (incoming.getName() != null) existing.setName(incoming.getName());
	    if (incoming.getMembershipId() != null) existing.setMembershipId(incoming.getMembershipId());
	    if (incoming.getPhone() != null) existing.setPhone(incoming.getPhone());
	    if (incoming.getAddress() != null) existing.setAddress(incoming.getAddress());
	    if (incoming.getGoal() != null) existing.setGoal(incoming.getGoal());
	    //If a plan is provided DAO.merge() attaches a managed reference
	    if (incoming.getPlan() != null && incoming.getPlan().getId() != 0) {
	        existing.setPlan(incoming.getPlan());
	    }
	    return dao.merge(existing);
	
	}
	// DELETE member (DELETE /member/{id})
	@DELETE
	@Path("/member/{id}")
	@Produces("text/plain")
	public String deleteMember(@PathParam("id") int id) {
	 
	    boolean ok = new MemberDAO().deleteById(id);
	    return ok ? ("Member " + id + " deleted.") : ("Member not found: " + id);
	}
	
	 //PAYMENTS

    // Create a payment for a member
	//Member id is resolved and attached before persisting
    @POST
    @Path("/payment")
    @Consumes("application/json")
    @Produces("text/plain")
    public String addPayment(Payment p) {
        //attach an existing Member entity
        if (p.getMember() == null || p.getMember().getId() == 0) {
            return "Member id required in payment JSON (e.g. {\"member\":{\"id\":1},\"amount\":25.0})";
        }
        MemberDAO mdao = new MemberDAO();
        Member m = mdao.findById(p.getMember().getId());
        if (m == null) return "Member not found: " + p.getMember().getId();

        p.setMember(m);//attach managed member
        new PaymentDAO().persist(p); //save payment
        return "Payment added: " + p.getAmount() + " for member " + m.getName();
    }

    // Get all payments
    @GET
    @Path("/json/payments")
    @Produces("application/json")
    public java.util.List<Payment> getAllPayments() {
        return new PaymentDAO().findAll();
    }

    // Get all payments for a member
    @GET
    @Path("/json/payments/member/{id}")
    @Produces("application/json")
    public java.util.List<Payment> getPaymentsForMember(@PathParam("id") int memberId) {
        return new PaymentDAO().findByMemberId(memberId);
    }
    
    //Return total paid by a member as a plain text
    @GET
    @Path("/json/payments/total/{id}")
    @Produces("text/plain")
    public String totalPaid(@PathParam("id") int memberId) {
        double sum = new PaymentDAO().totalPaidByMember(memberId);
        return Double.toString(sum);
    }

 
}
