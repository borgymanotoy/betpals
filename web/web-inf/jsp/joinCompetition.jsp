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

       function getInvitationDetails(competitionId) {
           jQuery('input', '#invitationDetailsForm').val(competitionId);
           jQuery('#invitationDetailsForm').submit();
       } 
       
       function goHome() {
           window.location = '<c:url value="/login.html"/>';
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
        <div class="span-9" style="padding-top: 40px; padding-bottom: 80px;">
	        <div class="prepend-2 span-2 labelDiv"><img class="userPic" src='<c:url value="/users/images/${competition.ownerId}.jpeg"/>'/></div>
	        <div style="padding-top: 5px; margin-bottom: 2px;" class="span-5 last">
	            <h5 style="color: #297085; margin-bottom: 2px;">${competition.owner.fullName}</h5>
	           <p style="color: #88898a; font-size: 11px; font-family: 'MS Trebuchet', sans-serif; margin: 0px;">
	            <spring:message code="${competition.owner.country}"/>
	           <br/>
	           ${competition.owner.bio}</p>
	        </div>
	        <div class="prepend-2 span-7 last">
		        <table class="palsTable reputationTable">
                   <tr><td colspan="2">&nbsp;</td></tr>
		           <tr>
		               <th><spring:message code="user.satisfaction"/></th>
		               <td style="width: 160px;">100%</td>
		           </tr>
		           <tr>
		               <th><spring:message code="user.resolved.bets"/></th>
		               <td style="width: 160px;">5</td>
		           </tr>
		        </table>
	        </div>
        </div>
        <div class="prepend-1 span-12 append-2 last" style="padding-top: 40px; padding-bottom: 80px;">
		    <div class="formSectionDiv">
		        <img src='<c:url value="/i/ex.png"/>' style="vertical-align: bottom;"/>&nbsp;
		        <span style="color: #297085;">${competition.owner.fullName}</span><span class="bold"> <spring:message code="user.bet.that"/> </span><span style="color: #297085; font-weight: bold;">${competition.name}.</span><br/>
		    </div>
		    <div style="background-color: #f6f6f6; padding: 20px 40px 20px 40px; margin-top: 15px; margin-bottom: 20px;">
		      <span style="color: #1b6d87; font-weight: bold; font-size: 12px; padding-right: 40px;"><spring:message code="user.do.you.bet.against"/></span>
		      <button class="greenButton90 noborder" onclick="getInvitationDetails(${competition.id});"><spring:message code="yes"/></button>&nbsp;
		      <button class="orangeButton90 noborder" onclick="goHome();"><spring:message code="no"/></button>
		    </div>
            <div style="padding-top: 10px;">
                <span class="detailTitle"><spring:message code="competition.deadline"/>: </span><fmt:formatDate value="${competition.deadline}" pattern="yyyy-MM-dd HH:mm"/><br/>
                <span class="detailTitle"><spring:message code="competition.resolved"/>: </span><fmt:formatDate value="${competition.settlingDeadline}" pattern="yyyy-MM-dd HH:mm"/><br/>
            </div>
            <p style="padding-top: 15px; font-size: 11px; color: #000000;">
            <span style="color: #626262; font-weight: bold;"><spring:message code="competition.rules.from.creator"/> (${competition.owner.fullName}):</span><br/>
            ${competition.description}
            </p>
        </div>
    </div>
    <div class="span-24" id="clearFooter">&nbsp;</div>
</div>
<form action='<c:url value="/linkinvitation.html"/>' method="post" id="invitationDetailsForm">
    <input type="hidden" name="competitionId" value=""/>
</form>

</body>
</html>
