<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<h2>Register</h2>
<h3>${message}</h3>
<form:form action="/account/register" modelAttribute="form" enctype="multipart/form-data">
	<div class="form-group">
		<label>Username</label>
		<form:input path="id" class="form-control"/>
	</div>
	<div class="form-group">
		<label>Password</label>
		<form:input path="password" class="form-control"/>
	</div>
	<div class="form-group">
		<label>Full name</label>
		<form:input path="fullname" class="form-control" />
	</div>
	<div class="form-group">
		<label>Email</label>
		<form:input path="email" class="form-control"/>
	</div>
	<div class="form-group">
		<label>Photo</label>
		<input type="file" name="photo_file">
		<form:hidden path="photo" class="form-control"/>
	</div>
	<div class="form-group">
		<button class="btn btn-default">Register</button>
	</div>
</form:form>