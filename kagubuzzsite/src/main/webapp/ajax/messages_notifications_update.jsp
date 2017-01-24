<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<c:forEach var="message" items="${inboxmessages}">
<c:choose>
<c:when test="${message.isSystemMessage()}">
     <kagutags:notifications_message_renderer sender="${system_user}" message="${message}" ajaxurl="none"/>
</c:when>                                  
<c:otherwise>
    <kagutags:notifications_message_renderer sender="${message.getSender()}" message="${message}" ajaxurl="none"/>
</c:otherwise>                          
</c:choose>
</c:forEach>
<div id="new_messages"></div>
<c:if test="${show_next == true}">
 <div style="text-align: center;"><button id="${next_page}_${group}" onclick="loadMoreNotifications('${next_page}_${group}')" class="btn-small btn btn-primary global-margin-top">Load More Notifications</button></div>
 </c:if>