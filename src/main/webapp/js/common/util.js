function notify_device(url, config) {
    if (navigator.userAgent.indexOf('android_native_navbar') > -1) {
        window.JSInterface.url_changed(url, JSON.stringify(config));
    } else if (navigator.userAgent.indexOf('ios_native_navbar') > -1) {
        try{
            jto_url_change({url: url, config: config});
        } catch (err){
            window.webkit.messageHandlers.url_changed.postMessage({url: url, config: config});
        }

    }
}

function notify_my_center() {
    notify_device(
        'my_center',
        {
            'title': 'My Center',
            'show_header_navibar': true,
            'show_footer_navibar': true
        }
    );
}
function notify_my_orders() {
    notify_device(
        'my_orders',
        {
            'title': 'My Orders',
            'show_header_navibar': true,
            'show_footer_navibar': false
        }
    );
}
function notify_contact_info() {
    notify_device(
        'contact_info',
        {
            'title': 'Contact Info',
            'show_header_navibar': true,
            'show_footer_navibar': false
        }
    );
}
function notify_tracking() {
    notify_device(
        'order_tracking',
        {
            'title': 'Tracking Info',
            'show_header_navibar': true,
            'show_footer_navibar': false
        }
    );
}
function notify_login() {
    notify_device(
        'login',
        {
            'title': 'Log In',
            'show_header_navibar': true,
            'show_footer_navibar': true
        }
    );
}
function notify_signup() {
    notify_device(
        'signup',
        {
            'title': 'Sign Up',
            'show_header_navibar': true,
            'show_footer_navibar': false
        }
    );
}
function notify_reset_password() {
    notify_device(
        'reset_password',
        {
            'title': 'Reset Password',
            'show_header_navibar': true,
            'show_footer_navibar': false
        }
    );
}
function notify_about_us() {
    notify_device(
        'about_us',
        {
            'title': 'About Us',
            'show_header_navibar': true,
            'show_footer_navibar': false
        }
    );
}
function notify_return() {
    notify_device(
        'return',
        {
            'title': 'Return',
            'show_header_navibar': true,
            'show_footer_navibar': false
        }
    );
}
function notify_shipping() {
    notify_device(
        'shipping',
        {
            'title': 'Shipping',
            'show_header_navibar': true,
            'show_footer_navibar': false
        }
    );
}
function notify_private_policy() {
    notify_device(
        'privacy_policy',
        {
            'title': 'Privacy Policy',
            'show_header_navibar': true,
            'show_footer_navibar': false
        }
    );
}
function notify_FAQ() {
    notify_device(
        'FAQ',
        {
            'title': 'FAQ',
            'show_header_navibar': true,
            'show_footer_navibar': false
        }
    );
}
function notify_balance() {
    notify_device(
        'Balance',
        {
            'title': 'Balance',
            'show_header_navibar': true,
            'show_footer_navibar': false
        }
    );
}
function notify_howitworks() {
    notify_device(
        'How It Works',
        {
            'title': 'How It Works',
            'show_header_navibar': true,
            'show_footer_navibar': false
        }
    );
}
function notify_ebay() {
    notify_device(
        'ebay',
        {
            'title': 'ebay',
            'show_header_navibar': true,
            'show_footer_navibar': false
        }
    );
}
function notify_terms() {
    notify_device(
        'terms',
        {
            'title': 'Terms & Conditions',
            'show_header_navibar': true,
            'show_footer_navibar': false
        }
    );
}
function notify_payment() {
    notify_device(
        'payment',
        {
            'title': 'Payment',
            'show_header_navibar': true,
            'show_footer_navibar': false
        }
    );
}
function notify_stripe() {
    notify_device(
        'stripe',
        {
            'title': '',
            'show_header_navibar': true,
            'show_footer_navibar': false
        }
    );
}
function notify_product_detail() {
    notify_device(
        'product_detail',
        {
            'title': '',
            'show_header_navibar': true,
            'show_footer_navibar': false
        }
    );
}
function notify_wishlist() {
    notify_device(
        'wishlist',
        {
            'title': 'Favorites',
            'show_header_navibar': true,
            'show_footer_navibar': false
        }
    );
}
function notify_cart() {
    notify_device(
        'cart',
        {
            'title': 'Shopping Cart',
            'show_header_navibar': true,
            'show_footer_navibar': false
        }
    );
}
function notify_product_list() {
    notify_device('shop_list', {});
}
function notify_theme() {
    notify_device(
        'theme',
        {
            'title': '',
            'show_header_navibar': true,
            'show_footer_navibar': false
        }
    );
}

