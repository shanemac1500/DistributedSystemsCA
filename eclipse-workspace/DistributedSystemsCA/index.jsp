<%@ page contentType="text/html;charset=UTF-8" %>
<h2>Distributed Systems CA — Fitness Centre Application</h2>
<p>Quick links to test REST endpoints:</p>

<h3>FitnessService Endpoints</h3>
<ul>
  <li><a href="restful-services/fitness/hello">/fitness/hello</a> — Test service connection</li>
  <li><a href="restful-services/fitness/json/plans">/fitness/json/plans</a> — Get all membership plans (JSON/XML)</li>
  <li><a href="restful-services/fitness/json/members">/fitness/json/members</a> — Get all members (JSON)</li>
</ul>

<h3>Member Endpoints</h3>
<ul>
  <li>POST /restful-services/fitness/member — Add a new member (JSON)</li>
  <li>PUT /restful-services/fitness/member/{id} — Update an existing member (JSON)</li>
  <li>DELETE /restful-services/fitness/member/{id} — Delete a member by ID</li>
</ul>

<h3>Payment Endpoints</h3>
<ul>
  <li><a href="restful-services/fitness/json/payments">/fitness/json/payments</a> — Get all payments (JSON)</li>
  <li><a href="restful-services/fitness/json/payments/member/1">/fitness/json/payments/member/1</a> — Get payments for member ID 1</li>
  <li><a href="restful-services/fitness/json/payments/total/1">/fitness/json/payments/total/1</a> — Get total paid by member ID 1</li>
  <li>POST /restful-services/fitness/payment — Add a new payment (JSON)</li>
</ul>
