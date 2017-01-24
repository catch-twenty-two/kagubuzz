<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html lang="en">


<!-- Jquery has to go before bootstrap -->

<kagutags:jquery />

<kagutags:bootstrap_css />
<kagutags:bootstrap_js />

<script src="/static/jquery/jquery.validate.js" type="text/javascript"></script>

<link rel="stylesheet" href="/static/css/main.css" type="text/css" />
<link rel="stylesheet" href="/static/css/bootstrapjumbotron.css" type="text/css" />

<head>
<meta charset="utf-8" />
<title>Resetting Your Password</title>
</head>

<body>
	<div class="wrapper">
		<kagutags:header navactivebutton="" headerpic="smallheader.jpg"></kagutags:header>
		<kagutags:infoboxtop cssclass="buzzinfoboxtitle homemessagebox" collapsed="flase" collapseable="true" title="" />
		<div class="buzzinfoboxpadding">

			<form id="resetpassword" action="/reset_password_send" method="post">				
				<fieldset>
					<legend>Resetting your Password is easy, what is the e-mail address associated with your account?</legend>
					<div class="pull-left" style="margin-right: 10px;">

						<input class="required" name="userId" type="email" placeholder="joeblow@schmoe.com">
                        <br>
						<button type="submit" class="btn">Reset My Password</button>

					</div>
				</fieldset>
			</form>
		</div>
		<kagutags:infoboxbottom />
		<kagutags:footer />
	</div>
</body>
</html>

<script type="text/javascript">
	$("#resetpassword").validate();
</script>
