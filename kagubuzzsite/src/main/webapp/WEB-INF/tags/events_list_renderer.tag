<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="events" required="true" type="java.util.Collection"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="accordion" id="accordion2" style="margin-bottom:0px;" >
	<c:if test="${not empty events}">
		<c:forEach var="event" items="${events}" varStatus="i">
		 <c:choose>
		<c:when test="${(i.count) % 2 == 0}">
			<c:set var="cssstyle" value=""/>
		</c:when>
		<c:otherwise>
		     <c:set var="cssstyle" value="yellow"/>
		    </c:otherwise>
		</c:choose>
            <div class="accordion-group browse-list  ${cssstyle}">
				<div class="accordion-heading infobubble darkgrey">
      		      	<div class="accordion-toggle" style="padding: 5px; " id="${event.getAddress()}" data-toggle="collapse" data-parent="#accordion2" href="#collapse${event.getId()}">
      		      	 <div class="pull-left" style="width:570px;">
					<span class="pull-right kagured"><kagutags:price no_compensation="Free" price="${event.getPrice()}"/></span>
					<span><strong>${event.getTitle()}</strong> </span><br>  					
					<div class="clearfix"></div>  
					<span><fmt:formatDate pattern="MMM d" value="${formatter.gmtDateToTzDate(event.getStartDate(), user.getTimeZoneOffset())}" /></span> 
					<c:if test = "${not event.startsAndEndsSameDay(user.getTimeZoneOffset())}">
						- <span><fmt:formatDate pattern="MMM d" value="${formatter.gmtDateToTzDate(event.getEndDate(), user.getTimeZoneOffset())}" /></span>
					</c:if>
					<span  class="listing-address pull-right">${event.getAddress()}</span>
					<div style="display: none;" class="listing-coordinates">${event.getLatitude()},${event.getLongitude()}</div>
                    </div>
					
					<div class="pull-right" style="width:60px; text-align:center; margin-left: 5px;">
                    <c:choose>
                        <c:when test="${not empty event.getSideBarImageName()}">
                            <img class="browse-list-thumbnail" src="${events_service.getSidebarImageURL(event)}" alt="">
                        </c:when>
                        <c:otherwise>
                            <img src="/static/images/adthumbimageblank.png" alt="">
                        </c:otherwise>
                    </c:choose>
                    </div>
                    <div class="clearfix"></div> 
                    </div>
                    
					   
					
				</div>   	             
             	<div id="collapse${event.getId()}" class="accordion-body collapse">
	             	<div class="accordion-inner browse-list-inner">
	             	
							<kagutags:message_render						
							imessage="${event}"
							ajaxurl="/event/"
							optionbookmark="Y"
							optionflag="Y"
							optionbuzzit="Y"
							optiondiscussit="Y"
							optionmoreinfo="Y"/>
					</div>
				</div>             
			</div>             	
		</c:forEach>
	</c:if>
</div>	