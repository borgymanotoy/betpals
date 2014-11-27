/**
 * jQuery plugin: FadeGallery
 * http://www.exalted-web.com/fadegallery/
 *
 * Written by Shay from http://www.exalted-web.com
 * Release date: 28/02/2010
 * Version: 0.2
 *
 * Licensed under the GNU GPL license.
 * http://www.gnu.org/licenses/gpl.txt
 */
(function($) {
	$.fn.gallery = function(options) {
		
		var defaults = {
			FadeIn		: 1000,
			FadeOut		: 1000,
			Delay		: 8000,
			Repeat		: true
		};
		var options = $.extend(defaults, options);
		
		return this.each(function() {
			var elements = $(this).children().filter(".galItem");
			
			elements.hide();
			elements.eq(0).show();
			
			var next_item = 1;
			var hide_item = 0;
			var t = setTimeout(Fade, options.Delay);
			
			function Fade() {
				t = clearTimeout();
				
				if(next_item >= elements.length) {
					if(!options.Repeat) {
						return false;
					}
					next_item = 0;
					hide_item = elements.length - 1;
				}
				
				elements.eq(hide_item).fadeOut(options.FadeOut, function callback() {
					elements.eq(next_item).fadeIn(options.FadeIn, function callback() {
						hide_item = next_item;
						next_item++;
						if(options.Repeat || next_item < elements.length) {
							t = setTimeout(Fade, options.Delay);
						}
					});
				});
			}
			
		});
		
	};
})(jQuery);