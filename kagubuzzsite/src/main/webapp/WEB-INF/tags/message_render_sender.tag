<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>

<%@ attribute name="sender" required="true" type="com.kagubuzz.datamodels.hibernate.TBLUser"%>
<%@ attribute name="imessage" required="true" type="com.kagubuzz.datamodels.JSPMessageRenderer"%>
<%@ attribute name="alert_class" required="false" type="java.lang.String"%>
<%@ attribute name="display_alternate" required="false" type="java.lang.String"%>

<div class="well well-small ${alert_class}">
   <div class="pull-left" style="margin-right: 5px;">
      <a href="javascript:void(null)" onclick="showUserProfile(${sender.getId()})">
        <img class="img-polaroid" style="height: 35px; width: 35px;" src="/fileServerServlet?type=GetAvatarByUserId&amp;id=${sender.getId()}" alt="${sender.getFirstName()}"/>
        </a>
   </div>
    <a href="javascript:void(null)" onclick="showUserProfile(${sender.getId()})"><small><strong>${sender.getFirstName()}</strong></small></a>
   <c:choose>
      <c:when test="${empty display_alternate}">
         <span>
            <small>
               <fmt:formatDate pattern="MMMMMMMMM d, yyyy" value="${formatter.gmtDateToTzDate(imessage.getCreatedDate(), user.getTimeZoneOffset())}"/>
               at
               <fmt:formatDate pattern="h:mm a" value="${formatter.gmtDateToTzDate(imessage.getCreatedDate(), user.getTimeZoneOffset())}"/>
            </small>
         </span>
      </c:when>
      <c:otherwise>
         <span><small> ${display_alternate}</small></span>
      </c:otherwise>
   </c:choose>
   <c:if test="${not empty rating}">
      <div class="pull-right">
         <div style="line-height: 25px" class="rateit" data-rateit-value="${rating}" data-rateit-readonly="true"></div>
      </div>
   </c:if>
</div>