var similarPopoverShown = false;
var similarPopoverShownTimer = null;

$('#title').popover({
	trigger : "manual",
	placement : "bottom",
	html : true
});

$('#title').keyup(function() {
	var title = $('#title').val();

	if (title.length < 4 ) {
		
		hideSimilarPopOver();
		
		if(similarPopoverShownTimer != null) {
			clearInterval(similarPopoverShownTimer);
			similarPopoverShownTimer = null;
		}
		
		return;
	}

	if(similarPopoverShownTimer == null){
		getSimilarEvents();
		similarPopoverShownTimer = setInterval(getSimilarEvents, 3000);
	} 
  
});

function getSimilarEvents() {
	var title = $('#title').val();
	
	$.ajax({
		type : 'GET',
		url : '/event/similar',
		data : {
			query : title
		},
		dataType : 'json',
		success : function(data) {
			var eventList = $(data);
			var similarHtml = "";
			var popover = $('#title').data('popover');
			
			if(eventList.length <= 0) {
				 hideSimilarPopOver();
				return;
			};
			
			for ( var i = 0; i < eventList.length; i++) {
				similarHtml += eventList[i] + '<br>';
			}

			$('#title').attr('data-content', similarHtml);
			popover.setContent();
			popover.$tip.addClass(popover.options.placement);

			if (!similarPopoverShown) {
				showSimilarPopOver();
			}
		}
	});
}

function hideSimilarPopOver() {
	
	$('#title').popover('hide');
	similarPopoverShown = false;
}

function showSimilarPopOver() {
	
	$('#title').popover('show');
	similarPopoverShown = true;
	
}

$('#title').blur(function() {
	similarPopoverShown = false;
	$('#title').popover('hide');
});