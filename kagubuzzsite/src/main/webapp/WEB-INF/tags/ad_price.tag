<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="ad" required="true" type="com.kagubuzz.datamodels.hibernate.TBLAd"%>
<c:choose>
   <c:when test="${ad.getAdGroup() == 'ForSale' || ad.getAdType() == 'Offered'}">
      <kagutags:price no_compensation="Free" price="${ad.getPrice()}" />
   </c:when>
   <c:otherwise>
      <kagutags:price no_compensation="Volunteer" price="${ad.getPrice()}" />
   </c:otherwise>
</c:choose>
<c:if test="${ad.getAdGroup() == 'CommunityServices' && ad.getPrice() != 0}">
   <sup><strong>${ad.getPerUnit().getEnumExtendedValues().getDescription()}</strong></sup>
</c:if>
<c:if test="${ad.getAdGroup() == 'CommunityServices' && ad.acceptsTimebanking()}">
<span> | </span>
<a href="http://omnui.com/Home/About" target="_blank" class="darkgrey">Omnui</a>
</c:if>