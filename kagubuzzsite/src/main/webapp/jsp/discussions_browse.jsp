<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html lang="en">
   <!-- Jquery has to go before bootstrap -->
   <kagutags:jquery />
      <kagutags:bootstrap_css/>
   <kagutags:bootstrap_js/>
   <link rel="stylesheet" href="/static/css/main.css" type="text/css" />
   <link rel="stylesheet" href="/static/css/bootstrap-tagmanager.css" type="text/css" />
   <script src="/static/jquery/bootstrap-tagmanager.js" type="text/javascript"/></script>
   <!-- Infinite Scrolling -->
   <script src="/static/jquery/jquery-ui-1.8.21.custom.min.js" type="text/javascript"></script>
   <script src="/static/jquery/jquery-ui.scrollAppend.js" type="text/javascript"></script>
   <script src="/static/jquery/postoptionclick.js" type="text/javascript"></script>
   <script type="text/javascript">
      $(document).ready(function() {
          <c:if test="${not empty category_selection}">
             $('#discussion_category_id').val('${category_selection}');
          </c:if>
      });
   </script>
   <meta charset="utf-8" />
   <title>${event.getTitle()}</title>
   </head>
   <body>
      <div class="wrapper">
         <kagutags:header navactivebutton="discuss-it" headerpic="smallheader.jpg"></kagutags:header>
         <div class="main-content clear-background">
            <c:if test="${not empty querystring}">
               <hr class="kagu-hr">
               <ul class="breadcrumb" style="margin: 0px;">
                  <span class="lead">Search results for <strong>${querystring}</strong></span>
               </ul>
               <hr class="kagu-hr">
            </c:if>
 			<c:if test="${not empty discussions_list}">
               <kagutags:discussion_list_renderer discussions_list="${discussions_list}"/>
            </c:if>
			<c:if test="${empty discussions_list}">
          	  <h3 align="center">No Results</h3>
          	</c:if>
            <kagutags:pagination/>
         </div>
         <div class="side-bar">
            <a href="/login"><img alt="" src="/static/images/kagutitlesmall.png"/></a>
            <hr class="kagu-hr">
            <h4>Browsing ${user.getTblKaguLocation().getCity()} Discussions </h4>
            <c:if test="${user.isLoggedIn()}">
               <hr class="kagu-hr">
               <a href="/discussion/create" class="btn btn-block btn-danger" type="button">Start A New Discussion</a>
            </c:if>
            <hr class="kagu-hr">
            <h5><a href="/discussions/browse/categories">Browse All Categories and Tags</a> </h5>
            <kagutags:discussion_sidebar/>
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
