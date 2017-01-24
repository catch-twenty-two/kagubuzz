<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<!DOCTYPE html>
<html lang="en">
   <!-- Jquery has to go before bootstrap -->
   <kagutags:jquery />
   <kagutags:bootstrap_css/>
   <kagutags:bootstrap_js/>
   <!-- Label Click-->
   <script src="/static/jquery/postoptionclick.js" /></script>
   <link rel="stylesheet" href="/static/css/main.css" type="text/css" />
   <link rel="stylesheet" href="/static/css/browse.css" type="text/css" />
   <!-- ratings -->
   <link rel="stylesheet" href="/static/css/rateit.css" type="text/css" />
   <script src="/static/jquery/jquery.rateit.min.js" type="text/javascript"/></script>
      <script src="/static/jquery/jquery.numeric.js" type="text/javascript"/></script>
   <head>
      <meta charset="utf-8" />
      <title>Local Community Events Happening In ${user.getTblKaguLocation().getCity()}</title>
   </head>
   <body>
      <div class="wrapper">
         <kagutags:header navactivebutton="attend-it" headerpic="smallheader.jpg"></kagutags:header>       
         <div class="main-content clear-background">
            <div class="buzzinfobox notitle">
            <kagutags:category_bread_crumbs breadcrumb_url="events"/>
            </div>
            <div id="eventlist">
            <c:if test="${not empty events}">
			  <kagutags:events_list_renderer events="${events}"/>            
			</c:if>
			<c:if test="${empty events}">
          	  <h3 align="center">No Results</h3>
          	</c:if>
            </div>
            <kagutags:pagination/>
         </div>
         <div class="side-bar">
            <a href="/login"><img alt="" src="/static/images/kagutitlesmall.png"/></a>       
            <hr class="kagu-hr">
            <h4>Browsing The ${user.getTblKaguLocation().getCity()} Event Board</h4>
            <kagutags:weather/>
            <c:if test="${empty hidesearchoptions}">
               <hr class="kagu-hr">
               <form action="/events/search?categoryid=${currentcategory}" class="form-search" style="margin-bottom: 10px; margin-top: 10px;">
                  <div class="input-append">
                     <input name="keywords" placeholder="Event Keyword Search" type="text" 
                     <c:if test="${not empty keywords}">value="${keywords}"</c:if>
                     class="input-medium search-query">
                     <span class="add-on"><i class="icon-search"></i></span>
                  </div>
                  <button type="submit" class="btn pull-right">Search</button>
                  <!--  <p><label class="checkbox inline"> <input type="checkbox" id="inlineCheckbox1" value="option1"> Sort Results by Buzz Rating </label></p> --><br><br>      
                  <select name="category_selection" id="eventCategory" class="span3">
                     <option value="">Choose a Category</option>
                     <c:forEach var="eventcategory" items="${eventcategories}">
                        <option value="${eventcategory.getId()}">${eventcategory.getName()}</option>
                     </c:forEach>
                  </select>
                  <hr class="kagu-hr">
                  <c:choose>
                     <c:when test="${not empty user.getTblKaguLocation()}">
                        <div style="text-align: center;"><a data-toggle="collapse" href="#searchcollapse">Search By Price, Age Or Distance</a></div>
                     </c:when>
                     <c:otherwise>
                        <div style="text-align: center;"><a data-toggle="collapse" href="#searchcollapse">Search By Price Or Age</a></div>
                     </c:otherwise>
                  </c:choose>
                  <div id="searchcollapse" style="border: 0px" class="buzzinfobox collapse">
                     <div class="buzzinfoboxpadding">
                        <div class="pull-left input-prepend input-append">
                           <span class="add-on">$</span> <input class="span1" id="price" maxlength="7" name="price" value="${price}" placeholder="Any" size="16" type="text">
                           <span class="add-on">.00 Or Less</span>  
                        </div>
                        <div class="clearfix"></div>
                        <div>
                           <c:forEach var="age" items="${ages}">
                              <label class="checkbox inline"> 
                              <input type="checkbox" name="age_selections" 
                                 value="${age.name()}">
                              ${age.getEnumExtendedValues().getDescription()}
                              </label>
                              <br>
                           </c:forEach>
                        </div>
                        <c:if test="${not empty user.getTblKaguLocation()}">
                           <div>
                              <span class="help-inline" style="margin-top:5px;">Within</span>
                              <c:forEach var="radius" items="${serachradius}">
                                 <label class="radio inline">
                                 <input type="radio" name="radius" 
                                 value="${radius.getEnumExtendedValues().getDescription()}" ${radius.getEnumExtendedValues().isChecked()}> 
                                 ${radius.getEnumExtendedValues().getDescription()}
                                 </label>
                              </c:forEach>
                              <span class="help-inline" style="margin-top:5px;">Miles</span> 
                           </div>
                        </c:if>
                     </div>
                  </div>
               </form>
            </c:if>
            <kagutags:infoboxtitle cssclass="buzzinfoboxtitle title-simple" title='Event Location' />
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
         </div>
         <kagutags:footer/>
      </div>
      <script>    
         var currentAddress;
         
         $(document).ready(function() {
             
             <c:if test="${show_options}">
                $('#searchcollapse').collapse('show')
             </c:if>
                
             <c:if test="${not empty age_selections}">
                <c:forEach items="${age_selections}" varStatus="loop">
                    $('input[value="${age_selections[loop.index]}"]').prop("checked", true); 
                </c:forEach>
             </c:if>
             
             <c:if test="${not empty radius_selection}">
                $('input:radio[name=radius]').filter('[value=${radius_selection}]').attr('checked', true);
                setGoogleMapRadiusSearch('${radius_selection}');
             </c:if>
         
             <c:if test="${not empty category_selection}">
                $('#eventCategory').val('${category_selection}');
             </c:if>
         });
         
         function showUserProfile(userId) {         
             $('#modal_dialog').load('/modal/userprofile/' + userId, function() {$('#modal_dialog').modal('show');});
         }
         
         $("input[name='radius']").change(function(){
             setGoogleMapRadiusSearch($('input:radio[name=radius]:checked').val());
         });
         
         $('#price').numeric(false);
          
      </script>
   </body>
</html>
