<%@ attribute name="title" required="true" type="java.lang.String" description="Text to use in the title" %>
<%@ attribute name="cssclass" required="true" type="java.lang.String" description="Banner color" %>
<%@ attribute name="icon" required="false" type="java.lang.String" description="Icon in title" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>

<div class="${cssclass}" style="margin-bottom:10px;">
	<div class="buzzinfoboxtitlepadding">
		<c:choose>
			<c:when test="${collapseable == 'true'}">
				<a class="black" data-toggle="collapse" href="#${title.hashCode()}">${title} </a>
			</c:when>
			<c:otherwise>
				${title}
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${empty icon}">
				<c:if test="${not empty title}">
					<img src="/static/images/abee.png" alt="${title}"/>
				</c:if>
			</c:when>
			<c:otherwise>
				<i class="${icon}"></i>
			</c:otherwise>
		</c:choose>
	</div>
</div>

