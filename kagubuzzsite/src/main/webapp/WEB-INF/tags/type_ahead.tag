<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>

<%@ attribute name="ajaxurl" required="true" type="java.lang.String" description="Typeahead url"%>
<%@ attribute name="textareaid" required="false" type="java.lang.String" description="Textbox that type ahead is attached to"%> 
<%@ attribute name="textareaid1" required="false" type="java.lang.String" description="Textbox that type ahead is attached to"%> 

<script type="text/javascript">

<c:if test="${not empty textareaid}">
$('#${textareaid}').typeahead({source : typeAheadHelper});
</c:if>

<c:if test="${not empty textareaid1}">
    $('#${textareaid1}').typeahead({source : typeAheadHelper});
</c:if>

function typeAheadHelper(query, dataprocessor)
{
	$.ajax({
	    type: 'GET',
	    url:  '${ajaxurl}',
	    data: { query: query},
	    dataType: 'json',
	    success: function(data) {
	    	var newMessage = $(data);
	    	dataprocessor(data);
	    }
	});
}

</script>
