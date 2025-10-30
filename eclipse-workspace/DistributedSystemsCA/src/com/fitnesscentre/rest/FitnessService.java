package com.fitnesscentre.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
	
	// Get all Membership Plans (supports both JSON and XML)
	@GET
	@Path("/json/plans")
	@Produces({ "application/json", "application/xml" })
	public MembershipPlan[] getAllPlans() {
	    MembershipPlanDAO dao = new MembershipPlanDAO();
	    List<MembershipPlan> list = dao.findAll();
	    return list.toArray(new MembershipPlan[0]);
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
	
	 //PAYMENTS

    // Create a payment for a member
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

        p.setMember(m);
        new PaymentDAO().persist(p);
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
    
    @GET
    @Path("/json/payments/total/{id}")
    @Produces("text/plain")
    public String totalPaid(@PathParam("id") int memberId) {
        double sum = new PaymentDAO().totalPaidByMember(memberId);
        return Double.toString(sum);
    }

 
}
