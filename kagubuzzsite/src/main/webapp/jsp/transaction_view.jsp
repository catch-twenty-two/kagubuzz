<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<!DOCTYPE html>
<html lang="en">
   <kagutags:jquery />
   <kagutags:bootstrap_js/>
   <script src="/static/jquery/update-message.js" type="text/javascript"/></script>
   <script src="/static/jquery/createnewmessage.js" type="text/javascript"/></script>
   <script src="/static/jquery/postoptionclick.js" type="text/javascript"/></script>
   <script src="/static/jquery/textarea-limit.js" type="text/javascript"/></script>
   <script src="/static/jquery/jquery.rateit.min.js" type="text/javascript"></script>
   <kagutags:redactor/>
   <head>
      <style type="text/css">
         .disable-me {}
         .modal { width: 400px; }
      </style>
      <kagutags:bootstrap_css/>
      <link rel="stylesheet" href="/static/css/main.css" type="text/css" />
      <link rel="stylesheet" href="/static/css/rateit.css" type="text/css" />
      <meta charset="utf-8" />
      <title>Exchange Message History For "${transaction.getAd().getTitle()}" With ${transaction.getOppositeParty(user).getFirstName()}</title>
   </head>
   <body>
      <div class="wrapper">
         <kagutags:header navactivebutton="buy-it" headerpic="smallheader.jpg"></kagutags:header>
         <div class="main-content clear-background">
            <c:if test="${not empty transaction}">
               <div class="buzzinfobox buzzinfoboxpadding notitle" style="padding: 10px; margin-bottom: 5px;">
                  <c:if test="${not empty transaction.getAd().getImagePath1()}">
                     <img style="height:50px; margin-right: 10px;" class="img-polaroid pull-left" src="${adService.getAdImageURL1(transaction.getAd())}" alt="">                               
                  </c:if>
                  <a href="/ad/view/${transaction.getAd().getId()}/${transaction.getAd().getFriendlyURL()}">
                     <c:choose>
                        <c:when test="${user == transaction.getBuyer()}">
                           <span class="lead black">Exchange Message History For "${transaction.getAd().getTitle()}" With ${transaction.getSeller().getFirstName()}</span>     
                        </c:when>
                        <c:otherwise>
                           <span class="lead black">Exchange Message History For <strong>Your</strong> Ad "${transaction.getAd().getTitle()}" With ${transaction.getBuyer().getFirstName()}</span>
                        </c:otherwise>
                     </c:choose>
                  </a>
                  <div class="clearfix"></div>    
               </div>
               <c:if test="${(transaction.getAdDiscussionState() == 'Accepted' || 
                              transaction.getAdDiscussionState() == 'Canceled' || 
                              transaction.getAdDiscussionState() == 'Complete') && empty feedback}">
                    <button id='rate_transaction' class="btn btn-primary btn-block btn-large" type="button">Rate This Exchange With ${transaction.getOppositeParty(user).getFirstName()}</button>
               </c:if>                   
               <div class="buzzinfobox buzzinfoboxpadding notitle" style="padding: 10px;">
                  <div id="attachhere">
                     <c:forEach var="message" items="${messages}">
                        <c:choose>
                           <c:when test="${message.getOfferActive()}">
                              <kagutags:message_render                           
                                 message="${message}"
                                 use_redactor="Y"
                                 imessage="${message}"
                                 ajaxurl="/transaction/"
                                 domattachid="attachhere" 
                                 optionreply="Y"  
                                 optionflag="Y"/>
                           </c:when>
                           <c:otherwise>
                              <div class="disable-me">
                                 <kagutags:message_render                           
                                    message="${message}"
                                    use_redactor="Y"
                                    imessage="${message}"
                                    ajaxurl="/transaction/"
                                    domattachid="attachhere" 
                                    optionreply="Y"  
                                    optionflag="Y"/>
                              </div>
                           </c:otherwise>
                        </c:choose>
                     </c:forEach>
                  </div>
               </div>
               <c:if test="${user == transaction.getBuyer() && 
                  transaction.getAd().isActive() &&
                  transaction.getAdDiscussionState() != 'Accepted' &&
                  transaction.getAdDiscussionState() != 'Complete'}">
                  <p><button id="make-an-offer-btn" class="btn btn-success btn-large btn-block" type="button">Make ${transaction.getSeller().getFirstName()} A New Offer</button></p>
               </c:if>
            </c:if>
         </div>
      <div class="side-bar">
         <a href="/login"><img alt="" src="/static/images/kagutitlesmall.png"/></a>
         <hr class="kagu-hr">
         <div style="text-align:center">
            <h4>
            <c:choose>
               <c:when test="${user != transaction.getBuyer()}">
               ${formatter.capitalizeFirstLetter(transaction.getBuyer().getFirstName())}&rsquo;s Current Offer:<br>               
               </c:when>
               <c:otherwise>
                     Your Current Offer:
               </c:otherwise>
            </c:choose>
            </h4>
            <h4>
                ${adService.getOfferSnippit(transaction)}   
            </h4>
         </div>
         <div class="alert alert-success" style="text-align:center">     
            <h4>Exchange Status: ${transaction.getAdDiscussionState().getEnumExtendedValues().getDescription()}</h4>
         </div>
         <c:if test="${showOmnui}">
         <hr class="kagu-hr">
            <a href="/transaction/omnui/${transaction.getId()}" class="btn btn-success btn-block btn-large">Pay Via Omnui</a>
         <hr class="kagu-hr">
         </c:if>
         <c:if test="${((transaction.getAdDiscussionState() == 'Accepted') || 
                        (transaction.getAdDiscussionState() == 'Canceled') || 
                        (transaction.getAdDiscussionState() == 'Complete')) && not empty feedback}">
            <hr class="kagu-hr">
            <div class="alert" style="text-align:center">
               <h5 class="muted">Your Feedback For This Exchange</h5>
               <p><strong>${feedback.getExchangeRatingType().getEnumExtendedValues().getDescription()} </strong> <i class="icon-black ${feedback.getExchangeRatingType().getIcon()} "></i></p>
               <button id='rate_transaction' class="btn btn-block" type="button"> Change my feedback </button> 
            </div>
         </c:if>
          
         <c:if test="${not empty other_party_feedback}">
            <div class="alert" style="text-align:center">
               <h5 class="muted"><strong>${other_party.getFirstName()}'s</strong> Feedback For This Exchange</h5>
               <span><strong>${other_party_feedback.getExchangeRatingType().getEnumExtendedValues().getDescription()}</strong> <i class="icon-black ${other_party_feedback.getExchangeRatingType().getIcon()} "></i></span> 
               <br>
               <p class="lead black">
                  &#8220;${other_party_feedback.getMessage()}&#8221;
               </p>
            </div>
            <hr class="kagu-hr">
         </c:if>
         <c:if test="${transaction.getAdDiscussionState() == 'Accepted'}">
            <button id="cancel_exchange" class="btn btn-danger btn-large btn-block" type="button">Cancel This Exchange</button>
            <hr>
            <div style="text-align:center">
               <c:choose>
                  <c:when test="${user.getId() == transaction.getBuyer().getId() }">                 
                     <c:if test="${not empty transaction.getSeller().getSwapLocation()}">
                        <kagutags:infoboxtitle cssclass="buzzinfoboxtitle title-simple" title="The Seller&rsquo;s Preferred Meeting Spot" />
                        <kagutags:google_maps 
                           markertag="${transaction.getSeller().getSwapLocationName()}" 
                           address="${transaction.getSeller().getSwapLocation()}" 
                           showdirectionsonload="Y" 
                           zoom="13"/>
                        <hr class="kagu-hr">
                     </c:if>
                  </c:when>
                  <c:otherwise>
                     <c:if test="${not empty transaction.getBuyer().getSwapLocation()}">
                        <kagutags:infoboxtitle cssclass="buzzinfoboxtitle title-simple" title="The Buyer &rsquo;s Preferred Meeting Spot" />
                        <kagutags:google_maps 
                           markertag="${transaction.getBuyer().getSwapLocationName()}" 
                           address="${transaction.getBuyer().getSwapLocation()}" 
                           showdirectionsonload="Y" 
                           zoom="13"/>
                        <hr class="kagu-hr">
                     </c:if>
                  </c:otherwise>
               </c:choose>
            </div>
         </c:if>
         <c:if test="transaction.getAd().active()">
            <hr class="kagu-hr">
            <h5><a href="/ad/view/${transaction.getAd().getId()}/${transaction.getAd().getFriendlyURL()}">
               View The Ad
               </a>
            </h5>
         </c:if>
          <div class="buzzinfobox notitle post-box-padding">
         <kagutags:account_profile user_profile="${other_party}"></kagutags:account_profile>
        </div>
        </div>
      <kagutags:footer />
</div>
      <script type="text/javascript">
         $(".disable-me").children().children().children().children().children().attr('href',"javascript: return false;");
         $(".disable-me").children().children().children().children().children().attr('disabled', true);
         
         $('#cancel_exchange').click(function() {
             $('#modal_dialog').load('/modal/transaction/cancel/' + '${transaction.getId()}', function() {$('#modal_dialog').modal('show');});
         });
         
         $('#rate_transaction').click(function() {
             $('#modal_dialog').load('/modal/transaction/rate/'+ '${transaction.getId()}' + '/' + 0, function() {$('#modal_dialog').modal('show');});
         });  
         
         $('#make-an-offer-btn').click(function() { 
          $('#modal_dialog').load('/modal/ad_ask_contact_method/${transaction.getAd().getId()}', function() {$('#modal_dialog').modal('show');});
         });
         
         $('#pay_omnui').click(function() {

         });
      </script>
      <script src="/static/jquery/postoptionclick.js" type="text/javascript"></script>
   </body>
</html>
<kagutags:scroll_to_message/>
