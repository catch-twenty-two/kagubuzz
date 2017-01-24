<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true"%>
<script src="/static/jquery/jquery.numeric.js" type="text/javascript" /></script>
<style type="text/css">
.phone-number {
	
}
</style>
<div class="tab-pane fade" id="selling_buying">
	<div class="pull-right" style="width: 300px;">
		<kagutags:google_maps markertag="${user.getSwapLocationName()}"
			address="${user.getSwapLocation()}" showdirectionsonload="N"
			zoom="13" />
	</div>
	<div class="pull-left">

		<div class="control-group">		
			<label class="control-label"><strong>Preferred Meeting Spot</strong></label>
			<div class="controls">
				<input type="text" 
				maxlength="75" 
				class="span4" 
				id="address" 
				value="${user.getSwapLocation()}" 
				name="swap_location_address"
				placeholder="1313 Mockingbird Lane., Erie, PA"/>
				 <span class="pull-right">   <kagutags:pop_over placement="bottom" title="What is a Preferred Meeting Spot?" template_name="meetingSpot"></kagutags:pop_over></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="title"><strong>Spot
					Name</strong></label>
			<div class="controls">
				<input type="text" maxlength="75" class="span3" id="location_name"
					name="swap_location_name" value="${user.getSwapLocationName()}"
					placeholder="Starbucks" />
			</div>
		</div>
		      <div class="control-group pull-left">
                    <label class="control-label"><strong>Phone Number</strong></label>
                    <div class="controls">      
                        ( <input id="phone_a" class="phone-number" type="text" maxlength="3" size="3" style="width: 2em;" placeholder="###"> )
                          <input id="phone_b" class="phone-number" type="text" maxlength="3" size="3" style="width: 2em;"  placeholder="###"> - 
                          <input id="phone_c" class="phone-number" type="text" maxlength="4" size="4" style="width: 3em;"  placeholder="####">
                          <input name="phone_number" id="phone_number" value="${formatter.getBasePhoneNumber(user.getPhone())}" type="hidden"> 
                    </div>
                </div>
               <span style="margin-left:8px;" class="pull-right"> <kagutags:pop_over title="Verifing Your Phone Number" template_name="verifyPhone"/></span>  
              		<button name="verify_phone_btn" id="verify_phone_btn" type="button" class="btn pull-right" autocomplete="off">Verify Phone Number</button>
		<div class="control-group">
			<div class="controls">
				<label class="checkbox inline"> <input type="checkbox"
					id="sms_ad_notifications" name="sms_ad_notifications"
					${formatter.isChecked(user.isSmsAdNotifications())}>Receive
					Text Notifications On Your Phone For Ads You Post
				</label>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
    var phoneNumber = '${user.getPhone()}';

    $("a[rel=popover]").popover({
        trigger: 'hover'
    });

    $('#phone_a').numeric(false);
    $('#phone_b').numeric(false);
    $('#phone_c').numeric(false);

    $('a[href="#selling_buying"]').on('shown', function (e) {
        codeAddress($('#address').val(), 11);
    });

    $('#address').blur(function (e) {
        if ($('#address').val() == "") {
            $('#map_canvas').hide();
            return;
        } else {
            $('#map_canvas').show();
        }
        codeAddress($('#address').val(), 11);
    });

    $('#location_name').blur(function (e) {
        updateMarkerName($('#location_name').val());
    });

    $('.phone-number').blur(function () {

        $('#phone_number').val($('#phone_a').val() + $('#phone_b').val() + $('#phone_c').val());

        // Enable the verify button if user has entered enough digits

        if (($('#phone_number').val() != "") &&
        	("1" + $('#phone_number').val() != phoneNumber) &&
            ($('#phone_number').val().length == 10)) {
            setVerify();
        }

        return true;
    });

    function setVerify() {
        $('#verify_phone_btn').prop('disabled', false);
        $("#verify_phone_btn").html('Verify Phone Number');
        $('#sms_ad_notifications').prop('disabled', true);
        $('#phone_a').prop('disabled', false);
        $('#phone_b').prop('disabled', false);
        $('#phone_c').prop('disabled', false);
    }
    
    function setVerified() {
        $.removeCookie('phone_verification');
        phoneNumber =  "1" + $('#phone_number').val();
        $('#phone_a').val(phoneNumber.substring(1, 4));
        $('#phone_b').val(phoneNumber.substring(4, 7));
        $('#phone_c').val(phoneNumber.substring(7, 11));
        $('#phone_a').prop('disabled', false);
        $('#phone_b').prop('disabled', false);
        $('#phone_c').prop('disabled', false);
        $("#verify_phone_btn").html('Verified');
        $('#verify_phone_btn').prop('disabled', true);
        $('#sms_ad_notifications').prop('disabled', false);       
    }
    
    function setVerifying() {
        $('#phone_a').prop('disabled', true);
        $('#phone_b').prop('disabled', true);
        $('#phone_c').prop('disabled', true);
        $('#verify_phone_btn').prop('disabled', true);
        $("#verify_phone_btn").html('<img src="/static/images/lightbox/loading-arrow.gif"> Verifying... Please Wait');
        $('#sms_ad_notifications').prop('disabled', true);
        $('#sms_ad_notifications').prop('checked', false);
        $('#phone_a').prop('disabled', true);
        $('#phone_b').prop('disabled', true);
        $('#phone_c').prop('disabled', true);
    }

    $(document).ready(function () {

        if (phoneNumber == "") {
            $('#sms_ad_notifications').prop('checked', false);
            $('#sms_ad_notifications').prop('disabled', true);
            $('#verify_phone_btn').prop('disabled', true);
            return;
        } else {
            $('#phone_a').val(phoneNumber.substring(1, 4));
            $('#phone_b').val(phoneNumber.substring(4, 7));
            $('#phone_c').val(phoneNumber.substring(7, 11));
        }

        if (${user.getPhoneVerified()}) {
        	setVerified();
        } else {
            setVerify();
        }

        var phoneCookie = $.cookie('phone_verification');

        if (phoneCookie) {
            setVerifying();
            startTimers();
        }

        if ($('#address').val() == "") {
            $('#map_canvas').hide();
        }
    });

    $('button[data-loading-text]').click(function () {
        $(this).button('loading');
    });

    $('#verify_phone_btn').click(function () {
    	setVerifying();
        $.ajax({
            type: "POST",
            url: '/verify_phone',
            data: {
                phone_number: $('#phone_number').val()
            },
            dataType: "json",
            success: function (data) {
            	
                if (data.alertType == "error") {
                    renderServerNotification(data);
                    setVerify();
                    return;
                } 
                
                if (data.alertType == "success") {
                	renderServerNotification(data);
                	setVerified();
                	return;
                }
                
                $.cookie('phone_verification', true, {
                    expires: new Date(new Date().getTime() + 600000),
                    path: '/'
                });

                startTimers();
                renderServerNotification(data);
            }
        });

    });

    function startTimers() {

        // Time out after 10 minutes of waiting

        var timeout = setTimeout(function () {
            window.clearInterval(checkVerified);
            setVerify();
            $('#modal_dialog').load('/modal/info/account_no_verify_phone', function () {
                $('#modal_dialog').modal('show');
            });
        }, 60000 * 10);
        
        // Query the server on status of verification

        var checkVerified = setInterval(function () {

            $.ajax({
                type: "POST",
                url: '/verifying_phone',
                dataType: "json",
                success: function (data) {

                    if (data.alertType == "info") {
                    	renderServerNotification(data);
                        return;
                    }
                    
                    setVerified();
                    window.clearInterval(checkVerified);
                    window.clearInterval(timeout);
                }
            });
        }, 30000);
    }
</script>