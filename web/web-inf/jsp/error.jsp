<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page isErrorPage="true" %>
<%@ page language="java" contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix='security' uri='http://www.springframework.org/security/tags' %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>myBetpals</title>
    <link rel="stylesheet" href='<c:url value="/css/blueprint/screen.css"/>' type="text/css" media="screen, projection"/>
    <link rel="stylesheet" href='<c:url value="/css/blueprint/print.css"/>' type="text/css" media="print"/>
    <!--[if IE]><link rel="stylesheet" href='<c:url value="/css/blueprint/ie.css"/>' type="text/css" media="screen, projection"/><![endif]-->
    <link rel="stylesheet" href='<c:url value="/css/custom-theme/jquery-ui-1.8.8.custom.css"/>' type="text/css" media="screen, projection"/>
    <link rel="stylesheet" href='<c:url value="/css/site.css"/>' type="text/css" media="screen, projection"/>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <script type="text/javascript" src='<c:url value="/js/jquery-1.4.4.min.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/js/jquery.Storage.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/js/jquery-ui-1.8.8.custom.min.js"/>'></script>
    <script type="text/javascript">
       function changeBgColor(color, logo) {
            jQuery("html").css("backgroundColor", color);
            jQuery("body").css("backgroundColor", color);
            jQuery("#headerPane").removeClass("logo1");
            jQuery("#headerPane").removeClass("logo2");
            jQuery("#headerPane").removeClass("logo3");
            jQuery("#headerPane").removeClass("logo4");
            jQuery("#headerPane").addClass(logo);
       }

       var errorCodes = {
    		   "400" : "Bad Request",
    		   "401" : "Unauthorized",
    		   "402" : "Payment Required",
    		   "403" : "Forbidden",
    		   "404" : "Not Found",
    		   "405" : "Method Not Allowed",
    		   "406" : "Not Acceptable",
    		   "407" : "Proxy Authentication Required",
    		   "408" : "Request Timeout",
    		   "409" : "Conflict",
    		   "410" : "Gone",
    		   "411" : "Length Required",
    		   "412" : "Precondition Failed",
    		   "413" : "Request Entity Too Large",
    		   "414" : "Request-URI Too Long",
    		   "415" : "Unsupported Media Type",
    		   "416" : "Requested Range Not Satisfiable",
    		   "417" : "Expectation Failed",
    		   "500" : "Internal Server Error",
    		   "501" : "Not Implemented",
    		   "502" : "Bad Gateway",
    		   "503" : "Service Unavailable",
    		   "504" : "Gateway Timeout",
    		   "505" : "HTTP Version Not Supported"
       }
       
       jQuery(document).ready(function() {

    	   var errorCode = "${param.code}";
    	   if (errorCode != "") {
    		   jQuery("#errorTitle").html("Error " + errorCode + " " + errorCodes[errorCode]);
	    	   if (errorCode.charAt(0) == "4") {
	    		   jQuery("#errorIcon").attr("src", '<c:url value="/i/warning_yellow.png"/>');
	    	   } else {
	    		   jQuery("#errorMessage").html("We are sorry, but there was an error processing your request.");
	    	   }
    	   } else {
               jQuery("#errorMessage").html("We are sorry, but there was an error processing your request.");
    	   }
    	   
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

    </script>
</head>
<body>
<div class="container">
    <div class="span-24 colorDiv">
    &nbsp;
        <a href="javascript:changeBgColor('#242b21', 'logo4');"><img src='<c:url value="/i/c1.jpg"/>'/></a>
        <a href="javascript:changeBgColor('#001d44', 'logo3');"><img src='<c:url value="/i/c2.jpg"/>'/></a>
        <a href="javascript:changeBgColor('#67b116', 'logo1');"><img src='<c:url value="/i/c3.jpg"/>'/></a>
        <a href="javascript:changeBgColor('#297085', 'logo2');"><img src='<c:url value="/i/c4.jpg"/>'/></a>
    </div>
    <div class="span-24 headerPane logo1" id="headerPane" onclick="goHome();">
        &nbsp;
    </div>
    <div id="contentArea" class="span-24">
        <div class="prepend-2 span-7" style="padding-top: 40px; padding-bottom: 80px;">
            <img id="errorIcon" src='<c:url value="/i/warning_red.png"/>'/>
        </div>
        <div class="prepend-1 span-12 append-2 last" style="padding-top: 40px; padding-bottom: 80px;">
            <h3 id="errorTitle">Error ${param.code}</h3>
            <p id="errorMessage" style="padding-top: 15px; font-size: 12px; color: #000000;">
            We are sorry, but either your request is incorrect or the page you are requesting is not available.
            </p>
            <br/>
            <button class="blueButton110" onclick="history:back();">Back</button>
        </div>
    </div>
    <div class="span-24" id="clearFooter">&nbsp;</div>
</div>
</body>
</html>
