<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<div class="modal-header" style="padding: 5px 5px 5px 15px;">
    <strong id="modalDialogLabel">Please Provide Your Beta Test Code</strong>
</div>

<div class="modal-body" >    
    Hello! At this time Kagu Buzz user creation is by invite only.  If you don't have a code you are still free to browse the events and see what's going on
    in your area!<br><br>   
     <input size="9" name="password" id="beta_test_code" class="span2" placeholder="Test Code"> <br>
     
</div>

<div class="modal-footer" style="padding: 5px;">
   <button data-dismiss="modal" class="btn"><small>Cancel</small></button>
   <button onclick="saveBetaTestCode();" data-dismiss="modal" class="btn btn-primary"><small>Okay</small></button>
   <a href="mailto:help@kagubuzz.com?subject=Sign me up for the beta test!" class="pull-left"><small>I Want To Participate In The Test!</small></a>
</div>