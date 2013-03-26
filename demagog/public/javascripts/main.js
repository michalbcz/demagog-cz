/* jQuery extensions */
jQuery.fn.extend({
    scrollToMe: function () {
        var x = jQuery(this).offset().top - 100;
        jQuery('html,body').animate({scrollTop: x}, 0);
}});

