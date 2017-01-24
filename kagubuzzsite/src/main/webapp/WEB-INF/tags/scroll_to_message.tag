<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${not empty scroll_id}">

<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js"></script>

<script>	
$(document).ready(function() 
{  					
	$('html, body').animate({scrollTop: $("#scroll_id${scroll_id}").offset().top - 45}, 3000);
    $('html, body').promise().done(function() {$("#scroll_id${scroll_id}").effect("highlight", {color: 'yellow'}, 3000);});
});
</script>
</c:if>