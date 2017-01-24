<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>

<form action="searchevents" class="form-search" style="margin-bottom: 10px; margin-top: 10px;">
   <div class="input-append pull-left">
	<input name="querystring" type="text" placeholder="Search Kagu Buzz" class="input-medium search-query">
	 <span class="add-on"><i class="icon-search"></i></span>
	</div>
	<input name="radius" type="hidden" value="30"  class="input-medium search-query" >
	<button type="submit" class="btn pull-right">Search</button>
	<div class="clearfix"></div>
</form>