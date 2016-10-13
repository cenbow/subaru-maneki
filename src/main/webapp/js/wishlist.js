var g_userName;
var g_symbol;
var g_exchangeRate;
var g_deleteId;
var g_deleteIndex;
var g_total;
var g_current_url_path;

$(function(){
    // pass variable to js
    var data = init();

    g_userName = data['user_name'];
    g_symbol = data['g_symbol'];
    g_exchangeRate = data['exchange_rate'];
    g_total = data['total'];
    if (g_total == 0){
        $('#wishlist-array').css('display','none');
        $('#wishlist-empty').css('display','block');
    } else {
        if (g_total % 2 == 1) {
            $('#end').css('visibility','visible');
        }
    }
    //url notify
    if(is_device()){
        url = 'wishlist';
        config = {
            'title': '',
            'show_header_navibar': true,
            'show_footer_navibar': false
        }
        notify_device(url, config);
    }

    // getWishlistItems();
    $('#deleteModal').bind('click', function() {
        deleteModalClose();
    });

});

function gotoDetails (id) {
    if(is_device()){
        notify_device('details', {
            'title': '',
            'show_header_navibar': true,
            'show_footer_navibar': false
        });
    }
    location = "/product/"+ id;
}


function deleteModalOpen(id, index) {
    g_deleteId = id;
    g_deleteIndex = index;
    $('#deleteModal').css({'visibility':'visible','opacity':1});
};

function deleteModalClose () {
    $('#deleteModal').css({'visibility':'hidden','opacity':0});
};

function deleteFromWishlist () {
	
	url = "/order/delete_user_wishlist?wishlistId=" + g_deleteId;
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data){
        	var result = JSON.parse(data);
        	if (result.isRedirect==1){
        		var url = result.msg;
        		location = url;
        	}else if(result.isSuccess == 1){
        		$('#wishlist-item-'+g_deleteIndex).remove();
                ga_remove_wishlist(g_deleteId);
                g_total-= 1;
                $("#item-count").html(g_total);
                if (g_total == 0) {
                    $('#wishlist-array').css('display','none');
                    $('#wishlist-empty').css('display','block');
                } else {
                    if (g_total % 2 == 0) {
                        $('#end').css('visibility','hidden');
                    } else {
                        $('#end').css('visibility','visible');
                    }
                }
        	}else{ 
        		alertModalOpen('Delete wishlist failed.', 2000);
        	}
        },
        error: function (error) {
            alertModalOpen('An error occurred. Please try again later.', 1500);
        }
    });
}

function ga_remove_wishlist () {
	/*
    ga('send', {
        hitType: 'event',
        eventCategory: 'Product',
        eventAction: 'Wishlist',
        eventLabel: 'Remove Wishlist ' + g_deleteId,
        nonInteraction: true
    });
    */
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

function continueShop(){
    if(is_device()){
        notify_device('shop_list', {});
    } else{
        location='/';
    }
}

window.onpageshow = function() {
    notify_wishlist();
}