<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<div class="modal-header" style="padding: 5px 5px 5px 15px;">
    <strong>This Event Has Expired</strong>
</div>
    
<div class="modal-body" >
    Oops, it looks like this event has expired, click Search to find similar events or cancel to view this event.
</div>

<div class="modal-footer" style="padding: 5px;"> 
    <button class="btn" data-dismiss="modal" aria-hidden="true"><small>Cancel</small></button>
    <a href="/events/search?keywords=${event.getTitle()}" class="btn btn-primary"><small>Search</small></a>
</div>
