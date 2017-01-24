var createAccountForm;

function createAccount() {

    $('#recaptcha_challenge_field_submit').val(Recaptcha.get_challenge());
    $('#recaptcha_response_field_submit').val(Recaptcha.get_response());

    $('#modal_dialog').modal('hide');
    
    createAccountForm.submit();
    
    return false;
}


function getCaptcha(form) {
	
    $('#modal_dialog').load('/modal/ask_captcha', function () {
        $('#modal_dialog').modal('show');
    });
    
    createAccountForm  = form;
    
    return false;

}

function signInWithSocialAccount(form_id) {
	$('#' + form_id).submit();
	return false;
}

function login(email, password) {
    $.ajax({
        type: "POST",
        url: "/j_spring_security_check",
        data: {
            j_username: $('#' + email).val(),
            j_password: $('#' + password).val()
        },
        dataType: "json",
        success: function (data) {

            if (data.redirect) {
                // data.redirect contains the string URL to redirect to
                window.location.href = data.redirect;
            } else {
                $('button[data-loading-text]').button('reset');
                //Incorrect username or password - Display text at label
                document.getElementById("login_error").innerHTML = "Sorry, We couldn't find your email and password combination.";
                $("#login_password").val('');
            }
        }
    });

    return false;
};