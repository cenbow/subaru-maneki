var commentSlider;
var g_product_template_id;
var g_product_id = 0;
var g_attribute_info;
var g_product_id_value_id_map;
var g_selected_attribute_values_id_map = {};
var g_sku_view_open = false;
var g_add_to_cart_enable = true;
var g_ga_data = {};
var current_url_path;

//1\ when back to the detail page, this event will happen
window.onpopstate = function(){
    try{
        if(is_device()){
            url = 'product_detail';
            config = {
                'title': '',
                'show_header_navibar': true,
                'show_footer_navibar': false
            }
            notify_device(url, config);
        }
    }catch(err){

    }
}

$(function(){
    // pass variable to js
    data = init();

    g_product_template_id = data[0];
    g_attribute_info = data[1];
    g_product_id_value_id_map = data[2];
    var image_num = data[3];
    g_ga_data = data[4];
    
    current_url_path = window.location.pathname;
//    console.log(current_url_path);
    
    console.log(g_product_template_id);
    console.log(g_attribute_info);
    console.log(g_product_id_value_id_map);
    console.log(image_num);
//    console.log(g_ga_data);

    //url notify
    if(is_device()){
        url = 'product_detail';
        config = {
            'title': '',
            'show_header_navibar': true,
            'show_footer_navibar': false
        }
        notify_device(url, config);
    }

    function Slider (parent, index) {
        return new Slider.prototype.init(parent);
    }

    Slider.prototype = {
        init: function (parent) {
            var self = this;
            self.parent = $(parent);
        },
        update: function (i, pn) {
            var self = this;
            self.index = i;
            self.picNum = pn;
            self.ul = self.parent.children('.slider-wrap');

            // init slider box
            self.ul.css('width', (100 * self.picNum)+'vw');
            self.ul.css('left', (-100 * self.index)+'vw');

            self.bindTouch();
            // init to target index
            $(self.parent).find(".slide-nav").find('li').eq(self.index)
                .addClass('selected').siblings().removeClass('selected');
        },
        move: function () {
            var self = this;
            self.ul.css('left', (-100 * self.index)+'vw');
            $(self.parent).find(".slide-nav").find('li').eq(self.index)
                .addClass('selected').siblings().removeClass('selected');
        },
        moveNext: function () {
            var self = this;
            if (self.index >= self.picNum - 1) {
                return;
            }
            self.index++;
            self.move();

        },
        movePre: function () {
            var self = this;
            if (self.index <= 0) {
                return;
            }
            self.index--;
            self.move();
        },
        bindTouch: function () {
            var self = this,
                startPosition,
                endPosition,
                deltaX,
                deltaY;
            self.ul.find('div').bind('touchstart', function (e) {
                var touch = e.originalEvent.targetTouches[0];
                startPosition = {
                    x: touch.pageX,
                    y: touch.pageY
                }
            }).bind('touchmove', function (e) {
                var touch = e.originalEvent.targetTouches[0];
                endPosition = {
                    x: touch.pageX,
                    y: touch.pageY
                };
                deltaX = endPosition.x - startPosition.x;
                deltaY = endPosition.y - startPosition.y;
            }).bind('touchend touchcancel', function (e) {
                    if (deltaX < 0) {
                        self.moveNext();
                    } else if (deltaX > 0) {
                        self.movePre();
                    }
            })
        }
    };
    Slider.prototype.init.prototype = Slider.prototype;

    var detailSlider = Slider('#Slider').update(0, image_num);
    commentSlider = Slider('#commentSlider');

    if (_.keys(g_product_id_value_id_map).length == 1) {
        g_product_id = _.keys(g_product_id_value_id_map)[0];
    }

    // convert rating to stars
    for (var i = 0; i < $('.star-rating').length; i++) {
        temp = $('.star-rating').eq(i).html();
        $('.star-rating').eq(i).html(convertRating(temp));
    }

    // toggle view
    $('.view').click(function () {
        if (this.innerHTML == 'See More') {
            this.innerHTML = 'See Less';
        } else {
            this.innerHTML = 'See More';
            var name = this.getAttribute('name');
            // $('html,body').animate({
            //     scrollTop: $(this.getAttribute('name')).offset().top
            // });
            if(name != '#product-returns') {
                window.scrollTo(0,$(name).offset().top);
            }
        }
    });

    // init bot btn style
    $('.footer_btns').first().css('z-index', '300');
    $('#cartBlock').css('bottom','-600px');

    // add to wishlist
    $('#btn_save_wishlist').click(function () {
        saveRpc();
    });
    $('#btn_view_wishlist').click(function () {
        location ='/order/get_user_wishlist';
    });

    // modal ctrls
    modalInit();
    
    // GA
    var params = getUrlParams();
    var t = params.t;
    var p = params.p;
    if (!t) {
        t = 'none';
    }
    if (!p) {
        p = 0;
    }
    /*
    ga('ec:addProduct', {
        'id': g_ga_data.product_no,    // 商品货号 (string).必填
        'name': g_ga_data.product_no,  // 商品标题 (string).必填
        'price': g_ga_data.price,      // 商品售价 (Currency).
        'position': p                  // 列表中的排序，从URL中获得 (number).
    });
    ga('ec:setAction', 'click', {list: t});// list的值从URL中获得.
    */
});


