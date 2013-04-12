﻿/**
 * Bookmarklet for overto.demagog.cz.
 *
 * This is script needs to have object 'Demagog.Bookmarklet.Settings.demagogVotingAppApiBaseUrl' set to url
 * of base server, where api sits and listens.
 *
 * All external libraries/scripts (javascript), css, images are loaded through HTTPS protocol.
 * If we would use HTTP and run bookmarklet on HTTPS server it will brake up.
 *
 *
 */


/*
 *  Initialize console object used for logging in javascript code.
 *  For case when console object is not present (old browsers) use dump implementation.
 *
 */

if (typeof(console) === "undefined") {
    console = {

        info : function() {},
        debug : function() {},
        error : function() {},
        warn : function() {}

    };
}

console.info = console.info || function() {};
console.error = console.error || function() {};
console.warn = console.warn || function() {};
console.debug = console.debug || function() {};

console.info("Detecting Demagog.Bookmarklet...");


if (typeof(Demagog.Bookmarklet.Events) === "undefined") {

    console.info("Demagog Bookmarklet is not detected on the page");
    console.info("Demagog Bookmarklet > Loading...");

    var Demagog = Demagog || {};
    Demagog.Bookmarklet = Demagog.Bookmarklet || {};
    Demagog.Bookmarklet.Events = Demagog.Bookmarklet.Events || {};
    Demagog.Bookmarklet.Util = Demagog.Bookmarklet.Util || {};

    Demagog.Bookmarklet.Events.onJqueryReady = function() {

        console.info("Demagog Bookmarklet > jQuery successfully loaded");

        jQuery(document).ready(function() {

            //TODO mbernhard : using jquery ui if exists or using something more minimalist...

            console.debug("Demagog Bookmarklet > injecting jquery ui css");

            var style = document.createElement("link");
            style.type = "text/css";
            style.rel = "stylesheet";
            style.href = "https://ajax.googleapis.com/ajax/libs/jqueryui/1.9.0/themes/base/jquery-ui.css";
            document.getElementsByTagName("head")[0].appendChild(style);

            console.debug("Demagog Bookmarklet > injecting and loading jQuery UI");

            var jQueryUiScriptUrl = "https://ajax.googleapis.com/ajax/libs/jqueryui/1.9.1/jquery-ui.min.js";
            var jQueryUiScriptOnLoad = function() { Demagog.Bookmarklet.Events.onJqueryUiReady() };
            Demagog.Bookmarklet.Util.injectJavascript(jQueryUiScriptUrl, jQueryUiScriptOnLoad);

        });

    };

    Demagog.Bookmarklet.Events.onJqueryUiReady = function() {

        console.debug("Demagog Bookmarklet > jQuery UI successfully loaded");
        var selectedQuoteText = Demagog.Bookmarklet.Util.getSelected();

        console.debug("Demagog Bookmarklet > Opening confirm dialog...")
        Demagog.Bookmarklet.openConfirmDialog(selectedQuoteText);
    };

    Demagog.Bookmarklet.openSuccessDialog = function(url) {

        console.debug("Demagog Bookmarklet > Opening success modal with url param: ", url);

        var dialogHtml =
                '<div id="demagogBookmarkletSuccessDialog" title="Demagog.cz" style="display: none">' +
                    '<p>Úspěšně přidáno na demagog.cz!</p><br/>' +
                    '<p id="demagogQuoteDetailUrl"><a href="' + url + '">' + url + '</a></p><br/>' +
                    '<p>Výrok bude nejdříve ověřen našim týmem, po schválení se dostane do hlasování a ' +
                    'pokud bude mít větší množství hlasů či bude zajímavý tak bude ověřen našimi experty.' +
                    '</p>'
                '</div>';

        jQuery("div:first").append(dialogHtml);

        jQuery("#demagogBookmarkletSuccessDialog").dialog({ width: 450});

    };

    Demagog.Bookmarklet.openErrorDialog = function () {

        console.debug("Opening error modal dialog");

        var $errorDialog = jQuery("#demagogBookmarkletErrorDialog");

        if ($errorDialog.size() > 0) {
            $errorDialog.dialog("destroy"); // if already on the page destroy it and open again with fresh data
        }

        var dialogHtml =
            '<div id="demagogBookmarkletErrorDialog" title="Demagog.cz - Chyba při odesílání" style="display: none">' +
                'Nedaří se nám odeslat citát. Omlouváme se za potíže, zkuste to prosím později nebo využijte ' +
                'možnost manuálního vložení na <a href="http://overto.demagog.cz/quote/add" target="_blank">overto.demamgo.cz</a>' +
            '</div>';

        jQuery("div:first").append(dialogHtml);
        $errorDialog.dialog();

    };

    Demagog.Bookmarklet.closeConfirmDialog = function() {
        console.debug("Demagog Bookmarklet > Closing confirm dialog");
        var $confirmDialogWindow = jQuery("#demagogBookmarkletConfirmDialog");
        $confirmDialogWindow.dialog("destroy");
    }

    Demagog.Bookmarklet.openConfirmDialog = function(quoteText) {

        console.debug("openConfirmDialog with quoteText: ", quoteText);

        var $dialogWindow = jQuery("#demagogBookmarkletConfirmDialog")
        if (/* dialog already created */ $dialogWindow.size() > 0) {
            $dialogWindow.remove();
        }

        var dialogHtml =
            '<div id="demagogBookmarkletConfirmDialog" style="display: none">' +
            '        <span>' +
            '            Níže je citát, který jste vybrali a který se po potvrzení odešle na stránku' +
            '            <a href="http://overto.demagog.cz" target="_blank">overto.demagog.cz</a>:' +
            '        </span>' +
            '        <div id="demagogBookmarkletConfirmDialogPreviewContent">' +
            '            <span id="demagogBookmarkletConfirmDialogQuotePreview">' +
            '            </span>' +
            '        </div>' +
            '        <div id="demagogBookmarkletConfirmDialogCaptcha">' +
            '            Pro potvrzení opište níže uvedený kód.' +
            '            <div id="demagogBookmarkletConfirmDialogCaptchaPlaceholder">' +
            '                <!-- captcha should be placed here -->' +
            '            </div>' +
            '            <div id="captchaError">' +
            '           ' +
            '           </div>' +
            '           ' +
            '        </div>' +
            '        <a class="button" id="demagogBookmarkletConfirmDialogConfirmButton" href="#">Odeslat citát</a>' +
            '    </div>';

        jQuery("body").append(dialogHtml);

        var confirmDialogOnOpen = function(event, ui) {

            console.debug("initializing confirm dialog...");

            console.debug("populating preview of quote...");
            jQuery("#demagogBookmarkletConfirmDialogQuotePreview").text(quoteText);

            console.debug("adding recaptcha widget...");
            var recaptchaApiLoaded = function() {

                console.debug("Focusing on response field in recaptcha widget.");
                Recaptcha.focus_response_field();

                console.debug("Enable confirm button and adding onclick behaviour.");

                var $confirmButton = jQuery("#demagogBookmarkletConfirmDialogConfirmButton");
                $confirmButton.click(function() {

                    var sourceUrl = window.location.href;
                    var recaptchaChallenge = Recaptcha.get_challenge();
                    var recaptchaResponse = Recaptcha.get_response();

                    var onSuccess = function(responseData) {
                        Demagog.Bookmarklet.closeConfirmDialog();
                        Demagog.Bookmarklet.openSuccessDialog(responseData.quotePermalinkUrl);
                    };

                    var onError = function() {
                        Demagog.Bookmarklet.openErrorDialog();
                    };

                    var onValidationError = function(responseData) {

                        var $errorText = jQuery("#captchaError");
                        $errorText.text("Text jste opsali špatně zkuste to znovu.")
                        $errorText.show();

                        Recaptcha.reload();
                        Recaptcha.focus_response_field();

                    };

                    Demagog.Bookmarklet.Util.postToUrlAsync({
                        selectedText : quoteText,
                        sourceUrl : sourceUrl,
                        recaptchaChallenge : recaptchaChallenge,
                        recaptchaResponse : recaptchaResponse
                    }, onSuccess, onValidationError, onError);

                });

                /* enable button */
                $confirmButton.button( "option", "disabled", false);

            }

            var recaptchaOnLoad = function() {

                Recaptcha.create(
                    "6LfnpN4SAAAAAEXiCrG_AlH8tx_T_hd3MrQjx55j" /* recaptcha public key */,
                    "demagogBookmarkletConfirmDialogCaptchaPlaceholder" /* id of element replaced by recaptcha's widget */,
                    {
                        theme: "white",
                        //TODO : czech localization of recaptcha
                        callback: recaptchaApiLoaded
                    }
                );

            }

            /* ReCaptcha Javascript API - see https://developers.google.com/recaptcha/docs/display#AJAX */
            if (typeof(Recaptcha) === "undefined") {
                var recaptchaScriptUrl = "https://www.google.com/recaptcha/api/js/recaptcha_ajax.js";
                Demagog.Bookmarklet.Util.injectJavascript(recaptchaScriptUrl, recaptchaOnLoad);
            } else {
                recaptchaOnLoad(); // we can recreate whole recaptcha widget directly without obtaining recaptcha api script as it's already loaded
            }

            console.debug("initializing confirm button");
            jQuery("#demagogBookmarkletConfirmDialogConfirmButton").button();
            jQuery("#demagogBookmarkletConfirmDialogConfirmButton").button( "option", "disabled", true );

            console.debug("initializing confirm dialog - DONE");
        };

        jQuery("#demagogBookmarkletConfirmDialog").dialog({
            open: confirmDialogOnOpen,
            width: 500,
            height: 300
        });

    };  // confirmDialogOnOpen

    Demagog.Bookmarklet.Util.getSelected = function() {
        var t = '';
        if(window.getSelection) {
            t = window.getSelection();
        } else if(document.getSelection) {
            t = document.getSelection();
        } else if(document.selection) {
            t = document.selection.createRange().text;
        }

        return t.toString();
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

    },

    Demagog.Bookmarklet.Util.postToUrlAsync = function(paramsToSend,
                                                       onSuccessCallback,
                                                       onValidationErrorCallback,
                                                       onSendingErrorCallback) {

        // modalni okno s preview a captchou
        var data = {

        };
        data.url = paramsToSend.sourceUrl;
        data.quoteText = paramsToSend.selectedText;
        data.recaptchaResponse = paramsToSend.recaptchaResponse;
        data.recaptchaChallenge = paramsToSend.recaptchaChallenge;

//        data.push({name: "url", value: paramsToSend.sourceUrl});
//        data.push({name: "quoteText", value: paramsToSend.selectedText});
//        data.push({name: "recaptcha_response_field", value: paramsToSend.recaptchaResponse});
//        data.push({name: "recaptcha_challenge_field", value: paramsToSend.recaptchaChallenge});
//        data.push({name: "token", value: "" + Demagog.Bookmarklet.Util.generateGuid()});

        var apiUrl = Demagog.Bookmarklet.Settings.demagogVotingAppApiBaseUrl + "/api/v1/quote/save"
        console.debug("Sending data", data, " to server: ", apiUrl);

        jQuery.ajax ({
            type: 'POST',
            url: apiUrl,
            data : JSON.stringify(data),
            processData : false,
            contentType: 'application/json',
            crossDomain: true,
            dataType: 'json',
            success: function(response) {
                console.log("Server response: ", response);

                if (response.metadata.status.text === "error") {
                     console.error("Server responds with an error message. Response:", response);
                     onValidationErrorCallback(response);
                }
                else {
                    onSuccessCallback(response.data)
                }
            },
            error: function(jqXhr, textStatus, errorThrown) {
                console.error("Error '", textStatus, "' when posting quote to api. Error thrown: ", errorThrown);
                onSendingErrorCallback();
            }
        });

    }

    /**
     * Inject javascript from given url to page.
     *
     * @param url point to javascript you would like to inject (expecting url format including http part) eg. http://jquery.com/jquery.js
     * @param onLoadCallback this function is called when script is succesfully injected and loaded by browser
     */
    Demagog.Bookmarklet.Util.injectJavascript = function(url, onLoadCallback) {

        var javascriptElement = document.createElement("script");
        javascriptElement.type = "text/javascript";
        javascriptElement.src = url;
        javascriptElement.onload = onLoadCallback || function() {};

        document.getElementsByTagName("head")[0].appendChild(javascriptElement);

    }

    console.debug("Demagog.Bookmarklet > Injecting jQuery...")
    var jQueryScriptUrl = "https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js";
    var jQueryScriptOnLoad = function() {
        Demagog.Bookmarklet.Events.onJqueryReady()
    };

    // TODO michalb_cz : reusing jQuery if already present on the page (detecting minimal jQuery version which support api we use)
    Demagog.Bookmarklet.Util.injectJavascript(jQueryScriptUrl, jQueryScriptOnLoad);

} /* if Demagog bookmarklet is not already loaded END*/
else {
    console.debug("Demagog.Bookmarklet is already loaded to this page.");

    var selectedQuoteText = Demagog.Bookmarklet.Util.getSelected();
    Demagog.Bookmarklet.openConfirmDialog(selectedQuoteText);
}