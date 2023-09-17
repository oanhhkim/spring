<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<c:forEach var="p" items="${list}">
	<div class="col-sm-4">
		<div class="thumbnail">
			<a href="/product/detail/${p.id}"> <img class="estore-prod"
				src="/static/images/products/${p.image}">
			</a>
			<div class="caption">
				<p>${p.name}</p>
				<div class="pull-right" data-id="${p.id}">
					<button class="btn btn-sm btn-danger">
						<span class="glyphicon glyphicon-shopping-cart btn-add-to-cart"></span>
					</button>
					<button class="btn btn-sm btn-warning btn-star">
						<span class="glyphicon glyphicon-star"></span>
					</button>
					<button class="btn btn-sm btn-success btn-open-dialog"
						data-toggle="modal" data-target="#myModal">
						<span class="glyphicon glyphicon-envelope"></span>
					</button>
				</div>
				<p>${p.unitPrice}</p>
			</div>
		</div>
	</div>
</c:forEach>
<jsp:include page="dialog.jsp" />