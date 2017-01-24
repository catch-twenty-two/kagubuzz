<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="template_name" required="true" type="java.lang.String"%>
<%@ attribute name="title" required="true" type="java.lang.String"%>
<%@ attribute name="placement" required="false" type="java.lang.String"%>

<a href="#" rel="popover" 
 data-toggle="popover" 
 title="${title}" 
 <c:if test="${not empty placement}"> data-placement="${placement}"</c:if>
 data-content='${template_service.getTemplatePopver(template_name)}'>
 <i class="icon-question-sign"></i></a>
