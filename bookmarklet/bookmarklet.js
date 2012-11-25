﻿/* 
	TODO: misto alertu "Saved to demagog" ukazat modalni okno s textem a linkem na demagog
*/

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
		var sourceUrl = window.location.href;
		Demagog.Bookmarklet.Util.postToUrlAsync(selectedText, sourceUrl);
		alert("Saved to demagog");
};

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
		
	var apiUrl = "http://localhost:9000/api/v1/quote/save"
	console.debug("Sending data", data, " to server: ", apiUrl);

	jQuery.ajax ({
		url: apiUrl,
		contentType: 'application/json',
		data: data,
		dataType: 'jsonp',		
		success: function(data) {
			console.log("Response:", data);
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

var console = console || {

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