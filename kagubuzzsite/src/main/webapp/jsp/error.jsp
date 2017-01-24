<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>

<!DOCTYPE html>
<html lang="en">
   <head>
      <meta charset="utf-8" />
       <kagutags:bootstrap_css />
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
      .container-narrow>hr {
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
      .marketing p+h4 {
      margin-top: 28px;
      }
   </style>
      <title>Kagu Buzz - Your Local Community Your Way</title>
   </head>
   <body>
      <div class="wrapper">
         <kagutags:header navactivebutton="" headerpic="smallheader.jpg"></kagutags:header>
         <div class="buzzinfoboxpadding">
            <div class="jumbotron">
               <c:choose>
                  <c:when test="${empty error_page_message}">
                     <h1>Uh Oh, There Was An Error!</h1>
                     <p class="lead">
                        You found a problem with the site! Please <br> <strong><a href="mailto:help@kagubuzz.com" data-uv-trigger>Let Us Know</a></strong> <br> what you did to get here.
                     </p>
                     <p class="lead">Thanks!</p>
                  </c:when>
                  <c:otherwise>
                     <h1>Uh Oh!</h1>
                     <p class="lead">${error_page_message}</p>
                  </c:otherwise>
               </c:choose>
               <c:choose>
                  <c:when test="${empty error_page_link}">
                     <a class="btn btn-large btn-success" href="/home/browse">Get Me Outta Here!</a>
                  </c:when>
                  <c:otherwise>
                     <a class="btn btn-large btn-success" href="${error_page_link}">${error_page_button}</a>
                  </c:otherwise>
               </c:choose>
            </div>
         </div>
         <kagutags:footer />
      </div>
      <kagutags:jquery />
      <script src="/static/jquery/uservoice.js" type="text/javascript"></script>
      <script>
         ${error_page_javascript}
      </script>
   </body>
</html>