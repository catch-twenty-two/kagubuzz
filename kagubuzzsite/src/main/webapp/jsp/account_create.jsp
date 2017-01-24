<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="en">


<!-- Jquery has to go before bootstrap -->

<kagutags:jquery/>

<kagutags:bootstrap_css/>

<script src="/static/jquery/jquery.cookie.js" type="text/javascript"/></script>

<link rel="stylesheet" href="/static/css/main.css" type="text/css" />
    <style type="text/css">
      body {
        padding-top: 20px;
        padding-bottom: 40px;
      }

      /* Custom container */
      .container-narrow {
        margin: 0 auto;
        max-width: 700px;
      }
      .container-narrow > hr {
        margin: 30px 0;
      }

      /* Main marketing message and sign up button */
      .jumbotron {
        margin: 60px 0;
        text-align: center;
      }
      .jumbotron h1 {
        font-size: 72px;
        line-height: 1;
      }
      .jumbotron .btn {
        font-size: 21px;
        padding: 14px 24px;
      }

      /* Supporting marketing content */
      .marketing {
        margin: 60px 0;
      }
      .marketing p + h4 {
        margin-top: 28px;
      }
    </style>
    
<head>
	<meta charset="utf-8" />
	<title>Kagu Buzz - Your Local Community Your Way</title>
</head>

<body>
	<div class="wrapper">
		<kagutags:header navactivebutton="" headerpic="smallheader.jpg"></kagutags:header>

		<div class="buzzinfoboxpadding">

			<div class="jumbotron">
				<h1>${message_title}</h1>
				<p class="lead">${message_lead}</p>
				<a class="btn btn-large btn-success" href="/home/browse">Take Me Home!</a>

			</div>

		</div>

		<kagutags:footer />
	</div>
</body>

</html>