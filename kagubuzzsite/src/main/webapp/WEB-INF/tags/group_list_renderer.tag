<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="activitiesList" required="true" type="java.util.Collection"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test="${not empty activitiesList}">
   <c:forEach var="activity" items="${activitiesList}">
      <div class="list_padding">
         <kagutags:message_render 
            tags="${activity.getTags()}"
            showTitle="Y"
            summarize="200"
            imessage="${activity}"            
            ajaxurl="/discussion/public/"                              
            optionflag="Y" 
            optionbuzzit="Y"
            optionmoreinfo="Y"/>
      </div>
   </c:forEach>
</c:if>