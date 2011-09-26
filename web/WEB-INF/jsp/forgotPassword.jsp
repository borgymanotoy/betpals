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

       function changePassword() {
           var newPassword = jQuery("#newPassword").val();
           var newPasswordRepeat = jQuery("#newPasswordRepeat").val();

           if (newPassword.length == 0 || newPasswordRepeat.length == 0) {
               var errorText = '<spring:message code="error.all.fields.are.mandatory"/>';
               jQuery("#changePasswordError").html(errorText);
           } else if (newPassword != newPasswordRepeat) {
               var errorText = '<spring:message code="error.passwords.mismatch"/>';
               jQuery("#changePasswordError").html(errorText);
           } else {
               jQuery("#changePasswordForm").submit();
           }
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
    <div class="span-24 headerPane logo1" id="headerPane">
        &nbsp;
    </div>
    <div id="contentArea" class="span-24">
        <div class="prepend-2 span-7" style="padding-top: 40px; padding-bottom: 80px;">
            <img id="errorIcon" src='<c:url value="/i/warning_yellow.png"/>'/>
        </div>
        <div class="prepend-1 span-12 append-2 last" style="padding-top: 40px; padding-bottom: 80px;">
            <h3><spring:message code="forgot.password.header"/></h3>
            <c:choose>
            <c:when test="${not empty success}">
                <p style="padding-top: 15px; font-size: 12px; color: #000000;">
                ${success}
                </p>
            </c:when>
            <c:when test="${not empty passwordRecoveryRequest}">
                <p style="padding-top: 15px; font-size: 12px; color: #000000;">
                <spring:message code="forgot.password.change.text"/>
                </p>
                <form id="changePasswordForm" action='<c:url value="/changeforgottenpassword.html"/>' method="post">
			    <p class="error" id="changePasswordError">
			    </p>
			    <div class="span-12 formSectionSlimDiv">
			        <div class="span-2 labelDiv" style="padding-top: 8px;"><spring:message code="profile.password.new"/></div>
			        <div class="span-10 last">
			            <input type="password" name="newPassword" id="newPassword"/>
			        </div>
			    </div>
			    <div class="span-12 formSectionSlimDiv">
			        <div class="span-2 labelDiv" style="padding-top: 8px;"><spring:message code="profile.password.new.repeat"/></div>
			        <div class="span-10 last">
			            <input type="password" name="newPasswordRepeat" id="newPasswordRepeat"/>
			        </div>
			    </div>
			    <div class="span-12 formSectionSlimDiv">
			        <div class="span-2 labelDiv">&nbsp;</div>
			        <div class="span-10 last">
                       <input type="hidden" name="requestHash" value="${passwordRecoveryRequest.requestHash}"/>
			           <button class="blueButton110" onclick="changePassword(); return false;"><spring:message code='button.change'/></button>
			        </div>
			    </div>
			    </form>
            </c:when>
            <c:otherwise>
	            <p style="padding-top: 15px; font-size: 12px; color: #000000;">
	            <spring:message code="forgot.password.text"/>
	            </p>
	            <c:if test="${not empty error}"><p class="error">${error}</p></c:if>
	            <form action='<c:url value="/forgotpassword.html"/>' method="post">
	                <div class="span-12 formSectionSlimDiv">
	                    <div class="span-2 labelDiv" style="padding-top: 8px;"><spring:message code="forgot.password.email"/></div>
	                    <div class="span-10 last">
	                        <input type="text" name="email" value="${email}"/>
	                    </div>
	                </div>
	                <div class="span-12 formSectionSlimDiv">
	                    <div class="span-2 labelDiv" style="padding-top: 8px;">&nbsp;</div>
	                    <div class="span-10 last">
	                        <input class="blueButton110" type="submit" value='<spring:message code="button.send"/>'/>
	                    </div>
	                </div>
	            </form>
	            <br/>
            </c:otherwise>
            </c:choose>
        </div>
    </div>
    <div class="span-24" id="clearFooter">&nbsp;</div>
</div>
</body>
</html>
