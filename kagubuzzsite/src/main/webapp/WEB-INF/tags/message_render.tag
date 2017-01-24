<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>

<%@ attribute name="imessage" required="true" type="com.kagubuzz.datamodels.JSPMessageRenderer"%>
<%@ attribute name="display_alternate" required="false" type="java.lang.String"%>
<%@ attribute name="ajaxurl" required="false" type="java.lang.String" description="If the ajaxurl is sent in as missing all ajax request will be sent to the current page" %>
<%@ attribute name="domattachid" required="false" type="java.lang.String"%>
<%@ attribute name="optionbookmark" required="false" type="java.lang.String"%>
<%@ attribute name="optionremove" required="flase" type="java.lang.String"%>
<%@ attribute name="optionflag" required="flase" type="java.lang.String"%>
<%@ attribute name="optionbuzzit" required="false" type="java.lang.String"%>
<%@ attribute name="optiondiscussit" required="flase" type="java.lang.String"%>
<%@ attribute name="optionmoreinfo" required="flase" type="java.lang.String"%>
<%@ attribute name="use_redactor" required="false" type="java.lang.String"%>
<%@ attribute name="optionreply" required="false" type="java.lang.String"%>
<%@ attribute name="optionedit" required="false" type="java.lang.String"%>
<%@ attribute name="showsubject" required="false" type="java.lang.String"%>
<%@ attribute name="message" required="false" type="com.kagubuzz.datamodels.hibernate.TBLMessage"%>
<%@ attribute name="summarize" required="false" type="java.lang.Integer"%>
<%@ attribute name="rating" required="false" type="java.lang.Double"%>
<%@ attribute name="showTitle" required="false" type="java.lang.String"%>
<%@ attribute name="tags" required="false" type="java.util.Set"%>

