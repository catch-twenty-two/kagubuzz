function askZip(forceAsk) {

	if (forceAsk == undefined) {
		return;
	}

	var zipCode = $.cookie('zip_code');

	if ((zipCode == null || forceAsk == true) && zipCode != "NA") {
		$('#modal_dialog').load('/modal/ask_zip_code', function() { $('#zip_code_update').numeric(); $('#modal_dialog').modal('show'); });
	}
}

function changeZip() {
	$.removeCookie('zip_code', { path : '/'});
	askZip(true);
}