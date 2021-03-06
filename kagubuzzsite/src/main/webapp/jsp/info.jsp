<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="en">
   <head>
      <kagutags:bootstrap_css/>
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
      <link rel="stylesheet" href="/static/css/main.css" type="text/css" />
      <meta charset="utf-8" />
      <title>Kagu Buzz - Your Local Community Your Way</title>
   </head>
   <body>
      <kagutags:jquery/>
      <script src="/static/jquery/jquery.cookie.js" type="text/javascript"/></script>
      <div class="wrapper">
         <kagutags:header navactivebutton="" headerpic="smallheader.jpg"></kagutags:header>
         <div class="buzzinfoboxpadding">
            <div class="jumbotron">
               <h1>${message_title}</h1>
               <p class="lead">${message_lead}</p>
               - The Team At Kagu Buzz
            </div>
         </div>
         <kagutags:footer />
      </div>
   </body>
</html>
