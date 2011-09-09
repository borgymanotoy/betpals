<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@include file="includes.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title><spring:message code="page.title"/></title>
    <link rel="stylesheet" href='<c:url value="/css/blueprint/screen.css"/>' type="text/css" media="screen, projection"/>
    <link rel="stylesheet" href='<c:url value="/css/blueprint/print.css"/>' type="text/css" media="print"/>
    <!--[if IE]><link rel="stylesheet" href='<c:url value="/css/blueprint/ie.css"/>' type="text/css" media="screen, projection"/><![endif]-->
    <link rel="stylesheet" href='<c:url value="/css/custom-theme/jquery-ui-1.8.8.custom.css"/>' type="text/css" media="screen, projection"/>
    <link rel="stylesheet" href='<c:url value="/css/site.css"/>' type="text/css" media="screen, projection"/>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <script type="text/javascript" src='<c:url value="/js/jquery-1.4.4.min.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/js/jquery-ui-1.8.8.custom.min.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/js/jquery-ui-timepicker-addon.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/js/jquery.Storage.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/js/ajaxupload.js"/>'></script>
    <script type="text/javascript">
       function changeBgColor(color, logo) {
	        jQuery("html").css("backgroundColor", color);
	        jQuery("body").css("backgroundColor", color);
	        jQuery("#langSelector").css("backgroundColor", color);
	        jQuery("#headerPane").removeClass("logo1");
	        jQuery("#headerPane").removeClass("logo2");
	        jQuery("#headerPane").removeClass("logo3");
	        jQuery("#headerPane").removeClass("logo4");
	        jQuery("#headerPane").addClass(logo);
	        jQuery.Storage.set("backgroundColor", color);
	   }

       jQuery(document).ready(function() {
    	    jQuery("table.altRows tr:even").addClass("even");
    	    jQuery("table.altRows tr:odd").addClass("odd");

    	    jQuery("#tabs").tabs();
    	    var bgColor = jQuery.Storage.get("backgroundColor");
    	    if (bgColor) {
                jQuery("html").css("backgroundColor", bgColor);
                jQuery("body").css("backgroundColor", bgColor);
                jQuery("#langSelector").css("backgroundColor", bgColor);
    	    }
	   });

	   function goHome() {
		    window.location = '<c:url value="/home.html"/>';
	   }
	   
       function changeLanguage() {
    	   var language = jQuery("#langSelector option:selected").val();
           jQuery('input', '#languageForm').val(language);
           jQuery('#languageForm').submit();
       } 

       function viewPublicCompetition(competitionId) {
           jQuery('#publicCompetitionToView').val(competitionId);
           jQuery('#viewPublicCompetitionForm').submit();
       } 
       
    		          
    </script>
</head>
<body>
	<div class="container">
		<tiles:insertAttribute name="header"/>
		<div id="contentArea" class="span-24">
			<div class="span-5">
	            <tiles:insertAttribute name="leftPane"/>
			</div>
			<div id="contentPane" class="span-14">
	    		<tiles:insertAttribute name="content"/>
			</div>
			<div class="span-5 last">
	            <tiles:insertAttribute name="rightPane"/>
			</div>
		</div>
		<tiles:insertAttribute name="footer"/>
	</div>
</body>
<form action="" method="post" id="languageForm">
    <input type="hidden" name="sitelang" value=""/>
</form>
<form action='<c:url value="/ongoingcompetition.html"/>' method="post" id="viewPublicCompetitionForm">
    <input type="hidden" name="competitionId" value="" id="publicCompetitionToView"/>
</form>

</html>
