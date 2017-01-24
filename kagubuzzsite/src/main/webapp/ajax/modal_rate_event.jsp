<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<div class="modal-header" style="padding: 5px 5px 5px 15px;">
    <strong>Rating event "${event.getTitle()}"</strong>
</div>

<div class="modal-body" style="padding: 10px;">
	
	<kagutags:message_create
		save_button_message="Post Feedback"
		messageid="${event.getId()}"
		message_data="${feedback.getMessage()}"
		messageboxmaxchars="${event.messageType().getMaxLength()}"
		parentid="${event.getId()}"
		ajaxurl="/event/rate"
		cancel_button_message="Cancel"
		cancel_button_function="currentValue = -1; $('#modal_dialog').modal('hide');"
		messageboxinitval="Add a short review here. Your feedback will appear in the comments/ratings section at the bottom of the event page."
		user_data="$('#rateit_modal_${event.getId()}').rateit('value')"
		sender="${user}"/>
</div>

<script>

var newRating = false;
var currentValue = '${event_rating}';

if(currentValue == 0) {
	newRating = true;
	currentValue = $('#rateit${event.getId()}').rateit('value');
}

$('#avatar_well').append($('<div class="rateit" id="rateit_modal_${event.getId()}" data-rateit-value="'+ currentValue +'"></div><span class="pull-right" id="hover_modal_${event.getId()}"></span>'));
$('div.rateit').rateit();

setDescription($('#rateit_modal_${event.getId()}').rateit('value'));

$("#rateit_modal_${event.getId()}").bind('over', function (event, value) {setDescription(value)});

$('#modal_dialog').on('hidden', function () {

	    if(currentValue == -1)  {
	    	
	    	if(newRating) {
	    		  $('#rateit${event.getId()}').rateit('value', 0);
	              $('div.rateit').rateit();
	             return;
	         }	   
	    	
	    	$('#rateit${event.getId()}').rateit('value', '${feedback.getRating()}');
	    	
	    	return;
	    }
	    
        document.getElementById('rating_div_${event.getId()}').innerHTML = '<div id="rateit${event.getId()}" data-rateit-value="' + currentValue + '" class="rateit"></div>';
        $('div.rateit').rateit();
        $('#rateit${event.getId()}').bind('rated', function (event, value) { rateEvent('${event.getId()}', value); });
})


function setDescription(value) {
	   currentValue = value;
	   var description;
	
	   switch(value) {
	    case .5:
	    case 1:
	    case 1.5:
	        description = '\"What a Waste of Time!\"';
	        break;
	        
	    case 2:
	    case 2.5:
	        description = '\"I\'ve been to better.\"';
	        break;
	        
	    case 3:
	    case 3.5:
	        description = '\"It was okay.\"';
	        break;
	        
	    case 4:
	    case 4.5:
	        description = '\"Definitely worth it.\"';
	        break;
	        
	    case 5:
	        description = '\"Amazing! TOTALLY worth it.\"';
	        break;
	        
	    default:
	    	//setDescription($('#rateit_modal_${event.getId()}').rateit('value'));
	    	break;
	    }
	   	    	  
	    $('#hover_modal_${event.getId()}').text(description);	  
}
</script>