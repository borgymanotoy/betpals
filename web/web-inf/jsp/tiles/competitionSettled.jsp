<%@include file="includes.jsp"%>
<div>
    <h2 class="dark"><spring:message code="competition.settled.title"/></h2>
</div>
<div class="rbDiv contentDiv">
    <div class="span-12">
        <div class="span-2 userPicDiv">
            <img class="userPic" src='<c:url value="/competition/images/${competition.id}.jpeg"/>'/>
        </div>
        <div class="span-10 last invitationDiv formSectionDiv">
            <h5>${competition.name}</h5>
            <p>${competition.description}</p>
        </div>
    </div>
    <div>
        <spring:message code="competition.competition.deadline"/>: <span class="bold"><fmt:formatDate value="${competition.deadline}" pattern="yyyy-MM-dd HH:mm"/></span>
    </div>
    <div>
        <spring:message code="competition.settling.deadline"/>: <span class="bold"><fmt:formatDate value="${competition.settlingDeadline}" pattern="yyyy-MM-dd HH:mm"/></span>
    </div>
    <p>&nbsp;</p>
    <h4><spring:message code="competition.alternatives.header"/></h4>
    <div class="formSectionDiv">
        <ul id="friendList">
        <c:forEach items="${competition.defaultEvent.alternatives}" var="altFromList" varStatus="status">
        <li>
            <div class="span-12">
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
                <div class="span-10 last">
                    ${altFromList.name}<br/>
                    <c:if test="${empty altFromList.bets}"><span class="detailTitle"><spring:message code="competition.no.bets"/><br/></span></c:if>
                    <c:forEach items="${altFromList.bets}" var="bet">
                        <span class="detailTitle"><c:choose><c:when test="${bet.ownerId == competition.ownerId}"><spring:message code="competition.you"/></c:when><c:otherwise><a href='<c:url value="/viewprofile/${bet.ownerId}.html"/>'>${bet.ownerName}</a></c:otherwise></c:choose> (${bet.stake} ${competition.currency})</span><br/>
                    </c:forEach>
                    <span class="detailTitle">${altFromList.turnover} ${competition.currency} <spring:message code="competition.on.this.alternative"/></span>
                </div>
                </div>
            </div>
        </li>
        </c:forEach>
        </ul>
        &nbsp;
    </div>
    <p>&nbsp;</p>
</div>
