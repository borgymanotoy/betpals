<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
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
    <link rel="stylesheet" href='<c:url value="/css/site.css"/>' type="text/css" media="screen, projection"/>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <script type="text/javascript" src='<c:url value="/js/jquery-1.4.4.min.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/js/jquery.Storage.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/js/fadegallery.jquery.js"/>'></script>
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

       function facebookLogin() {
    	   window.location.href = '<c:url value="/facebooklogin.html"/>';
       } 

       function changeLanguage() {
           var language = jQuery("#langSelector option:selected").val();
           jQuery('input', '#languageForm').val(language);
           jQuery('#languageForm').submit();
       } 
       
       jQuery(document).ready(function() {
           jQuery("#usernameField").focus();
           jQuery("#gallery").gallery();
           var bgColor = jQuery.Storage.get("backgroundColor");
           if (bgColor) {
               jQuery("html").css("backgroundColor", bgColor);
               jQuery("body").css("backgroundColor", bgColor);
               jQuery("#langSelector").css("backgroundColor", bgColor);
           }
           
       });
      
    </script>
</head>
<body>
<div class="container">
    <div class="span-21 colorDiv">
    &nbsp;
        <a href="javascript:changeBgColor('#242b21', 'logo4');"><img src='<c:url value="/i/c1.jpg"/>'/></a>
        <a href="javascript:changeBgColor('#001d44', 'logo3');"><img src='<c:url value="/i/c2.jpg"/>'/></a>
        <a href="javascript:changeBgColor('#67b116', 'logo1');"><img src='<c:url value="/i/c3.jpg"/>'/></a>
        <a href="javascript:changeBgColor('#297085', 'logo2');"><img src='<c:url value="/i/c4.jpg"/>'/></a>
    </div>
    <div class="span-3 last left" style="padding-top: 42px;">
	    <select id="langSelector" style="background-color: #67b116; color: white; margin: 0; font-size: 12px;" onchange="changeLanguage();">
	        <option value="en" <c:if test="${pageContext.response.locale == 'en'}">selected="selected"</c:if>>English</option>
	        <option value="sv" <c:if test="${pageContext.response.locale == 'sv'}">selected="selected"</c:if>>Svenska</option>
	    </select>
    </div>
    <div class="span-24 headerPane logo1" id="headerPane">
        <form action="<c:url value='j_spring_security_check'/>" method="post"">
        <div class="span-12">&nbsp;</div>
        <div id="loginInput1" class="span-4">
            E-mail
            <input id="usernameField" type="text" name="j_username" tabindex="1" />
            <input type="checkbox" name="_spring_security_remember_me" tabindex="3"/> <span>Remember me</span>
        </div>
        <div id="loginInput2" class="span-4">
            Password
            <input type="password" name="j_password" tabindex="2"/>
            <a href="">Forgot your password?</a>
        </div>
        <div id="loginButtonPane" class="span-4 last">
            <input type="submit" id="loginButton" value="Login"/><br/>
            <button id="facebookLink" onclick="facebookLogin(); return false;">Facebook login</button>
        </div>
        </form>
    </div>
    <div id="contentArea" class="span-24">
        <div class="span-11">
            <div id="introArea">
                <div id="gallery">
                    <img class="galItem" src='<c:url value="/i/gallery/5.jpg"/>'/>
                    <img class="galItem" src='<c:url value="/i/gallery/6.jpg"/>'/>
                    <img class="galItem" src='<c:url value="/i/gallery/img-2.jpg"/>'/>
                    <img class="galItem" src='<c:url value="/i/gallery/1.jpg"/>'/>
                    <img class="galItem" src='<c:url value="/i/gallery/3.jpg"/>'/>
                </div>
                <div class="prepend-2 append-1">
	                <p style="padding-top: 10px; color: #1b6b87; font-size: 12px;"><span style="font-size: 18px;">MyBetpals</span> - here goes some text with description, slogan or something similar, but not very long, or we will need to scroll page.</p>
                </div>
            </div>
        </div>
        <div id="signupArea" class="span-13 last">
            <form action='<c:url value="/register.html"/>' method="post">
            <div class="span-4 right label">E-mail</div>
            <div class="span-9 last"><input type="text" name="email"/></div>
            <div class="span-4 right label">First name</div>
            <div class="span-9 last"><input type="text" name="name"/></div>
            <div class="span-4 right label">Last name</div>
            <div class="span-9 last"><input type="text" name="surname"/></div>
            <div class="span-4 right label">Password</div>
            <div class="span-9 last"><input type="password" name="password"/></div>
            <div class="span-4">&nbsp;</div>
            <div class="span-9 last"><input type="submit" id="registerButton" value="Register and login"/></div>
            <div class="span-4">&nbsp;</div>
            </form>
            <div class="span-9 last">&nbsp;</div>
        </div>
    </div>
    <div class="span-24" id="bottom">&nbsp;</div>
</div>
<form action="" method="post" id="languageForm">
    <input type="hidden" name="lang" value=""/>
</form>

</body>
</html>
