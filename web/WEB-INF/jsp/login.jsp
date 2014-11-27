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
    <script type="text/javascript" src='<c:url value="/js/date.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/js/mybetpals.js"/>'></script>
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

		var validateRegister = function() {
			var email = $.trim($("#email").val());
			var name = $.trim($("#name").val());
			var password = $.trim($("#password").val());
			var is_checked = $("#over181").is(":checked");
			var emailEmpty = (email.length == 0) ? 1 : 0;
			var nameEmpty = (name.length == 0) ? 1 : 0;
			var passwordEmpty = (password.length == 0) ? 1 : 0;
			var strObj = "";
			var errorCount = emailEmpty + nameEmpty + passwordEmpty;
			if( errorCount == 0 && is_checked){
				$("#userProfile").submit();
			}else{
				if(emailEmpty == 1) showHideSpan("spn_error_emailRequired" ,true);
				if(nameEmpty == 1) showHideSpan("spn_error_firstNameRequired" ,true);
				if(passwordEmpty == 1) showHideSpan("spn_error_passwordRequired" ,true);
				if(!is_checked) showHideSpan("spn_error_under18" ,true);
			}
			return false;
		};
		var showHideSpan = function(spanName, isShow){
			if(isShow){
				$("#"+spanName).css({'display':'block'});
			}else{
				if($("#"+spanName).is(":visible")) $("#"+spanName).css({'display':'none'});
			}
		};
		var clearErrorMessages = function(){
			showHideSpan("spn_error_emailRequired" ,false);
			showHideSpan("spn_error_firstNameRequired" ,false);
			showHideSpan("spn_error_passwordRequired" ,false);
			showHideSpan("spn_error_under18" ,false);
			showHideSpan("spn_error_alreadyExist" ,false);
		};
		var showHideRegistrationErrors = function(){
			var blankEmail = '<c:out value="${emailRequired}"/>';
			var blankFirstName = '<c:out value="${firstNameRequired}"/>';
			var blankPassword = '<c:out value="${passwordRequired}"/>';
			var under18 = '<c:out value="${under18}"/>';
			var userExist = '<c:out value="${alreadyExist}"/>';

			blankEmail = blankEmail=='true'? true : false;
			blankFirstName = blankFirstName=='true' ? true : false;
			blankPassword = blankPassword=='true' ? true : false;
			under18 = under18=='true' ? true : false;
			userExist = userExist=='true' ? true : false;

			showHideSpan("spn_error_emailRequired" ,blankEmail);
			showHideSpan("spn_error_firstNameRequired" ,blankFirstName);
			showHideSpan("spn_error_passwordRequired" ,blankPassword);
			showHideSpan("spn_error_under18" ,under18);
			showHideSpan("spn_error_alreadyExist" ,userExist);
		};

		/* START: On submit handler for non IE Browsers */
		var formEventHandler = function (eventObject) {
			if (eventObject.preventDefault) {
				eventObject.preventDefault();
			} else if (window.event) /* for ie */ {
				window.event.returnValue = false;
			}
			return true;
		};
		var preventFormSubmitDefault = function(form_name){
			if(form_name && form_name.length > 0){
				if(element = window.document.getElementById(form_name)){
					if (element.addEventListener) {
						element.addEventListener("submit", formEventHandler, false);
					} else if (element.attachEvent) {
						element.attachEvent("onsubmit", formEventHandler);
					}
				}
			}
		};
	   /* END: On submit handler for non IE Browsers */

       jQuery(document).ready(function() {
           jQuery("#usernameField").focus();
           jQuery("#gallery").gallery();
           var bgColor = jQuery.Storage.get("backgroundColor");
           if (bgColor) {
               jQuery("html").css("backgroundColor", bgColor);
               jQuery("body").css("backgroundColor", bgColor);
               jQuery("#langSelector").css("backgroundColor", bgColor);
           }
           showHideRegistrationErrors();
           preventFormSubmitDefault("userProfile");
       });

       function goHome() {
           window.location = '<c:url value="/login.html"/>';
       }

       function submitLogin() {
           var strDT = jQuery('#clientDateTime').val();
           if(strDT!=null && strDT.length > 0) jQuery('#loginForm').submit();
       }
       
       jQuery(document).ready(function() {
           updateTopPublicCompetition(getClientUTCOffsetHours());
       });
    </script>
