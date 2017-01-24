<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<div class="modal-header" style="padding: 5px 5px 5px 15px;">
   <strong>Canceling An Exchange With ${other_user.getFirstName()}</strong>
</div>
<div class="modal-body" >
You are about to cancel an accepted offer with ${other_user.getFirstName()} for the ad '${transaction.getAd().getTitle()}'
<br />
<br />
<strong>WARNING: Canceling an accepted offer may lead to bad feedback from ${other_user.getFirstName()}. Please be sure you have made every effort to notify ${other_user.getFirstName()} and have their ok, before doing so.</strong>
</div>
<div class="modal-footer" style="padding: 5px;"> 
   <button class="btn" data-dismiss="modal" aria-hidden="true"><small>Cancel</small></button>
   <button id="cancel_transaction" class="btn btn-primary"><small>Ok</small></button>
</div>
<script>   
   $('#cancel_transaction').click(function() {
	   
	   $('#modal_dialog').modal('hide');
	   
	   $.ajax({
           type: "POST",
           url:  '/transaction/cancel',
           data: { id: '${transaction.getId()}'},
           dataType: "json",
           success: function(data) {                         
        	   window.location.reload();
           }           
       });
   });
</script>
