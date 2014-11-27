/* mybetpals.js
 *
 * MyBetPals Javascript library
 *
 * contains javascript functions that are common in most pages.
 * Functions and variables declared in this JS file will be available
 * in all pages that uses this library.
 */

//Function for showing/hiding span object
var showHideSpan = function(spanName, isShow){
	if(isShow){
		$("#"+spanName).css({'display':'block'});
	}else{
		if($("#"+spanName).is(":visible")) $("#"+spanName).css({'display':'none'});
	}
};

//Function for showing/hiding zero stake error display
function showHideZeroStakeError(isVisible){
	showHideSpan("spn_error_stake0" ,isVisible);
}

//Function for hiding zero stake error display
function clearZeroStakeError(){
	showHideZeroStakeError(false);
}
