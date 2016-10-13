var g_address_id;
var currency;
//1\ when back to the payment page, this event will happen
window.onpopstate = function(){
    try{
        if(is_device()){
            url = 'acquirer';
            config = {
                'title': 'Payment',
                'show_header_navibar': true,
                'show_footer_navibar': false
            }
            notify_device(url, config);
        }
    }catch(err){

    }
}


$(function(){
    //1 2\ when enter into the page, the js will notify the phone device
    try{
        if(is_device()){
            url = 'acquirer';
            config = {
                'title': 'Payment',
                'show_header_navibar': true,
                'show_footer_navibar': false
            }
            notify_device(url, config);
        }
    }catch(err){

    }
    
    var data = init();
    g_address_id = data[0];
    currency = data[1];
})

function check_address(){
    var has_error = false;
    var email_error = false;
    //TODO
    $(".edit_address_form input, .edit_address_form select, .edit_address_form textarea").each(function(i){
        if($(this).val() =='' || $(this).val() == null){
            if ($(this).attr("id")=="edit_zip") {
                if(!$(".no_zip .iconfont").hasClass("checked")){
                    $(this).addClass("error_border");
                    has_error = true;
                }
            }
            else {
                $(this).addClass("error_border");
                    has_error = true;
            };     
        }
    });

    var email_ptn = new RegExp("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,63}$");
    if (!email_ptn.test($("#edit_email").val())) {
        has_error = true;
        $(".error_font").show();
        $("#edit_email").addClass("error_border");
    };

    if (has_error) {
        return false;
    }
    else{
        //update_address();
        return true;
    }
}

var update_address = function () {
    $.ajax({
        url: "/api/trade/save/shipping_info",
        data: {
            email: $("#edit_email").val(), 
            name: $("#edit_name").val(), 
            street: $("#edit_street").val(),
            city: $("#edit_city").val(),
            state_id: $("#edit_state").val(),
            country_id: $("#edit_country").val(),
            zip: $("#edit_zip").val(), 
            phone: $("#edit_phone").val()
        },
        contentType: 'application/x-www-form-urlencoded',
        type: 'POST',
        success: function(data){
        },
    }).fail(function(){
        alertModalOpen ("An error occurred. Please try again later.", 1500)
    });
}

$(".edit_address").click(function(){
    
    $(".edit_address_form").slideDown("400");
    $(".show_address").hide();
})

$(".edit_address_form input, .edit_address_form select, .edit_address_form textarea").focus(function(){
    $(this).removeClass("error_border")
})
$("#edit_email").focus(function(){
    $(".error_font").hide();
})

$(".edit_address_form").ready(function(){
    $states=[];
    $country=$("#edit_country option:selected").val();
    $("#edit_country option").each(function(){
        var $stateOptGroup = $("#stateOptGroup"+$(this).val());
        var $country_id = $stateOptGroup.attr("label");
        $stateOptGroup.attr("label",$("#edit_country [value=\""+$country_id+"\"]").text());
        $states.push($stateOptGroup);
    })

    for (var i in $states){
        if($states[i].attr("id").indexOf($country) < 0){
            $states[i].remove();
        }
    }
})

//当用户更改国家时，要重新计算总金额
$("#edit_country").change(function(){
    $country = $("#edit_country option:selected").val();
    $("#edit_state optgroup").remove();
    for (var i in $states){
        if($states[i].attr("id").match(/\d+/g) == $country){
            $("#edit_state").append($states[i]);
        }
    }
    var subtotal = $("#subtotal").text();
    $.ajax({
        url: "/api/trade/update/shipping_info",
        data: {
            countryId: $("#edit_country").val(),
        },
        contentType: 'application/x-www-form-urlencoded',
        type: 'POST',
        success: function(data){
            var res = JSON.parse(data);
            res = JSON.parse(res.msg);
            
            var subtotal = parseFloat(res.subtotal);
            var shipping_fee = parseFloat(res.postFee);
            var amount_total = parseFloat(subtotal) + shipping_fee;
                  
            $("#subtotal").text(subtotal.toFixed(2));
            //要判断原来是否是free的状态
            var status = $("#shipping_line").text();
            if(shipping_fee > 0){
            	if (status.trim() == "Free"){ 
            		$("#shipping_line").html("");
            		var shipping_fee_html = "<span>" + currency + "</span>" +
            		'<span id="shipping_fee">' + shipping_fee.toFixed(2) +'</span>';
            		$("#shipping_line").first().html(shipping_fee_html)
            	}else{
            		$("#shipping_fee").text(shipping_fee.toFixed(2));
            	}
            }else{ 
            	$("#shipping_line").first().html("Free");
            }
            $("#amount_total").text(amount_total.toFixed(2));
        },
    }).fail(function(){
        alertModalOpen ("An error occurred. Please try again later.", 1500)
    });
})