function notify_categories() {
    notify_device(
        'Categories',
        {
            'tab_index': 1,
            'title': 'Categories',
            'show_header_navibar': true,
            'show_footer_navibar': true
        }
    );
}

function notify_subcategories() {
    notify_device(
        'subCategories',
        {
            'title': 'Categories',
            'show_header_navibar': true,
            'show_footer_navibar': false
        }
    );
}

function notify_category (category_id, parent_id, name) {
    if (navigator.userAgent.indexOf('android_native_navbar') > -1) {
        config = {
            name: name,
            parent_id: parent_id,
            category_id: category_id,
        }
        window.JSInterface.change_categ(JSON.stringify(config));
    } else if (navigator.userAgent.indexOf('ios_native_navbar') > -1) {
        try{
            window.webkit.messageHandlers.change_categ.postMessage({
                parent_id: parent_id,
                category_id: category_id,
            });
        } catch (err){
            return;
        }
    }
}

function is_device() {
    return navigator.userAgent.indexOf('android_native_navbar') > -1 || navigator.userAgent.indexOf('ios_native_navbar') > -1;
}

function device_add_to_cart() {
    if (navigator.userAgent.indexOf('android_native_navbar') > -1) {
        window.JSInterface.add_to_cart();
    } else if (navigator.userAgent.indexOf('ios_native_navbar') > -1) {
        try{
            jto_add_to_cart();
        } catch (err){
            window.webkit.messageHandlers.add_to_cart.postMessage("test");
        }
    }
}

function device_add_to_wishlist() {
    if (navigator.userAgent.indexOf('android_native_navbar') > -1) {
        window.JSInterface.add_to_wishlist();
    } else if (navigator.userAgent.indexOf('ios_native_navbar') > -1) {
        try{
            jto_add_to_wishlist();
        } catch (err){
            window.webkit.messageHandlers.add_to_wishlist.postMessage("test");
        }
    }
}

function device_confirm_order() {
    if (navigator.userAgent.indexOf('android_native_navbar') > -1) {
        window.JSInterface.add_to_confirm_order();
    } else if (navigator.userAgent.indexOf('ios_native_navbar') > -1) {
        try{
            jto_confirm_order();
        } catch (err){
            window.webkit.messageHandlers.confirm_order.postMessage("test");
        }
    }
}

function device_payment_success(amount) {
    if (navigator.userAgent.indexOf('android_native_navbar') > -1) {
        window.JSInterface.add_to_payment_success(amount);
    } else if (navigator.userAgent.indexOf('ios_native_navbar') > -1) {
        try{
            jto_payment(amount);
        } catch (err){
            window.webkit.messageHandlers.payment.postMessage(amount);
        }
    }
}

function device_fb_login() {
    if (navigator.userAgent.indexOf('android_native_navbar') > -1) {
        window.JSInterface.fb_login();
    } else if (navigator.userAgent.indexOf('ios_native_navbar') > -1) {

        try{
            jto_fb_login();
        } catch (err){
            window.webkit.messageHandlers.fb_login.postMessage("test");
        }
    }
}

function device_fb_logout() {
    if (navigator.userAgent.indexOf('android_native_navbar') > -1) {
        window.JSInterface.fb_logout();
    } else if (navigator.userAgent.indexOf('ios_native_navbar') > -1) {

        try{
            jto_fb_logout();
        } catch (err){
            window.webkit.messageHandlers.fb_logout.postMessage("test");
        }
    }
}
