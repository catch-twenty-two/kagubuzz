<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<div class="modal-header">
   <strong id="modalDialogLabel">Prove your not a robot</strong>
</div>
<div class="modal-body">
    <small>Type the two pieces of text below</small>
   <div id="recaptcha">
   </div>
   <script type="text/javascript">
      Recaptcha.create('${recaptcha_public_key}', 'recaptcha', { theme: 'clean', callback: Recaptcha.focus_response_field });
   </script>
</div>
<div class="modal-footer" style="padding: 5px;">
   <button data-dismiss="modal" class="btn" onclick="$('button[data-loading-text]').button('reset')"><small>Cancel</small></button>
   <button onclick="return createAccount();" class="btn btn-primary"><small>Submit</small></button>
</div>