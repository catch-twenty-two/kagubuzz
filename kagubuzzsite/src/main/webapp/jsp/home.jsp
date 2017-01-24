<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page trimDirectiveWhitespaces="true" %>
<!DOCTYPE html>
<html lang="en">
   <head>
      <meta charset="utf-8" />
      <title>Kagu Buzz - Your Local Community Your Way</title>
      <kagutags:bootstrap_css/>
      <link rel="stylesheet" href="/static/css/main.css" type="text/css" />
      <link rel="stylesheet" href="/static/css/home.css" type="text/css" />
      <link rel="stylesheet" href="/static/css/rateit.css" type="text/css" />
      <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
      <!--[if lt IE 9]>
      <script src="https://html5shim.googlecode.com/svn/trunk/html5.js"></script>
      <![endif]-->
   </head>
   <body>
      <kagutags:jquery/>
      <kagutags:bootstrap_js/>
      <script src="/static/jquery/jquery.form.js" type="text/javascript"></script>
      <script src="/static/jquery/textarea-limit.js" type="text/javascript"></script>
      <script src="/static/jquery/createnewmessage.js" type="text/javascript"></script>
      <script src="/static/jquery/jquery.rateit.min.js" type="text/javascript"></script>
      <script src="/static/jquery/postoptionclick.js" type="text/javascript"></script>
      <div class="wrapper">
         <kagutags:header navactivebutton="home" headerpic="smallheader.jpg"/>
         <div class="main-content">
            <div class="buzzinfobox notitle" style="padding: 5px;">
               <div class="pull-left following-box">
                  <img class="img-polaroid pull-left" style="height: 60px; width: 60px; margin-right: 15px;" alt="${user.getFirstName()}" src="/fileServerServlet?type=GetAvatarByUserId&amp;id=${user.getId()}"/>
                  <span class="textlarge pull-left" >Welcome<br> ${welcome_message}<br> ${user.getFirstName()}!</span>
               </div>
               <div>
                  <small class="muted"><i>You Are Currently Receiving Updates From</i></small>
                  <ul style="list-style: none;">
                     <c:forEach var="following" items="${following_users}">
                        <li class="pull-left following">
                           <a href="javascript:void(null)" onclick="showUserProfile(${following.getId()})">
                           <img  class="img-polaroid" alt="${following.getFirstName()}" src="/fileServerServlet?type=GetAvatarByUserId&amp;id=${following.getId()}" style="height: 30px; width: 30px;" /><br>
                           <small>${following.getFirstName()}</small>
                           </a>                                     
                        </li>
                     </c:forEach>
                     <c:if test="${not empty user_overflow}">
                        <li class="pull-left following stitched">                          
                           <a href="/accountsettings"><small class="text-success"><i>And<br>${user_overflow}<br>More</i></small></a>
                        </li>
                     </c:if>
                  </ul>
               </div>
               <div class="clearfix"></div>
            </div>
            <div class="alert alert-success" style="margin-bottom: 10px; padding-right: 5px;">            
               <button id="autoadsbtn" class="btn btn-mini pull-left" data-toggle="collapse" data-target="#autosearchads">Set up my Ad Search</button>
                <span class="pull-right"><kagutags:pop_over title="Ad search" template_name="adSearch"/></span>
               <c:if test="${adsearchhits > 0}">
                  <a href="/ads/browse/autosearch" class="pull-right black">Show Matches</a>
                  <div class="clearfix"></div>
                  <strong id="ad_search_hits"> You have <span class="texthuge">${adsearchhits}</span> FIND IT search matches </strong>
               </c:if>
               <div class="clearfix"></div>
               <div id="autosearchads" class="collapse">
                  <hr class="kagu-hr black">
                  <p><span>Notify me when someone posts an Ad for either of these two things in <strong>FIND IT</strong></span>  
                  </p>
                  <form id="adautosearchform" action="/saveadsearch" method="POST" class="form-inline">
                     <c:choose>
                        <c:when test="${not empty user.getSearchableKeyWordList()}">                                
                           <input type="text" class="input-large"  name="searchterm1" value="${user.getSearchableKeyWordList().getAdKeyword1()}" placeholder="2010 Honda Civic">                                
                           <input type="text" class="input-large"  value="${user.getSearchableKeyWordList().getAdKeyword2()}" name="searchterm2" placeholder="Macbook Pro 2007">                               
                        </c:when>
                        <c:otherwise>
                           <input type="text" class="input-large"  name="searchterm1"  placeholder="2010 Honda Civic"> 
                           <input type="text" class="input-large"  name="searchterm2" placeholder="Macbook Pro 2007">
                        </c:otherwise>
                     </c:choose>
                     <button id="adsearchsave" type="submit" class="btn" data-loading-text="Loading..." autocomplete="off">Save Search</button>                             
                  </form>
               </div>
            </div>
            <div class="alert alert-info" style="margin-bottom: 10px;  padding-right: 5px;">
               <button id="autoeventsbtn" class="btn btn-mini pull-left" data-toggle="collapse" data-target="#autosearchevents">Set up my Event Search</button>
                <span class="pull-right"><kagutags:pop_over title="Event search" template_name="eventSearch"/></span>
               <c:if test="${eventsearchhits > 0}">
                  <a  href="/events/browse/autosearch" class="pull-right black"> Show Matches</a>               
                  <div class="clearfix"></div>
                  <strong id="event_search_hits"> You have <span class="texthuge">${eventsearchhits}</span> ATTEND IT search matches </strong>       
               </c:if>
               <div class="clearfix"></div>
               <div id="autosearchevents" class="collapse">
                  <hr class="kagu-hr">
                  <p><span>Notify me when someone posts an Event for either of these two things in <strong>ATTEND IT</strong></span></p>
                  <form id="eventautosearchform" action="/saveeventsearch" method="POST" class="form-inline">
                     <c:choose>
                        <c:when test="${not empty user.getSearchableKeyWordList()}">                                
                           <input type="text" class="input-large"  name="searchterm1" value="${user.getSearchableKeyWordList().getEventKeyword1()}" placeholder="Highschool Football">                              
                           <input type="text" class="input-large"  value="${user.getSearchableKeyWordList().getEventKeyword2()}" name="searchterm2" placeholder="Jazz">                                
                        </c:when>
                        <c:otherwise>
                           <input type="text" class="input-large"  name="searchterm1"  placeholder="Highschool Football"> 
                           <input type="text" class="input-large"  name="searchterm2" placeholder="Jazz">
                        </c:otherwise>
                     </c:choose>
                     <button type="submit" class="btn" data-loading-text="Loading..." autocomplete="off">Save Search</button>                           
                  </form>
               </div>
            </div>
            <kagutags:infoboxtop cssclass="buzzinfoboxtitle homecommentbox" icon="ratingstar.png" title="Bookmarks, Inquiries And Ratings"/>
            <div id ="bookmarks" style="margin-top: -5px;">
            <c:if test="${not empty dateFloor}">
               <span class="pull-right"><i><small class="text-warning">Your Ratings and Feedback</small></i>
                <kagutags:pop_over title="Bookmarks, Inquiries And Ratings" template_name="bookMarks"/></span>
                  <span class="text-warning">
                     <i>
                        <small>
                           <fmt:formatDate pattern="MMM d" value="${formatter.gmtDateToTzDate(dateCeiling, user.getTimeZoneOffset())}" />
                           to 
                           <fmt:formatDate pattern="MMM d" value="${formatter.gmtDateToTzDate(dateFloor, user.getTimeZoneOffset())}" />
                        </small>
                     </i>
                  </span>
               </c:if>
               <hr class="kagu-hr">
               <div style="max-height:250px; overflow: scroll; overflow-x: hidden;" >
                  <table  class="table table-condensed table-striped table-hover global-margin-bottom">
                     <tbody>
                        <c:forEach var="bookmark" items="${bookmarks}">
                           <tr class="${bookmark.getBookMarkCSS()}">
                              <td>
                                 <c:if test="${bookmark.bookmarkCanBeDeleted()}">
                                    <a onclick="deleteBookmark('${bookmark.deleteBookMarkURL()}', '${bookmark.getId()}', this)"><i  class="icon-remove" style="line-height: 14px; text-align: center;"></i></a>
                                 </c:if>
                              </td>
                              <td>
                                 <small >
                                    <fmt:formatDate pattern="MMM d" value="${formatter.gmtDateToTzDate(bookmark.bookmarkRelevantDate(), user.getTimeZoneOffset())}" />
                                 </small>
                              </td>
                              <td><i class="${bookmark.bookmarkIcon()}"></i></td>
                              <td><small><strong><a class="darkgrey" href="${bookmark.getViewingURL()}">${formatter.getSummary(bookmark.getTitle(), 55)} </a></strong></small></td>
                              <td>
                                 <kagutags:home_ratings bookmark="${bookmark}"/>
                              </td>
                           </tr>
                        </c:forEach>
                     </tbody>
                  </table>
               </div>
            </div>
            <kagutags:infoboxbottom/>
            <div class="clearfix"></div>
            <a class="anchor-offset" id="skip_to_notifications"></a>
            <kagutags:infoboxtop cssclass="buzzinfoboxtitle homemessagebox" collapsed="flase" icon="annoucement.png" collapseable="true" title="Notifications "/>
            <span class="pull-right"><kagutags:pop_over title="Notifications" template_name="notifications"/></span>
            <ul class="nav nav-tabs" style="margin-bottom: 0px;">
               <c:forEach var="notification_type" items="${notifcation_types}">
                  <li class="${notification_type.getEnumExtendedValues().isActive()}">
                     <a href="#tab_${notification_type.name()}" id="${notification_type.name()}" data-toggle="tab">
                     <i class="${notification_type.getIcon()}"></i> ${notification_type.getEnumExtendedValues().getDescription()} 
                     </a>
                  </li>
               </c:forEach>
             
            </ul>

            <div  class="buzzinfobox notitle" style="border-top-width: 0px; padding-bottom: 5px;
               border-radius: 0px 0px 5px 5px;">
               <div id="messagecontainer" style="  margin-top: -5px; padding: 5px 5px 20px 5px;">
                  <c:forEach var="message" items="${inboxmessages}">
                     <c:choose>
                        <c:when test="${message.isSystemMessage()}">
                           <kagutags:notifications_message_renderer sender="${system_user}" message="${message}"/>
                        </c:when>
                        <c:otherwise>
                           <kagutags:notifications_message_renderer sender="${message.getSender()}" message="${message}"/>
                        </c:otherwise>
                     </c:choose>
                  </c:forEach>
                  <div id="new_messages"></div>
                  <c:if test="${show_next == true}">
                     <div style="text-align: center;"><button id="${next_page}_${group}" onclick="loadMoreNotifications('${next_page}_${group}')" class="btn-small btn btn-primary global-margin-top">Load More Notifications</button></div>
                  </c:if>
               </div>
            </div>
            <kagutags:infoboxbottom/>
         </div>
         <div class="side-bar">
            <a href="/login"><img src="/static/images/kagutitlesmall.png" alt="Landing Page"/></a>           
            <kagutags:weather/>
            <hr class="kagu-hr">
            <!-- <kagutags:search/> -->
            <c:if test="${not empty suggested_events}">
               <kagutags:infoboxtitle cssclass="buzzinfoboxtitle title-simple" title='Events Buzzing Near You'/>
               <p>
               <p>${eventsuggestion}</p>
               <hr class="kagu-hr">
               <ol>
                  <c:forEach var="event" items="${suggested_events}">
                     <li>
                        <a class="darkgrey" href="/event/view/${event.getId()}/${event.getFriendlyURL()}">
                        <small>${event.getTitle()}</small>
                        </a>
                        <hr class="kagu-hr">
                     </li>
                  </c:forEach>
               </ol>
            </c:if>
            <kagutags:adsense_300x250/>
            <kagutags:infoboxtitle cssclass="buzzinfoboxtitle title-simple" title='Forum Topics Buzzzing In ${user.getTblKaguLocation().getCity()}'/>
            <ol>
               <c:forEach var="discussion" items="${discussionList}">
                  <li>
                     <a class="darkgrey" href="/discussion/public/view/${discussion.getId()}/${discussion.getFriendlyURL()}">
                     <small>${discussion.getFirstMessageInThread().getTitle()}</small>
                     </a>
                     <hr class="kagu-hr">
                  </li>
               </c:forEach>
            </ol>
         </div>
         <kagutags:footer/>
      </div>
      <script type="text/javascript">
      
      $("a[rel=popover]").popover({trigger:'hover'});
         var protectRemove = false;
         
         function deleteBookmark(url, id, rowElement) {
        	 $.ajax({
                 type: 'POST',
                 url:  url + '/bookmark/delete',
                 data: { id: id},
                 dataType: 'html',
                 success: function(data) {
                	 var row = $(rowElement).parent().parent();
                	 row.animate({opacity: 0},'slow',null,function(){ row.remove()});
                 }
        	 });        	         
         }
         
         function showUserProfile(userId) {         
             $('#modal_dialog').load('/modal/userprofile/' + userId, function() {$('#modal_dialog').modal('show');});
         }
         
         function rateEvent(eventId, rating) { 
             $('#modal_dialog').load('/modal/rate_event/'+ eventId + '/' + rating, function() {$('#modal_dialog').modal('show');});
         }
         
         $(document).ready(function() {
        	 
          <c:if test='${new_account}'>
               changeZip();
          </c:if>
            
          $('button[data-loading-text]').click(function () {
                $(this).button('loading');
            });
         
             $('#eventautosearchform').ajaxForm({dataType:  'json',success: displayAjaxServerMessageEvent});
             $('#adautosearchform').ajaxForm({dataType:  'json', success: displayAjaxServerMessageAd});
             
             function displayAjaxServerMessageEvent(data) {
                 $('button[data-loading-text]').button('reset');
                 renderServerNotification(data);
                 $('#event_search_hits').html("");
                 $('#autosearchevents').collapse('hide');
             }
             
             function displayAjaxServerMessageAd(data) { 
                 $('button[data-loading-text]').button('reset');
                 renderServerNotification(data);
                 $('#ad_search_hits').html("");
                 $('#autosearchads').collapse('hide');
             }
             
         }); 
         
         function removeMessage(messageType, id) {
             
             if(protectRemove) return;
             
             protectRemove = true;
             
             var message = $('#' + messageType + id);
                         
             $.ajax({
                 type: "POST",
                 url:  'close_notification',
                 data: { mType: messageType, id: id},
                 dataType: "html",
                 success: function(data) {
                     
                     message.css('opacity', 0);  
                     message.css('padding', 0);
                     message.css('margin', 0);
                     message.animate({height: 0},'slow',null,function(){message.remove()});
                       
                     var newMsgHeight = message.height();
                     var newMessage = $(data);
                     var messageCount = $('#new_message_count').text() - 1;
                     
                     if(messageCount >= 0) {
                         $('#new_message_count').text(messageCount); 
                     }            
                     
                     if(data == 'false') {
                         protectRemove = false;
                         return;
                     }
                     
                     newMessage.css('opacity', 0);
                     newMessage.css('height', 0);
                     newMessage.appendTo('#messagecontainer');
                     newMessage.animate({height: newMsgHeight},'slow');
                     newMessage.animate({opacity: 1},'fast',null, function(){protectRemove = false;});           
                 }
                 
             });
         }
         
         function loadMoreNotifications(id) {    
          
          $('#' + id).remove();
          
          var id = id.split("_");
          var next_page = id[0];
          var group = id[1];
          
             $.ajax({
                 type: "GET",
                 url:  'load_more_notifications',
                 data:{group: group, next_page : next_page},
                 dataType: "html",
                 success: function(data) {                        
                    
                      if(data == 'false') { return; }
                      var newMessage = $(data);
                      newMessage.appendTo('#new_messages');            
                 }  
             });             
         } 
         
         $('a[data-toggle="tab"]').on('shown', function (e) {    
           $.ajax({
                  type: "GET",
                  url:  'reload_notifications',
                  data:{group: e.target.id},
                  dataType: "html",
                  success: function(data) {   
                      
                       $('#messagecontainer').children().remove();
                       $('#messagecontainer').css('opacity', 0);
                       if(data == 'false') {
                           data = '<div style="text-align: center"> <h2>No New Notifications</h2><div>';
                        }
                       $('#messagecontainer').html(data);
                       $('#messagecontainer').animate({opacity: 1},'slow');            
                  }  
              });             
         });                     
      </script>
   </body>
</html>