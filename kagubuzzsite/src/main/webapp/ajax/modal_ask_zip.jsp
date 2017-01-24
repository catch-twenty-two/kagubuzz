<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<div class="modal-header" style="padding: 5px 5px 5px 15px;">
    <strong id="modalDialogLabel">Please Provide Your Zip Code For A Better Experience</strong>
</div>

<div class="modal-body" >    
    In order to provide you with a more centralized and community oriented experience please provide your local zip code. 
    We will <strong>never</strong> share this information without your permission.
    <br>
    <br>        
     <input size="5" maxlength="5" name="password" id="zip_code_update" style="width: 75px" placeholder="Zip Code">
</div>

<div class="modal-footer" style="padding: 5px;">
   <button onclick="saveZip();" type="button" data-dismiss="modal" class="btn btn-primary"><small>Okay</small></button>
</div>

<script>
function saveZip() {
    
    var zipCode = $('#zip_code_update').val();

    if (zipCode == "") {
        $.cookie('zip_code', 'NA', { expires: 365 , path: '/'});
        return;
    }

    $.cookie('zip_code', zipCode, { expires: 365 , path: '/'});

    $.ajax({
        type: "POST",
        url: '/request_save_zip',
        data: {
            zipCode: zipCode
        },
        dataType: "json",
        success: function (data) {
            renderServerNotification(data)
        }
    });
}
</script>