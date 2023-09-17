<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2>Product Table</h2>

<table class="table table-hover">
  <thead>
  	<tr>
  		<th>Id</th>
  		<th>Name</th>
  		<th>Unit Price</th>
  		<th>Product Date<th>
  		<th>Quantity<th>
  		<th>Discount<th>
  		<th><th>
  	</tr>
  </thead>
  <tbody>
  <c:forEach var="e" items="${list}">
  	<tr>
  		<td>${e.id }</td>
  		<td>${e.name }</td>
  		<td>${e.unitPrice}</td>
  		<td>${e.productDate }</td>
  		<td>${e.quantity }</td>
  		
  		<td>${e.discount }</td>
  		<td>
  			<a class="btn btn-sm btn-info" href="${base}/edit/${e.id}">Edit</a>
  			<a class="btn btn-sm btn-danger" href="${base}/delete/${e.id}">Delete</a>
  		</td>
  	</tr>
  	</c:forEach>
  </tbody>
</table>

