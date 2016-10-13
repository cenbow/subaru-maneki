
var g_config = {};

// end of g_config

set_unveil_event = function(selector) {
    $(selector).unveil(g_config.unveil.threshold);
};


// set_product_img_width = function(selector) {
//     var window_width = $(window).width();
//     var img_width;
//
//     for (var key in g_config.bootstrap) {
//         var width_config = g_config.bootstrap[key];
//         if (window_width >= width_config.min && window_width <= width_config.max) {
//             var content_width;
//             if (width_config.content_width) {
//                 content_width = width_config.content_width;
//             } else {
//                 content_width = window_width;
//             }
//             img_width = parseInt(
//                 ((content_width - 2*width_config.content_inner_padding) / width_config.num_per_row) -
//                 (2 * width_config.item_padding)
//             );
//             break;
//         }
//     }
//
//     $(selector).each(function() {
//         this.style.height = img_width - 2*g_config.bootstrap.img_border + 'px';
//         $('#' + this.id.substring(4))[0].style.height = (img_width) + "px";
//     });
// }


init_product_img = function(selector) {
    if (!selector) {
        selector = '.jscroll-added img';
    }
    set_unveil_event(selector);
    // set_product_img_width(selector);

}

load_next_page_callback = function() {
    if ($('#append_page_url').length) {
        var el = $('#append_page_url');
        /*
        ga('send', {
            hitType: 'pageview',
            title: 'next page',
            page: el.html(),
        });
        */
        el.remove();
    }
    init_product_img();
}

jQuery(function($) {
    g_config = {
        'unveil': {
            'threshold': 1000 // The distance from the top or the bottom of the window at which to trigger load img.
        },
        'jscroll': {
            'padding': 1000
        },
        'bootstrap': { // window width breakpoint
            'img_border': 1,
            'screen_sm_ex': {
                'max': 320,
                'min': 0,
                'num_per_row': 2,
                'item_padding': 3.5,
                'content_inner_padding': 6.5,
            },
            'screen_sm': {
                'max': 767,
                'min': 321,
                'num_per_row': 2,
                'item_padding': 5,
                'content_inner_padding': 5,
            },
            'screen_md': {
                'max': 991,
                'min': 768,
                'content_width': 750,
                'num_per_row': 3,
                'item_padding': 5,
                'content_inner_padding': 5,
            },
            'screen_lg': {
                'max': 1119,
                'min': 992,
                'content_width': 970,
                'num_per_row': 4,
                'item_padding': 5,
                'content_inner_padding': 5,
            },
            'screen_lg_ex': {
                'min': 1200,
                'max': 10000,
                'content_width': 1170,
                'num_per_row': 4,
                'item_padding': 5,
                'content_inner_padding': 5,
            }
        }
    };

    init_product_img('#scroll_content img');

    $('#products_grid').jscroll({
        debug: false, // When set to true, outputs useful information to the console display if the console object exists.
        autoTrigger: true, // When set to true, triggers the loading of the next set of content automatically when the user scrolls to the bottom of the containing element. When set to false, the required next link will trigger the loading of the next set of content when clicked
        loadingHtml: '<img src="/img/ajax-loader.png"></img>',
        padding: g_config.jscroll.padding, // The distance from the bottom of the scrollable content at which to trigger the loading of the next set of content. This only applies when autoTrigger is set to true.
        contentSelector: '', // A convenience selector for loading only part of the content in the response for the next set of content. This selector will be ignored if left blank and will apply the entire response to the DOM.
        nextSelector: '.infinite_scroll_next a:last', // The selector to use for finding the link which contains the href pointing to the next set of content. If this selector is not found, or if it does not contain a href attribute, jScroll will self-destroy and unbind from the element upon which it was called.
        callback: load_next_page_callback, //Pass a function to this option and it will be called at the end of each page load. Alternatively, you can pass a function as the only argument to the jScroll instantiation instead of an options object, and it will be returned as a callback.
    });
});

function tutorial(){
    if (document.cookie.indexOf("tutorial_displayed")<0 && document.cookie.indexOf("isNew_customer")>-1) {
        position_size();
        show_tutorial();
    };
    $(".oh_i_see").click(function(){
        $("#tutorial").fadeOut(300);
    })
}

function show_tutorial(){
    $("#tutorial .circle").fadeIn(800, function() {
        $("#tutorial .line").show(1000, function() {
            $("#tutorial .note").fadeIn(500);
        })
    });
    document.cookie = 'tutorial_displayed=yes';
}

function position_size () {
    var $width = $(".oe_product:first .contain").width()*0.88;

    $(".oe_product:first .contain").append($("#tutorial"));
    $("#tutorial .line").css("width",$width);
    $("#tutorial .note").css("left",$width-3);
} 