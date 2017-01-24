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
   <title>Urban Gardening Berkeley, Ca</title>
   </head>
   <body>
      <div class="wrapper">
         <kagutags:header navactivebutton="discuss-it" headerpic="smallheader.jpg"/>
         <div class="container-outer container-rounded-top">
            <h1 class="title text-center">
               Urban Gardening Berkeley, Ca
            </h1>

            <div class="container-inner">
            <div style="margin-left: 30px;">
               <ul style="list-style: none;">
                  <li class="pull-left">
                     <div style="display: table; height: 35px; margin-left:auto; margin-right:auto;">
                        <div style="display: table-cell; vertical-align: middle; overflow: hidden;">
                           <img class="img-rounded avatar-img-size-35x35" src="https://fbcdn-profile-a.akamaihd.net/hprofile-ak-ash2/c22.22.274.274/s160x160/282746_383324235049739_1794994782_n.jpg" alt="User Profile"/>
                        </div>
                     </div>
                     <a href="javascript:void(null)" onclick="showUserProfile(${following.getId()})"><small>Berkeley Youth Alternatives</small> </a>
                  </li>
                  <li class="pull-left vertical-rule">
                     <div style="display: table; height: 35px; margin-left:auto; margin-right:auto;">
                        <div style="display: table-cell; vertical-align: middle; overflow: hidden;">
                           <img class="img-rounded avatar-img-size-35x35" src="http://ecologycenter.org/wp-content/themes/ec/images/ecology_center_header.png" alt="User Profile"/>             
                        </div>
                     </div>
                     <a href="javascript:void(null)" onclick="showUserProfile(${following.getId()})"><small>The Ecology Center</small> </a>
                  </li>
                  <li class="pull-left  vertical-rule">
                     <div style="display: table; height: 35px; margin-left:auto; margin-right:auto;">
                        <div style="display: table-cell; vertical-align: middle; overflow: hidden;">
                           <img class="img-rounded avatar-img-size-35x35" src="http://cdn.publicsurplus.com/sms/docviewer/logo/885/4985934" alt="User Profile"/>
                        </div>
                     </div>
                     <a href="javascript:void(null)" onclick="showUserProfile(${following.getId()})"><small>Whittier School Garden</small> </a>
                  </li>
                  <li class="pull-left  vertical-rule">
                     <div style="display: table; height: 35px; margin-left:auto; margin-right:auto;">
                        <div style="display: table-cell; vertical-align: middle; overflow: hidden;">
                           <img class="img-rounded avatar-img-size-35x35" src="http://www.caregiving.com/wp-content/uploads/2013/06/Flowers-Pincushion-flower-300x300-300x300.jpg" alt="User Profile"/>             
                        </div>
                     </div>
                     <a href="javascript:void(null)" onclick="showUserProfile(${following.getId()})"><small>Gill Track Urban Farm</small> </a>
                  </li>
                  <li class="pull-left  vertical-rule">
                     <div style="display: table; height: 35px; margin-left:auto; margin-right:auto;">
                        <div style="display: table-cell; vertical-align: middle; overflow: hidden;">
                           <img class="img-rounded avatar-img-size-35x35" src="http://ashbycommunitygarden.webs.com/mahtin_artichoke.jpg" alt="User Profile"/>             
                        </div>
                     </div>
                     <a href="javascript:void(null)" onclick="showUserProfile(${following.getId()})"><small>Spiral Gardens</small> </a>
                  </li>
                  <li class="pull-left  vertical-rule">
                     <div style="display: table; height: 35px; margin-left:auto; margin-right:auto;">
                        <div style="display: table-cell; vertical-align: middle; overflow: hidden;">
                           <img class="img-rounded avatar-img-size-35x35" src="http://ashbycommunitygarden.webs.com/chickens.jpg" alt="User Profile"/>             
                        </div>
                     </div>
                     <a href="javascript:void(null)" onclick="showUserProfile(${following.getId()})"><small>Ashby Community Garden</small> </a>
                  </li>
               </ul>
            </div>
            <div class="clearfix"></div>
            </div>
         </div>
         <div class="container-outer container-two-column-left">
               <span class="lead title">Urban Gardening Group Activity</span>
               <img class="pull-left" style="margin-right: 10px;" alt="" src="/static/images/eventit.png">
               <div class="clearfix"></div>
 
            <hr class="style-one">
            <c:if test="${not empty querystring}">
               <hr class="style-one">
               <ul class="breadcrumb" style="margin: 0px;">
                  <span class="lead">Search results for <strong>${querystring}</strong></span>
               </ul>
               <hr class="kagu-hr">
            </c:if>
            <kagutags:group_list_renderer activitiesList="${activitiesList}"/>
            <c:if test="${empty discussions_list}">
               <h3 align="center">No Results</h3>
            </c:if>
            <%--             <kagutags:pagination/> --%>

         </div>
         <div class="container-outer container-two-column-right">
         <div class="container-inner">
            <c:if test="${user.isLoggedIn()}">
               <h3 style="line-height: 35px;"><a class="" href="/discussion/create">
                  <img class="pull-left"  style="margin-right: 10px;" alt="" src="/static/images/add_discussion.png"> Post A Discussion</a>
               </h3>
               <h3 style="line-height: 35px; "><a class="" href="/event_create"> 
                  <img class="pull-left"  style="margin-right: 10px;" alt="" src="/static/images/create_event_bw.png">  Post An Event</a>
               </h3>
               <h3 style="line-height: 35px; "><a class="" href="/ad/create">
                  <img class="pull-left"  style="margin-right: 10px;" alt="" src="/static/images/create_exchange_bw.png">  Offer An Exchange</a>
               </h3>
            </c:if>
            <hr class="style-one">
            <form action="/discussions/public/search" class="form-search ">
               <div class="input-append pull-left side-bar-spacing">
                  <input name="keywords" placeholder="Search Within Group" type="text" 
                  <c:if test="${not empty keywords}">value="${keywords}"</c:if>
                  class="input-medium search-query">
                  <button type="submit" class="btn"><i class="icon-search"></i> Search</button> 
               </div>
               
               <div class="clearfix"></div>
               <h6 class="muted">With Tags (optional)</h6>
               <div class="clearfix"></div>
               <kagutags:search_tags ajaxurl="/discussions/browse/type_ahead_tags"/>
            </form>
            <hr class="style-one">
            <div class="clearfix"></div>
            <button id="subscribe" class="btn btn-large btn-warning side-bar-spacing btn-block pull-right" title="Receive updates for this conversation" alt="Bookmark">Join This Group</button>  
            <div class="clearfix"></div>
         </div>
</div>
          <div class="container-outer container-two-column-right container-stacked">
                      <span class="lead title">
               Trending Discussions
            </span>
            <hr class="style-one">
                <div class="container-inner">
                
         <ol>
    <c:if test="${not empty discussion_trends}">
        <c:forEach var="discussion" items="${discussion_trends}">
            <li><a class="darkgrey" href="/discussion/public/view/${discussion.getId()}/${discussion.getFriendlyURL()}"> <small>${discussion.getFirstMessageInThread().getTitle()}</small>
            </a>
                <hr class="kagu-hr"></li>
        </c:forEach>
    </c:if>
</ol>
</div>
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
