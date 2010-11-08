<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@include file="includes.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>betPals</title>
    <link rel="stylesheet" href='<c:url value="/css/ui-lightness/jquery-ui-1.8.6.custom.css"/>' type="text/css" media="screen, projection"/>
    <link rel="stylesheet" href='<c:url value="/css/blueprint/screen.css"/>' type="text/css" media="screen, projection"/>
    <link rel="stylesheet" href='<c:url value="/css/blueprint/print.css"/>' type="text/css" media="print"/>
    <!--[if IE]><link rel="stylesheet" href='<c:url value="/css/blueprint/ie.css"/>' type="text/css" media="screen, projection"/><![endif]-->
    <link rel="stylesheet" href='<c:url value="/css/site.css"/>' type="text/css" media="screen, projection"/>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <script type="text/javascript" src='<c:url value="/js/jquery-1.4.3.min.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/js/jquery-ui-1.8.6.custom.min.js"/>'></script>
    <script type="text/javascript">
       function changeBgColor(color) {
            jQuery("html").css("backgroundColor", color);
            jQuery("body").css("backgroundColor", color);
       }

       jQuery(document).ready(function() {
    	    jQuery("tr:even").addClass("even");
    	    jQuery("tr:odd").addClass("odd");

    	    jQuery("#tabs").tabs();
    	    
	   });
    		          
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
</html>
