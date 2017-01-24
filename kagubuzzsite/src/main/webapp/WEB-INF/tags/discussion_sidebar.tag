<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>

<hr class="kagu-hr">
<form action="/discussions/public/search" class="form-search" style="margin-bottom: 10px; margin-top: 10px;">
	<input name="querystring" type="text" placeholder="Discussion Search" value="${querystring}" }class="input-medium search-query">
	<button type="submit" class="btn">Search</button>
	<br> <select style="margin-top: 10px;" name="discussion_category_id" id="discussion_category_id" class="span2">
		<option value="">Categories</option>
		<c:forEach var="discussion_category" items="${discussion_categories}">
			<option value="${discussion_category.getId()}">${discussion_category.getName()}</option>
		</c:forEach>
	</select>
	<h6 class="muted">With Tags (optional)</h6>
	<div class="clearfix"></div>
	<kagutags:search_tags ajaxurl="/discussions/browse/type_ahead_tags"/>
</form>
<div class="clearfix"></div>

<kagutags:infoboxtitle cssclass="buzzinfoboxtitle title-simple" title='Current Discussion Trends' />
<hr class="kagu-hr">
<ol>
	<c:if test="${not empty discussion_trends}">
		<c:forEach var="discussion" items="${discussion_trends}">
			<li><a class="darkgrey" href="/discussion/public/view/${discussion.getId()}/${discussion.getFriendlyURL()}"> <small>${discussion.getFirstMessageInThread().getTitle()}</small>
			</a>
				<hr class="kagu-hr"></li>
		</c:forEach>
	</c:if>
</ol>

<div class="global-margin-top">
	<kagutags:adsense_300x250 />
</div>
