
var g_ga_data = {};
$(document).ready(function() {
    var publishable_key = $('#payment_publishable_key').val();
    Stripe.setPublishableKey(publishable_key);
    $('input#cc-num').payment('formatCardNumber');
    query_ga_data();
});

function query_ga_data() {
	/*
    var order_id = $('#payment_order_id').val();
    $.ajax({
            url: "/order/" + order_id,
            success: function (resp) {
                console.log("ga data: ", resp);
                g_ga_data = resp;
            },
            type: 'GET',
    }).fail(function () {
        console.log("query ga order info failed");
    });
    */
}


jQuery(function($) {
    $('#cc-number').payment('formatCardNumber');
    $('#cc-exp').payment('formatCardExpiry');
    $('#cc-cvc').payment('formatCardCVC');

    $.fn.toggleInputError = function(erred) {
        this.parent('.form-group').toggleClass('has-error', erred);
        return this;
    };

});
  
jQuery(function($) {
    $('#cc-number').on('input', function(e) {
        var cardType = $.payment.cardType($('.cc-number').val());
        $('#card-type').toggleClass('');
        if (cardType !== null) {
            $('#card-type').addClass(cardType.concat('-card'));
        }
        clearError(this);
    });
});

jQuery(function($) {
    $('#cc-exp').on('input', function(e) {
        clearError(this);
    });
});

jQuery(function($) {
    $('#cc-cvc').on('input', function(e) {
        clearError(this);
    });
});

jQuery(function($) {
    $('#cc-number').on('keyup', function(e) {
        var cardType = $.payment.cardType($('#cc-number').val());
        $('#card-type').attr('class', '');
        if (cardType !== null) {
            $('#card-type').addClass(cardType.concat('-card'));
        }
    });
});

function showError(msg) {
    $('#error_msg').text(msg);
    $('#error_msg').show(500);
    ga_stripe_failed(msg);
}
function clearError(el) {
    $('#error_msg').text("");
    $('#error_msg').hide(500);
    $(el).toggleInputError(false);
};

function disableElement() {
	var $form = $('#new-payment-form');
	$form.find('button').prop('disabled', true);
	$('#cc-number').prop('disabled', true);
	$('#cc-exp').prop('disabled', true);
	$('#cc-cvc').prop('disabled', true);
};
function enableElement() {
	var $form = $('#new-payment-form');
	$form.find('button').prop('disabled', false);
	$('#cc-number').prop('disabled', false);
	$('#cc-exp').prop('disabled', false);
	$('#cc-cvc').prop('disabled', false);
};

function showLoading() {
	$('#btn-normal').attr("style", "display: none !important");
	$('#btn-loading').attr("style", "display: inline-block !important");
};
function hideLoading() {
	$('#btn-loading').attr("style", "display: none !important");
	$('#btn-normal').attr("style", "display: inline-block !important");
}
function showComplete() {
	$('#btn-loading').attr("style", "display: none !important");
	$('#btn-complete').attr("style", "display: inline-block !important");
}


jQuery(function($) {
    $('#new-payment-form').submit(function(e) {
        e.preventDefault();

        var number_valid = $.payment.validateCardNumber($('#cc-number').val());
        if (!number_valid) {
            $('#cc-number').toggleInputError(true);
        }
        var expiry_valid = $.payment.validateCardExpiry($('#cc-exp').payment('cardExpiryVal'));
        if (!expiry_valid) {
            $('#cc-exp').toggleInputError(true);
        }
        var cardType = $.payment.cardType($('#cc-number').val());
        var cvc_valid = $.payment.validateCardCVC($('#cc-cvc').val(), cardType);
        if (!cvc_valid) {
            $('#cc-cvc').toggleInputError(true);
        }
        if (!number_valid || !expiry_valid || !cvc_valid) {
            return false;
        }

        showLoading();
        disableElement();

        // Disable the submit button to prevent repeated clicks
        var $form = $(this);
        //$form.find('button').prop('disabled', true);
        var expiry_info = $('#cc-exp').val().split(' / ');
        Stripe.card.createToken({
            number: $('#cc-number').val(),
            cvc: $('#cc-cvc').val(),
            exp_month: expiry_info[0],
            exp_year: expiry_info[1],
        }, stripeResponseHandler);
        // Prevent the form from submitting with the default action
        return false;
    });
});


