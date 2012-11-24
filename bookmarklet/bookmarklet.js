var Demagog = Demagog || {};
Demagog.Bookmarklet = Demagog.Bookmaklet || {};
Demagog.Bookmarklet.Events = Demagog.Bookmarklet.Events || {};
Demagog.Bookmarklet.Util = Demagog.Bookmarklet.Util || {};

Demagog.Bookmarklet.Events.onJqueryReady = function() {

	jQuery(document).ready(function() {
		Demagog.Bookmarklet.submitSelectedQuote();
	});

};

Demagog.Bookmarklet.submitSelectedQuote = function() {
		var selectedText = Demagog.Bookmarklet.Util.getSelected();
		Demagog.Bookmarklet.Util.postToUrl("http://localhost:9000/quote/save", {"url": window.location.href, "quoteText": selectedText });
		alert("Saved to demagog");
};

Demagog.Bookmarklet.Util.getSelected = function(){
	var t = '';
	if(window.getSelection) {
		t = window.getSelection();
	} else if(document.getSelection) {
		t = document.getSelection();
	} else if(document.selection) {
		t = document.selection.createRange().text;
	}
	
	return t;
};

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
};

/* inject jquery */
var jqueryScript = document.createElement("script");
jqueryScript.src = "https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js";
jqueryScript.type = "text/javascript";
jqueryScript.onload = function() { Demagog.Bookmarklet.Events.onJqueryReady() };
document.getElementsByTagName("head")[0].appendChild(jqueryScript);