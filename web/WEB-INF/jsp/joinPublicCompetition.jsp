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
    <script type="text/javascript" src='<c:url value="/js/jquery.numeric.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/js/autoNumeric.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/js/mybetpals.js"/>'></script>
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
       function getInvitationDetails(competitionId, alternativeId) {
           var betValue = jQuery('#myStake_' + alternativeId).val();
           betValue = (betValue!=null && betValue.length > 0) ? betValue : jQuery('#fixedStake_' + alternativeId).val();
           if(betValue!=null && betValue.length > 0){
               var stake = (betValue.length > 0) ? Number(betValue) : 0;
               if(stake > 0){
                   jQuery('#competitionId').val(competitionId);
                   jQuery('#alternativeId').val(alternativeId);
                   jQuery('#betValue').val(betValue);
                   jQuery('#invitationDetailsForm').submit();
               }else{
                   showHideZeroStakeError(true);
               }
           }
       } 
        var getInvitationDetailsByKeypress = function(e, competitionId, alternativeId){
            clearZeroStakeError();
            if(e.keyCode == 13){
                getInvitationDetails(competitionId, alternativeId);
            }
        };

       function goHome() {
           window.location = '<c:url value="/login.html"/>';
       }
       jQuery(document).ready(function() {
            $(".positive_number_only").numeric({ negative: false }, function() { alert("No negative values"); this.value = ""; this.focus(); });
       });
    </script>
