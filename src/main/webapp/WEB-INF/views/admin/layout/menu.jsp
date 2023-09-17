<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="/admin/home/index">Trang chủ</a>
    </div>
    
    <li class="dropdown">
        <a class="dropdown-toggle" data-toggle="dropdown" href="#">Quản lý
        <span class="caret"></span></a>
	        <ul class="dropdown-menu">
				<li><a href="/admin/category/index">Category</a></li>
				<li><a href="/admin/product/index">Product</a></li>
	       	</ul>
      </li>
    \
  </div>
</nav>