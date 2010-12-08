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
    <title>betPals</title>
    <link rel="stylesheet" href='<c:url value="/css/blueprint/screen.css"/>' type="text/css" media="screen, projection"/>
    <link rel="stylesheet" href='<c:url value="/css/blueprint/print.css"/>' type="text/css" media="print"/>
    <!--[if IE]><link rel="stylesheet" href='<c:url value="/css/blueprint/ie.css"/>' type="text/css" media="screen, projection"/><![endif]-->
    <link rel="stylesheet" href='<c:url value="/css/site.css"/>' type="text/css" media="screen, projection"/>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <script type="text/javascript" src='<c:url value="/js/jquery-1.4.3.min.js"/>'></script>
    <script type="text/javascript">
       function changeBgColor(color) {
    	    jQuery("html").css("backgroundColor", color);
    	    jQuery("body").css("backgroundColor", color);
       }

       jQuery(document).ready(function() {
           jQuery("#usernameField").focus();
      });
      
    </script>
</head>
<body>
<div class="container">
    <div class="span-24 colorDiv">
    &nbsp;
        <a href="javascript:changeBgColor('#242b21');"><img src='<c:url value="/i/c1.jpg"/>'/></a>
        <a href="javascript:changeBgColor('#001d44');"><img src='<c:url value="/i/c2.jpg"/>'/></a>
        <a href="javascript:changeBgColor('#67b116');"><img src='<c:url value="/i/c3.jpg"/>'/></a>
        <a href="javascript:changeBgColor('#297085');"><img src='<c:url value="/i/c4.jpg"/>'/></a>
    </div>
    <div class="span-24 headerPane">
        <form action="<c:url value='j_spring_security_check'/>" method="post" name="login_form">
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
            <button class="buttonG" onclick="document.login_form.submit();">Login</button><br/>
            <button class="buttonB" onclick="window.location = '<c:url value="/facebooklogin.html"/>'">Facebook login</button>
        </div>
        </form>
    </div>
    <div id="contentArea" class="span-24">
        <div class="span-11">
            <div id="introArea" class="prepend-2 append-1">
                <h3>BetPals is a real-time network.</h3>
                <p>If you can understand the main course of a matter you omit minor details. However these minor details are of great importance. You must consider the good and the bad in even trifle matters.</p>
                <p>Listening to golden sayings or deeds of men of old is to learn their wisdom. This is an unselfish attitude. If you talk with others discuss these excellent well known accomplishments, dismiss your narrow minded ideas and your course of action will not be wrong.</p>
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
            <div class="span-9 last"><img src='<c:url value="/i/facebook.jpg"/>'/>&nbsp;<a href='<c:url value="/facebooklogin.html"/>'>Facebook login</a></div>
        </div>
    </div>
    <div class="span-24" id="bottom">&nbsp;</div>
</div>
</body>
</html>