$(".no_zip").click(function(){
    $(".no_zip .iconfont").toggleClass("checked");
    $(this).siblings().removeClass("error_border");
    if ($(".no_zip .iconfont").hasClass("checked")) {
        $("#edit_zip").attr("disabled",true);
        $("#edit_zip").val("");
    }
    else {
        $("#edit_zip").attr("disabled",false);
    };
})

$(".submit_btn").click(function(){
    // TODO
    if (!check_address()) 
    {
        if ($(".edit_address_form").is(":hidden")) {
            $(".edit_address_form").slideDown("400");
            $(".show_address").hide();
        };
    }
    else
    {
    	$.ajax({
            url: "/api/trade/save/shipping_info",
            data: {
                email: $("#edit_email").val(), 
                name: $("#edit_name").val(), 
                street: $("#edit_street").val(),
                city: $("#edit_city").val(),
                stateId: $("#edit_state").val(),
                countryId: $("#edit_country").val(),
                zip: $("#edit_zip").val(), 
                phone: $("#edit_phone").val(),
                isDefault: true,
            },
            contentType: 'application/x-www-form-urlencoded',
            type: 'POST',
            success: function(data){
            	console.log(data);
            	var result = JSON.parse(data);
            	if (0 == result.isSuccess){ 
            		alertModalOpen (result.msg, 1500);
            		return;
            	}
            	var shippingId = parseInt(result.msg);
            	$.ajax({
                    url: "/api/trade/order/create",
                    data: {
                    	shippingId: shippingId
                    },
                    success: function(data){
                        //\2 when click the submit order, this event will happen

                        notify_stripe();
                        device_confirm_order();
                        //ga_create_order(data.result.order_id, data.result.amount_total);
                        result = JSON.parse(data);
                        if (result.isSuccess == 1 && result.isRedirect == 1){
                        	location = result.redirectURL;
                        }else if(result.isSuccess == 0){ 
                        	alertModalOpen (result.msg, 1500)
                        }
                    },
                    contentType: 'application/x-www-form-urlencoded',
                    type: 'POST',
                }).fail(function(){alert("failed!")});
            },
        }).fail(function(){
            alertModalOpen ("An error occurred. Please try again later.", 1500)
        });
    }
})

$(".show_order_summary").click(function(){
    $(".item_list").slideToggle("fast");
    if ($(".toggle_icon").hasClass("flipy")) {
        $(".toggle_icon").removeClass("flipy");
    }
    else {
        $(".toggle_icon").addClass("flipy");
    };
})

$(".up").click(function(){
    $(".item_list").slideUp();
})

function alertModalOpen (text, duration) {
    var target = $('#alertModal');
    $('body').css('overflow', 'hidden');
    target.find('.alertContent').first().html(text);
    target.css({'opacity':'1','visibility':'visible'});
    setTimeout(function(){
        $('body').css('overflow', 'visible');
        target.css({'opacity':'0','visibility':'hidden'});
    }, duration, target);
}

$("#alertModal").click(function(){
    $('body').css('overflow', 'visible');
    $(this).css({'opacity':'0','visibility':'hidden'});
})

function ga_create_order(order_id, amount_total) {
    ga('send', {
        hitType: 'event',
        eventCategory: 'Checkout',
        eventAction: 'Quotations',
        eventLabel: 'SO' + order_id + ' & ' + amount_total,
        nonInteraction: true
    });
};

$("#cardNumber").focus(function(){
    $(".cardNumber_eg").show()
})
$("#cardNumber").blur(function(){
    $(".cardNumber_eg").hide()
})
$("#cardExpiry").focus(function(){
    $(".cardExpiry_eg").show()
})
$("#cardExpiry").blur(function(){
    $(".cardExpiry_eg").hide()
})
$("#cardCVC").focus(function(){
    $(".cardCVC_eg").show()
})
$("#cardCVC").blur(function(){
    $(".cardCVC_eg").hide()
})

$('#cardNumber').payment('formatCardNumber');
$('#cardExpiry').payment('formatCardExpiry');
$('#cardCVC').payment('formatCardCVC');
