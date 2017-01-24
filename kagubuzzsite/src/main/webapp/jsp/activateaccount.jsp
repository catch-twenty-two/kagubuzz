<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html lang="en">


<!-- Jquery has to go before bootstrap -->

<kagutags:jquery />

   <kagutags:bootstrap_css/>
   <kagutags:bootstrap_js/>

<link rel="stylesheet" href="/static/css/main.css" type="text/css" />
<link rel="stylesheet" href="/static/css/bootstrapjumbotron.css" type="text/css" />


<head>
<meta charset="utf-8" />
<title>Kagu Buzz Activate Your Account</title>
</head>

<body>
	<div class="wrapper">
		<kagutags:header navactivebutton="" headerpic="smallheader.jpg"></kagutags:header>
        <kagutags:infoboxtop cssclass="buzzinfoboxtitle homemessagebox" collapsed="flase" collapseable="true" title=""/>
		<div class="buzzinfoboxpadding">
		             
			<form action="home">
			<input name="securityCode" value="${user.getSecurityCode()}" type="hidden"/>
			<input name="id" value="${user.getId()}" type="hidden"/>
			<fieldset>
			 <legend>Hello ${user.getFirstName()}, Let's Reset Your Password</legend>
			<div class="pull-left" style="margin-right: 10px;">
				
	               
					<label>Pick a New Password</label> 
					   <input name="newpassword1" type="password" value="password"> 
					<label>Re-Type Your New Password</label> 
                       <input name="newpassword2" type="password" value="password"> 					   
					<p>
					<button type="submit" class="btn">Submit</button></p>
				
			</div>
			<div class="pull-left">
			${recaptcha}
			</div>
			</fieldset>
			</form>
		</div>
        <kagutags:infoboxbottom/>
		<kagutags:footer />
	</div>
</body>
</html>

<script type="text/javascript">
    <c:if test="${not empty captcha_error}">
      toastr.error('${captcha_error}', 'reCaptcha Error');  
    </c:if>
</script>
