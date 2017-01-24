
var characterMin;
var characterMax;
var usingRedactor = false;
var redactorId;
var editing = false;
var charactersNeeded;

function monitorRedactorInput(minChars, maxChars, redId) {
	characterMin = minChars;
	characterMax = maxChars;
	usingRedactor = true;
	setEditing(true);
	redactorId = redId;
	document.getElementsByTagName('body')[0].onkeyup = function(e) { 
		 updateRedactorInput(redactorId);
	}
	
	updateRedactorInput(redactorId);
}

function updateRedactorInput(redactorId) {
	var redactorBox = $('#' + redactorId);
	var body = $(redactorBox).getText();
	var charsRemainingNotify = $('#chars_remaining_' + redactorId);
	var charactersUsed = body.toString().length;
	var charsRemaining = characterMax - charactersUsed;
	charactersNeeded = (characterMin - charactersUsed);
	
	if(charsRemaining < 0 ) {
		charsRemainingNotify.html('<span class="kagu-red">Characters remaining 0. (Over by ' + charsRemaining *-1 + ' characters)</span>');
		return;
	}
	
	if(charactersUsed < characterMin) {
		charsRemainingNotify.html('<span class="kagu-red">You need to add ' + charactersNeeded + ' more characters before you can post or save.</span>');
	}
	else {
		charsRemainingNotify.html("You can post or save now. Space remaining for this post " + (charsRemaining) + " characters.");	
	}
}

function checkRedactorInput() {
	
	if(usingRedactor == false) return true;
	
	var body = $("#"+redactorId).getText();
	
	if(body.toString().length < characterMin) {
		toastr.error("Your posting needs more details, it's to short! Try entering a bit more of a description before submitting it.", charactersNeeded + ' More Characters Needed!');

		return false;
	}
	
	if(body.toString().length > characterMax) {
		toastr.error('Woah! Your posting is too long, try and making to more brief and then resubmit it.', 'Woah Nelly!');

		return false;
		
	}
	
	usingRedactor = false;
	 
	return true;
}

// called by a text area

function textlimiter(characterMax, messageboxid) {
	
	var messagebox = $('#newmessagebox' + messageboxid);

    var charactersUsed = $(messagebox).val().length;
    
    if(charactersUsed > characterMax)  {  
        charactersUsed = characterMax;  
        $(messagebox).val($(messagebox).val().substr(0, characterMax));  
        $(messagebox).scrollTop($(messagebox)[0].scrollHeight);  
    }
    
    var totalChars = characterMax - charactersUsed;
    
    $('#chars_remaining_newmessagebox' + messageboxid).html('<span>Characters remaining </span>' + totalChars);
}

function setEditing(editingMessage) {
	editing = editingMessage;
	
	if(editingMessage == false) {
		$('.edit-message-button').prop('disabled', false);
	} 
	else {
		$('.edit-message-button').prop('disabled', true);
	}
}

function showEditingMessage() {
	toastr.error("Please save or cancel the message you are currently editing before editing a new one.");
	return false;
}

function noLimitBoxError(message) {
	toastr.error(message);
	return false;
}
