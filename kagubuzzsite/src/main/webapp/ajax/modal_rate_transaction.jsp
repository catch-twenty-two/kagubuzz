<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<div class="modal-header" style="padding: 5px 5px 5px 15px;">
    <strong>Rating Your Exchange For The Ad "${transaction.getTitle()}"</strong>
</div>

<div class="modal-body" style="padding: 10px;">
<span class="help-inline">How would you rate your experience with ${transaction.getOppositeParty(user).getFirstName()}?</span>
<br>
<c:forEach items="${feedback_types}" var="feedback_type">
    <label class="radio inline">
     <i class="${feedback_type.getIcon()}"></i>    
        <input type="radio" name="thumbs_radios" value="${feedback_type.name()}" ${feedback_type.getEnumExtendedValues().isChecked()}>
         ${feedback_type.getEnumExtendedValues().getDescription()} 
    </label>
</c:forEach>
<script type="text/javascript">
$('input:radio[name=thumbs_radios]').filter('[value=${feedback.getExchangeRatingType().name()}]').attr('checked', true);
</script>
<hr>
    <kagutags:message_create
        hide_avatar="Y"
        save_button_message="Save Feedback"
        messageid="${transaction.getId()}"
        message_data="${feedback.getMessage()}"
        messageboxmaxchars="${transaction.messageType().getMaxLength()}"
        parentid="${transaction.getId()}"
        ajaxurl="/transaction/rate"
        cancel_button_message="Cancel"
        cancel_button_function="$('#modal_dialog').modal('hide');"
        messageboxinitval="Add any comments about the exchange here."
        user_data="$('input:radio[name=thumbs_radios]:checked').val()"
        sender="${user}"/>
</div>