<div id='post_id_${imessage.getId()}' class="container-inner global-margin-top global-margin-bottom">
   <div>
   <c:if test="${not empty showTitle}">
   <div class="global-margin-bottom">
            <a href="${imessage.getViewingURL()}" class="lead black">
         <img class="pull-right" src="/static/images/${imessage.getIconName()}" alt="">
         ${imessage.getTitle()}</a>
                  </div>
         </c:if>

      <c:if test="${empty showsubject}">
         <c:if test="${not empty message}">
            <c:if test="${not empty message.getSubject()}">
               <c:if test="${not empty message.getRecipient()}">
                  <small>
                  <strong> @${message.getRecipient().getFirstName()} </strong>
                  Re: ${message.getSubject()}
                  </small>
               </c:if>
            </c:if>
         </c:if>
      </c:if>
   </div>
   <div  id="scroll_id${imessage.getId()}">
      <c:choose>
         <c:when test="${imessage.isSystemMessage() && not empty system_user}">
            <kagutags:message_render_sender display_alternate="${display_alternate}" imessage="${imessage}" alert_class="alert-danger" sender="${system_user}"/>
         </c:when>
         <c:otherwise>
            <kagutags:message_render_sender display_alternate="${display_alternate}" imessage="${imessage}" sender="${imessage.getSender()}"/>
         </c:otherwise>
      </c:choose>

      <div title="summary of this event" id="messagebox${imessage.getId()}" style="overflow: auto;">
         <c:choose>
            <c:when test="${not empty summarize}">
               ${formatter.getSummary(imessage.getMessage(), summarize)}
            </c:when>
            <c:otherwise>
               ${imessage.getMessage()}
            </c:otherwise>
         </c:choose>
      </div>

      <div style="margin-top: 5px;">
         <c:if test="${not empty optionreply && user.isLoggedIn()}">
            <c:if test="${message.recipientCanReply() && (message.getRecipient().getId() == user.getId()) || message.getPublicCanReply()}">
               <c:if test="${message.getSender().getId() != user.getId()}">
                  <span class="pull-left" style="margin-right: 5px;">
                  <small>   
                   <button autocomplete="off" class="btn btn-mini btn-primary edit-message-button" 
                     href="#collapsable_${imessage.getId()}" 
                     data-parent="#threadgroup" 
                     data-toggle="collapse" 
                     id="reply-${imessage.getId()}" 
                     >Reply to ${imessage.getSender().getFirstName()}</button>                  
                  </small></span>
               </c:if>
            </c:if>
         </c:if>
         <c:if test="${not empty optionedit && user.isLoggedIn()}">
            <c:if test="${imessage.getSender() == user}">
            <div class="pull-left"  style="margin-right: 10px;">
               <small>
               &nbsp;
               <span id="chars_remaining_messagebox${imessage.getId()}"></span>
               <span id="edit_btn_${imessage.getId()}">
               <span id="edit${imessage.getId()}">
               <button onclick="editMessage('${imessage.getId()}'); return false;"  class="btn btn-mini btn-warning edit-message-button">Edit</button>               
               </span>
               </span>
               &nbsp;
               </small>
               </div>
                 <button onclick="if(!checkRedactorInput()) return;
                  updateMessage('${imessage.getId()}', 'edit', $('#messagebox${imessage.getId()}').getCode());                            
                  resetMessageEdit(${imessage.getId()}, false);" 
                  id="save_button${imessage.getId()}" 
                  class="btn btn-small btn-info pull-right" 
                  data-loading-text="One Moment..." autocomplete="off">Save Changes</button>                        
               <button onclick="resetMessageEdit(${imessage.getId()}, true);" 
                  id="cancel_button${imessage.getId()}" 
                  class="btn btn-small btn-danger pull-right" style="margin-right: 5px;">Cancel</button>                                    
               <script type="text/javascript">
                  $('#save_button${imessage.getId()}').hide();
                  $('#cancel_button${imessage.getId()}').hide();
               </script>
            </c:if>
         </c:if>
         <kagutags:post_options 
            ajaxurl="${ajaxurl}"
            optionbookmark="${optionbookmark}"
            optionbuzzit="${optionbuzzit}"
            optiondiscussit="${optiondiscussit}"
            optionflag="${optionflag}"
            optionmoreinfo="${optionmoreinfo}"
            optionremove="${optionremove}"
            imessage="${imessage}"/>
      </div>

      <c:if test="${not empty message}">
         <c:if test="${not empty optionreply}">
            <c:if test="${message.recipientCanReply() && (message.getRecipient() == user) || message.getPublicCanReply()}">
               <div id="collapsable_${message.getId()}" class="global-margin-top collapse" >
                  <div class="accordion-inner">
                     <kagutags:message_create
                        save_button_message="Post Reply"
                        use_redactor="${use_redactor}"
                        messageid="${message.getId()}"
                        messageboxmaxchars="${message.messageType().getMaxLength()}"
                        sender="${user}"                            
                        parentid="${imessage.getParent().getId()}"
                        ajaxurl="${ajaxurl}reply"
                        domattachid="message${message.getId()}"
                        messageboxtitle="<strong>@${message.getSender().getFirstName()}</strong> Re: ${message.getSubject()}"
                        messageboxinitval="Type your response to ${message.getSender().getFirstName()}s message here."/>
                  </div>
               </div>
            </c:if>
         </c:if>
      </c:if>
   </div>
             
               
        <c:if test="${not empty tags}">
          <hr class="style-one">
            <div style="margin-top: 12px;">
         <small class="muted pull-left" style="margin-right: 10px; line-height: 17px">Tags: </small>
         <c:forEach var="tags" items="${tags}">
            <a href="#${tags.getName()}" onclick="notifyAdded('${tags.getName()}')" class="myTagBreadCrumb muted">${tags.getName()}</a>
         </c:forEach>
         </div>  
           <div class="clearfix"></div>
      </c:if>
     
   <div  id="message${imessage.getId()}"></div>
</div>
<div class="clearfix"></div>