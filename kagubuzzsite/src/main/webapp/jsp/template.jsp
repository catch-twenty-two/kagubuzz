<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <kagutags:bootstrap_css/>
    <link rel="stylesheet" href="/static/css/main.css" type="text/css" />
	<meta charset="utf-8" />
	<title>Kagu Buzz - Your Local Community Your Way</title>
</head>

<body>
    <kagutags:jquery/>
	<div class="wrapper">
	<kagutags:header navactivebutton="" headerpic="smallheader.jpg"></kagutags:header>
		<div class="main-content">
			<div class="buzzinfoboxpadding">
			<!-- Main Content goes here -->
			</div>
		</div>
		<div class="side-bar">
			<a href="/login"><img alt="" src="/static/images/kagutitlesmall.png"/></a>
			<hr class="kagu-hr">
			<div  class="buzzinfoboxpadding">
			<!-- Side Bar Content Goes Here -->
			</div>					
		</div>
		<kagutags:footer/>
	</div>
</body>

</html>


