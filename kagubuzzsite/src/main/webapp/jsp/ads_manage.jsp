<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html lang="en">
   <!-- Jquery has to go before bootstrap -->
   <kagutags:jquery />
   <kagutags:bootstrap_css/>
   <kagutags:bootstrap_js/>
   <!-- label click js -->
   <link rel="stylesheet" href="/static/css/main.css" type="text/css" />
   <link rel="stylesheet" href="/static/css/postsmanage.css" type="text/css" />
   <meta charset="utf-8" />
   <title>Manage Your Ad Posts</title>
   <head></head>
   <body>
      <div class="wrapper">
         <kagutags:header navactivebutton="ad-it" headerpic="smallheader.jpg" />
         <div class="main-content clear-background">
            <div class="buzzinfoboxpadding">
               <c:if test="${not empty userads}">
                  <c:forEach var="ad" items="${userads}">
                     <div class="manage-row">
                        <small>                           
                            <c:if test="${ad.getInquiries().size() > 0}">
                              <span class="badge badge-info" style="margin-right: 10px;">
                             ${ad.getInquiries().size()}
                              </span>
                              </c:if>
                           <strong>
                              Created on 
                              <fmt:formatDate pattern="MMMMMMMMM dd, yyyy" value="${formatter.gmtDateToTzDate(ad.getPostedDate(), user.getTimeZoneOffset())}" />
                           </strong>
                        </small>
                           <c:choose>
                              <c:when test="${ad.isActive()}">
                                 <span class="label label-important pull-right"><strong>Listed</strong></span>
                              </c:when>
                              <c:otherwise>
                                 <span class="label pull-right"><strong>Not Listed</strong></span>
                              </c:otherwise>
                           </c:choose>
                        <div class="well well-small post-well">
                           <c:if test="${not empty ad_service.getAdImageIcon(ad)}">
                              <div class="pull-left" style="margin-right: 5px;">
                                 <img class="img-polaroid" style="height: 30px; width: 30px; margin-bottom: 5px;" src="${ad_service.getAdImageIcon(ad)}" alt="Item Preview">
                              </div>
                           </c:if>
                           <strong><span class="pull-left black">${ad.getTitle()}</span></strong> 
                           <strong><a data-toggle="collapse" href="#${ad.getId()}" class="pull-right text-info">Options</a></strong>
                        </div>
                        <div class="clearfix"></div>
                        <div id="${ad.getId()}" class="textsmall collapse">                        
                           <c:forEach var="transaction" items="${ad.getOffers()}">
                              <div style="line-height: 20px;">
                                 <a href="/transaction/view/${transaction.getId()}/${transaction.getFriendlyURL()}">
                                 <span><strong>Details&nbsp;</strong></span></a> 
                                 <span>${formatter.gmtDateToTzDate(transaction.getPostedDate(), user.getTimeZoneOffset())}</span>
                                 <span class="black">
                                    <strong>
                                       ${transaction.getBuyer().getFirstName()} offered:
                                      ${adService.getOfferSnippit(transaction) }   
                                    </strong>
                                 </span>
                                 <c:choose>
                                    <c:when test="${transaction.getAdDiscussionState().name() == 'Accepted'}">
                                       <span class="label label-important pull-right"><strong>${transaction.getAdDiscussionState().getEnumExtendedValues().getDescription()}</strong></span>
                                    </c:when>
                                    <c:otherwise>
                                       <span class="pull-right"><strong>${transaction.getAdDiscussionState().getEnumExtendedValues().getDescription()}</strong></span>
                                    </c:otherwise>
                                 </c:choose>
                              </div>
                           </c:forEach>
                           <hr class="kagu-hr">
                           <a href="/ad/edit?id=${ad.getId()}" class="btn pull-left btn-mini manage-option">Edit</a> 
                          
                           <c:if test="${ad.isActive()}">
                             <%--  <a href="renew?id=${ad.getId()}" type="post" class="btn btn-mini btn-primary pull-right manage-option">Renew</a> --%>
                           </c:if>
                           <c:choose>
                              <c:when test="${ad.isActive()}">
                                 <a href="/ad/view/${ad.getId()}/${ad.getFriendlyURL()}" type="post" class="btn btn-mini pull-leftmanage-option">View Ad</a>
                                 <a href="remove?id=${ad.getId()}" class="btn btn-mini pull-right manage-option">Remove From Listings</a>
                              </c:when>
                              <c:otherwise>
                                 <c:if test="${!ad.isExpired()}">
                                    <a href="activate?id=${ad.getId()}" class="btn btn-mini pull-right manage-option">Add To Listings</a>
                                 </c:if>
                              </c:otherwise>
                           </c:choose>
                        </div>
                     </div>
                  </c:forEach>
                  <kagutags:pagination/>
               </c:if>
               <c:if test="${empty userads}">
          	  	  <h3 align="center">No Results</h3>
          	   </c:if>
            </div>
         </div>
         <div class="side-bar">
            <a href="/login"><img alt="" src="/static/images/kagutitlesmall.png" /></a>
            <hr class="kagu-hr">
            <h4>Browsing Your AD IT History</h4>
            <kagutags:infoboxtitle cssclass="buzzinfoboxtitle title-simple" title='' />
            <h5>Search For</h5>
            <form action="/ads/manage/search">
               <fieldset>
                  <label>Title</label> <input type="text" name="title" value="${title}" placeholder="Title">
                  <!--  <label>Date Range</label>
                     <input style="width: 90px;" type="text" placeholder="Starts After" name="startDate" id="datepicker">
                     <input style="width: 90px;" type="text" placeholder="Ends Before" name="endDate" id="datepicker"><br>
                                    <label class="checkbox">
                     <input type="checkbox" name="expired"> Show expired ads</label>-->
                  <label class="checkbox"> <input name="active" type="checkbox" ${not_active}> Show unlisted ads only
                  </label>
                  <hr class="kagu-hr">
                  <button type="submit" class="btn btn-primary">Go</button>
               </fieldset>
            </form>
         </div>
         <kagutags:footer />
      </div>
   </body>
</html>