</head>
<body>
<div class="container">
    <div class="span-24 colorDiv">
    &nbsp;
        <a href="javascript:changeBgColor('#242b21', 'logo1');"><img src='<c:url value="/i/c1.jpg"/>'/></a>
        <a href="javascript:changeBgColor('#001d44', 'logo1');"><img src='<c:url value="/i/c2.jpg"/>'/></a>
        <a href="javascript:changeBgColor('#67b116', 'logo1');"><img src='<c:url value="/i/c3.jpg"/>'/></a>
        <a href="javascript:changeBgColor('#297085', 'logo1');"><img src='<c:url value="/i/c4.jpg"/>'/></a>
    </div>
    <div class="span-24 headerPane logo1" id="headerPane">
		<div id="divHeaderLogo" onclick="goHome();">
			<img src="../i/transparent.gif" alt="Mybetpals Logo"/>
		</div>
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
                       <th><spring:message code="user.resolved.bets"/></th>
                       <td style="width: 160px;">5</td>
                   </tr>
                </table>
            </div>
        </div>
        <div class="span-12 append-2 last" style="padding-top: 40px; padding-bottom: 10px;">
            <div class="formSectionDiv" style="width: 528px;">
                <img src='<c:url value="/i/ex.png"/>' style="vertical-align: bottom;"/>&nbsp;
                <span style="color: #297085;">${competition.owner.fullName}</span><span class="bold"> <spring:message code="user.bet.that"/> </span><span style="color: #297085; font-weight: bold;">${competition.name}.</span><br/>
            </div>
            <div style="background-color: #f6f6f6; width: 448px; padding: 20px 40px 10px 40px; margin-top: 5px; margin-bottom: 10px;">
                <!-- START: Display Alternatives-->
                <div class="span-13">
                    <div class="span-2 userPicDiv">
                        <img class="userPic" src='<c:url value="/competition/images/${competition.id}.jpeg"/>'/>
                    </div>
                    <div class="span-10 last invitationDiv formSectionDiv">
                        <h5 class="txt11">${competition.name}</h5>
                        <p class="txt11">${competition.description}</p>
                    </div>
                </div>
                <div class="txt11">
                    <spring:message code="competition.competition.deadline"/>: <span class="bold"><fmt:formatDate value="${competition.deadline}" pattern="yyyy-MM-dd HH:mm"/></span>
                </div>
                <div class="txt11">
                    <spring:message code="competition.settling.deadline"/>: <span class="bold"><fmt:formatDate value="${competition.settlingDeadline}" pattern="yyyy-MM-dd HH:mm"/></span>
                </div>
                <p>&nbsp;</p>

                <h5 style="font-weight: bold; color: #626262; width: 468px; border-bottom: 1px solid #d0d0d0;"><spring:message code="competition.alternatives.header"/></h5>
                <span class="error hide" id="spn_error_stake0"><spring:message code='error.zero.stake'/></span>
                <div class="formSectionDiv" style="width: 468px;">
                    <ul id="friendList">
                    <c:if test="${not empty param.invalidStake}">
                        <p class="error"><spring:message code="error.invalid.stake"/></p>
                    </c:if>
                    <c:if test="${not empty param.noAccount}">
                        <p class="error"><spring:message code="error.no.account"/>&nbsp;<a href='<c:url value="/addaccount.html"/>'><spring:message code="button.add.account"/></a></p>
                    </c:if>

                    <c:forEach items="${sortedAlternativeList}" var="altFromList" varStatus="status">
                        <li>
                            <div class="span-12 txt11">
                                <div class="span-2 userPicDiv">
                                    <img class="userPic" src='<c:url value="/competition/images/alt_${altFromList.id}.jpeg"/>'/>
                                </div>
                                <c:choose>
                                <c:when test="${status.last}">
                                <div class="span-10 last altListDetails noborder">
                                </c:when>
                                <c:otherwise>
                                <div class="span-10 last altListDetails">
                                </c:otherwise>
                                </c:choose>
                                <div class="span-5">
                                    ${altFromList.name}<br/>
                                    <c:if test="${empty altFromList.bets}"><span class="detailTitle"><spring:message code="competition.no.bets"/><br/></span></c:if>
                                    <c:forEach items="${altFromList.bets}" var="bet">
                                        <span class="detailTitle"><c:choose><c:when test="${bet.ownerId == competition.ownerId}"><spring:message code="competition.you"/></c:when><c:otherwise><a href='<c:url value="/viewprofile/${bet.ownerId}.html"/>'>${bet.ownerName}</a></c:otherwise></c:choose> (${bet.stake} ${competition.currency})</span><br/>
                                    </c:forEach>
                                    <span class="detailTitle">${altFromList.turnover} ${competition.currency} <spring:message code="competition.on.this.alternative"/></span>
                                </div>
                                <c:choose>
                                    <c:when test="${altFromList.taken}">
                                        <div class="span-5 last right">
                                            <spring:message code="alternative.already.taken.by"/> <br/><a href='<c:url value="/viewprofile/${altFromList.participantId}.html"/>'>${altFromList.participantName}</a>
                                        </div>
                                    </c:when>
                                    <c:when test="${competition.deadlineReached}">
                                        <div class="span-5 last right">
                                            <spring:message code="betting.is.closed"/>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="span-5 last right">
                                            <c:choose>
                                                <c:when test="${competition.competitionType == 'POOL_BETTING'}">
                                                    <input type="text" id="myStake_${altFromList.id}" size="3" maxlength="3" name="stake" class="positive_number_only" onkeypress="getInvitationDetailsByKeypress(event, ${competition.id}, ${altFromList.id});" onchange="clearZeroStakeError();" /> ${competition.currency}&nbsp;
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="hidden" id="fixedStake_${altFromList.id}" value="${competition.fixedStake}"/>
                                                    ${competition.fixedStake} ${competition.currency}&nbsp;
                                                </c:otherwise>
                                            </c:choose>
                                            <button id="placeBetButton_${altFromList.id}" class="greenButton90" onclick="getInvitationDetails(${competition.id}, ${altFromList.id});"><spring:message code="button.place.bet"/></button>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                                </div>
                            </div>
                        </li>
                    </c:forEach>
                    </ul>
                    &nbsp;
                </div>
                <!-- END: Display Alternatives-->

            </div>
        </div>
    </div>

    <div class="span-24" id="clearFooter">&nbsp;</div>

<!--
    <div class="span-24 versionInfo">
        <spring:eval expression="@buildProperties['build.number']" var="buildNumber"/>
        <spring:eval expression="@buildProperties['build.date']" var="buildDate"/>
        <spring:eval expression="@buildProperties['build.version']" var="buildVersion"/>
        Betpals version: <a class="white" href="Changes.txt">${buildVersion}</a>, build: ${buildNumber} at ${buildDate}
    </div>
-->

</div>
<form action='<c:url value="/ongoingcompetition.html"/>' method="post" id="invitationDetailsForm">
    <input type="hidden" id="competitionId" name="competitionId" value=""/>
    <input type="hidden" id="alternativeId" name="alternativeId" value=""/>
    <input type="hidden" id="betValue" name="betValue" value=""/>
</form>

</body>
</html>
