<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<div class="modal-header" style="padding: 5px 5px 5px 15px;">
    <strong>${user_profile.getFirstName()}</strong>
</div>
        
<div class="modal-body">
    <kagutags:account_profile user_profile="${user_profile}"/>
</div>
<div class="clearfix"></div>
        <div class="modal-footer" style="padding: 5px;"> 
        <button class="btn" data-dismiss="modal" aria-hidden="true"><small>Close</small></button>
</div>