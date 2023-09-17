<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<h1>Purchase Successfully</h1>
<table class="table">
	<thead>
		<tr>
			<th>Id</th>
			<th>Order Date</th>
			<th>Address</th>
			<th>Amount</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="p" items="${orders}">
			<tr>
				<td>${p.id}</td>
				<td>${p.orderDate}</td>
				<td>${p.address }</td>
				<td>${p.amount}</td>
				<td>
					<a href="/order/detail/${p.id}" class="btn btn-sm btn-warning">
						Detail
					</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
