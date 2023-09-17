<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<table class="table">
	<thead>
		<tr>
			<th>Id</th>
			<th>Name</th>
			<th>Unit Price</th>
			<th>Discount</th>
			<th>Quantity</th>
			<th>Amount</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="p" items="${sessionScope['scopedTarget.cartService'].items}">
			<tr>
				<td>${p.id}</td>
				<td>${p.name }</td>
				<td>${p.unitPrice }</td>
				<td>${p.discount}</td>
				<td>${p.quantity }</td>
				<td>${p.quantity*p.unitPrice*(1-p.discount)}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<h2>Checkout</h2>
<form:form action="/order/checkout" modelAttribute="order" >
	<div class="form-group">
		<label>Customer</label>
		<form:input path="customer.id" class="form-control"/>
	</div>
	<div class="form-group">
		<label>Order Date</label>
		<form:input path="orderDate" class="form-control"/>
	</div>
	<div class="form-group">
		<label>Address</label>
		<form:input path="address" class="form-control" />
	</div>
	<div class="form-group">
		<label>Total Amount</label>
		<form:input path="amount" class="form-control"/>
	</div>
	<div class="form-group">
		<label>Note</label>
		<form:textarea path="description" rows="3" class="form-control"/>
	</div>
	<div class="form-group">
		<button class="btn btn-default">Purchase</button>
	</div>
</form:form>