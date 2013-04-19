/**
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

console = console || {};

console.info = console.info || function() {};
console.error = console.error || function() {};
console.warn = console.warn || function() {};
console.debug = console.debug || console.info; // when console.debug doesn't exist (like in IE8+) fallback to info

console.info("Detecting Demagog.Bookmarklet...");


if (typeof(Demagog.Bookmarklet.Events) === "undefined") {

    console.info("Demagog Bookmarklet is not already loaded on the page. Loading it now...");

    var Demagog = Demagog || {};
    Demagog.Bookmarklet = Demagog.Bookmarklet || {};
    Demagog.Bookmarklet.Events = Demagog.Bookmarklet.Events || {};
    Demagog.Bookmarklet.Util = Demagog.Bookmarklet.Util || {};

    Demagog.Bookmarklet.confirmSelectedTextAsQuote = function() {
        var selectedQuoteText = Demagog.Bookmarklet.Util.getSelectedText();
        Demagog.Bookmarklet.openConfirmDialog(selectedQuoteText);
    }

    Demagog.Bookmarklet.Events.onJqueryReady = function() {

        jQuery(document).ready(function() {

            //TODO mbernhard : using jquery ui if exists or using something more minimalist...

            console.debug("Demagog Bookmarklet > injecting jquery ui css");
            var jQueryUiCssUrl = "https://ajax.googleapis.com/ajax/libs/jqueryui/1.9.1/themes/sunny/jquery-ui.css";
            Demagog.Bookmarklet.Util.injectCss(jQueryUiCssUrl);

            console.debug("Demagog Bookmarklet > injecting and loading jQuery UI");
            var jQueryUiScriptUrl = "https://ajax.googleapis.com/ajax/libs/jqueryui/1.9.1/jquery-ui.min.js";
            var jQueryUiScriptOnLoad = function() { Demagog.Bookmarklet.Events.onJqueryUiReady() };
            Demagog.Bookmarklet.Util.injectJavascript(jQueryUiScriptUrl, jQueryUiScriptOnLoad);

        });

    };

    Demagog.Bookmarklet.Events.onJqueryUiReady = function() {

        console.debug("Demagog Bookmarklet > jQuery UI successfully loaded");

        /*
        var bookmarkletCssUrl = Demagog.Bookmarklet.Settings.bookmarkletSourceBaseUrl + "/bookmarklet.css";
        Demagog.Bookmarklet.Util.injectCss(bookmarkletCssUrl);
        */

        var cssContent = ".bookmarkletDialog {\n" +
            "    text-align: left;\n" +
            "    font-size: 14px !important;\n" +
            "}\n" +
            "\n" +
            ".bookmarkletDialog #recaptcha_area {\n" +
            "    /* recaptcha widget - align to center */\n" +
            "    margin-right: auto;\n" +
            "    margin-left: auto;\n" +
            "}\n" +
            "\n" +
            "#demagogBookmarkletConfirmDialog #captchaError {\n" +
            "    color: #ff3030;\n" +
            "    font-weight: bolder;\n" +
            "    text-align: center;\n" +
            "}\n" +
            "\n" +
            ".demagogBookmarkletSuccessText {\n" +
            "    font-weight: bolder;\n" +
            "    text-align: center;\n" +
            "}\n" +
            "\n" +
            ".demagogQuoteDetailUrlLabel {\n" +
            "    text-align: center;\n" +
            "}\n" +
            "\n" +
            ".demagogBookmarkletSuccessLink {\n" +
            "    font-weight: bolder;\n" +
            "}\n" +
            "\n" +
            "#demagogBookmarkletConfirmDialogCaptchaPlaceholder {\n" +
            "    margin-top: 5px;\n" +
            "}\n" +
            "\n" +
            "#demagogBookmarkletConfirmDialogPreviewContent {\n" +
            "    font-weight: bolder;\n" +
            "    padding: 10px 10px;\n" +
            "    font-size: smaller;\n" +
            "}\n" +
            "\n" +
            ".bookmarkletDialog hr {\n" +
            "    width: 100%;\n" +
            "    background-color: black;\n" +
            "    height: 1px;\n" +
            "    border: 1px;\n" +
            "    margin-top: 10px;\n" +
            "    margin-bottom : 10px;\n" +
            "    display: block;\n" +
            "}\n" +
            "\n" +
            "#demagogQuoteDetailUrl {\n" +
            "    text-align: center;\n" +
            "    font-weight: bolder;\n" +
            "}\n" +
            "\n" +
            ".demagogBookmarkletDialogFooter {\n" +
            "    text-align: center;\n" +
            "}";

        var styleElement = document.createElement("style");
        styleElement.type = "text/css";
        styleElement.onload = function() {
            console.debug("Demagog Bookmarklet > Loaded custom css style.");
        };
        jQuery(styleElement).text(cssContent);
        document.getElementsByTagName("head")[0].appendChild(styleElement);

        console.debug("Demagog Bookmarklet > Opening confirm dialog...")
        var selectedQuoteText = Demagog.Bookmarklet.Util.getSelectedText();
        Demagog.Bookmarklet.openConfirmDialog(selectedQuoteText);
    };

    Demagog.Bookmarklet.openSuccessDialog = function(url) {

        console.debug("Demagog Bookmarklet > Opening success modal with url param: ", url);

        var $successDialog = jQuery("#demagogBookmarkletSuccessDialog");
        if ($successDialog.size() > 0) {
            $successDialog.remove(); // if already on the page destroy it and open again with fresh data
        }

        var dialogHtml =
                '<div id="demagogBookmarkletSuccessDialog" title="Demagog.cz" style="display: none">' +
                    '<span>' +
                        '<p class="demagogBookmarkletSuccessText">Váš citát byl úspěšně přidán na overto.demagog.cz. </p><br/>' +
                        '<p class="demagogQuoteDetailUrlLabel">Výsledek najdete na níže uvedené adrese:</p>' +
                    '</span>' +
                    '<p id="demagogQuoteDetailUrl"><a href="' + url + '" target="_blank">' + url + '</a></p><br/>' +
                    '<p>Výrok bude nejdříve ověřen našim týmem a po schválení se dostane do hlasování a ' +
                    'pokud bude mít větší množství hlasů či bude zajímavý tak bude ověřen našimi experty.' +
                    '</p>' +
                    '<hr>' +
                    '<div class="demagogBookmarkletDialogFooter">' +
                    '          <a class="button" id="demagogBookmarkletSuccessDialogCancelButton" href="#">Zavřít</a>' +
                    '</div>' +
                '</div>';


        jQuery("div:first").append(dialogHtml);

        jQuery("#demagogBookmarkletSuccessDialog").dialog({
            title: "overto.Demagog.cz - Citát odeslán",
            dialogClass: "bookmarkletDialog",
            width: 580,
            height: "auto",
            open: function() {
                var $successDialogCloseButton = jQuery("#demagogBookmarkletSuccessDialogCancelButton");
                $successDialogCloseButton.click(function() {
                    var $successDialogWindow = jQuery("#demagogBookmarkletSuccessDialog");
                    $successDialogWindow.dialog("close");
                });
                $successDialogCloseButton.button();
            }
        });

    };

    Demagog.Bookmarklet.openErrorDialog = function () {

        console.debug("Opening error modal dialog");

        var $errorDialog = jQuery("#demagogBookmarkletErrorDialog");

        if ($errorDialog.size() > 0) {
            $errorDialog.remove(); // if already on the page destroy it and open again with fresh data
        }

        var dialogHtml =
            '<div id="demagogBookmarkletErrorDialog" title="Demagog.cz - Chyba při odesílání" style="display: none">' +
                'Nedaří se nám odeslat citát. Omlouváme se za potíže, zkuste to prosím později nebo využijte ' +
                'možnost manuálního vložení na <a href="http://overto.demagog.cz/quote/add" target="_blank">overto.demamgo.cz</a>' +
            '</div>';

        jQuery("div:first").append(dialogHtml);

        jQuery("#demagogBookmarkletErrorDialog").dialog({
            title: "overto.Demagog.cz - Chyba při odesílání",
            dialogClass: "bookmarkletDialog"
        });

    };

    Demagog.Bookmarklet.closeConfirmDialog = function() {
        console.debug("Demagog Bookmarklet > Closing confirm dialog");
        var $confirmDialogWindow = jQuery("#demagogBookmarkletConfirmDialog");
        $confirmDialogWindow.dialog("destroy");
    }

    Demagog.Bookmarklet.openConfirmDialog = function(quoteText) {

        console.debug("Demagog Bookmarklet > openConfirmDialog with quoteText: ", quoteText);

        var $confirmDialog = jQuery("#demagogBookmarkletConfirmDialog")
        if (/* dialog already created */ $confirmDialog.size() > 0) {
            $confirmDialog.remove();
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
            '            Pro potvrzení opište níže uvedený kód:' +
            '            <div id="demagogBookmarkletConfirmDialogCaptchaPlaceholder">' +
            '                <!-- captcha should be placed here -->' +
            '            </div>' +
            '            <div id="captchaError">' +
            '           ' +
            '           </div>' +
            '           ' +
            '        </div>' +
            '       <hr/>' +
            '       <div class="demagogBookmarkletDialogFooter">' +
            '          <a class="button" id="demagogBookmarkletConfirmDialogConfirmButton" href="#">Odeslat citát</a>' +
            '          <a class="button" id="demagogBookmarkletConfirmDialogCancelButton" href="#">Zavřít</a>' +
            '       </div>' +
            '    </div>';

        jQuery("div:first").append(dialogHtml);

        var confirmDialogOnOpen = function(event, ui) {

            console.debug("Demagog Bookmarklet > initializing confirm dialog...");

            console.debug("Demagog Bookmarklet > populating preview of quote...");
            jQuery("#demagogBookmarkletConfirmDialogQuotePreview").text(quoteText);

            console.debug("Demagog Bookmarklet > adding recaptcha widget to confirm dialog...");
            var recaptchaApiLoaded = function() {

                console.debug("Demagog Bookmarklet > Focusing on response field in recaptcha widget.");
                Recaptcha.focus_response_field();

                console.debug("Demagog Bookmarklet > Enable confirm button and adding onclick behaviour.");

                var $confirmDialogCloseButton = jQuery("#demagogBookmarkletConfirmDialogCancelButton");
                $confirmDialogCloseButton.click(function() {
                    var $confirmDialogWindow = jQuery("#demagogBookmarkletConfirmDialog");
                    $confirmDialogWindow.dialog("close");
                });
                $confirmDialogCloseButton.button();

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

                    var dataToSend = {
                        selectedText : quoteText,
                        sourceUrl : sourceUrl,
                        recaptchaChallenge : recaptchaChallenge,
                        recaptchaResponse : recaptchaResponse
                    }

                    Demagog.Bookmarklet.Util.postToUrl(dataToSend, onSuccess, onValidationError, onError);

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

            console.debug("Demagog Bookmarklet > initializing confirm button");
            jQuery("#demagogBookmarkletConfirmDialogConfirmButton").button();
            jQuery("#demagogBookmarkletConfirmDialogConfirmButton").button( "option", "disabled", true );

            console.debug("Demagog Bookmarklet > initializing confirm dialog - DONE");
        };

        jQuery("#demagogBookmarkletConfirmDialog").dialog({
            title: "overto.Demagog.cz - Odeslání citátu k ověření",
            dialogClass: "bookmarkletDialog",
            open: confirmDialogOnOpen,
            width: 580,
            minHeight: 300,
            height: "auto"

        });

    };  // confirmDialogOnOpen

    /**
     *
     * @return {String} selected (highlighted) text on the page if any
     */
    Demagog.Bookmarklet.Util.getSelectedText = function() {
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

    Demagog.Bookmarklet.Util.postToUrl = function(
                                            paramsToSend, onSuccessCallback, onValidationErrorCallback, onSendingErrorCallback) {

        var data = {
            url: paramsToSend.sourceUrl,
            quoteText: paramsToSend.selectedText,
            recaptchaResponse: paramsToSend.recaptchaResponse,
            recaptchaChallenge: paramsToSend.recaptchaChallenge
        };

        var apiUrl = Demagog.Bookmarklet.Settings.demagogVotingAppApiBaseUrl + "/api/v1/quote/save"
        console.debug("Demagog Bookmarklet > Sending data: ", data, " to server: ", apiUrl);

        jQuery.ajax({
            type: 'POST',
            url: apiUrl,
            data: JSON.stringify(data),
            processData : false,
            contentType: 'application/json',
            crossDomain: true,
            dataType: 'json',
            success: function(response) {
                console.log("Server response: ", response);

                if (response.metadata.status.text === "error") {
                     console.error("Demagog Bookmarklet > Server responds with an error message. Response:", response);
                     onValidationErrorCallback(response);
                }
                else {
                    onSuccessCallback(response.data)
                }
            },
            error: function(jqXhr, textStatus, errorThrown) {
                console.error(
                    "Demagog Bookmarklet > Ajax error: ", textStatus, " when posting quote to api. Error thrown: ", errorThrown);
                onSendingErrorCallback();
            }
        });

    };

    /**
     * Inject css from given url to page.
     *
     * @param cssUrl point to css you would like to inject (expecting url format including http part) eg. http://jquery.com/jquery.js
     * @param [optional] onLoadCallback this function is called when css is successfully injected and loaded by browser
     */
    Demagog.Bookmarklet.Util.injectCss = function(cssUrl, onLoadCallback) {

        console.debug("Demagog Bookmarklet > Loading custom css style from ", cssUrl);

        var styleElement = document.createElement("link");
        styleElement.type = "text/css";
        styleElement.rel = "stylesheet";
        styleElement.href = cssUrl;
        styleElement.onload = function() {

            console.debug("Demagog Bookmarklet > css from: ", cssUrl, "successfully loaded.");

            if (typeof(onLoadCallback) != "undefined") {
                onLoadCallback();
            }

        };
        document.getElementsByTagName("head")[0].appendChild(styleElement);

    };

    /**
     * Inject javascript from given url to page.
     *
     * @param url point to javascript you would like to inject (expecting url format including http part) eg. http://jquery.com/jquery.js
     * @param [optional] onLoadCallback this function is called when script is successfully injected and loaded by browser
     */
    Demagog.Bookmarklet.Util.injectJavascript = function(url, onLoadCallback) {

        var javascriptElement = document.createElement("script");
        javascriptElement.type = "text/javascript";
        javascriptElement.src = url;
        javascriptElement.onload = function() {

            console.debug("Demagog Bookmarklet > Loaded javascript from: ", url);

            if (typeof(onLoadCallback) != "undefined") {
                onLoadCallback();
            }

        };

        document.getElementsByTagName("head")[0].appendChild(javascriptElement);

    };

    console.debug("Demagog.Bookmarklet > Injecting jQuery 1.7.2 ...");
    var jQueryScriptUrl = "https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js";
    var jQueryScriptOnLoad = function() {
        console.info("Demagog Bookmarklet > jQuery ", jQuery.fn.jquery, "successfully loaded");
        Demagog.Bookmarklet.Events.onJqueryReady()
    };

    // TODO michalb_cz : reusing jQuery if already present on the page (detecting minimal jQuery version which support api we use)
    Demagog.Bookmarklet.Util.injectJavascript(jQueryScriptUrl, jQueryScriptOnLoad);

    Demagog.Bookmarklet

} /* if Demagog bookmarklet is not already loaded END*/
else {
    console.debug("Demagog.Bookmarklet is already loaded to this page.");
    Demagog.Bookmarklet.confirmSelectedTextAsQuote();
}