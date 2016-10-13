var price_left, shipping_limit, shipping_fee;
var id, template_id;
var g_ga_data;
var g_cart_expanded = false;
var g_unavailable_products = 0;

//1\ when back to the cart page, this event will happen
window.onpopstate = function(){
    try{
        if(is_device()){
            url = 'cart';
            config = {
                'title': 'Shopping Cart',
                'show_header_navibar': true,
                'show_footer_navibar': false
            }
            notify_device(url, config);
        }
    }catch(err){

    }
}

$(function(){
    shipping_limit = $('#shipping_limit').html();
    price_left = $('#shipping-box .price_left').first().html();
    shipping_fee = $('.shipping_info').first().html();
    shippingDisplay(price_left);

    //2\ when enter into the page, the js will notify the phone device
    try{
        if(is_device()){
            url = 'cart';
            config = {
                'title': 'Shopping Cart',
                'show_header_navibar': true,
                'show_footer_navibar': false
            }
            notify_device(url, config);
        }
    }catch(err){

    }

    // Update unavailable alert
    var unavailables = $('#cart-collapse').find('.item_content');
    if (unavailables.length == 1) {
         unavailables.find('text').html('this&nbsp;products&nbsp;is&nbsp;');
         unavailables.find('.toggle-btn').css('display','none');
    }

})

function shippingDisplay (price_left) {
    if (price_left > 0) {
        $('#shipping-box').css('display','inline-block');
        $('.shipping_info').first().html(shipping_fee);
        $('#sticker_top').css('height','90px');
        // $('.shipping_info').first().css('color','#CCC');
    } else {
        $('#shipping-box').css('display','none');
        $('#sticker_top').css('height','76px');
        $('.shipping_info').first().html('FREE');
        $('.shipping_info').first().css('color','#F15440');
    }
    $('#shipping-box .price_left').first().html((price_left*1).toFixed(2));
}


function g (str, id) {
    return $('#'+str+'_'+id)
}

function update (self, action, product_id) {
    var id = self.getAttribute('data');
    var list = $('.item_content');
    var add = 0;
    if (action == 'add') {
        add = 1;
    } else {
        add = -1;
        if (g('quantity',id).html() == 1) {
            g('box',id).find('.quantity_sub').first().css('opacity', '0.5');
            return;
        }
    }
    var quantity = g('quantity',id).html() * 1;
    quantity = quantity + add;
    
    url = "/cart/update?" + "skuId=" + product_id + "&quantity=" + quantity;  
    $.ajax({
      url: url,
      type: 'GET',
      success: function(data){
    	  data = JSON.parse(data);
          if (data.isSuccess == 1){
        	  //向后台发请求获取购物车里新的总额
        	  url = "/cart/cart_total";
        	  $.ajax({
        		  url : url,
        		  type: 'GET',
        		  success: function(data){
        			  data = JSON.parse(data);
        			  data = JSON.parse(data.data);
        			  price_left = data.priceLeft;
        			  sub_total = data.actualPrice;
        			  
        			  if (add == 1){
                          g('quantity',id).html(g('quantity',id).html()*1+1);
                          g('box',id).find('.quantity_sub').first().css('opacity', '1');
                      }else{
                          if (g('quantity',id).html() > 1) {
                              g('quantity',id).html(g('quantity',id).html()*1-1);
                          }
                      }
                      //$('.sub_total_amount').first().html(sum.toFixed(2));
                      $('.sub_total_amount').first().html(sub_total.toFixed(2));
                      shippingDisplay(price_left);
        		  }
        	  });
              
          }else{
              var errMsg = data.msg;
              console.log(errMsg);
          }
      },
      error: function(error){
          return;
      }
    });
}

function checkout () {
    unavailable = false;
    if ($('.caution').length > 0){
        console.log("The page exist some unavailable line");
        unavailable = true;
    }
    if (unavailable) {
        error = true;
        show_error = true;
        // error_msg = 'Please remove unavailable item(s)!';
        cautionModalOpen();

        // var parent_width = $('body').width();
        // if (parent_width){
        //     var left = (parent_width - 200)/2;
        //     $('#error_alert').css('left', left);
        // }
        // $('#error_alert').css('display','block');
        // $('.error_alert_content').first().html(error_msg);
        // var mytimeout = setTimeout(function () {
        //     console.log(error_msg);
        //     $('#error_alert').css('display', 'none');
        // }, 1500);
    } else {
        //3\ when click the checkout button, will notify phone device url change
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
        notify_payment();
        location = '/trade/confirm';
    }

}

//先把ga_data的数据忽略，后面再加
function deleteModalOpen(pid, tid) {
    id = pid;
    template_id = tid;
    //g_ga_data = ga_data;
    $('#deleteModal').css('display','block');
};

function deleteModalClose () {
    $('#deleteModal').css('display','none');
};

function jumpToEmptyPage(){
    var empty_html = document.getElementById('cart_empty').innerHTML;
    $('body').replaceWith(empty_html);
}

