/*
var $ = null;

require("jsdom").env("", function(err, window) {
    if (err) {
        console.error(err);
        return;
    }

    $ = require("jquery")(window);
});
*/

function request(url, post_data, success_handler){
	$.ajax({
		url: url,
        data: post_data,
        contentType: 'application/x-www-form-urlencoded',
        type: 'POST',
        'crossDomain':true,
        'dataType': 'JSON',
        success: success_handler,
	})
}

var success_handler = function(data){
	console.log(data);
}

var url = "http://localhost:8080/auth/oauth_login";
var post_data = {
	'accessToken': 'EAACMgU5BffABACfo2jA4HmRG2dZAq0BXxG85hRP9jOqBAt1EMVH2yuDtz5DsmFoW947yUYrsB0RNQBYrmjrxtOxJ2zzatRjeFFUkOl769WUApP6vuokd056zp0Vrsi0cz7a3jC6O89oJ9ZCsMR7s0CtT89Ts2bW4fyUHeIqcyzo2pCGUywQwQCHJFYDKJfA8SmR7OUBQZDZD',
	'loginType': 2,
}

request(url, post_data, success_handler)