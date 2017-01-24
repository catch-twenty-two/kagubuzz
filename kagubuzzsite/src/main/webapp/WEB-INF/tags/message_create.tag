<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ attribute name="sender" required="true" type="com.kagubuzz.datamodels.hibernate.TBLUser"%>
<%@ attribute name="parentid" required="true" type="java.lang.String" description="Links message to event,ad,trans"%> 
<%@ attribute name="messageid" required="true" type="java.lang.String" description="Message box id"%> 
<%@ attribute name="domattachid" required="false" type="java.lang.String" description="Where to put new message in the dom"%>
<%@ attribute name="messageboxinitval" required="true" type="java.lang.String"%>
<%@ attribute name="ajaxurl" required="true" type="java.lang.String" description="Event,Ad or Discussion Post URL"%>
<%@ attribute name="messageboxtitle" required="false" type="java.lang.String" description="Where to put new message in the dom"%> 
<%@ attribute name="messageboxmaxchars" required="true" type="java.lang.String" description="Where to put new message in the dom"%> 
<%@ attribute name="save_button_message" required="true" type="java.lang.String" description="Where to put new message in the dom"%> 
<%@ attribute name="user_data" required="false" type="java.lang.String" description="user data structure"%> 
<%@ attribute name="message_data" required="false" type="java.lang.String" description="user data structure"%>
<%@ attribute name="cancel_button_message" required="false" type="java.lang.String" description="Where to put new message in the dom"%> 
<%@ attribute name="cancel_button_function" required="false" type="java.lang.String" description="Where to put new message in the dom"%> 
<%@ attribute name="use_redactor" required="false" type="java.lang.String" description="Use Redactor Instead of text editor"%> 
<%@ attribute name="hide_avatar" required="false" type="java.lang.String" description="Show the users avatar"%> 
<c:if test="${empty hide_avatar}">
   <div id="avatar_well" class="well well-small" style="height: 35;">
      <div class="pull-left">
         <img class="img-polaroid" style="height: 35px; width: 35px;" alt="${sender.getFirstName()}" src="/fileServerServlet?type=GetAvatarByUserId&amp;id=${sender.getId()}">
      </div>
      <div class="pull-left" style="margin-left: 5px;">
         <small>${messageboxtitle}</small><br>
      </div>
   </div>
</c:if>
<form class="form-horizontal" id="formcreatemessage${messageid}" style="margin-bottom:5px">
   <div id="message_input_box_div_${messageid}" class="control-group">
      <textarea onkeyup="textlimiter(${messageboxmaxchars}, '${messageid}')" 
         class="boxsizing-border" 
         id="newmessagebox${messageid}"               
         placeholder="${messageboxinitval}" 
         rows="<fmt:formatNumber value='${messageboxmaxchars/150}' maxFractionDigits='0'/>"
         style="height:100px; width:100%; resize:none;"><c:if test="${not empty message_data}">${message_data}</c:if></textarea>
   </div>
   <c:if test="${not empty use_redactor}">
      <script type="text/javascript">
         $('#collapsable_${messageid}').on('hide', function () {
             $('#newmessagebox${messageid}').setCode('');
             $('#newmessagebox${messageid}').destroyEditor();
            })
          $('#collapsable_${messageid}').on('show', function () {   
              $('#newmessagebox${messageid}').redactor({ autoresize: true ,minHeight: 100,  buttons: ['bold', 'italic','underline','fontcolor','backcolor','|',
                                                                                                      'unorderedlist', 'orderedlist', '|',
                                                                                                      'image', 'video', 'link', '|', 'alignment', '|', 'horizontalrule'] });
              monitorRedactorInput(10, '${messageboxmaxchars}', 'newmessagebox${messageid}');
          })
      </script>
   </c:if>
   <script type="text/javascript">
      $('#collapsable_${messageid}').on('hide', function () {
          setEditing(false);
         
          })
          $('#collapsable_${messageid}').on('show', function () { 
            setEditing(true);
          })
   </script>
</form>
<hr class="kagu-hr">
<div class="pull-right">
   <button type="button" onclick="createNewMessage('newmessagebox',
   '${messageid}',
   '${parentid}',
   '${messageboxinitval}', 
   '${domattachid}',
   '${ajaxurl}',
   '${messageboxmaxchars}'<c:if test="${not empty user_data }">,${user_data}</c:if>); return false;" id="post_btn_${messageid}" class="btn btn-small btn-info pull-right" autocomplete="off">${save_button_message}</button>
   <c:if test="${not empty cancel_button_message}">
      <button onclick="${cancel_button_function}; return false;" style="margin-right: 5px;" class="btn btn-small pull-right">${cancel_button_message}</button>
   </c:if>
   <c:if test="${not empty use_redactor}">
      <button onclick="$('#collapsable_${messageid}').collapse('hide');" 
         class="btn btn-small btn-danger pull-right" 
         style="margin-right: 5px;">Cancel</button>
   </c:if>
</div>
<div class="muted pull-left">
   <small>
   <span id="chars_remaining_newmessagebox${messageid}"><span>Characters remaining </span>${messageboxmaxchars}</span>
   </small>
</div>
<div class="clearfix"></div>