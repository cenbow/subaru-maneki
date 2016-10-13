

jQuery(function($) {
    //当cookies android_id的值不为空时（设备唯一标识）
    var android_id = Cookies.get("android_id");
    if (android_id) {
        //ga('set', 'dimension1', android_id)
    }
    //当cookies  UID的值不为空时（会员标识）
    var uid = Cookies.get("uid");
    if (uid) {
        //ga('set','dimension2', uid);
    }
    //当cookies  utm_source的值不为空时（广告来源）
    var utm_source = Cookies.get("utm_source");
    if (utm_source) {
        //ga('set','dimension3', utm_source);
    }

    //ga('send', 'pageview');
});
