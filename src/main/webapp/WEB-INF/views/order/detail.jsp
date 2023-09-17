<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<h2>Order Info</h2>
<form:form action="/order/detail" modelAttribute="orderdt" >
	<div class="form-group">
		<label>Order Id</label>
		<form:input path="customer.id" class="form-control" readonly="true"/>
	</div>
	<div class="form-group">
		<label>Customer</label>
		<form:input path="customer.id" class="form-control" readonly="true"/>
	</div>
	<div class="form-group">
		<label>Order Date</label>
		<form:input path="orderDate" class="form-control" readonly="true"/>
	</div>
	<div class="form-group">
		<label>Address</label>
		<form:input path="address" class="form-control" readonly="true"/>
	</div>
	<div class="form-group">
		<label>Total Amount</label>
		<form:input path="amount" class="form-control" readonly="true"/>
	</div>
	<div class="form-group">
		<label>Note</label>
		<form:textarea path="description" rows="3" class="form-control" readonly="true"/>
	</div>
</form:form>
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
		<c:forEach var="d" items="${details}">
			<tr>
				<td>${d.product.id}</td>
				<td>${d.product.name }</td>
				<td>${d.unitPrice }</td>
				<td>${d.discount}</td>
				<td>${d.quantity }</td>
				<td>${d.quantity*d.unitPrice*(1-d.discount)}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>