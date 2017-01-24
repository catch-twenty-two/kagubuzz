
var orignalText = {};

function editMessage(id) {
	
    orignalText['messagebox' + id] = $('#messagebox'+ id).html();
    
	$('#messagebox'+ id).fadeOut(function() {
	    setEditing(true);
		$('#messagebox'+ id).redactor({ focus: true }); 
		$('#edit_btn_' + id).hide();
		$('#post_options_' + id).hide();
		$('#chars_remaining_messagebox' +id).show();
		monitorRedactorInput(10, 2500, 'messagebox' + id);
		$('#cancel_button' + id).fadeIn();
	    $('#save_button' + id).fadeIn();
	  });
	
    $('#messagebox' + id).fadeIn();

}

function resetMessageEdit(id, cancelEdit) {

	$('#messagebox' + id).destroyEditor(); 
	
	if(cancelEdit == true) {
		$('#messagebox' + id).html(orignalText['messagebox' + id]);
	}
	$('#chars_remaining_messagebox' +id).hide();
	$('#edit_btn_' + id).show();
	$('#post_options_' + id).show();
	$('#cancel_button' + id).hide();
    $('#save_button' + id).hide();
    
    setEditing(false);
}

function updateMessage(messageid, ajaxurl , messagedata) {

        $.ajax({
        type: "POST",
        url:  ajaxurl,
        data: { messageid: messageid, message: messagedata},
        dataType: "json",
        success: function(data) {
           renderServerNotification(data);
        }
    });
}