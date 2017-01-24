<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page trimDirectiveWhitespaces="true" %>
<!DOCTYPE html>
<html lang="en">

<head>
<kagutags:bootstrap_css/>
<link rel="stylesheet" href="/static/css/main.css" type="text/css" />
<link rel="stylesheet" href="/static/css/postsmanage.css" type="text/css" />
<meta charset="utf-8" />
<title>Manage Your Posts</title>
</head>
<body>
<kagutags:jquery/>
<kagutags:bootstrap_js/>
<script src="/static/jquery/bootstrap-datepicker.js" type="text/javascript"></script>
	<div class="wrapper">	
		<kagutags:header navactivebutton="home" headerpic="smallheader.jpg"></kagutags:header>		
			<div class="main-content clear-background">
				<div class="buzzinfoboxpadding" >		
					<c:if test="${not empty events}">					
						<c:forEach var="event" items="${events}">	
						        <div class="manage-row">
						        <small><strong>Starts on <fmt:formatDate pattern="MMMMMMMMM dd, yyyy" value="${formatter.gmtDateToTzDate(event.getStartDate(), user.getTimeZoneOffset())}" />, ${event.capitalizeFirstLetter(event.getEventCycle().getFormattedRepeatString(formatter.gmtDateToTzDate(event.getStartDate(), user.getTimeZoneOffset())))} 
						              </strong></small>	
						        	<c:choose>																												
										<c:when test="${!event.isExpired()}">
										    <c:choose>
										    <c:when test="${event.isActive()}">
											      <span class="label label-important pull-right"><strong>Listed - ${event.daysLeftUntilRenew()} Days Left To Renew</strong></span>
											</c:when>
											<c:otherwise>
											      <span class="label pull-right"><strong>Not Listed</strong></span>
											</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
										   <span class="label label-inverse pull-right"><strong>Expired</strong></span>
										</c:otherwise>
									</c:choose>
																						
									<div id="scroll_id${event.getId()}"class="well well-small post-well">
									
									<c:if test="${not empty event.getSideBarImageName()}">
										<div class="pull-left" style="margin-right: 5px;">
											<img class="img-polaroid" style="height: 30px; width: 30px; margin-bottom: 5px;" src="${event_service.getSidebarImageURL(event)}">									
										</div>
									</c:if>
										<strong><span class="pull-left black">${event.getTitle()}</span></strong>
										<strong>
											<a data-toggle="collapse" href="#${event.getId()}" class="pull-right text-info">Options</a>
										</strong>							
									</div>									
									<div class="clearfix"> </div>																	
									<div id="${event.getId()}" class="textsmall collapse">
                                            <a href="duplicate?id=${event.getId()}" class="btn pull-left btn-mini manage-option" >Duplicate</a>  
											<a href="/eventedit?id=${event.getId()}" class="btn pull-left btn-mini manage-option" >Edit</a>											
											<a href="delete?id=${event.getId()}" type="post" class="btn btn-mini pull-left btn-danger manage-option" >Delete</a>	
											<c:if test="${event.isActive()}">	
											     <a href="renew?id=${event.getId()}" type="post" class="btn btn-mini btn-primary pull-right manage-option" >Renew</a>	
											</c:if>										
											<c:choose>
												<c:when test="${event.isActive()}">
												    <a href="/event/view/${event.getId()}/${event.getFriendlyURL()}/" type="post" class="btn btn-mini pull-leftmanage-option" >View</a>
													<a href="remove?id=${event.getId()}" class="btn btn-mini pull-right manage-option" >Remove From Listings</a>
												</c:when>
												<c:otherwise>
												<c:if test="${!event.isExpired()}">
													<a href="activate?id=${event.getId()}" class="btn btn-mini pull-right manage-option" >Add To Listings</a>
												</c:if>
												</c:otherwise>											
											</c:choose>											
										</div> 								
									</div>
						</c:forEach>
						<kagutags:pagination/>
					</c:if>
					<c:if test="${empty events}">
          	  			<h3 align="center">No Results</h3>
          			</c:if>
				</div>
			</div>		
			<div class="side-bar">
			       <a href="/login"><img alt="" src="/static/images/kagutitlesmall.png"/></a>       
          <hr class="kagu-hr">  
			<h4>Browsing Your EVENT IT History</h4>
				
				<kagutags:infoboxtitle cssclass="buzzinfoboxtitle title-simple" title='' />
				    <h5>Search For</h5>
				            
					<form action="/events/manage/search">
                    <fieldset>
                    <label>Title</label>
					<input type="text" name="title" value="${title}" placeholder="Title">

					<!--  <label>Date Range</label>
					<input style="width: 90px;" type="text" placeholder="Starts After" name="startDate" id="datepicker">
					<input style="width: 90px;" type="text" placeholder="Ends Before" name="endDate" id="datepicker"><br>
					               <label class="checkbox">
                    <input type="checkbox" name="expired"> Show expired events</label>-->
                    <label class="checkbox" > <input name="active" type="checkbox" ${not_active}> Show unlisted events only</label>
                      <hr class="kagu-hr">  
					<button type="submit" class="btn btn-primary">Go</button>
                    
					</fieldset>
					</form>
				
						
			</div>			
		<kagutags:footer/>
	</div>
</body>



