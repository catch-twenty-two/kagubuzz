<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ attribute name="message" required="true" type="com.kagubuzz.datamodels.hibernate.TBLMessage"%>
<%@ attribute name="ajaxurl" required="true" type="java.lang.String"%>
<%@ attribute name="domattachid" required="false" type="java.lang.String"%>
<%@ attribute name="system_user" required="true" type="com.kagubuzz.datamodels.hibernate.TBLUser"%>

<div class="clearfix"></div>
<p>${message.getMessage()}</p>
<div class="clearfix"></div>
<div style="margin-top: 5px;">
	<c:if test="${message.recipientCanReply()}">
		<c:if test="${message.getSender().getId() != user.getId()}">
			<span><small><a class="accordion-toggle" href="#${message.getId()}" data-parent="#threadgroup" data-toggle="collapse" id="${ajaxurl}-reply-${message.getId()}" class="text-info">Reply to ${message.getSender().getFirstName()}</a></small></span>
		</c:if>
	</c:if>
</div>
<c:if test="${message.recipientCanReply()}">
<div id="${message.getId()}" class="collapse" >
	<div class="accordion-inner">
    	<kagutags:message_create
    	        save_button_message="Send Reply"
    	        messageid="${message.getId()}"
    	        messageboxmaxchars="${message.messageType().getMaxLength()}"
    			sender="${user}"
    			parentid="${message.getId()}"
				ajaxurl="${ajaxurl}"
				domattachid="${domattachid}"
				messageboxtitle="<strong>@${message.getSender().getFirstName()}</strong> Re: ${message.getSubject()}"
				messageboxinitval="Send a message to ${message.getSender().getFirstName()}."/>
	</div>
</div>
</c:if>
<hr class="kagu-hr">