<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Team 10 Store</title>
  <tiles:insertAttribute name="head"/>
  <style>
  	.bg{
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
  	<h1>Administrator Tool</h1>
  </header>
  <nav class="row">
  	<tiles:insertAttribute name="menu"/>
  </nav>
  <div class="row">
  	<article class="col-sm-9">
  		<tiles:insertAttribute name="body"/>
  	</article>
  	
  </div>
  <footer>
  	<p class="text-center">&copy; 2022.All rights reserved. </p>
  </footer>
</div>

</body>
</html>