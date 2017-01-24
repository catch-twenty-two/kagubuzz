<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>

<c:if test="${not user.isLoggedIn()}">
 <hr class="kagu-hr">
<c:choose>
<c:when test="${adSense == false}">
    <img src="//storage.googleapis.com/support-kms-prod/SNP_2922281_en_v1" alt="Ad Sense PlaceHolder">
</c:when>
<c:otherwise>
	<script type="text/javascript"><!--
	google_ad_client = "ca-pub-6787566224506612";
	/* Side Bar Ads */
	google_ad_slot = "6369183388";
	google_ad_width = 300;
	google_ad_height = 250;
	//-->
	</script>
	<script type="text/javascript"
	src="//pagead2.googlesyndication.com/pagead/show_ads.js">
	</script>
</c:otherwise>
</c:choose>
</c:if>

