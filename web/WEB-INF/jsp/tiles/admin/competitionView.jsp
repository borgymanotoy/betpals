<%@include file="includes.jsp"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<script type="text/javascript">
    jQuery(document).ready(function() {
        jQuery(".logEntry").click(function() {
            jQuery(".logInfo", this).toggle();
            jQuery(this).toggleClass("bold");
        });
        
        jQuery(".logEntry").hover(
              function () {
                jQuery(this).addClass("logHover");
              }, 
              function () {
                jQuery(this).removeClass("logHover");
              }
            );

    });

    function manageCompetition(competitionId) {
        jQuery('#competitionToManage', '#manageCompetitionForm').val("/managecompetition.html?competitionId=" + competitionId);
        jQuery('#manageCompetitionForm').submit();
    } 
    
</script>
<div>
    <h2 class="dark_long"><spring:message code="competition.view.title"/></h2>
</div>
<div class="rbDiv contentDiv">
    <div class="span-16">
        <div class="span-2 userPicDiv">
            <img class="userPic" src='<c:url value="/competition/images/${competition.id}.jpeg"/>'/>
        </div>
        <div class="span-14 last formSectionDiv">
            <h5>${competition.name}</h5>
            <p>${competition.description}</p>
        </div>
    </div>
    <div>
        <spring:message code="competition.status"/>: ${competition.status} <br/>
        <spring:message code="competition.created"/>: <fmt:formatDate value="${competition.created}" pattern="yyyy-MM-dd HH:mm"/> <br/>
        <spring:message code="competition.owner"/>: ${competition.owner.fullName} <br/>
        <spring:message code="competition.competition.deadline"/>: <fmt:formatDate value="${competition.deadline}" pattern="yyyy-MM-dd HH:mm"/> <br/>
        <spring:message code="competition.settling.deadline"/>: <fmt:formatDate value="${competition.settlingDeadline}" pattern="yyyy-MM-dd HH:mm"/>
    </div>
    <div>
        <button class="blueButton110" onclick="manageCompetition(${competition.id});"><spring:message code="button.manage.competition"/></button>
    </div>
    <p>&nbsp;</p>
    <h4><spring:message code="competition.alternatives.header"/></h4>
    <div class="formSectionDiv">
        <ul id="friendList">
        <c:forEach items="${competition.defaultEvent.alternatives}" var="altFromList" varStatus="status">
        <li>
            <div class="span-16">
                <div class="span-2 userPicDiv">
                    <img class="userPic" src='<c:url value="/competition/images/alt_${altFromList.id}.jpeg"/>'/>
                </div>
                <c:choose>
                <c:when test="${status.last}">
                <div class="span-14 last altListDetails noborder">
                </c:when>
                <c:otherwise>
                <div class="span-14 last altListDetails">
                </c:otherwise>
                </c:choose>
                <div class="span-7">
                    ${altFromList.name}<br/>
                    <c:if test="${empty altFromList.bets}"><span class="detailTitle"><spring:message code="competition.no.bets"/><br/></span></c:if>
                    <c:forEach items="${altFromList.bets}" var="bet">
                        <span class="detailTitle"><a href='<c:url value="/admin/viewuser.html?userId=${bet.ownerId}"/>'>${bet.ownerName}</a> (${bet.stake} ${competition.currency})</span><br/>
                    </c:forEach>
                    <span class="detailTitle">${altFromList.turnover} ${competition.currency} <spring:message code="competition.on.this.alternative"/></span>
                </div>
                    <div class="span-7 last right">
                    </div>
                </div>
            </div>
        </li>
        </c:forEach>
        </ul>
        &nbsp;
    </div>
    <div class="span-16 formSectionSlimDiv" style="padding-top: 16px;">
       <h4><spring:message code="admin.competition.log.title"/></h4>
    <ul class="logList">
        <c:forEach items="${competitionLog}" var="entry">
            <li class="logEntry clickable">
                <fmt:formatDate value="${entry.created}" pattern="yyyy-MM-dd HH:mm"/> <c:out value="${entry.shortMessage}"/>
                <div class="logInfo">
                <span class="logFieldTitle">Date</span>: <fmt:formatDate value="${entry.created}" pattern="yyyy-MM-dd HH:mm:ss"/><br/>
                <span class="logFieldTitle">Message</span>: <c:out value="${entry.message}"/><br/>
                </div>
            </li>
        </c:forEach>
    </ul>&nbsp;
    </div>   
    
    <p>&nbsp;</p>
</div>
<form action='<c:url value="/j_spring_security_switch_user"/>' method="post" id="manageCompetitionForm">
    <input type="hidden" name="j_username" value="${competition.owner.user.username}"/>
    <input type="hidden" name="targetUrl" value="" id="competitionToManage"/>
    <input type="hidden" name="returnUrl" value="/admin/viewcompetition.html?competitionId=${competition.id}" />
</form>
