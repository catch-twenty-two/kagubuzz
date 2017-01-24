<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<!DOCTYPE html>
<html lang="en">
   <head>
      <kagutags:bootstrap_css/>
      <link rel="stylesheet" href="/static/css/main.css" type="text/css" />
      <link rel="stylesheet" href="/static/css/browse.css" type="text/css" />
      <meta charset="utf-8" />
      <title>Browsing Classified Ads In and Around ${user.getTblKaguLocation().getCity()}, ${user.getTblKaguLocation().getState()}</title>
   </head>
   <body>
      <kagutags:jquery/>
      <kagutags:bootstrap_js/>
      <script src="/static/jquery/postoptionclick.js" /></script>
      <script src="/static/jquery/jquery.numeric.js" type="text/javascript"/></script>
      <div class="wrapper">
         <kagutags:header navactivebutton="buy-it" headerpic="smallheader.jpg"></kagutags:header>
         <div class="main-content clear-background">
            <div class="buzzinfobox notitle">
               <kagutags:category_header 
                  ajaxurl="ad" 
                  ad_group="${ad.getAdGroup()}" 
                  category="${ad.getCategory()}" 
                  category_url="ads" 
                  searchterms="${keywords}" 
                  imessage="${ad}" />
            </div>
            <div id="adListRenderer">
               <c:if test="${not empty ads}">
                  <kagutags:ads_list_renderer ads="${ads}"></kagutags:ads_list_renderer>
               </c:if>
               <c:if test="${empty ads}">
                  <h3 align="center">No Results</h3>
               </c:if>
            </div>
            <kagutags:pagination/>
         </div>
         <div class="side-bar">
            <a href="/login"><img alt="Login" src="/static/images/kagutitlesmall.png"/></a>       
            <hr class="kagu-hr side-bar-spacing">
            <h4>Browsing The ${user.getTblKaguLocation().getCity()} Ad Board</h4>
            <hr class="kagu-hr side-bar-spacing">
            <form action="/ads/search" class="form-search side-bar-spacing">
               <div class="control-group">
                  <div class="controls">
                     <c:forEach var="ad_group" items="${ad_groups}">
                        <label class="radio">
                        <input name="ad_group" type="radio" data-url-name="${ad_group.getUnderScoreName()}" value="${ad_group.name()}"}> ${ad_group.getEnumExtendedValues().getDescription()}</label><br>
                     </c:forEach>
                  </div>
               </div>
               <hr class="hr.style-one">
               <div class="input-append pull-left side-bar-spacing">
                  <input name="keywords" placeholder="Keyword Ad Search" type="text" 
                  <c:if test="${not empty keywords}">value="${keywords}"</c:if>
                  class="input-medium search-query">
                  <span class="add-on"><i class="icon-search"></i></span>
               </div>
               <button type="submit" class="btn pull-right side-bar-spacing">Search</button>      
               <div class="clearfix"></div>
               <div class="side-bar-spacing">
                  <select name="category_selection" id="ad_category" class="span3">
                     <option value="">Choose a Category</option>
                     <c:forEach var="adcategory" items="${adcategories}">
                        <option value="${adcategory.getId()}">${adcategory.getName()}</option>
                     </c:forEach>
                  </select>
               </div>
               <div class="pull-left input-prepend input-append side-bar-spacing">
                  <span class="add-on">$</span> <input class="span1" id="price" name="price" value="${price}" maxlength="7" placeholder="Any" size="16" type="text">
                  <span class="add-on">.00 Or Less</span>  
               </div>
               <div class="clearfix"></div>
               <label class="side-bar-spacing checkbox inline">Only Search For Ads Offering Omnui
               <input type="checkbox" id="timebanking" name="timebanking">
               <kagutags:pop_over title="What Can I Do With Time Banking?" placement="left" template_name="timeBanking"/>
               </label>
               <div class="clearfix"></div>
               <c:if test="${empty hidesearchoptions}">
                  <c:if test="${not empty user.getTblKaguLocation()}">
                     <div class="side-bar-spacing">
                        <span class="help-inline">Within</span>
                        <c:forEach var="radius" items="${serach_radius_options}">
                           <label class="radio inline">
                           <input type="radio" name="radius" 
                              value="${radius.getEnumExtendedValues().getDescription()}"> 
                           ${radius.getEnumExtendedValues().getDescription()}
                           </label>
                        </c:forEach>
                        <span class="help-inline">Miles</span> 
                     </div>
                  </c:if>
               </c:if>
               <c:choose>
                  <c:when test="${not empty user.getTblKaguLocation()}">
                     <kagutags:google_maps 
                        showdirectionsonload="N"
                        zoom="8" 
                        kagulocation="${user.getTblKaguLocation()}"/>
                  </c:when>
                  <c:otherwise>
                     <kagutags:google_maps 
                        showdirectionsonload="N"
                        zoom="11" 
                        address="Oakland, Ca"/>
                  </c:otherwise>
               </c:choose>
               <kagutags:adsense_300x250/>
               <kagutags:adsense_300x250/>
            </form>
         </div>
         <kagutags:footer />
      </div>
      <script type="text/javascript">
      function showUserProfile(userId) {         
          $('#modal_dialog').load('/modal/userprofile/' + userId, function() {$('#modal_dialog').modal('show');});
      }
      
      $("a[rel=popover]").popover({trigger:'hover'});
         $(document).ready(function() {
          <c:if test="${not empty radius}">
                 $('input:radio[name=radius]').filter('[value=${radius}]').attr('checked', true);
                 setGoogleMapRadiusSearch('${radius}');
             </c:if>
             <c:if test="${not empty category_selection}">
                 $('#ad_category').val('${category_selection}');
             </c:if>
         });
         
         $("#timebanking").prop('checked', '${formatter.isChecked(timebanking)}');
         
         $('input:radio[name=ad_group]').filter('[value=${ad_group.name()}]').attr('checked', true);
         
         $("input[name='ad_group']").change(function() { 
        	   window.location.href = "/ads/browse/" + $(this).data('url-name'); 
         });       
         
         $("input[name='radius']").change(function() { setGoogleMapRadiusSearch($(this).val()); }); 
         $('#price').numeric(false);
         
      </script>
   </body>
</html>