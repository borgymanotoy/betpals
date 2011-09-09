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
    <title><spring:message code="page.title"/></title>
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
       
       function forgotPassword() {
           var email = jQuery("#usernameField").val();
           jQuery('input', '#forgotPasswordForm').val(email);
           jQuery('#forgotPasswordForm').submit();
           return false;
       } 
       
       function joinCompetition(competitionId) {
           jQuery('#competitionToJoin').val(competitionId);
           jQuery('#joinCompetitionForm').submit();
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
            <spring:message code="login.email"/>
            <input id="usernameField" type="text" name="j_username" tabindex="1" value='<c:if test="${not empty param.login_error}">${SPRING_SECURITY_LAST_USERNAME}</c:if>'/>
            <input type="checkbox" name="_spring_security_remember_me" tabindex="3"/> <span><spring:message code="login.remember.me"/></span>
        </div>
        <div id="loginInput2" class="span-4">
            <spring:message code="login.password"/>
            <input type="password" name="j_password" tabindex="2"/>
            <a href="" onclick="forgotPassword(); return false;"><spring:message code="login.forgot.password"/></a>
        </div>
        <div id="loginButtonPane" class="span-3 append-1 last right">
            <input type="submit" id="loginButton" value="Login"/><br/>
            <button id="facebookLink" onclick="facebookLogin(); return false;"><spring:message code="login.facebook.login"/></button>
        </div>
        </form>
        <c:if test="${not empty param.login_error}">
        <div class="span-23 append-1 right error">
            <spring:message code="login.not.successfull"/>
            <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>
        </div>
	    </c:if>
    </div>
    <div id="loginContentArea" class="span-24">
        <div class="span-11 whiteBg">
            <div id="introArea">
                <div id="gallery">
                    <img class="galItem" src='<c:url value="/i/gallery/5.jpg"/>'/>
                    <img class="galItem" src='<c:url value="/i/gallery/6.jpg"/>'/>
                    <img class="galItem" src='<c:url value="/i/gallery/img-2.jpg"/>'/>
                    <img class="galItem" src='<c:url value="/i/gallery/1.jpg"/>'/>
                    <img class="galItem" src='<c:url value="/i/gallery/3.jpg"/>'/>
                </div>
            </div>
            <div id="lcomptitle" class="prepend-1 span-10 last">New competitions</div>
            <div class="span-11 last pbDiv">
	            <ul id="pbList">
	            <c:forEach items="${topPublicCompetitions}" var="competition" varStatus="status">
	                <li>
	                    <div class="prepend-1 span-10 pb_${status.count}">
	                        <div class="span-2 pbPicDiv">
	                            <img class="userPic" src='<c:url value="/competition/images/${competition.id}.jpeg"/>'/>
	                        </div>
	                        <div class="span-8 last">
	                            <h5 class="clickable pbTitle" onclick="joinCompetition(${competition.id});">${competition.name}</h5>
	                            <span class="detailTitle"><spring:message code="competition.creator"/>: </span>${competition.owner.fullName}<br/>
	                            <span class="detailTitle"><spring:message code="competition.deadline"/>: </span><fmt:formatDate value="${competition.deadline}" pattern="yyyy-MM-dd HH:mm"/>
	                        </div>
	                    </div>
	                </li>
	            </c:forEach>
	            </ul>
            </div>
        </div>
        <div id="signupArea" class="span-13 last">
            <c:url value="/register.html" var="registerURL"/>
            <form:form commandName="userProfile" action="${registerURL}" method="post">
            <div class="span-4 right label"><spring:message code="registration.email"/></div>
            <div class="span-9 last"><form:input path="email"/></div>
            <div class="span-4 right label"><spring:message code="registration.first.name"/></div>
            <div class="span-9 last"><form:input path="name"/></div>
            <div class="span-4 right label"><spring:message code="registration.last.name"/></div>
            <div class="span-9 last"><form:input path="surname"/></div>
            <div class="span-4 right label"><spring:message code="registration.password"/></div>
            <div class="span-9 last"><form:password path="password"/></div>
            <div class="span-4">&nbsp;</div>
            <div class="span-9 last">
                <p class="error">
                    <form:errors path="*"/>
                    <c:if test="${alreadyExist}"><spring:message code='error.user.exist'/></c:if>
                </p>
                <input type="submit" id="registerButton" value="<spring:message code='registration.login'/>"/>
            </div>
            <div class="span-4">&nbsp;</div>
            </form:form>
            <div class="span-9 last">&nbsp;</div>
            <div class="prepend-2 span-9 append-2 last" style="padding-top: 30px;">
                <p class="loginText"><span style="font-size: 18px;">myBetpals</span> - here goes some text with description, slogan or something similar, but not very long, or we will need to scroll page. <br/><br/><br/><br/><br/><br/></p>
            </div>
            <div class="prepend-2 span-5" style="padding-bottom: 20px;">
                <a href="">Contact Us</a><br/>
                <a href="">Security</a><br/>
                <a href="">Terms and conditions</a>
            </div>
            <div class="span-4 append-2 last" style="padding-bottom: 20px;">
                <a href="">Customer service</a><br/>
                <a href="">Privacy policy</a><br/>
                <a href="">Help</a>
            </div>
        </div>
    </div>
    <div class="span-24" id="bottom">&nbsp;</div>
</div>
<form action="" method="post" id="languageForm">
    <input type="hidden" name="sitelang" value=""/>
</form>
<form action='<c:url value="/forgotpasswordview.html"/>' method="post" id="forgotPasswordForm">
    <input type="hidden" name="email" value=""/>
</form>
<form action='<c:url value="/joincompetition.html"/>' method="post" id="joinCompetitionForm">
    <input type="hidden" name="competitionId" value="" id="competitionToJoin"/>
</form>

</body>
</html>
