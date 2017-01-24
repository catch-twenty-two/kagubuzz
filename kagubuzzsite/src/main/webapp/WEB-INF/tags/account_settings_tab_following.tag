<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<script type="text/javascript">
   function showUserProfile(userId) {         
       $('#modal_dialog').load('modal/userprofile/' + userId, function() {$('#modal_dialog').modal('show');});
   }
</script>
<div class="tab-pane fade" id="following">
   <kagutags:infoboxtitle cssclass="buzzinfoboxtitle title-simple yellow left" icon="none" title=' You Are Currently Receiving Updates From' />
   <div class="clear-background buzzinfobox notitle" style="padding: 10px;">
      <ul style="list-style: none;">
         <c:forEach var="following" items="${user.getFollowing()}">
            <li class="pull-left following">
               <a href="javascript:void(null)" onclick="showUserProfile(${following.getId()})"> 
               <img class="img-polaroid" alt="" src="/fileServerServlet?type=GetAvatarByUserId&amp;id=${following.getId()}" width=50 height=50 />
               <br>
               <small>${following.getFirstName()}</small>
               </a>
            </li>
         </c:forEach>
      </ul>
      <div class="clearfix"></div>
   </div>
</div>