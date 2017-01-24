<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="user_profile" required="true" type="com.kagubuzz.datamodels.hibernate.TBLUser" %>
<div>
   <div class="pull-right" style="text-align:center;">
      <div class="well well-small global-margin-bottom" style="text-align:center; min-height: 90px; min-width: 140px;">
         <strong>Reputation</strong>
         <h1 class="muted">${user_service.getUserReputation(user_profile)}</h1>
      </div>
      <div class="pull-left" 
         style="background-image: url('/static/images/halo_bee.png'); background-repeat:repeat-x; margin-right: 10px; height:27px; width: ${user_service.getTrustLevel(user_profile)}px;">
      </div>
   </div>
   <div class="pull-left" style="text-align:center;">
      <div class=" img-polaroid" style="display: table; height: 100px;">
         <div style="display: table-cell; vertical-align: middle; overflow: hidden;">
            <img class="avatar-img-size-100x100" src="/fileServerServlet?type=GetAvatarByUserId&amp;id=${user_profile.getId()}" alt="User Profile"/>
         </div>
      </div>
      <div class="global-margin-top">
         <strong>${user_profile.getFirstName()}</strong>
      </div>
   </div>
   <div class="clearfix"></div>
   <hr class="kagu-hr">
   <span class="pull-left"><small>Email</small></span><span class="pull-right"><i class="icon-ok"></i> <small>Verified</small></span>
   <div class="clearfix"></div>
   <c:if test="${user_profile.isSocialAccount()}">
      <span class="pull-left"><small>${formatter.capitalizeFirstLetter(user_profile.getSocialUserAccount().getProviderId())}</small></span><span class="pull-right"><i class="icon-ok"></i> <small>Verified</small></span>      
      <div class="clearfix"></div>
   </c:if>
   <c:if test="${user_profile.getPhoneVerified()}">
      <span class="pull-left"><small>Phone Number</small></span><span class="pull-right"><i class="icon-ok"></i> <small>Verified</small></span>
      <div class="clearfix"></div>
   </c:if>
   <hr class="kagu-hr">
   Buzzing since 
   <fmt:formatDate value="${user_profile.getCreationDate()}" pattern="MMM dd, yyyy"/>
   <br />
   ${user_profile.getTblKaguLocation().getCity()}, ${user_profile.getTblKaguLocation().getState()}
   <br />   
   <hr class="kagu-hr">
   <span>Recommendations ${user_service.getUserRecommendationCount(user_profile)}</span>
   <c:if test="${is_admin == true}">
    <br />
      <strong class="text-error">Kagu Buzz Adminstrator</strong>
   </c:if>
   <br>
   <a href="/accountfeedback/${user_profile.getId()}">View ${user_profile.getFirstName()}'s Feedback</a>
</div>
<c:if test="${is_admin == true}">
   <hr class="kagu-hr">
   <div>
      <a href="http://www.squareup.com"><img src="/static/images/squareuplogo.jpeg" alt="" style="width:50px;"/></a>
      <a href="http://www.paypal.com"><img src="/static/images/paypallogo.gif" alt="" style="width:50px;"/></a>
      <a href="http://en.wikipedia.org/wiki/United_States_dollar"><img src="/static/images/cash.png" alt=""  style="width:50px;"/></a>
      <a href="http://omnui.com/Home/About"><img src="http://omnui.com/Images/stick-person-helping-others.gif" alt=""  style="width:50px;"/></a>
   </div>
</c:if>
<c:if test="${user.isLoggedIn() && !user.equals(user_profile)}">
         <hr class="kagu-hr">
   <c:choose>
      <c:when test="${following}">
         <button autocomplete="off" id="btn_follow" class="btn btn-block btn-danger" onclick="userFollow('${user_profile.getId()}', false);" type="button">Stop Following ${user_profile.getFirstName()}'s Posts</button>
      </c:when>
      <c:otherwise>
         <button autocomplete="off" id="btn_follow"  class="btn btn-block btn-primary" onclick="userFollow('${user_profile.getId()}', true);" type="button">Follow ${user_profile.getFirstName()}'s Posts</button>
      </c:otherwise>
   </c:choose>   
   <button autocomplete="off" id="btn_reccomend" class="btn btn-block btn-success" type="button">Recommend ${user_profile.getFirstName()}</button>
</c:if>
<script type="text/javascript">

   <c:if test="${reccomended}">
   $('#btn_reccomend').addClass('disabled');
   $('#btn_reccomend').attr("disabled", "disabled");
   </c:if>   

   $('button[data-loading-text]').click(function () {
       $(this).button('loading');
   });
   
   $('#btn_reccomend').click(function(){
	   
	   $(this).addClass('disabled');
	   $(this).attr("disabled", "disabled");
	   
	   $.ajax({type: "POST",
		       url:  '/userreccomend',
		       data: { userId: ${user_profile.getId()}},
		       dataType: "json",
		       success: function(data) { 
		    	    renderServerNotification(data);           
		    	}
         });
   });
   
   function userFollow(userId, follow) {
     $("#btn_follow").addClass('disabled');
     $("#btn_follow").attr("disabled", "disabled");
     $.ajax({
            type: "POST",
            url:  '/userfollow',
            data: { userId: userId , follow: follow},
            dataType: "json",
            success: function(data) { 
             $('button[data-loading-text]').button('reset');
             renderServerNotification(data);
            
            }
            });
   }
   
</script>