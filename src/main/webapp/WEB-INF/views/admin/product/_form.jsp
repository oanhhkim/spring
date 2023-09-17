<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt"  prefix="c"%>
<h2>Product Form</h2>

<form:form action="${base}/index" modelAttribute="entity" enctype="multipart/form-data">
	<div class="form-group">
		<label>Id</label>
		<form:input path="id" class="form-control"/>
	</div>
	<div class="form-group">
		<label>Name</label>
		<form:input path="name" class="form-control"/>
	</div>
	<div class="form-group">
		<label>Unit Price</label>
		<form:input path="unitPrice" class="form-control"/>
	</div>
	<div class="form-group">
		<label>Image</label>
		<input type="file" name="image_file" class="form-control"/>
		<form:hidden path="image"/>
	</div>
	<div class="form-group">
		<label>Product Date</label>
		<form:input path="productDate" class="form-control"/>
	</div>
	<div class="form-group">
		<label>Available</label>
		<div class="form-control">
			<form:radiobutton path="available" value="true" label="Yes"/>
			<form:radiobutton path="available" value="false" label="No"/>
		</div>
	</div>
	<div class="form-group">
		<label>Category</label>
		<form:select path="category.id" class="form-control">
			<form:options items="${cates}" itemLabel="name" itemValue="id"/>
		</form:select>
	</div>
	<div class="form-group">
		<label>Quantity</label>
		<form:input path="quantity" class="form-control"/>
	</div>
	<div class="form-group">
		<label>Description</label>
		<form:textarea path="description" rows="3" class="form-control"/>
	</div>
	<div class="form-group">
		<label>Discount</label>
		<form:input path="discount" class="form-control"/>
	</div>
	<div class="form-group">
		<label>View Count</label>
		<form:input path="viewCount" class="form-control"/>
	</div>
	<div class="form-group">
		<label>Special</label>
		<div class="form-control">
			<form:radiobutton path="special" value="true" label="Yes"/>
			<form:radiobutton path="special" value="false" label="No"/>
		</div>
	</div>
	<div class="form-group">
		<button class="btn btn-primary" formaction="${base}/create">Create</button>
		<button class="btn btn-warning" formaction="${base}/update">Update</button>
		<button class="btn btn-danger" formaction="${base}/delete">Delete</button>
		<a class="btn btn-default" href="${base}/index">Reset</a>
	</div>
	
</form:form>


