<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ attribute name="headerpic" required="true" type="java.lang.String"%>
<%@ attribute name="navactivebutton" required="true" type="java.lang.String"%>
<%@ attribute name="dont_ask_zip" required="false" type="java.lang.String"%>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>

<div id="modal_dialog" class="modal fade" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="modalDialogLabel" aria-hidden="true"></div>
<header>
   <div id="headerimage">   
<!--       <img style="height:15px;" src="/static/images/smallheader.jpg" alt="Kagu Buzz"/> -->
   </div>
   <div class="navbar navbar-fixed-top">
      <div class="navbar-inner">
         <div class="container" style="width:960px;">
            <ul class="nav" >
               <c:if test="${user.isLoggedIn() == true }">
                  <li
                  <c:if test="${navactivebutton == 'home'}"> class="active"</c:if>
                  >
                  <a class="nav-a" href="/home/browse"><img src="/static/images/home.png" alt="HOME"> HOME</a>
                  </li>
               </c:if>
               <li
               <c:if test="${navactivebutton == 'find-it'}"> class="active"</c:if>
               >
               <a href="/ads/browse/for_sale" title="Find items or services for sale or barter"><img src="/static/images/exchange.png" alt="Classifieds in ${user.getTblKaguLocation().getCity()}"> EXCHANGES</a>
               </li>
               <li
               <c:if test="${navactivebutton == 'attend-it'}"> class="active"</c:if>
               >
               <a class="nav-a" href="/events/browse" title="Find events in your local community"><img src="/static/images/attendit.png" alt="Browse Local Events in ${user.getTblKaguLocation().getCity()}"> EVENTS</a>
               </li>
               <li
               <c:if test="${navactivebutton == 'discuss-it'}"> class="active"</c:if>
               >
               <a class="nav-a" href="/discussions/browse/categories" title="Start a discussion, or join one" ><img src="/static/images/discussion.png" alt="Discuss Topics in ${user.getTblKaguLocation().getCity()}"> DISCUSSIONS</a>
               </li>
            </ul>
            <c:if test="${user.isLoggedIn() == false }">
               <div> 
                  <button class="btn btn-danger pull-right" onclick="changeZip(true);">Change My Location</button>
               </div>
            </c:if>
            <c:if test="${user.isLoggedIn() == true}">
               <div id="userdropdown" class="btn-group pull-right" title="Manage your events or ads, or change your account settings">
                  <a class="btn dropdown-toggle" data-toggle="dropdown" href="javascript:void(null)">
                  <i class="icon-user"></i> 
                  ${user.getFirstName()} in
                  ${user.getTblKaguLocation().getCity()} <span class="caret"></span>
                  </a>
                  <ul class="dropdown-menu">
                     <li><a href="/ads/manage/browse" title="Manage your services and items for sale">Manage Your <strong> AD IT</strong> Posts</a></li>
                     <li><a href="/events/manage/browse" title="View, edit, renew, and delete your events">Manage Your <strong> EVENT IT</strong> Posts</a></li>
                     <li class="divider"></li>
                     <li><a href="/accountsettings" title="">Account Settings</a></li>
                     <li class="divider"></li>
                     <li><a href="<c:url value="j_spring_security_logout" />" title="">Sign Out</a></li>
                  </ul>
               </div>
               
                    <c:if test="${inboxmessgecount gt 0}">
                    <a href="/home/browse#skip_to_notifications" id="new_message_count" data-placement="bottom" title="Total Notifications" style="margin: 10px 10px 0px 0px;" class="badge badge-important pull-right"><small>${inboxmessgecount}</small></a>
                    </c:if>
               <ul class="nav pull-right" >
                  <li class="divider-vertical"></li>
                 
                  <li class="divider-vertical"></li>
               </ul>
            </c:if>
         </div>
      </div>
   </div>
</header>
<script src="/static/jquery/ask-zipcode.js" type="text/javascript"></script>
<script type="text/javascript">
   
   $.cookie('time_zone', (new Date().getTimezoneOffset() * -1).toString(), { expires: 7, path: '/'});
   $.cookie('iPhone', (new Date().getTimezoneOffset() * -1).toString(), { expires: 7, path: '/'});
   
   $(document).ready(function() {
    <c:if test='${ask_zip}'>
        askZip(${ask_zip});
    </c:if>
   
   // Start sending and getting notifications
   
   <c:if test="${user.isLoggedIn()}">
       window.setTimeout(getNotifications, 500);
       window.setInterval(getNotifications, 30000);
  </c:if>
   
   });
   
</script>