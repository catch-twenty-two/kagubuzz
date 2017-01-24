<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ attribute name="bookmark" required="true" type="com.kagubuzz.datamodels.Bookmark"%>
<c:if test="${not empty bookmark.getUserFeedback(user)}">
   <c:if test="${bookmark.getClass().name == 'com.kagubuzz.datamodels.EventWithUserFeedback'}">
      <div id="rating_div_${bookmark.getId()}" >
         <c:choose>
            <c:when test="${bookmark.canBeRated()}">
               <c:choose>
                  <c:when test="${not empty bookmark.getUserFeedback(user)}">
                     <div id="rateit${bookmark.getId()}" data-rateit-resetable="false" data-rateit-value="${bookmark.getUserFeedback(user).getRating()}" class="rateit"></div>
                     <script type="text/javascript">
                        $('#rateit${bookmark.getId()}').bind('rated', function (event, value) { rateEvent('${bookmark.getId()}', value); });
                     </script>    
                  </c:when>
                  <c:otherwise>
                     <div onMouseOver="$('#nr${bookmark.getId()}').hide()" onMouseOut="$('#nr${bookmark.getId()}').show()" >
                        <div id="rateit${bookmark.getId()}" data-rateit-resetable="false" data-rateit-value="0" class="rateit">
                           <div id="nr${bookmark.getId()}" class="overlay-star-rating darkgrey"><small><i>Rate Me!</i></small></div>
                        </div>
                     </div>
                     <script type="text/javascript">
                        $('#rateit${bookmark.getId()}').bind('rated', function (event, value) { rateEvent('${bookmark.getId()}', 0); });
                     </script>
                  </c:otherwise>
               </c:choose>
            </c:when>
            <c:otherwise>
               <span class="bookmark-perm-rating">
                  <c:if test="${not empty bookmark.getUserFeedback(user)}">
                     <div class="rateit" data-rateit-resetable="false" data-rateit-value="${bookmark.getUserFeedback(user).getRating()}" data-rateit-readonly="true">
                        <div id="nr${bookmark.getId()}" class="overlay-star-rating"><small><i>Expired</i></small></div>
                     </div>
                  </c:if>
               </span>
            </c:otherwise>
         </c:choose>
      </div>
   </c:if>
   <c:if test="${bookmark.getClass().name == 'com.kagubuzz.datamodels.hibernate.TBLDiscussionAd'}">
      <c:choose>
         <c:when test="${not empty bookmark.getUserFeedback(user)}">
            <span>
                <a href="javascript:void(null)" class="darkgrey" id="rate_transaction_${bookmark.getId()}"><small>${bookmark.getUserFeedback(user).getExchangeRatingType().getEnumExtendedValues().getDescription()}</small> <i class="icon-black ${bookmark.getUserFeedback(user).getIcon()} "></i></a>
            </span>    
              <script>
                $('#rate_transaction_${bookmark.getId()}').click(function() {
                    $('#modal_dialog').load('/modal/transaction/rate/'+ '${bookmark.getId()}' + '/' + 0, function() {$('#modal_dialog').modal('show');});
                });  
              </script>
         </c:when>
         <c:otherwise>
            <c:if test="${bookmark.canBeRated()}">
                <a href="javascript:void(null)" class="darkgrey" id="rate_transaction_${bookmark.getId()}"><small><i>Rate Me!</i></small></a>
                <script>
                $('#rate_transaction_${bookmark.getId()}').click(function() {
                    $('#modal_dialog').load('/modal/transaction/rate/'+ '${bookmark.getId()}' + '/' + 0, function() {$('#modal_dialog').modal('show');});
                });  
                </script>
            </c:if>
         </c:otherwise>
      </c:choose>
   </c:if>
</c:if>
