<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="f"%>
<html>
	<head>
	<title>Team 10 Store</title>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">

	<script src="//ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>

	<link rel="stylesheet"
	href="//maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">	
	<script
	src="//maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

	<link href="/static/css/estore.css" rel="stylesheet">
	<script src="/static/js/estore.js"></script>
	<style>
	.bg {
		background-image: url(/static/images/background.jpg);
		background-size: cover;
		background-repeat: no-repeat;
		background-position: center;
		padding: 7% 0;
		text-decoration: blink;
		font-weight: bold;
		color: yellow;
	}
	</style>
</head>
<body>
	<div class="container">
		<header class="row bg text-center">
			<h1>Team 10's Store</h1>
		</header>
		<nav class="row">
			<nav class="navbar navbar-inverse">
				<div class="container-fluid">
					<div class="navbar-header">
						<a class="navbar-brand" href="/home/index">Home</a>
					</div>
					<ul class="nav navbar-nav">
						<li><a href="/home/about">About us</a></li>
						<li><a href="/home/contact">Contact us</a></li>
						<li class="dropdown"><a class="dropdown-toggle"
							data-toggle="dropdown" href="#">Account <span class="caret"></span></a>
							<ul class="dropdown-menu">
								<li><a href="#">Đăng nhập</a></li>
								<li><a href="#">Đăng ký</a></li>
								<li><a href="#">Đăng xuất</a></li>
								<li><a href="#">Đổi mật khẩu</a></li>
								<li><a href="#">Đơn hàng</a></li>
							</ul></li>
					</ul>
				</div>
			</nav>
		</nav>
		<div class="row">
			<article class="col-sm-9">
				<div class="row">
					<div class="col-sm-5 text-center">
						<img class="detail-img" src="/static/images/products/${prod.image}" >
					</div>
					<div class="col-sm-7">
						<ul class="detail-info">
							<li>Ten: ${prod.name}</li>
							<li>Don gia: <f:formatNumber value="${prod.unitPrice}" pattern="#,###.00"/>VND</li>
							<li>Ngay nhap: "${prod.productDate}"</li>
							<li>Loai: ${prod.category.nameVN}</li>
							<li>So luong: ${prod.quantity}</li>
							<li>Giam gia: <f:formatNumber value="${prod.discount}" type="percent"/></li>
							<li>Luot xem: ${prod.viewCount}</li>
							<li>San hang: ${prod.available?'Yes':'No'}</li>
							<li>Dac biet: ${prod.special?'Yes':'No'}</li>
						</ul>
					</div>
				</div>
				
				<div class="text-justify">${prod.description}</div><br/><br/>
				
				<ul class="nav nav-tabs">
				  <li class="active"><a data-toggle="tab" href="#tab1">CÙNG LOẠI</a></li>
				  <li><a data-toggle="tab" href="#tab2">YÊU THÍCH</a></li>
				  <li><a data-toggle="tab" href="#tab3">ĐÃ XEM</a></li>
				</ul>
				
				<div class="tab-content">
				  <div id="tab1" class="tab-pane fade in active">
				  	<div>
					    <h3>HÀNG CÙNG LOẠI</h3>
						<c:forEach var="p" items="${list}">
							<a href="/product/detail/${p.id}">
								<img class="thumb-img" src="/static/images/products/${p.image}" width="150px" height="150px">
							</a>
						</c:forEach>
					</div>
				  </div>
				  <div id="tab2" class="tab-pane fade">
				  	<div>
					    <h3>HÀNG YÊU THÍCH</h3>
						<c:forEach var="p" items="${favo}">
							<a href="/product/detail/${p.id}">
								<img class="thumb-img" src="/static/images/products/${p.image}" width="150px" height="150px">
							</a>
						</c:forEach>
					</div>
				  </div>
				  <div id="tab3" class="tab-pane fade">
				  	<div>
					    <h3>HÀNG ĐÃ XEM</h3>
						<c:forEach var="p" items="${viewed}">
							<a href="/product/detail/${p.id}">
								<img class="thumb-img" src="/static/images/products/${p.image}" width="150px" height="150px">
							</a>
						</c:forEach>
					</div>
				  </div>
				</div>
			</article>
			<aside class="col-sm-3">
				<div class="panel panel-default">
					<div class="panel-heading">Shopping Cart</div>
					<div class="panel-body">
						<img src="/static/images/shoppingcart.png" class="col-sm-5">
						<ul class="col-sm-7">
							<li><b id="cart-cnt">${cart.count}</b> mặt hàng</li>
							<li><b id="cart-amt">${cart.amount}</b>VND</li>
							<li> <a href="/cart/view">Xem giỏ hàng</a> </li>
						</ul>
					</div>
				</div>

				<div class="panel panel-default">
					<div class="panel-heading">TÌM KIẾM</div>
					<div class="panel-body">
						<form action="/product/list-by-keywords" method="post">
							<input value="${param.keywords}" name="keywords"
								class="form-control" placeholder="Keywords">
						</form>
					</div>
				</div>

				<div class="panel panel-default">
					<div class="panel-heading">DANH MỤC</div>
					<div class="list-group">
						<c:forEach var="c" items="${cates}">
							<a href="/product/list-by-category/${c.id}" class="list-group-item">${c.nameVN }</a> 
						</c:forEach>
					</div>
				</div>

				<div class="panel panel-default">
					<div class="panel-heading">ĐẶC BIỆT</div>
					<div class="list-group">
						<a href="#" class="list-group-item">Hàng mới</a> <a href="#"
							class="list-group-item">Bán chạy</a> <a href="#"
							class="list-group-item">Yêu thích</a> <a href="#"
							class="list-group-item">Giảm giá</a>
					</div>
				</div>
			</aside>
		</div>
		<footer>
			<p class="text-center">© 2022.All rights reserved.</p>
		</footer>
	</div>

</body>
</html>

