<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<%@ page trimDirectiveWhitespaces="true" %>

<div class="modal-header" style="padding: 5px 5px 5px 15px;">
    <strong>Flagging a post by ${name}.</strong>
</div>
    
<div class="modal-body" >
    <p><strong><small>Please let us know why you are flagging this post.</small></strong></p> 
   
    <c:forEach items="${flagtypes}" var="flagtype">
        <label class="radio">
            <input type="radio" name="flagRadios" value="${flagtype.name()}" ${flagtype.getEnumExtendedValues().isChecked()}>
            <small>${flagtype.getEnumExtendedValues().getDescription()}</small>             
        </label>
    </c:forEach>
</div>

<div class="modal-footer" style="padding: 5px;"> 
    <button class="btn" data-dismiss="modal" aria-hidden="true"><small>Cancel</small></button>
    <button onclick="postOptionClick('${ajax_url}', 'flag', '${id}', $('input:radio[name=flagRadios]:checked').val());" data-dismiss="modal" class="btn btn-primary"><small>Flag</small></button>
</div>