function stripeResponseHandler(status, response) {
    var $form = $('#new-payment-form');

    if (response.error) {
        // Show the errors on the form
        showError(response.error.message);
        hideLoading();
        enableElement();
    } else {
        // response contains id and card, which contains additional card details
        var token = response.id;
        /*
        $.ajax({
                url: "/payment/stripe/ipn",
                data: JSON.stringify({
                    jsonrpc: "2.0",
                    method: "call",
                    id: Math.floor(Math.random() * 1000 * 1000 * 1000),  
                    params: {
                        token_id: token, 
                        token_email: '', 
                        amount: $('#payment_amount').val(),
                        item_name: $('#payment_item_name').val(),
                        item_number: $('#payment_item_number').val(),
                    }
                }), 
                success: chargeResopnseHandler,
                contentType: 'application/json',
                type: 'POST',
        }).fail(chargeError);
        */
        var post_data = {};
        post_data["tokenId"] = token;
        //先固定
        post_data["orderNumber"] = $('#payment_order_id').val();
        $.ajax({
            url: "/api/trade/pay/stripe/ipn",
            data: post_data,
            success: chargeResopnseHandler,
            contentType: 'application/x-www-form-urlencoded',
            type: 'POST',
    }).fail(chargeError);
    }
};

function chargeError() {
    showError("Charge error.Please try again!");
    hideLoading();
    enableElement();
};

function callValidate(redirectUrl) {
	console.log(redirectUrl);
    //location = "/trade/pay_success?orderNumber" + $('#payment_order_id').val();
	location = redirectUrl;
};

//不同版本的app在支付成功后跳到的页面不一样，这个逻辑待重构完后根据app联调的情况来定
function redirectAfterPaid() {
    var order_id = $('#payment_order_id').val();
    var version = Cookies.get('v');
    if (navigator.userAgent.indexOf('ios_native_navbar') > -1 && !version) {
        setTimeout(function() {$(location).attr('href', '/user_center/orders')}, 3000);
    } else {
        history.pushState({}, "Orders", "/user_center/orders");
        setTimeout(function() {$(location).attr('href', '/payment/complete/' + order_id)}, 1500);
    }
}

function chargeResopnseHandler(resp) {
    var result = resp;
    if (_.isString(result)) {
        result = JSON.parse(result);
    }
    console.log(result);
    if (result.isSuccess == 0) { // success
    	showError(result.msg);
        hideLoading();
        enableElement();
    }else if(result.isRedirect == 1){
        showComplete();
        ga_stripe_success();
        device_payment_success(g_ga_data.amount_total);
        callValidate(result.redirectURL);
    } else if (result.isSuccess == 2) { // already paid，这个后面再添加相关的
        showError(result.msg);
        showComplete();
        callValidate(result.redirectURL);
    } else { // error
        ;
    }
};

function ga_stripe_failed(err_msg) {
	/*
    ga('send', {
        hitType: 'event',
        eventCategory: 'Checkout',
        eventAction: 'Stripe Pay Failure',
        eventLabel: $('#payment_item_number').val() + ' & ' + g_ga_data.amount_total + ' & ' + err_msg,
        nonInteraction: true
    });
    */
}

function ga_stripe_success() {
	/*
    for (var i in g_ga_data.lines) {
        var line = g_ga_data.lines[i];
        ga('ec:addProduct', {               
            'id': line.product_no,
            'name': line.name,
            'category': line.category_name,
            'brand': 'ClubFactory',
            'variant': line.attrs, 
            'price': line.price_unit, 
            'quantity': line.quantity,
        });
    }
    //订单的基本信息
    ga('ec:setAction', 'purchase', {          
        'id': $('#payment_item_number').val(), 
        'revenue': g_ga_data.amount_total, 
        'shipping': g_ga_data.delivery_price,
        'option': 'Stripe',
        'coupon': '',
    }); 

    ga('send', {
        hitType: 'event',
        eventCategory: 'Checkout',
        eventAction: 'Order Paid Success',
        eventLabel: 'Stripe & ' + $('#payment_item_number').val() + ' & ' + g_ga_data.amount_total,
        nonInteraction: true
    });
    */
}