function modalInit () {
    $('#product_price').click(function () {
        $('.footer_btns').first().css('z-index', '100');
        parityModalOpen();
    });

    $('#product_shipping').click(function () {
        notify_shipping();
        location = '/info/shipping';
    });

    $('#product_sku').click(function () {
        $('.footer_btns').first().css('z-index', '300');
        cartModalOpen();
    });

    $('#parityModal').click(function () {
        parityModalClose();
    });
    $('#commentModal').click(function () {
        commentModalClose();
    });
    $('#sizeModal').click(function () {
        sizeModalClose();
    });
}

function convertRating (rating) {
    var stars = "";
    var starcount = 0;
    for (var i = 0; i < rating; i++) {
        stars += '★';
        starcount+=1;
    }
    for (var i = 0; i < (5-starcount); i++) {
        stars += '☆';
    }
    return stars;
};

function saveRpc() {
	url = "/order/add_user_wishlist?spuId=" + g_product_template_id;
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
        		$('#btn_view_wishlist').css('display','inline-block');
                $('#btn_save_wishlist').css('display','none');
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
}

function updateQuantity (operation) {
    tgt = $('#quantity').html();
    if (operation==="+") {
        $('#quantity').html(tgt*1+1);
    }
    else {
        if (tgt > 1) {
            $('#quantity').html(tgt*1-1);
        }
    }
}

function disable_add_to_cart_btn() {
    g_add_to_cart_enable = false;
    $('.add_to_cart span').css('color', '#F37666');
    $('#add_to_cart_btn').unbind('click');
}

function enable_add_to_cart_btn() {
    if (!g_add_to_cart_enable) {
        g_add_to_cart_enable = true;
        $('.add_to_cart span').css('color', '#FFF');
        $('#add_to_cart_btn').click(function () {
            cartModalOpen();
        });
    }
}

/*
function set_product_id() {
    var selected_attribute_values_ids = [];
    for (var attribute_id in g_selected_attribute_values_id_map) {
        selected_attribute_values_ids.push(g_selected_attribute_values_id_map[attribute_id]);
    }
    for (var product_id in g_product_id_value_id_map) {
        if (_.isEmpty(_.difference(g_product_id_value_id_map[product_id], selected_attribute_values_ids))) {
            g_product_id = product_id;
            enable_add_to_cart_btn();
            console.log(g_product_id);
            return;
        }
    }
    g_product_id = 0;
}
*/
function set_product_id(){
	for (var product_id in g_product_id_value_id_map){
		//比较两个list，g_product_id_value_id_map[product_id]和g_selected_attribute_values_id_map
		var skuValueCurr = g_selected_attribute_values_id_map;
		var skuValueCmp = g_product_id_value_id_map[product_id];
		var totalLength = skuValueCmp.length;
		var cnt = 0;
		for (var item in skuValueCmp){
			var flag = false;
			for (var inner in skuValueCurr){
				if (skuValueCurr[inner] == skuValueCmp[item]){
					flag = true;
					break;
				}
			}
			if (flag == true){
				cnt = cnt + 1;
			}
		}
		if (cnt == totalLength){
			g_product_id = parseInt(product_id);
//			console.log(g_product_id);
			enable_add_to_cart_btn();
			return;
		}
	}
	g_product_id = 0;
}


