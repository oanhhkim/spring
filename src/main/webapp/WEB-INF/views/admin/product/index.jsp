<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt"  prefix="c"%>
<h2>Product Manager</h2>
<c:set var="base" value="/admin/product" scope="request"/>
<h4 class="label label-success">${message}${param.message}</h4>

<jsp:include page="_form.jsp"/>

<jsp:include page="_table.jsp"/>

