$(document).ready(function() {
    if ($("#paid_nav li:first").hasClass("active"))
        $("#paid_nav li:first a").removeAttr("href");
    else 
        $("#paid_nav li:last a").removeAttr("href");
})

$(".total_line").click(function(){
    $(this).parent().children(".items:gt(0)").slideToggle(200);
    $(this).children(".view").children().toggleClass("flipy");
    $(this).children(".view").children("span").toggle();
    if ($(this).parent().children().length-2 > 3) {
        var $id = $(this).parent().attr("id");
        $("html,body").animate({scrollTop:$("#"+$id).offset().top},200);
    };  
})

$(".del_btn").click(function(){
    var $id=$(this).attr("id").match(/\d+/g);
    $("#deleteOrderModal .modal_dialog span").text($id);
    $("#deleteOrderModal").show();
})

$(".modal_no_btn").click(function(){
    $("#deleteOrderModal").hide();
})

$(".modal_delete_btn").click(function(){
  $order_id= parseInt($(this).parent().siblings("span").text());
  console.log()
  $.ajax({
    url: "/order/delete",
    type: 'GET',
    contentType: 'application/x-www-form-urlencoded',
    data: { 
    	'orderNumber':$order_id,
    },
    success: function(data){
        console.log(data);
        var res = JSON.parse(data);
        if (res.isSuccess){
	        $("#items"+$order_id).remove();
	        $("#deleteOrderModal").hide();
	        $("#paid_nav .order_num").text($("#paid_nav .order_num").text()-1);
        }
    },
    error: function(error){
        return;
    }
  });
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

$("#paid_nav li").click(function(){
	history.replaceState({}, "", "/user/user_center"); 
})