
function getNotifications() {
	
	 $.ajax({type: "GET",
	         url:  '/notifications',
	         dataType: "json",
	         success: function(data) {
	         
	        	 if(data.length == 0)  { return; }
	        	 
	        	 if(data.hasOwnProperty('redirect'))  {
	        		 window.location.href = "home"; 
	        		 return;
	        	  }
	        	
        	   for(var i = 0; i < data.length; i++) { 
        		   renderServerNotification(data[i]);
        	    }
	        	
        	   updateDOM(data.length);		        	
	        }
	 });
}

function updateDOM(messageCount) {
	
	// Update anything in the DOM that needs to reflect a new notification
	
	// Home Page
	
	if ($('#messagereloadbtn').length > 0) {
		$('#messagereloadbtn').addClass('btn-primary')
	};
	
	// Footer Message Count

	$('#new_message_count').text(parseInt($('#new_message_count').text()) + messageCount);
}

function reloadPageWithCleanURL() {
	document.location = location.protocol + "//" + location.host + location.pathname;
}

function renderServerNotification(data) {
	
	toastr.options.fadeIn = data.fadeIn;
    toastr.options.fadeOut = data.fadeOut;
    toastr.options.timeOut = data.timeOut;
    toastr.options.positionClass = data.position;
    
   // if(data.id) toastr.clear($toastlast);
    
    if(data.isModal == true)  {	        
    	 $('#modal_dialog').load('/modal/info/' + data.modalTemplateName, function() {$('#modal_dialog').modal('show');});
    	 return;
	}
    
	if(data.reload == true)  {	
		window.setTimeout(reloadPageWithCleanURL() , 500);
	}
	
    switch (data.alertType) {
    
    	case "user":
    		toastr.success(data.message, data.title);
    		$("#pageElementID").css("color", "#ff0000");
    		break;
    		
		case "success":
			toastr.success(data.message, data.title);
			break;
			
		case "info":
			toastr.info(data.message, data.title);    
		    break;
		    
		case "warning":
			toastr.warning(data.message, data.title); 
		    break;
		    
		case "error":
			toastr.error(data.message, data.title); 
		    break;
		    
		default:
			break;
	}
}
