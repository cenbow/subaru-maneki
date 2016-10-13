//
// common
//
req_odoo_json = function(url, post_data, resp_success_handler, resp_failed_handler) {
    $.ajax({
        url: url,
        data: post_data,
        contentType: 'application/x-www-form-urlencoded',
        type: 'POST',
        success: resp_success_handler,
    }).fail(resp_failed_handler);
}

function alertModalOpen(text, duration) {
    var target = $('#alertModal');
    $('body').css('overflow', 'hidden');
    target.find('.alertContent').first().html(text);
    target.css({'opacity':'1','visibility':'visible'});
    setTimeout(function(){
        $('body').css('overflow', 'visible');
        target.css({'opacity':'0','visibility':'hidden'});
    }, duration, target);
}

showError = function(err_msg) {
    alertModalOpen(err_msg, 1500);
}

onReqFail = function() {
    showError("An error occurred. Please try again later.");  
}

redirectAfterSuccess = function(redirectUrl) {
    //var redirect_url = $.url().param('redirect');
    if (!redirectUrl) {
        notify_my_center();
        location = redirectUrl;
    } else {
        if (redirectUrl.indexOf('cart') > -1) {
            notify_cart();
        } else if (redirectUrl.indexOf('user_center') > -1) {
            notify_my_center();
        }
        location = redirectUrl;
    }
}


//
// login page
//
onFBLoginSuccess = function(resp) {
	if (resp == undefined){ 
		showError("Server error", 1500);
		return;
	}
	
	resp = JSON.parse(resp);
	console.log(typeof(resp));
	
    if (resp.result == 'Success') {
    	var redirectUrl = resp.redirectUrl;
        redirectAfterSuccess(redirectUrl);
    }else if(resp.result == 'Redirect'){
    	location = resp.redirectUrl;
    } else {
        showError(resp.message, 1500);
    }
}

backend_fb_login = function(resp) {
    console.log("backend_fb_login, resp: ", resp);
    var resp_json = JSON.parse(resp);
    url = '/auth/oauth_login';
    post_data = {
        'accessToken': resp_json.authResponse.accessToken,
        'loginType': 2,
        /*'expiresIn': resp_json.authResponse.expiresIn,*/
    };
    req_odoo_json(url, post_data, onFBLoginSuccess, onReqFail);
}

$(document).ready(function(){
    if (location.search.indexOf("cart") > -1) {
        if (location.search.indexOf("product_id") > -1) {
            $(".save_your_cart").show(500);
        } else {
            $(".view_your_cart").show(500);
        }
    } else if(location.search.indexOf("add_wishlist") > 0){
        $(".save_your_fav").show(500);
    }
    window.login = backend_fb_login;
});

onLoginSuccess = function(resp) {
    if (resp.result && resp.result.result == 'Success') {
        redirectAfterSuccess();
    } else {
        showError(resp.result.message);
    }
}


$("#login_btn").click(function() {
    var email = $('#loginEmail').val();
    var password = $('#loginPassword').val();
    if (!email || !password) {
        showError('Empty login/password');
        return;
    }
});

$("#fb_btn").click(function() {
    device_fb_login();
});

//
// signup page
//
$("#signupPassword1").change(function(){
    $("#signupPassword2").val($("#signupPassword1").val());
});

$("#signupPassword2").change(function(){
    $("#signupPassword1").val($("#signupPassword2").val());
});

$("input[type='checkbox']").change(function(){
    if($(this).is(':checked')){
        $("#signupPassword1").hide();
        $("#signupPassword2").show();
    } else{
        $("#signupPassword2").hide();
        $("#signupPassword1").show();
    }
});


onSignupSuccess = function(resp) {
    if (resp.result && resp.result.result == 'Success') {
        redirectAfterSuccess();
    } else {
        showError(resp.result.message, 2000);
    }
}

$("#signup_btn").click(function(){
    var has_error = false;
    console.log("signup begin");
    $(".signup_form input").each(function(){
        if ($(this).val() == "") {
            has_error = true;
        };
    });
    if (has_error) {
        showError("Please fill in all fields.", 1500);
        return;
    }
    var email_ptn = new RegExp("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$");
    var email = $('#signupEmail').val();
    if (!email_ptn.test(email)) {
        showError("Your email is invalid", 1500);
        return;
    }
});


// 
// reset password page
//

onResetPasswdSuccess = function(resp) {
    if (resp.result && resp.result.result == 'Success') {
        $('#alert_msg').html(resp.result.message).show(500);
    } else {
        showError(resp.result.message);
    }
}

$('#confirm_btn').click(function () {
    $('#alert_msg').hide(500);
    var email = $('#reset_email').val(); 
    if (!email) {
        showError("Empty email");
        return;
    } else {
        var url = "/auth/reset_password";
        var redirect_url = $.url().attr('source');
        if (redirect_url.indexOf('?') > -1) {
            redirect_url += ("&email=" + encodeURIComponent(email) + "&");
        } else {
            redirect_url += ("?email=" + encodeURIComponent(email) + "&");
        }
        post_data = {
            'email': email,
            'url': redirect_url, 
        }
        req_odoo_json(url, post_data, onResetPasswdSuccess, onReqFail);          
    }
});

onResetSuccess = function(resp) {
    if (resp.result && resp.result.result == 'Success') {
        redirectAfterSuccess();
    } else {
        showError(resp.result.message, 2000);
    }
}

$('#confirm_reset_btn').click(function() {
    var email = $('#signupEmail').val();
    var name = $('#signupName').val();
    var password = $('#signupPassword2').val();
    var token = $('#reset_token').val();
    url = "/auth/reset_password_with_token"; 
    post_data = {
        'email': email,
        'name': name,
        'password': password,
        'token': token, 
    }
    req_odoo_json(url, post_data, onSignupSuccess, onReqFail);
});


