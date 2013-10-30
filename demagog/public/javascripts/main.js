/*
 *  Initialize console object used for logging in javascript code.
 *  For case when console object is not present (old browsers) use dump implementation.
 */

window.console = window.console || {}; // in old browsers like IE7 there is console object

console.info = console.info || function() {};
console.error = console.error || function() {};
console.warn = console.warn || function() {};
console.debug = console.debug || console.info; // in later IE there is console but without debug method, so fallback to info

/* functions */
function filterByAuthorSelectOnChange() {
    var _gaq = _gaq || [];
    _gaq.push(['_trackEvent', 'Others', 'Quote List Filter Used']);

    $('#filterByAuthor').submit();
}

/* jQuery stuff */
jQuery(document).ready(function() {
    $("input[type='button']").button();
    $("input[type='submit']").button();
});

/* jQuery extensions */
jQuery.fn.extend({
    scrollToMe: function () {
        var x = jQuery(this).offset().top - 100;
        jQuery('html,body').animate({scrollTop: x}, 0);
    }});

/*
 ==========================================================
 * bootstrap-alert.js v2.3.1
 * http://twitter.github.com/bootstrap/javascript.html#alerts
 * ==========================================================
 * Copyright 2012 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ========================================================== */


!function ($) {

    "use strict"; // jshint ;_;


    /* ALERT CLASS DEFINITION
     * ====================== */

    var dismiss = '[data-dismiss="alert"]'
        , Alert = function (el) {
            $(el).on('click', dismiss, this.close)
        }

    Alert.prototype.close = function (e) {
        var $this = $(this)
            , selector = $this.attr('data-target')
            , $parent

        if (!selector) {
            selector = $this.attr('href')
            selector = selector && selector.replace(/.*(?=#[^\s]*$)/, '') //strip for ie7
        }

        $parent = $(selector)

        e && e.preventDefault()

        $parent.length || ($parent = $this.hasClass('alert') ? $this : $this.parent())

        $parent.trigger(e = $.Event('close'))

        if (e.isDefaultPrevented()) return

        $parent.removeClass('in')

        function removeElement() {
            $parent
                .trigger('closed')
                .remove()
        }

        $.support.transition && $parent.hasClass('fade') ?
            $parent.on($.support.transition.end, removeElement) :
            removeElement()
    }


    /* ALERT PLUGIN DEFINITION
     * ======================= */

    var old = $.fn.alert

    $.fn.alert = function (option) {
        return this.each(function () {
            var $this = $(this)
                , data = $this.data('alert')
            if (!data) $this.data('alert', (data = new Alert(this)))
            if (typeof option == 'string') data[option].call($this)
        })
    }

    $.fn.alert.Constructor = Alert


    /* ALERT NO CONFLICT
     * ================= */

    $.fn.alert.noConflict = function () {
        $.fn.alert = old
        return this
    }


    /* ALERT DATA-API
     * ============== */

    $(document).on('click.alert.data-api', dismiss, Alert.prototype.close)

}(window.jQuery);