function postOptionClick(ajaxurl, action, id, data) {
	
	var optionid =  action + id;
	
	$.ajax({
	    type: "POST",
	    url:  ajaxurl + action,
	    data: { id: id, data: data},
	    dataType: "json",
	    success: function(data) {
	    		renderServerNotification(data);
				$("#" + optionid).fadeOut("slow", function() {
				$('#' + optionid + ' button').html(data.title);		
				$('#' + optionid + ' button').attr('disabled', 'disabled');
				$("#" + optionid).fadeIn("slow");
			});
	    }
	});
	
	return false;
}

function removeGenericMessage(id) {
	
	var message = $('#post_id_' + id);
	
	message.css('opacity', 0);  
    message.css('padding', 0);
    message.css('margin', 0);
    message.animate({height: 0},'slow',null,function(){ message.remove()});
}

function flagPostDialog(ajax_url, id, name) {
	$('#modal_dialog').load('/modal/flagpost', {name: name, ajax_url : ajax_url, id : id}, function() {$('#modal_dialog').modal('show');});		
}

