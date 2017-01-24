<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ attribute name="ads" required="true" type="java.util.Collection"%>
<div class="accordion" id="accordion2" style="margin-bottom:0px;" >
   <c:if test="${not empty ads}">
     <table class="table table-striped" style="width: 400px;">
      <c:forEach var="ad" items="${ads}">
      <tr style="width: 400px;">
     <td style="width: 400px;">
            <div class="accordion-heading infobubble darkgrey" style="width: 400px;">
               <div class="accordion-toggle" style="padding: 5px;" id="${ad.getId()}" data-toggle="collapse" data-parent="#accordion2" href="#collapse${ad.getId()}">
                  <div class="pull-left" style="width:570px;">
                     <span class="pull-right kagured">
                     <kagutags:ad_price ad="${ad}"/>
                     </span>
                     <span><strong>${ad.getTitle()}</strong></span><br>                      
                     <div class="clearfix"></div>
                     <span>
                        <fmt:formatDate pattern="MMM d" value="${formatter.gmtDateToTzDate(ad.getPostedDate(), user.getTimeZoneOffset())}" />
                     </span>
                     <span class="listing-address pull-right">${ad.getTblKaguLocation().getCity()}, ${ad.getTblKaguLocation().getState()}</span>                   
                  </div>
                  <div style="display: none;" class="listing-coordinates">${ad.getTblKaguLocation().getLatitude()},${ad.getTblKaguLocation().getLongitude()}</div>
                  <div class="pull-left" style="width:60px; text-align:center; margin-left: 5px;">
                     <c:choose>
                        <c:when test="${not empty ad_service.getAdImageIcon(ad)}">
                           <img class="browse-list-thumbnail" src="${ad_service.getAdImageIcon(ad)}" alt="">
                        </c:when>
                        <c:otherwise>
                           <img  src="/static/images/adthumbimageblank.png" alt="">
                        </c:otherwise>
                     </c:choose>
                  </div>
                  <div class="clearfix"></div>
               </div>
            </div>
            <div id="collapse${ad.getId()}" class="accordion-body collapse" style="width: 400px;">
               <div class="accordion-inner browse-list-inner">
                  <kagutags:message_render
                     imessage="${ad}" 
                     ajaxurl="/ad/"               
                     optionbookmark="Y"
                     optionflag="Y"
                     optionmoreinfo="Y"/>
               </div>
            </div>

         </td>
         <tr>
      </c:forEach>
      </table>
   </c:if>
</div>