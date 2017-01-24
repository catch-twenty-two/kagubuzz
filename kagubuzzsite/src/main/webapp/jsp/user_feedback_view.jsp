<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html lang="en">
   <!-- Jquery has to go before bootstrap -->
   <kagutags:jquery />
   <kagutags:bootstrap_css/>
   <kagutags:bootstrap_js/>
   <link rel="stylesheet" href="/static/css/main.css" type="text/css" />
   <meta charset="utf-8" />
   <title>Kagu Buzz User Feedback For ${userInquiry.getFirstName()}</title>
   </head>
   <body>
      <div class="wrapper">
         <kagutags:header navactivebutton="discuss-it" headerpic="smallheader.jpg"></kagutags:header>
         <div class="clear-background ">
            <div class="buzzinfobox notitle five-px-padding">
               <img class="img-polaroid pull-left" style="height: 60px; width: 60px; margin-right: 15px;" alt="${user.getFirstName()}" src="/fileServerServlet?type=GetAvatarByUserId&amp;id=${userInquiry.getId()}"/>                            
               <h1>Kagu Buzz User  Feedback For ${userInquiry.getFirstName()}</h1>
               <div class="clearfix"></div>
            </div>
            <c:choose>
            <c:when test="${not empty feedback }">
            <div class="list_padding">
               <c:forEach var="message" items="${feedback}">
                  <span class="lead black">${message.getSubject()}</span>
                  <span class="pull-right">
                  <strong class="${message.getExchangeRatingType().getColor()}">${message.getExchangeRatingType().getEnumExtendedValues().getDescription()}</strong> <i class="icon-black ${message.getExchangeRatingType().getIcon()}"></i>
                  </span>
                  <kagutags:message_render 
                     message="${message}"
                     showsubject="N"
                     imessage="${message}"/>
               </c:forEach>
            </div>
            </c:when>
            <c:otherwise>
            <h3 class="text-center">There's No Feedback For ${userInquiry.getFirstName()} Yet</h3>
            </c:otherwise>
            </c:choose>
         </div>
         <kagutags:footer />
      </div>
      <script type="text/javascript">
         function showUserProfile(userId) {         
           $('#modal_dialog').load('/modal/userprofile/' + userId, function() {$('#modal_dialog').modal('show');});
         }        
      </script>
   </body>
</html>
