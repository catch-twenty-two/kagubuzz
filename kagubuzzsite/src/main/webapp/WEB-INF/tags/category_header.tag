<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>

<%@ attribute name="category_url" required="true" type="java.lang.String"%>
<%@ attribute name="category" required="true" type="com.kagubuzz.datamodels.hibernate.LSTCategoryBase"%>
<%@ attribute name="ad_group" required="false" type="com.kagubuzz.datamodels.enums.AdGroup"%>
<%@ attribute name="ad_type" required="false" type="com.kagubuzz.datamodels.enums.AdType"%>

<%@ attribute name="tags" required="false" type="java.util.Set"%>
<%@ attribute name="searchterms" required="false" type="java.lang.String"%>

<%@ attribute name="showuserpostoptions" required="false"%>
<%@ attribute name="imessage" required="false" type="com.kagubuzz.datamodels.JSPMessageRenderer"%>
<%@ attribute name="ajaxurl" required="false" type="java.lang.String"%>

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
      <c:if test="${not empty ad_group}">
       <li>
        <a href="/${category_url}/browse/${ad_group.getUnderScoreName()}">${ad_group.getEnumExtendedValues().getDescription()}</a>        
        </li>
      </c:if>
      <c:if test="${not empty ad_type}">
        <span class="divider"> / </span>
        <li>
        <a href="/${category_url}/browse/${ad_group.getUnderScoreName()}/${ad_type.name()}">${ad_type.getEnumExtendedValues().getDescription()}</a>
        </li>
      </c:if>
       <c:if test="${not empty category}">
      <li>
        <a href="/${category_url}/browse/category/${category.getId()}/${category.getFriendlyURL()}"><c:if test="${not empty ad_group}"><span class="divider"> / </span></c:if>${category.getName()}</a>                
      </li>
      </c:if>
      </c:otherwise>
   </c:choose>
   <c:if test="${not empty showuserpostoptions}">

      <li class="pull-right fix-bootstrap-btn-alignment">

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