$(".contact_info_form").ready(function(){
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

$("#edit_country").change(function(){
    $country = $("#edit_country option:selected").val();
    $("#edit_state optgroup").remove();
    for (var i in $states){
        if($states[i].attr("id").match(/\d+/g) == $country){
            $("#edit_state").append($states[i]);
        }
    }
})

$(".contact_info_form input, .contact_info_form select").focus(function(){
    $(this).removeClass("error_border")
})

$("#edit_email").focus(function(){
    $(".error_font").hide();
    $("label[for='email']").css("color","#8d8d8d");
})

$(".no_zip").click(function(){
    $(".no_zip .iconfont").toggleClass("checked");
    $("#edit_zip").removeClass("error_border");
    if ($(".no_zip .iconfont").hasClass("checked")) {
        $("#edit_zip").attr("disabled",true);
        $("#edit_zip").val("");
    }
    else {
        $("#edit_zip").attr("disabled",false);
    };
})

function check_address(){
    var has_error = false;
    var email_error = false;
    //TODO
    $(".contact_info_form input, .contact_info_form select").each(function(i){
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
        $("label[for='email']").css("color","#F15440");
        $("#edit_email").addClass("error_border");
    };

    if (has_error) {
        return false;
    }
    else{
        return true;
    }
}

$(".confirm_btn").click(function(){
    if (!check_address()){
        $("html,body").animate({scrollTop:$(".error_border:first").siblings().offset().top},200)
    }
    else
    {
        $.ajax({
            url: "/user/update/contact_info",
            data: {
                email: $("#edit_email").val(), 
                name: $("#edit_name").val(), 
                street: $("#edit_street").val(),
                city: $("#edit_city").val(),
                stateId: $("#edit_state").val(),
                countryId: $("#edit_country").val(),
                zip: $("#edit_zip").val(), 
                phone: $("#edit_phone").val()
            },
            success: function(data){
            	var result = JSON.parse(data);
            	if (result.isSuccess){
            		location.href="/user/user_center"
            	}else if (result.isRedirect){
            		location.href = result.msg;
            	}else{ 
            		alertModalOpen (result.msg, 1500);
            	}
            },
            contentType: 'application/x-www-form-urlencoded',
            type: 'POST',
        }).fail(function(){
            alertModalOpen ("An error occurred. Please try again later.", 1500);
        });
    }
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