//1\ when back to the payment page, this event will happen
$(function(){
    //1 2\ when enter into the page, the js will notify the phone device
    try{
        if(is_device()){
            url = 'payment_successful';
            config = {
                'title': 'Order',
                'show_header_navibar': true,
                'show_footer_navibar': false
            }
            notify_device(url, config);
        }
    }catch(err){

    }
});