/*
function attributeClick(self, index, attribute_id, attribute_value_id){
    if (self.style.borderColor=="rgb(204, 204, 204)") {
        $('#sku_'+index).find('button').css({'border-color':'rgb(204, 204, 204)','color':'#333333'});
        self.style.borderColor='rgb(241, 84, 64)';
        self.style.color='rgb(241, 84, 64)';
    }
    g_selected_attribute_values_id_map[attribute_id] = attribute_value_id;
    set_product_id();
}
*/

function attributeClick(self, index, attribute_value, attribute_key){
	if (self.style.borderColor=="rgb(204, 204, 204)") {
        $('#sku_'+index).find('button').css({'border-color':'rgb(204, 204, 204)','color':'#333333'});
        self.style.borderColor='rgb(241, 84, 64)';
        self.style.color='rgb(241, 84, 64)';
    }
	
	g_selected_attribute_values_id_map[attribute_key] = attribute_value;
	set_product_id();
}

/*
 * 现在的逻辑变成是：
 * 用户点击add to cart按钮后，
 * 当用户未登陆时，调到登陆页面，登陆成功后回到商品详情页面，
 * 当用户已经登陆后，那么直接弹窗提示添加到购物车成功，并更新到购物车的数量
 */
function addToCart() {
    try{
        if(is_device()){
            device_add_to_cart();
        }
    }catch(err){

    }
    var add_qty = parseInt($('#quantity').html());
    if (g_product_id != 0) {
        url = "/cart/add?skuId=" + g_product_id +  "&quantity=" + add_qty;
        //ga_add_to_cart();
        cartModalClose();
        $.ajax({
            url: url,
            type: 'GET',
            success: function(data){
            	console.log(data);
            	data = JSON.parse(data);
            	
            	if (data.isSuccess == 1){
            		url = "/cart/count";
            		$.ajax({
            			url:url,
            			type: 'GET',
            			success: function(data){ 
            				data = JSON.parse(data);
//            				console.log(data);
            				added_success(data.data);
            			}
            		});
            	}else if(data.isRedirect== 1){
            		url = data.redirectURL;
            		//当添加购物车时返回redirect url，那么需要手动再url后面再添加当前商品详情页面的url地址
            		url = url + "?redirectUrl=" + current_url_path;
            		console.log("redirect url path = " + url);
            		location = url;
            	}else{
            		console.log(data);
            	}
            },
        });
//        notify_cart();
//        location = url;
    }
}

function added_success (add_qty) {
    alertModalOpen("<div class='add_success'><i class='iconfont'>&#xe614</i><p>Added Successfully</p></div>",2000);
    $(".cart_btn").addClass("added");
    //$(".cart_num").text(parseInt($(".cart_num").text())+add_qty);
    $(".cart_num").text(add_qty);
    jump();
    setTimeout(jump, 2000);
    history.replaceState({},"",location.pathname);
}

var jump = function(){
    $(".cart_num").animate({"top":"-12px",},200).animate({"top":"-3px",},150).animate({"top":"-9px",},200).animate({"top":"-3px",},150);
}

function parityModalOpen () {
    $('body').css('overflow', 'hidden');
    $('#parityModal').css('display','block');
    ga_click_price_compare();
}
function parityModalClose () {
    $('body').css('overflow', 'visible');
    $('#parityModal').css('display','none');
    $('.footer_btns').first().css('z-index', '300');
}

function commentModalOpen (bigImg, imgSet, index) {
    change_ui_top('hide');
    $('body').css('overflow', 'hidden');
    var img = '';
    var nav = '';
    imgSet = imgSet.substr(1, imgSet.length-2).split(', ');

    for (var i = 0; i < imgSet.length; i++) {
        //img+=('<div><img style="background:url('+imgSet[i].substr(2,imgSet[i].length-3)+')"></img></div>');
    	img+=('<div><img style="background:url('+imgSet[i].substr(0,imgSet[i].length)+')"></img></div>');
        nav+=('<li><div class="dot"></div></li>');
    }

    $('#commentSlider').append('<div class="clearfix slider-wrap">'+img+'</div>'+'<ul class="slide-nav">'+nav+'</ul>');

    commentSlider.update(index, imgSet.length);

    $('#commentModal').css('opacity','1');
    setTimeout(function(){
        $('#commentModal').css('visibility','visible');
    },10);
}

