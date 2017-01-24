<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
   <head>
      <title>${ad.getTitle()}</title>
      <kagutags:bootstrap_css/>
      <link rel="stylesheet" href="/static/css/main.css" type="text/css" />
      <link rel="stylesheet" href="/static/css/lightbox/lightbox.css" type="text/css" />
      <meta charset="utf-8" />
      <style type="text/css">
         .disable-me {}
         .offer-btn {}
         .modal { width: 460px; }
      </style>
      <title>${ad.getTitle()} in ${ad.getTblKaguLocation().getCity()}, ${ad.getTblKaguLocation().getState()}</title>
   </head>
   <body>
      <kagutags:jquery/>
      <kagutags:bootstrap_js/>
      <script src="/static/jquery/createnewmessage.js" type="text/javascript"/></script>
      <script src="/static/jquery/textarea-limit.js" type="text/javascript"/></script>
      <script src="/static/jquery/postoptionclick.js" type="text/javascript"/></script>
      <script src="/static/jquery/jquery.numeric.js" type="text/javascript"/></script>
      <script src="/static/jquery/lightbox.js"/></script>
      <div class="wrapper">
         <kagutags:header navactivebutton="buy-it" headerpic="smallheader.jpg"></kagutags:header>
         <div class="main-content clear-background">
            <div class="buzzinfobox notitle" >
               <kagutags:category_header 
                  ajaxurl="/ad/" 
                  ad_group="${ad.getAdGroup()}" 
                  ad_type="${ad.getAdType()}" 
                  category="${ad.getCategory()}" 
                  category_url="ads"
                  optionbookmark="Y" 
                  optionbuzzit="Y" 
                  optionflag="Y"  
                  searchterms="${keywords}" 
                  imessage="${ad}" 
                  showuserpostoptions="Y"/>
            </div>
            <div class="buzzinfobox notitle" <c:if test="${ad.getAdGroup() == 'CommunityServices'  && not empty adService.getAdImageURL3(ad)}">style="background-image: url('${adService.getAdImageURL3(ad)}');"</c:if>>            
            <div class="post-box-padding fix-bootstrap-btn-alignment">
               <c:choose>
                  <c:when test="${user != ad.getOwner()}">                     
                     <button class="offer-btn btn btn-primary  pull-right" type="button" data-loading-text="One Moment..." autocomplete="off">${offer_btn_text}</button>
                  </c:when>
                  <c:otherwise>
                     <button class="offer-btn btn btn-primary pull-right disabled" type="button" disabled="disabled">${offer_btn_text}</button>
                  </c:otherwise>
               </c:choose>
               <c:if test="${!ad.isFirm()}">
                  <a id="offers" style="padding-left: 0px;" class="btn btn-link btn-tiny pull-left darkgrey">
                     Offers So Far ${ad.getOffers().size()}       
                     <c:if test="${not empty acceptedoffer}">
                        <small> (An Offer Has Been Accepted)</small>
                     </c:if>
                  </a>
                  <span>
                     <kagutags:pop_over title="Making An Offer" template_name="offerDisclaimer"/>
                  </span>
               </c:if>
               <div class="clearfix"></div>
               <hr class="kagu-hr">
               <h4 class="pull-left">
                  <c:if test="${ad.getAdGroup() == 'CommunityServices'}"><span> ${ad.getAdType().getEnumExtendedValues().getDescription()}</span> - </c:if>
                  ${ad.getTitle()} 
               </h4>
               <h4 class="pull-right">
                  <kagutags:ad_price ad="${ad}"/>               
               </h4>
               <div class="clearfix"></div>
               <c:if test="${ad.getPrice() > 0}">
               <c:choose>
                  <c:when test="${ad.isFirm()}" >
                     <div  class="pull-right fix-bootstrap-btn-alignment" >
                        <span class="label">Firm</span>
                     </div>
                  </c:when>
                  <c:otherwise>
                     <div  class="pull-right fix-bootstrap-btn-alignment" >
                        <span class="label">Negotiable</span>
                     </div>
                  </c:otherwise>
               </c:choose>
               </c:if>
               <p>
                  Posted 
                  <fmt:formatDate pattern="EEEEEEEEE, MMMMMMMM, dd, yyyy h:mm a" value="${formatter.gmtDateToTzDate(ad.getCreatedDate(), user.getTimeZoneOffset())}"/>
               </p>
               <p>${ad.getTblKaguLocation().getCity()}, ${ad.getTblKaguLocation().getState()}</p>
               <div style="overflow: auto;">
                  <p >${ad.getBody()}</p>
               </div>
               <br>
               <c:if test="${not empty ad.getLastUpdated()}">
                  <small>
                     Last Updated 
                     <fmt:formatDate pattern="EEEEEEEEE, MMMMMMMM, dd, yyyy h:mm a" value="${formatter.gmtDateToTzDate(ad.getLastUpdated(), user.getTimeZoneOffset())}"/>
                  </small>
               </c:if>
               <c:if test="${not empty adService.getImagePathsURLS(ad) && ad.getAdGroup() != 'CommunityServices'}">
                  <hr class="kagu-hr">
                  <small><strong>Select Any Image Below For a Larger Image And Slide Show</strong></small>                    
                  <div style="margin: 5px 0px auto auto; width:100%; height:115px;">
                     <ul class="thumbnails" style="margin: 8px;">
                        <c:forEach items="${adService.getImagePathsURLS(ad)}" var="imagepath" >
                           <c:if test="${not empty imagepath}">
                              <li style="width:150px; margin: 0px 2px 0px 2px;">
                                 <div class="thumbnail" style="margin: 0px;">                         
                                    <a href="${imagepath}" rel="lightbox[userimage]" title="${ad.getTitle()} ">
                                    <img style="width:150px;"  src="${imagepath}" alt=""></a>
                                 </div>
                              </li>
                           </c:if>
                        </c:forEach>
                     </ul>
                     <div class="clearfix"></div>
                  </div>
                  <div class="clearfix"></div>
               </c:if>
            </div>
         </div>
         <c:if test="${empty editing}">
            <c:choose>
               <c:when test="${user.isLoggedIn()}">
                  <c:if test="${user != ad.getOwner()}">
                                 <button class="global-margin-bottom offer-btn btn btn-primary btn-block btn-large pull-right" type="button" data-loading-text="One Moment..." autocomplete="off">${offer_btn_text}</button>
                <div class="clearfix"></div>
                     <kagutags:infoboxtitle cssclass="buzzinfoboxtitle title-simple yellow left" icon="none" title='Ask ${ad.getOwner().getFirstName()} a question about this Ad. This question will be viewable by the public.'/>
                     <kagutags:message_create
                        save_button_message="Post Question"
                        messageid="${message.getId()}"
                        messageboxmaxchars="300"
                        parentid="${ad.getId()}"
                        sender="${user}"
                        ajaxurl="/ad/question/"
                        domattachid="questions"
                        messageboxinitval="DO NOT use this space for negotiation or exchanging contact information, for that please use the &ldquo;I&rsquo;m Interested&rdquo; button. Questions or Answers violating this policy will be flagged for removal."
                        messageboxtitle="<strong>${user.getFirstName()}</strong>"/>
                  </c:if>
               </c:when>
               <c:otherwise>
                  <kagutags:infoboxtitle cssclass="buzzinfoboxtitle title-simple yellow left" icon="none" title="Comments And Questions (Sign In To Ask a Question)"/>
               </c:otherwise>
            </c:choose>
            <div id="questions">
               <c:forEach items="${ad.getQuestionsAndAnswers().getMessages()}" var="question">
                  <kagutags:message_render
                     message="${question}"
                     imessage="${question}"
                     ajaxurl="/ad/question/"
                     optionremove="Y"
                     optionbuzzit="Y"
                     optionreply="Y"   
                     optionflag="Y"/>
               </c:forEach>
            </div>
         </c:if>
         <c:if test="${not empty editing}">
            <div class="global-margin-top">
           <c:if test="${empty iPhoneAd}">
               <form method="GET" action="edit">
                  <input type="hidden" value="${ad.getId()}" name="id">
                  <button type="submit" class="btn btn-primary btn-danger pull-left"><i class="icon-chevron-left icon-white"></i> Not Quite Done</button>
               </form>
               <form method="POST" action="post">
                  <button type="submit" class="btn btn-success pull-right">Looks Good Post It! <i class="icon-chevron-right icon-white"></i></button>    
               </form>
               <div class="clearfix"></div>
           </c:if>   
            </div>
         </c:if>
      </div>
      <div class="side-bar">
         <a href="/login"><img alt="" src="/static/images/kagutitlesmall.png"/></a>
         <kagutags:infoboxtitle cssclass="buzzinfoboxtitle title-simple left" icon="none" title='Item Location'/>
         <div id="contentpreviewaddress">
            <kagutags:google_maps 
               showdirectionsonload="Y"
               zoom="13"
               zip_code="${ad.getTblKaguLocation().getZip()}"
               save_lat_long_url="${save_lat_long_url}"
               post_id="${ad.getId()}"
               kagulocation="${ad.getTblKaguLocation()}"/>
         </div>
         <c:if test="${ad.getAdGroup() == 'CommunityServices' && not empty adService.getAdImageURL2(ad)}">
            <hr class="kagu-hr">
            <ul class="thumbnails centeredImage" style="width:100%; ">
               <li style="margin-top: 3px; margin-bottom: 0px; text-align: center; width:100%">
                  <div class="thumbnail" style="max-height: 250px; overflow: hidden; padding:3px;">
                     <img src="${adService.getAdImageURL2(ad)}" alt="">
                  </div>
               </li>
            </ul>
         </c:if>
         <div class="buzzinfobox notitle post-box-padding">
         <kagutags:account_profile user_profile="${ad.getOwner()}"></kagutags:account_profile>        
      </div>
       <div class="global-margin-top">
            <kagutags:adsense_300x250/>
         </div>
      <div class="clearfix"></div>
      </div>
      <kagutags:footer/>
      </div>
            <kagutags:add_this />
      <script type="text/javascript">   
         $("a[rel=popover]").popover({trigger:'hover', placement:'bottom'});
         
         $('.offer-btn').click(function() { $('#modal_dialog').load('/modal/ad_ask_contact_method/${ad.getId()}', function() {
          $('#modal_dialog').modal('show');
          $('button[data-loading-text]').button('reset');
          });
         });
         
         $('button[data-loading-text]').button('reset');
         
         $('button[data-loading-text]').click(function () {
             $(this).button('loading');
         });
         
      </script>
   </body>
</html>
<kagutags:scroll_to_message/>