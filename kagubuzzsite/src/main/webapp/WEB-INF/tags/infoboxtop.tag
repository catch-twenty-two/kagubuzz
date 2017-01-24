<%@ attribute name="title" required="false" type="java.lang.String" description="Text to use in the title"%>
<%@ attribute name="cssclass" required="true" type="java.lang.String" description="Banner color"%>
<%@ attribute name="icon" required="false" type="java.lang.String" description="Icon in title"%>
<%@ attribute name="collapseable" required="false" type="java.lang.String"%>
<%@ attribute name="collapsed" required="false" type="java.lang.String"%>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="${cssclass}">
	<div class="buzzinfoboxtitlepadding">
		<c:choose>
			<c:when test="${collapseable == 'true'}">
				<a class="black" data-toggle="collapse" href="#${title.hashCode()}">${title}</a>
			</c:when>
			<c:otherwise>
				${title} 
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${empty icon}">
				<c:if test="${not empty title}">
					<img  src="/static/images/abee.png" alt=""/>
				</c:if>
			</c:when>
			<c:otherwise>
				<img  src="/static/images/${icon}" alt="${title}"/>
			</c:otherwise>
		</c:choose>
	</div>
</div>

<div id="${title.hashCode()}" class="buzzinfobox">
    <div class="buzzinfoboxpadding clear-background" >