function commentModalClose () {
    change_ui_top('show');
    $('body').css('overflow', 'visible');
    $('#commentModal').css('opacity','0');
    setTimeout(function(){
        $('#commentSlider').empty();
        $('#commentModal').css('visibility','hidden');
    },10);
}

function sizeModalOpen () {
    change_ui_top('hide');
    $('body').css('overflow', 'visible');
    $('#sizeModal').css('opacity','1');
    setTimeout(function(){
        $('#sizeModal').css('visibility','visible');
    },10);
}
function sizeModalClose () {
    change_ui_top('show');
    $('body').css('overflow', 'hidden');
    $('#sizeModal').css('opacity','0');
    setTimeout(function(){
        $('#sizeModal').css('visibility','hidden');
    },10);
}

function cartModalOpen () {
    $('body').css('overflow', 'hidden');
    if (g_sku_view_open) {
        addToCart();
        return;
    } else {
        g_sku_view_open = true;
        $('#cartModal').css('display','block');
        if (g_product_id == 0) {
            disable_add_to_cart_btn();
        }

        setTimeout(function () {
            $('#cartBlock').css('bottom','44px');
        }, 10);
    }
}

function cartModalClose () {
    $('body').css('overflow', 'visible');
    if (g_sku_view_open) {
        g_sku_view_open = false;
        $('#cartModal').css('display','none');
        $('#cartBlock').css('bottom','-600px');
        enable_add_to_cart_btn();
    }
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




function checkUri(uri, key){
    var re = new RegExp("([?&])" + key + "=.*?(&|$)", "i");
    var separator = uri.indexOf('?') !== -1 ? "&" : "?";
    if (uri.match(re)){
        console.log("yes");
        return true;
    }else{
        return false;
    }
}

function getUrlParams(){
    var args = new Object();
    var query = location.search.substring(1);
    var pairs = query.split("&");
    for(var i = 0; i < pairs.length; i++){
        var pos = pairs[i].indexOf('=');
        if (pos == -1){
            continue;
        }
        var arg_name = pairs[i].substring(0,pos);
        var arg_value = pairs[i].substring(pos+1);
        args[arg_name] = unescape(arg_value);
    }
    return args;
}


function ga_click_price_compare() {
	/*
    ga('send', {
        hitType: 'event',
        eventCategory: 'Product',
        eventAction: 'Compare',
        eventLabel: 'Compare Price ' + g_ga_data.product_no,
        nonInteraction: true
    });
    */
}

function ga_add_to_cart() {
    console.log("ga_add_to_cart");
    var attr_value_str = '';
    /*
    for (var attr_id in g_selected_attribute_values_id_map) {
        for (var attr in g_attribute_info) {
            if (attr.id == attr_id) {
                for (var attr_value in attr.values) {
                    if (attr_value.id == attr_id) {
                        attr_value_str += attr_value.value;
                        attr_value_str += " ";
                        break;
                    }
                }
                break;
            }
        }
    }
    */
    /*
    ga('ec:addProduct', {
        'id': g_ga_data.product_no,
        'name': g_ga_data.product_name,
        'category': g_ga_data.category_name_str,
        'brand': 'ClubFactory',
        'variant': attr_value_str,
        'price': g_ga_data.price,
        'quantity': 1
    });
    ga('ec:setAction', 'add');
    ga('send', {
        hitType: 'event',
        eventCategory: 'Product',
        eventAction: 'Add product',
        eventLabel: 'Add to cart ' + g_ga_data.product_no,
        nonInteraction: true
    });
    */
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

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length,c.length);
        }
    }
    return "";
}

function change_ui_top (option) {
    var config;
    if (option == 'hide') {
        config = {
            'title':'',
            'show_header_navibar': false,
            'show_footer_navibar': false
        }
    } else {
        config = {
            'title': '',
            'show_header_navibar': true,
            'show_footer_navibar': false
        }
    }
    if (navigator.userAgent.indexOf('android_native_navbar') > -1) {
        window.JSInterface.url_changed('product_detail',JSON.stringify(config));
    } else if (navigator.userAgent.indexOf('ios_native_navbar') > -1) {
        try {
            jto_url_change({'config':config});
        } catch (err){
            window.webkit.messageHandlers.url_changed.postMessage({config: config});
        }
    }
}
