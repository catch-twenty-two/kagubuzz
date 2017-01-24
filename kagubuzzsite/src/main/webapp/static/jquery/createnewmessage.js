/**
 * 
 * Makes bootstrap labels clickable and ajaxable
 * 
 */

function createNewMessage(messageboxname, 
						  id,
						  parentid, 
						  messageboxvalue, 
						  domattachid, 
						  ajaxurl, 
						  maxChars,
						  userData) {
	
	var postButton = $('#post_btn_' + id);

	if(!checkRedactorInput()) {
		return;
	}
	
	var messagedata = $('#' + messageboxname + id).val();
	
	if(!messagedata) {
		noLimitBoxError("Oops! We need some text in the text field before you can save your feedback, comment or question.");
		$('#message_input_box_div_' + id).addClass("control-group error");
		return false;
	}
	
	if(domattachid) {
		document.getElementById('formcreatemessage' + id).reset();
		document.getElementById('chars_remaining_newmessagebox' + id).innerHTML = maxChars;
	}
	
	var postBtnMessage = postButton.html();
	
	$(postButton).prop('disabled', true);	
	$(postButton).html('One Moment...');
	
	$.ajax({
	    type: "POST",
	    url:  ajaxurl,
	    data: { messageid: id, parentid: parentid, message: messagedata, userdata: userData},
	    dataType: "html",
	    success: function(data) {
	    	// If we dont have a dom to attach the new message to assume 
	    	// the original was sent via a modal dialog
	     
	    	if(!domattachid) { 
	    		$('#modal_dialog').modal('hide');
	    		 renderServerNotification(JSON.parse(data));
	    		return;
	    	}
		    	
		    var newMessage = $(data);
		    
		    $('#collapsable_' + id).collapse('hide');
		    newMessage.css('opacity', 0);
		    newMessage.prependTo('#' + domattachid);
		    newMessage.hide();
		    $('#messagebox' + id).val("");
		    newMessage.slideDown('slow');	   
		    
		    newMessage.promise().done(function()
		    {
		    	newMessage.animate({opacity: 1},'slow');		    			
		    });		   
		    
			$(postButton).prop('disabled', false);
			$(postButton).html(postBtnMessage);
	    }
	});
}