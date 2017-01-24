<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="price" required="true" type="java.lang.String"%>
<%@ attribute name="no_compensation" required="true" type="java.lang.String"%>

<c:choose >
	<c:when test="${price != 0}">
	<fmt:setLocale value="en_US"/>
	<fmt:formatNumber value="${price}" type="currency"/>
	</c:when>
	<c:otherwise>
	   <span class="kagu-red">${no_compensation}</span>
	</c:otherwise>
</c:choose>
