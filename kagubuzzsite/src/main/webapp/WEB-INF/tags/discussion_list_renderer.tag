<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="discussions_list" required="true" type="java.util.Collection"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test="${not empty discussions_list}">
   <c:forEach var="discussion" items="${discussions_list}">
      <div class="list_padding">
         <kagutags:message_render 
            showsubject="N"
            summarize="200"
            imessage="${discussion.getFirstMessageInThread()}"
            message="${discussion.getFirstMessageInThread()}"
            ajaxurl="/discussion/public/" 
            display_alternate="${discussion.getLastUpdated(user.getTimeZoneOffset())}"                        
            optionflag="Y" 
            optionbuzzit="Y"
            optionmoreinfo="Y"/>
      </div>
   </c:forEach>
</c:if>