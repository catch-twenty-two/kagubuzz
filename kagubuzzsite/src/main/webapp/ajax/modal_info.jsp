<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>

<div class="modal-header" style="padding: 5px 5px 5px 15px;">
    <strong id="modalDialogLabel">
        ${modal_title}
    </strong>
</div>

<div class="modal-body" >
    <small>${modal_body}</small>
</div>

<div class="modal-footer" style="padding: 5px;"> 
     <a href="javascript:void(null)" data-dismiss="modal" class="btn btn-primary"><small>Okay</small></a>
</div>
