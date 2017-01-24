<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<div class="modal-header" style="padding: 5px 5px 5px 15px;">
   <strong id="modalDialogLabel">
      <c:choose>
         <c:when test="${ad.isFirm() || ad.price == 0}">
            Contacting ${ad.getOwner().getFirstName()} about the Ad "${ad.getTitle()}"    
         </c:when>
         <c:otherwise>
            Making An Offer On "${ad.getTitle()}"
         </c:otherwise>
      </c:choose>
   </strong>
</div>
<div class="modal-body">   
   <p>${ad_contact_message}</p>   
   <small>
      <c:forEach var="method" items="${deliveryMethod}">
         <label class="radio">
            <c:choose>
               <c:when test="${user.getPhone() ==  null && (method == 'Voice' || method == 'Text')}">
                  <input type="radio" name="contact_method" id="contact_method" value="${method.name()}" disabled>        
                  <small> Via ${method.getEnumExtendedValues().getDescription()} (No Phone Number in Your Profile)</small>
               </c:when>
               <c:otherwise>
                  <input type="radio" name="contact_method" id="contact_method" value="${method.name()}"
                  <c:if test="${user.getContactMethod() == method.name()}">checked</c:if>
                  >       
                  <small>
                     Via ${method.getEnumExtendedValues().getDescription()}
                     <c:if test="${method == 'KaguBuzz'}">(No personal contact information will be exchanged.)</c:if>
                  </small>
               </c:otherwise>
            </c:choose>
         </label>
      </c:forEach>
   </small>
       <hr class="kagu-hr">
       <div id="payment_options">
   <c:if test="${ad.getPrice() != 0 && !ad.isFirm()}">
        <h5>Payment Options</h5>            
      <h5>How much are you willing to pay for this service/item?</h5>
      <div class="controls">
         <div class="input-prepend input-append">
            <span class="add-on">$</span> 
            <input class="span1" value="<c:if test="${ad.getPrice() != 0 && ad.getPrice() != null}">${ad.getPrice()}</c:if>"
            id="offer" name="offer" placeholder="Free" maxlength="6" size="16" type="text"> 
            <span class="add-on">.00</span>
         </div>
         <c:if test="${ad.getPrice() != 0 && ad.getAdGroup() == 'CommunityServices' }"><sup>${ad.getPerUnit().getEnumExtendedValues().getDescription()}</sup></c:if>
      </div>
   </c:if>
   <c:if test="${ad.isFirm() && ad.getPrice() != 0}">
      <input type="hidden" id="offer" value="${ad.getPrice()}">
      <strong>
        Ad is Set As Firm -
         <c:choose>
            <c:when test="${ad.getAdGroup() != 'CommunityServices'}">
               <kagutags:price no_compensation="Free" price="${ad.getPrice()}"/>
            </c:when>
            <c:otherwise>
               <kagutags:price no_compensation="Volunteer" price="${ad.getPrice()}"/>
               <c:if test="${ad.getPrice() != 0}"><sup>${ad.getPerUnit().getEnumExtendedValues().getDescription()}</sup></c:if>
            </c:otherwise>
         </c:choose>
      </strong>
   </c:if>
   </div>
      <c:if test="${ad.acceptsTimebanking()}">       
          <label class="checkbox ">
      <input type="checkbox" id="timebanking" name="timebanking"><strong><span class="darkgrey inline" >Request The Use Of <a href="http://omnui.com/Home/About" target="_blank">Omnui</a> For This Exchange</span></strong>
   </label>
   </c:if>
</div>
<div class="modal-footer" style="padding: 5px;"> 
   <button class="btn" data-dismiss="modal" aria-hidden="true"><small>Cancel</small></button>
   <button id="sendOffer" onClick="sendOffer()" data-dismiss="modal" class="btn btn-primary"><small>Send</small></button>
</div>
<script>
   $('#offer').numeric(false);
   $('#timebanking').click(function(){
	   $('#payment_options').toggle("slow")	   
   })
   function sendOffer() { 
   $.ajax({type: 'POST',
       url: '/ad/make_inquiry',
       data: { ad_id:'${ad.getId()}',
               offer:  $('#offer').val(),
               timebanking: $('#timebanking').val(),
               contact_method: $('input:radio[name=contact_method]:checked').val()
              },
       dataType: "json",
       success: function(data) {renderServerNotification(data);}});
   }
</script>