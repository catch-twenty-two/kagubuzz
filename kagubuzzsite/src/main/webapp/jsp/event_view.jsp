<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<%@ page trimDirectiveWhitespaces="true" %>
<!DOCTYPE html>
<html lang="en">
   <head>
      <kagutags:bootstrap_css/>
      <link rel="stylesheet" href="/static/css/main.css" type="text/css" />
      <link rel="stylesheet" href="/static/css/rateit.css" type="text/css" />
      <meta charset="utf-8" />
      <title>${event.getTitle()} in ${event.getTblKaguLocation().getCity()}, ${event.getTblKaguLocation().getState()}</title>
   </head>
   <body>
      <kagutags:jquery/>
      <kagutags:bootstrap_js/>
      <script src="/static/jquery/createnewmessage.js" type="text/javascript"/></script>
      <script src="/static/jquery/postoptionclick.js" type="text/javascript"/></script>
      <script src="/static/jquery/textarea-limit.js" type="text/javascript"/></script>
      <script src="/static/jquery/jquery.rateit.min.js" type="text/javascript"/></script>
 	  <kagutags:add_this />
      <div class="wrapper">
         <kagutags:header navactivebutton="attend-it" headerpic="smallheader.jpg"></kagutags:header>
         <div class="main-content clear-background">
            <div class="buzzinfobox notitle">
               <kagutags:category_bread_crumbs   
                  breadcrumbs="${breadcrumbs}" 
                  showuserpostoptions="Y" 
                  imessage="${event}" 
                  ajaxurl="/event/" 
                  breadcrumb_url="events"
                  optionbookmark="Y" 
                  optiondiscussit="Y" 
                  optionbuzzit="Y" 
                  optionflag="Y" />
            </div>
            <div class="buzzinfobox notitle" 
            <c:if test="${not empty event.getBackgroundImageName()}">
               style="background-image: url('${backgroungImageURL}');" 
            </c:if>
            >
            <div class="post-box-padding">
               <div class="rateit pull-right" data-rateit-value="${event_rating}" data-rateit-readonly="true"><small class="muted"><i>(${rating_count})</i></small></div>
               <h3>${event.getTitle()}</h3>
               <strong>
                  <span class="pull-right">
                     <kagutags:price no_compensation="Free" price="${event.getPrice()}"></kagutags:price>
                  </span>
               </strong>
               <c:choose>
                  <c:when test="${empty repeats}">
                     <fmt:formatDate pattern="EEEEEEEEE, MMMMMMMM, dd, yyyy h:mm a" value="${formatter.gmtDateToTzDate(event.getStartDate(), user.getTimeZoneOffset())}"/>
                     <c:choose>
                        <c:when test="${event.startsAndEndsSameDay(user.getTimeZoneOffset())}">
                           to 
                           <fmt:formatDate pattern="h:mm a" value="${formatter.gmtDateToTzDate(event.getEndDate(), user.getTimeZoneOffset())}"/>. 
                        </c:when>
                        <c:otherwise>
                           to 
                           <fmt:formatDate pattern="EEEEEEEEE, MMMMMMMM, dd, yyyy" value="${formatter.gmtDateToTzDate(event.getEndDate(), user.getTimeZoneOffset())}"/>.
                        </c:otherwise>
                     </c:choose>
                      <c:if test="${event.isExpired()}"><a href="/events/search?keywords=${event.getTitle()}" class="label label-important">This Event Has Already Occurred</a></c:if>
                     <br> 
                     (Takes place for ${event.getEventDuration().getEnumExtendedValues().getDescription()}.)
                    
                  </c:when>
                  <c:otherwise>
                     Takes place ${repeats} for ${event.getEventDuration().getEnumExtendedValues().getDescription()}. 
                     <br> 
                     Next event is on 
                     <strong>
                        <fmt:formatDate pattern="EEEEEEEEE, MMMMMMMM, dd, yyyy" value="${formatter.gmtDateToTzDate(event.getStartDate(), user.getTimeZoneOffset())}"/>
                     </strong>
                     . 
                  </c:otherwise>
               </c:choose>
                <c:if test="${!event.isExpired()}"><a href="/fileServerServlet?type=GetEventIcs&id=${event.getId()}"><img alt=""  src="/static/images/calendar.png"/></a></c:if>                      
               <br>
               ${event.getAddress()}
               <br>                                              
               <div class="clearfix"></div>
               <hr class="kagu-hr">
               <div style="overflow: auto;">
                  <span>${event.getBody()}</span>
               </div>
               <div class="clearfix"></div>
               <c:if test="${not empty event.getLastUpdated() }">
                  <small>
                     Last Updated 
                     <fmt:formatDate pattern="EEEEEEEEE, MMMMMMMM, dd, yyyy h:mm a" value="${formatter.gmtDateToTzDate(event.getLastUpdated(), user.getTimeZoneOffset())}"/>
                  </small>
               </c:if>
            </div>
         </div>
         <c:if test="${not empty feedback}">
            <div id="feedback">
               <kagutags:infoboxtitle cssclass="buzzinfoboxtitle title-simple yellow left" icon="ratingstar.png"  title="Reviews"/>
               <c:forEach items="${feedback}" var="feedback">
                  <kagutags:message_render
                     optionremove="Y"
                     rating="${feedback.getRating()}"
                     imessage="${feedback}"
                     optionbuzzit="Y"       
                     optionflag="Y"/>
               </c:forEach>
            </div>
         </c:if>
         <c:if test="${empty addevent}">
            <c:choose>
               <c:when test="${user.isLoggedIn()}">
                  <kagutags:infoboxtitle cssclass="buzzinfoboxtitle title-simple yellow left" icon="none" title="Comments And Questions"/>
                  <kagutags:message_create
                     save_button_message="Post A Comment or Ask a Question"
                     messageid="${event.getId()}"
                     messageboxmaxchars="${event.messageType().getMaxLength()}"
                     parentid="${event.getId()}"
                     ajaxurl="/event/comment/"
                     domattachid="comments"                
                     messageboxinitval="Is something on your mind? Add a comment or ask a question...Please keep it clean, or your comment or question may be flagged for removal."
                     sender="${user}"/>
               </c:when>
               <c:otherwise>
                  <kagutags:infoboxtitle cssclass="buzzinfoboxtitle title-simple yellow left" icon="none" title="Comments And Questions (Sign In To Add a Comment)"/>
               </c:otherwise>
            </c:choose>
            <hr class="kagu-hr">
            <div id="comments">
               <c:forEach items="${comments}" var="comment">
                  <kagutags:message_render
                     optionremove="Y"
                     imessage="${comment}"
                     ajaxurl="/event/comment/"
                     optionbuzzit="Y"   
                     optionflag="Y"/>
               </c:forEach>
            </div>
         </c:if>
         <div class="global-margin-top">
            <c:if test="${not empty addevent}">
               <form method="post" action="/event_create">
                  <button type="submit" class="btn btn-primary btn-danger pull-left"><i class="icon-chevron-left icon-white"></i> Not Quite Done</button>
               </form>
               <form method="post" action="/eventpost">
                  <button type="submit" class="btn btn-success pull-right">Looks Good Post It! <i class="icon-chevron-right icon-white"></i></button>   
               </form>
               <div class="clearfix"></div>
            </c:if>
         </div>
      </div>
      <div class="side-bar">
         <a href="/login"><img alt="" src="/static/images/kagutitlesmall.png"/></a>
         <div id="contentpreviewaddress">
            <kagutags:infoboxtitle cssclass="buzzinfoboxtitle title-simple left" icon="none" title='Event Location'/>
            <kagutags:google_maps
               showdirectionsonload="Y"
               save_lat_long_url="${save_lat_long_url}"
               post_id="${event.getId()}"
               zoom="13" 
               address="${event.getAddress()}"/>
            <hr class="kagu-hr">
         </div>
         <c:if test="${not empty event.getSideBarImageName()}">
            <ul class="thumbnails centeredImage" style="width:100%; ">
               <li style="margin-top: 3px; margin-bottom: 0px; text-align: center; width:100%">
                  <div class="thumbnail" style="max-height: 250px; overflow: hidden; padding:3px;">
                     <img src="${sideBarImageURL}" alt="">
                  </div>
               </li>
            </ul>
         </c:if>
          <div class="buzzinfobox notitle post-box-padding">
         <kagutags:account_profile user_profile="${event.getOwner()}"/>
         </div>
         <kagutags:adsense_300x250/>
      </div>
      <kagutags:footer/>
      </div>
      <script type="text/javascript">
      $(document).ready(function() {       
      <c:if test="${event.isExpired()}">
      $('#modal_dialog').load('/modal/event_expired/${event.getId()}', function() { $('#modal_dialog').modal('show');});
      </c:if>
      });
      </script>
   </body>
</html>
<kagutags:scroll_to_message/>