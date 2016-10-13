//
// common
//
req_odoo_json = function(url, post_data, resp_success_handler, resp_failed_handler) {
    $.ajax({
        url: url,
        data: JSON.stringify(post_data),
        contentType: 'application/json',
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


redirectAfterLogout = function() {
    if (is_device()) {
        notify_product_list();
    } else {
        location = '/product_list';
    }
}

onLogoutSuccess = function(resp) {
    if (resp.result.result == 'Success') {
        device_fb_logout();
        redirectAfterLogout();
    } else {
        showError(resp.result.message);
    }
}

onLogoutFail = function() {
    showError("An error occurred. Please try again later.");  
}

$("#logout_btn").click(function() {
//    var url = "/auth/logout";
//    var post_data = {};
//    req_odoo_json(url, post_data, onLogoutSuccess, onLogoutFail);
	location = "/auth/logout";
})

