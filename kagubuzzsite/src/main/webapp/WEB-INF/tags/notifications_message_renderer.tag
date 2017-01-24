<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ attribute name="message" required="true" type="com.kagubuzz.datamodels.hibernate.TBLMessage"%>
<%@ attribute name="ajaxurl" required="false" type="java.lang.String"%>
<%@ attribute name="domattachid" required="false" type="java.lang.String"%>
<%@ attribute name="sender" required="true" type="com.kagubuzz.datamodels.hibernate.TBLUser"%>

<div id="${message.getParent().messageType()}${message.getId()}"  style="margin: 5px 0px 0px 0px;" class="buzzinfobox notitle five-px-padding">
    <a class="pull-left black" href="${message.getMessageViewingURL()}">
    <small><strong>Re: &quot;${message.getSubject()}&quot; </strong></small>
    </a>
    <button type="button"  class="close shifted" onclick="removeMessage('${message.messageType()}',${message.getId()}); return false;" >×</button><br>
    <div class="clearfix"></div>
    <div class="pull-left" style="margin: 15px 5px 0px 10px;  text-align: center;">
        <a href="javascript:void(null)" onclick="showUserProfile(${sender.getId()})">
        <img class="img-polaroid" style="height: 35px; width: 35px;" src="/fileServerServlet?type=GetAvatarByUserId&amp;id=${sender.getId()}" alt="${sender.getFirstName()}"/>
        </a>
    </div>
    <div>
        <div class="well well-small" style="margin: 5px 5px 5px 5px;">
            <div class="pull-left"></div>
            <span class="darkgrey" >
            <small><strong>${message.messageType().getName()}</strong> from 
            <a href="javascript:void(null)" onclick="showUserProfile(${message.getSender().getId()})">
            <strong>${message.getSender().getFirstName()}</strong>
            </a>
            sent via ${message.getDeliveryMethod().getEnumExtendedValues().getDescription()} </small>
            </span>
            <span class="pull-right">       
            <span><small>${message.timeSinceCreatedShortForm()}</small></span>
            </span>
        </div>
        <div class="clearfix"></div>
        <div class="pull-left">
            <small>${formatter.getSummary(message.getMessage(), 90)}</small>
        </div>
        <span class="pull-right">
        <small>
        &nbsp;
        <a href="${message.getMessageViewingURL()}">More&#8230;
        &nbsp;
        <img src="/static/images/details.png" alt="More Infomation"></a>              
        </small>
        </span>
    </div>
    <div class="clearfix"></div>
</div>