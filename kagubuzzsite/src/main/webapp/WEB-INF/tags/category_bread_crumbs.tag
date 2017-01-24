<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="breadcrumbs" required="false" type="java.util.List"%>
<%@ attribute name="tags" required="false" type="java.util.Set"%>
<%@ attribute name="breadcrumb_url" required="true" type="java.lang.String"%>
<%@ attribute name="showuserpostoptions" required="false"%>
<%@ attribute name="imessage" required="false" type="com.kagubuzz.datamodels.JSPMessageRenderer"%>
<%@ attribute name="ajaxurl" required="false" type="java.lang.String"%>
<%@ attribute name="searchterms" required="false" type="java.lang.String"%>
<%@ attribute name="optionbookmark" required="false" type="java.lang.String"%>
<%@ attribute name="optionremove" required="flase" type="java.lang.String"%>
<%@ attribute name="optionflag" required="flase" type="java.lang.String"%>
<%@ attribute name="optionbuzzit" required="false" type="java.lang.String"%>
<%@ attribute name="optiondiscussit" required="flase" type="java.lang.String"%>
<%@ attribute name="optionmoreinfo" required="flase" type="java.lang.String"%>
<%@ attribute name="optionreply" required="false" type="java.lang.String"%>
<ul class="breadcrumb" style="margin: 0px;">
   <c:choose>
      <c:when test="${not empty searchterms}">
         <span class="lead">Search results for <strong>${searchterms}</strong></span>
      </c:when>
      <c:otherwise>
         <c:if test="${not empty breadcrumbs}">
            <c:forEach var="crumb" items="${breadcrumbs}" varStatus="i">
               <li>
                  <a href="/${breadcrumb_url}/browse/category/${crumb.getId()}/${crumb.getFriendlyURL()}">${crumb.getName()}</a> 
                  <c:if test="${i.count <breadcrumbs.size()}">
                     <span class="divider">/</span>
                  </c:if>
               </li>
            </c:forEach>
         </c:if>
      </c:otherwise>
   </c:choose>
   <c:if test="${not empty showuserpostoptions}">
      <li class="pull-right">
         <kagutags:post_options 
            ajaxurl="${ajaxurl}"
            optionbookmark="${optionbookmark}"
            optionbuzzit="${optionbuzzit}"
            optiondiscussit="${optiondiscussit}"
            optionflag="${optionflag}"
            optionmoreinfo="${optionmoreinfo}"
            optionremove="${optionremove}"
            imessage="${imessage}"/>
      </li>
   </c:if>
   <li class="pull-right">
      <c:if test="${not empty tags}">
         <small class="muted pull-left" style="margin-right: 5px;">Tags: </small>
         <c:forEach var="tags" items="${tags}">
            <a href="#${tags.getName()}" onclick="notifyAdded('${tags.getName()}')" class="myTagBreadCrumb">${tags.getName()}</a>
         </c:forEach>
      </c:if>
   </li>
   <li>
      <div class="clearfix"></div>
   </li>
</ul>