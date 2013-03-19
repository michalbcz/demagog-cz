/* 	
	TODO: misto alertu "Saved to demagog" ukazat modalni okno s textem a linkem na demagog
*/

var Demagog = Demagog || {};
Demagog.Bookmarklet = Demagog.Bookmarklet || {};
Demagog.Bookmarklet.Events = Demagog.Bookmarklet.Events || {};
Demagog.Bookmarklet.Util = Demagog.Bookmarklet.Util || {};

Demagog.Bookmarklet.Events.onJqueryReady = function() {

	jQuery(document).ready(function() {
	
	
		/* inject jquery ui css */
		var style = document.createElement("link");
		style.type = "text/css";
		style.rel = "stylesheet";
		style.href = "http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.0/themes/base/jquery-ui.css";
		document.getElementsByTagName("head")[0].appendChild(style);
			
		/* inject and load jQuery UI */
	
		var jqueryUiScript = document.createElement("script");
		jqueryUiScript.src = "https://ajax.googleapis.com/ajax/libs/jqueryui/1.9.1/jquery-ui.min.js";
		jqueryUiScript.type = "text/javascript";
		jqueryUiScript.onload = function() { Demagog.Bookmarklet.Events.onJqueryUiReady() };
		document.getElementsByTagName("head")[0].appendChild(jqueryUiScript);
	
		
	});

};

Demagog.Bookmarklet.Events.onJqueryUiReady = function() {
	Demagog.Bookmarklet.submitSelectedQuote();
}

Demagog.Bookmarklet.submitSelectedQuote = function() {
	var selectedText = Demagog.Bookmarklet.Util.getSelected();
	var sourceUrl = window.location.href;
	Demagog.Bookmarklet.Util.postToUrlAsync(selectedText, sourceUrl);
};

Demagog.Bookmarklet.openSuccessDialog = function(url) {
	
	console.debug("Opening success modal with url param: ", url);
	
	var dialogHtml = 
			'<div id="demagogBookmarkletSuccessDialog" title="Demagog.cz" style="display: none;">' +
				'<p>Úspěšně přidáno na demagog.cz!</p><br/>' +
				'<p id="demagogQuoteDetailUrl"><a href="' + url + '">' + url + '</a></p><br/>' +
				'<p>Výrok bude nejdřívě ověřen našim týmem potom se dostane do hlasování a ' +
				'pokud bude mít hodně hlasů či bude zajímavý tak bude ověřen našimi experty.' +
				'</p>'
			'</div>';
	
	jQuery("div:first").append(dialogHtml);
	
	jQuery("#demagogBookmarkletSuccessDialog").dialog();
	
}

Demagog.Bookmarklet.Util.getSelected = function() {
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

Demagog.Bookmarklet.Util.generateGuid = function() {

    var S4 = function ()
    {
        return Math.floor(
                Math.random() * 0x10000 /* 65536 */
            ).toString(16);
    };

    return (
            S4() + S4() + "-" +
            S4() + "-" +
            S4() + "-" +
            S4() + "-" +
            S4() + S4() + S4()
        );

}

Demagog.Bookmarklet.Util.postToUrlAsync = function(selectedText, sourceUrl) {

	var data = [];
	data.push({name: "url", value: sourceUrl});
	data.push({name: "quoteText", value: selectedText});
	data.push({name: "token", value: "" + Demagog.Bookmarklet.Util.generateGuid()});
		
	var apiUrl = Demagog.Bookmarklet.Settings.demagogVotingAppApiBaseUrl + "/api/v1/quote/save"
	console.debug("Sending data", data, " to server: ", apiUrl);

	jQuery.ajax ({
		url: apiUrl,
		contentType: 'application/json',
		data: data,
		dataType: 'jsonp',		
		success: function(data) {
			console.log("Response:", data);
			Demagog.Bookmarklet.openSuccessDialog(data.quotePermalinkUrl);
		},
		error: function(jqXhr, textStatus, errorThrown) {
			console.error("Error '", textStatus, "' when posting quote to api. Error thrown: ", errorThrown);
		}
	});

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
};

/*
 *  Initialize console object used for logging in javascript code.
 * 
 * For case when console object is not present (old browsers) use dump implementation.
 * 
 */

console = console || {

	info : function() {},
	debug : function() {},
	error : function() {},
	warn : function() {}

};

console.info = console.info || function() {};
console.error = console.error || function() {};
console.warn = console.warn || function() {};
console.debug = console.debug || function() {};

/* inject jquery */
var jqueryScript = document.createElement("script");
jqueryScript.src = "https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js";
jqueryScript.type = "text/javascript";
jqueryScript.onload = function() { Demagog.Bookmarklet.Events.onJqueryReady() };
document.getElementsByTagName("head")[0].appendChild(jqueryScript);