</head>
<body>
<div class="container">
    <div class="span-21 colorDiv">
    &nbsp;
        <a href="javascript:changeBgColor('#242b21', 'loginHeaderPane');"><img src='<c:url value="/i/c1.jpg"/>'/></a>
        <a href="javascript:changeBgColor('#001d44', 'loginHeaderPane');"><img src='<c:url value="/i/c2.jpg"/>'/></a>
        <a href="javascript:changeBgColor('#67b116', 'loginHeaderPane');"><img src='<c:url value="/i/c3.jpg"/>'/></a>
        <a href="javascript:changeBgColor('#297085', 'loginHeaderPane');"><img src='<c:url value="/i/c4.jpg"/>'/></a>
    </div>
    <div class="span-3 last left" style="padding-top: 42px;">
	    <select id="langSelector" style="background-color: #67b116; color: white; margin: 0; font-size: 12px;" onchange="changeLanguage();">
	        <option value="en" <c:if test="${pageContext.response.locale == 'en'}">selected="selected"</c:if>>English</option>
	        <option value="sv" <c:if test="${pageContext.response.locale == 'sv'}">selected="selected"</c:if>>Svenska</option>
	    </select>
    </div>
    <div class="span-24 headerPane loginHeaderPane" id="headerPane">
		<div id="divHeaderLogo" onclick="goHome();">
			<img src="../i/transparent.gif" alt="Mybetpals Logo"/>
		</div>
        <form id="loginForm" action="<c:url value='j_spring_security_check'/>" method="post"">
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
            <input type="submit" id="loginButton" value="Login" onclick="submitLogin();"/><br/>
            <button id="facebookLink" onclick="facebookLogin(); return false;"><spring:message code="login.facebook.login"/></button>
        </div>
        </form>
        <c:if test="${not empty param.login_error}">
        <div class="span-23 append-1 right error">
            <spring:message code="login.not.successfull"/>
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
            <div class="span-9 last"><form:input path="email" onkeypress="clearErrorMessages();" /></div>
            <div class="span-4 right label"><spring:message code="registration.first.name"/></div>
            <div class="span-9 last"><form:input path="name" onkeypress="clearErrorMessages();" /></div>
            <div class="span-4 right label"><spring:message code="registration.last.name"/></div>
            <div class="span-9 last"><form:input path="surname" onkeypress="clearErrorMessages();" /></div>
            <div class="span-4 right label"><spring:message code="registration.password"/></div>
            <div class="span-9 last"><form:password path="password" onkeypress="clearErrorMessages();" /></div>
            <div class="span-4 right label">&nbsp;</div>
            <div class="span-9 last"><form:checkbox path="over18" onclick="clearErrorMessages();" style="margin-left: 0;"/>&nbsp;<spring:message code="registration.over18"/></div>
            <div class="prepend-4 span-9 last">
                <p class="error">
					<span class="error hide" id="spn_error_emailRequired"><spring:message code='NotBlank.userProfile.email'/></span>
					<span class="error hide" id="spn_error_firstNameRequired"><spring:message code='NotBlank.userProfile.name'/></span>
					<span class="error hide" id="spn_error_passwordRequired"><spring:message code='password.empty'/></span>
					<span class="error hide" id="spn_error_under18"><spring:message code='error.over.18'/></span>
					<span class="error hide" id="spn_error_alreadyExist"><spring:message code='error.user.exist'/></span>
                </p>
			   <button id="registerButton" onclick="validateRegister();"><spring:message code="registration.login"/></button>
            </div>
            </form:form>
            <div class="prepend-2 span-9 append-2 last" style="padding-top: 30px;">
                <p align="justify" class="loginText"><span style="font-size: 18px;">myBetpals</span> - bets your way.<br/>Meet your friends, make new friends. Bet them, beat them.<br/>Challenge your pals and together you can giggle at the thrill while you bet what you want and when you want.<br/>myBetpals is a place for socialising, betting and networking. The fans at myBetpals can create their own bets within any domain and set their own odds to their bets. Bets, that the fans in the network can bet against.<br/>Come on in and join the fun!</p>
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
<br>
</body>
</html>
