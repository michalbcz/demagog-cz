/* inject jquery */
var jqueryScript = document.createElement("script");
jqueryScript.src = "https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js";
jqueryScript.type = "text/javascript";
jqueryScript.onload = function() { Demagog.Bookmarklet.Events.onJqueryReady() };
document.getElementsByTagName("head")[0].appendChild(jqueryScript);

var Demagog.Bookmarklet = DemagogBookmarklet || {}

Demagog.Bookmarklet.Events.onJqueryReady = function() {

	jQuery(document).ready(function() {
		Demagog.Bookmarklet.submitSelectedQuote();
	});

}

Demagog.Bookmarklet.submitSelectedQuote() {


}

Demagog.Bookmarklet.Util.postToUrl = function(path, params, method) {
    method = method || "post"; // Set method to post by default, if not specified.

    // The rest of this code assumes you are not using a library.
    // It can be made less wordy if you use one.
    var form = document.createElement("form");
    form.setAttribute("method", method);
    form.setAttribute("action", path);

    for(var key in params) {
        if(params.hasOwnProperty(key)) {
            var hiddenField = document.createElement("input");
            hiddenField.setAttribute("type", "hidden");
            hiddenField.setAttribute("name", key);
            hiddenField.setAttribute("value", params[key]);

            form.appendChild(hiddenField);
         }
    }

    document.body.appendChild(form);
    form.submit();
}