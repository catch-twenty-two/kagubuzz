<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ attribute name="imessage" required="true" type="com.kagubuzz.datamodels.JSPMessageRenderer"%>
<%@ attribute name="ajaxurl" required="true" type="java.lang.String"%>
<%@ attribute name="optionbookmark" required="false" type="java.lang.String"%>
<%@ attribute name="optionremove" required="flase" type="java.lang.String"%>
<%@ attribute name="optionflag" required="flase" type="java.lang.String"%>
<%@ attribute name="optionbuzzit" required="false" type="java.lang.String"%>
<%@ attribute name="optiondiscussit" required="flase" type="java.lang.String"%>
<%@ attribute name="optionmoreinfo" required="flase" type="java.lang.String"%>

<div>
<div class="pull-left" style="margin-top: 8px;" id="post_options_${imessage.getId()}">
   <c:if test="${not empty optionbuzzit}">
      <c:if test="${imessage.getSender().getId() != user.getId()}">
         <!-- <small>
            
            <span id="${ajaxurl}${imessage.getId()}">
            <a href=""  class="text-warning optiontag">
                <img src="/static/images/buzzit.png">
                 BuZzZz This Up
            </a>
            </span>
            
            </small> -->
      </c:if>
   </c:if>
   <c:if test="${not empty optionbookmark  && user.isLoggedIn()}">
      <c:if test="${imessage.getSender().getId() != user.getId()}">
         <small>
                      
         <span id="bookmark${imessage.getId()}">
         <button title="Track and recieve updates on this post" onclick="postOptionClick('${ajaxurl}','bookmark','${imessage.getId()}'); return false;" class="text-info btn btn-mini optiontag">Bookmark <img src="/static/images/bookmark.png" alt="Bookmark"></button>
         </span>
         
         </small>
      </c:if>
   </c:if>
   <c:if test="${not empty optiondiscussit && user.isLoggedIn()}">
      <c:if test="${imessage.getSender().getId() != user.getId()}">         
         <small>
         
         <a title="Start a discussion about this event post" href="${ajaxurl}discuss/${imessage.getId()}" class="btn btn-mini">Discuss <img src="/static/images/discussit.png" alt="Discuss"></a>
                       
         </small>            
      </c:if>
   </c:if>
   <c:if test="${not empty optionremove && user.isLoggedIn()}">
      <c:if test="${imessage.getSender() == user || imessage.getParent().getSender() == user}">
         <small>
                         
         <span id="remove${imessage.getId()}">
         <button autocomplete="off" onclick="postOptionClick('${ajaxurl}','remove','${imessage.getId()}'); removeGenericMessage('${imessage.getId()}'); return false;" class="btn btn-mini edit-message-button">Delete Post <img src="/static/images/remove.png" alt="Remove"></button>
         </span>
         
         </small>
      </c:if>
   </c:if>
   <c:if test="${not empty optionflag && user.isLoggedIn()}">
      <c:if test="${imessage.getSender() != user && !imessage.isSystemMessage()}">
         <small>
         
         <span id="flag${imessage.getId()}">
         <button title="Let us know if this ad is inappropriate, spam, or miscategorized" onclick="flagPostDialog('${ajaxurl}','${imessage.getId()}', '${imessage.getSender().getFirstName()}'); return false;" class="btn btn-mini">Flag <img src="/static/images/flag.png" alt="Flag"></button>
         </span>
         
         </small>
      </c:if>
   </c:if>
</div>
<c:if test="${not empty optionmoreinfo}">

   <h4 class="pull-right title"><a href="${imessage.getViewingURL()}">More...</a></h4>

</c:if>
</div>
<div class="clearfix"></div>
