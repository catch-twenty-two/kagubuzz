<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>

<c:forEach var="discussion_tag" items="${discussion_tags}">

	<a href="#${discussion_tag.getName()}" 
	onclick="notifyAdded('${discussion_tag.getName()}')" 
	class="myTagBreadCrumb">${discussion_tag.getName()}</a>
	
</c:forEach>