function deleteCartItem(action) {
    if (action == 1) {
    	url = "/order/add_user_wishlist?spuId=" + template_id;
    	var current_url_path = window.location.pathname;
        $.ajax({
            url: url,
            type: 'GET',
            success: function(data){
            	var result = JSON.parse(data);
            	if (result.isRedirect==1){
            		var url = result.msg;
            		url = url + "?redirectUrl=" + current_url_path;
            		location = url;
            	}else if(result.isSuccess == 1){
            		sendCartReq();
                    ga_add_wishlist();
                    //1\ notify the url change
                    device_add_to_wishlist();
            	}else{ 
            		alertModalOpen('Save to wishlist failed.', 2000);
            	}
            },
            error: function (error) {
                alertModalOpen('An error occurred. Please try again later.', 1500);
            }
        });
    } else {
        sendCartReq();
    }
    deleteModalClose();
}

function sendCartReq () {
    var url = "/cart/delete" + "?cartId=" + id;
   
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data){
            //ga_delete_product();
        	data = JSON.parse(data);
            if(data.isSuccess == 1){
                if ($('.item_content').length > 1){
                	url = "/cart/cart_total";
	              	$.ajax({
	              		url : url,
	              		type: 'GET',
	              		success: function(data){
	              			data = JSON.parse(data);
	              			data = JSON.parse(data.data);
	              			price_left = data.priceLeft;
	              			sub_total = data.actualPrice;
	              			  
	              			g('row', template_id).remove();
	              			$('.sub_total_amount').first().html(sub_total.toFixed(2));
	              			shippingDisplay(price_left);
	              		}
	              	});
                }else{
                    jumpToEmptyPage();
                }
            }
        },
        error: function(error){
            alertModalOpen('Sorry, a connection error has occurred', 1500);
            return;
        }
    });
}

function goToDetail(id){
    url = "/product/" + id;
    notify_product_detail();
    location = (url);
}

//5\ when the cart page is empty and click the continue shop button, will notify phone device the url change
function continueShop(){
    if (is_device()) {
        notify_product_list();
    } else {
        location='/';
    }
}
function ga_delete_product() {
    ga('ec:addProduct', {
        'id': g_ga_data.product_no,
        'name': g_ga_data.product_name,
        'category': g_ga_data.category_name_str,
        'brand': 'ClubFactory',
        'variant': g_ga_data.attr_value_str,
        'price': g_ga_data.price,
        'quantity': 1
    });
    ga('ec:setAction', 'remove');
    ga('send', {
        hitType: 'event',
        eventCategory: 'Product',
        eventAction: 'Cart',
        eventLabel: 'Remove Cart ' + g_ga_data.product_no,
        nonInteraction: true
    });
}

function ga_add_wishlist() {
	/*
    ga('send', {
        hitType: 'event',
        eventCategory: 'Product',
        eventAction: 'Wishlist',
        eventLabel: 'Add Wishlist ' + g_ga_data.product_no,
        nonInteraction: true
    });
    */
}

function removeUnavailables() {
    var items = $('#cart-collapse').find('.item_content');
    for (var i = 0; i < items.length; i++) {
        var args = items[i].getAttribute('data').split(',');
        id = args[0];
        template_id = args[1];
        g_ga_data = args[2];
        sendCartReq();
    }
    // $('#cart-collapse').remove();

    // Go to payment
    try {
        if (is_device()) {
            url = 'acquirer';
            config = {
                'title': 'Payment',
                'show_header_navibar': true,
                'show_footer_navibar': false
            }
            notify_device(url, config);
        }
    } catch(err){
        return;
    }
    notify_payment();
    location = '/payment';
}

function cautionModalOpen () {
    $('#cautionModal').css({'visibility':'visible','opacity':'1'});
}

function cautionModalClose () {
    $('#cautionModal').css({'visibility':'hidden','opacity':0});

    // Add outline to unavailable item

    // $('#cart-collapse').css('border','2px solid #f15440');
    // setTimeout(function(){
    //     $('.cart').bind('click', function () {
    //         $('#cart-collapse').css('border','0');
    //         $('.cart').unbind();
    //     });
    // }, 1000);
}

function alertModalOpen (text, duration) {
    var target = $('#alertModal');
    if (typeof(text) != 'number') {
        $('body').css('overflow', 'hidden');
        target.find('.alertContent').first().html(text);
        target.css({'opacity':'1','visibility':'visible'});

        var t = setTimeout(function(){
            $('body').css('overflow', 'visible');
            target.css({'opacity':'0','visibility':'hidden'});
            target.unbind();
        }, duration, target);
        target.bind('click', function() {
            alertModalOpen(t, 0);
        });

    } else {
        clearTimeout(text);
        $('body').css('overflow', 'visible');
        target.css({'opacity':'0','visibility':'hidden'});
        target.unbind();
    }
}

function toggleCart () {
    var c_box = $('#cart-collapse');
    if (g_cart_expanded) {
        c_box.css('max-height','140px');
        $('#toggle-cart').html("See All&nbsp;&nbsp;<span class='iconfont'>&#xe600;</span>");
    } else {
        var itemCnt = c_box.find('.item_content').length;
        c_box.css('max-height', itemCnt*140+'px');
        $('#toggle-cart').html("See Less&nbsp;&nbsp;<span class='iconfont'>&#xe610;</span>");
    }
    g_cart_expanded = !g_cart_expanded;
}
