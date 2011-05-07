<%@include file="includes.jsp"%>
<!-- TODO: All strings to message resources -->
<div>
    <h2 class="dark">Ongoing competition</h2>
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
        Deadline for competition: <span class="bold"><fmt:formatDate value="${competition.deadline}" pattern="yyyy-MM-dd HH:mm"/></span>
    </div>
    <div>
        Deadline for settling: <span class="bold"><fmt:formatDate value="${competition.settlingDeadline}" pattern="yyyy-MM-dd HH:mm"/></span>
    </div>
    <p>&nbsp;</p>
    <h4>Alternatives</h4>
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
                <div class="span-5">
                    ${altFromList.name}<br/>
                    <c:if test="${empty altFromList.bets}"><span class="detailTitle">No bets<br/></span></c:if>
                    <c:forEach items="${altFromList.bets}" var="bet">
                        <span class="detailTitle"><c:choose><c:when test="${bet.ownerId == competition.ownerId}">You</c:when><c:otherwise><a href='<c:url value="/viewprofile/${bet.ownerId}.html"/>'>${bet.ownerName}</a></c:otherwise></c:choose> (${bet.stake} ${competition.currency})</span><br/>
                    </c:forEach>
                    <span class="detailTitle">${altFromList.turnover} ${competition.currency} on this alternative.</span>
                </div>
                <c:choose>
                  <c:when test="${altFromList.taken}">
                    <div class="span-5 last right">
                          Already taken by <br/><a href='<c:url value="/viewprofile/${altFromList.participantId}.html"/>'>${altFromList.participantName}</a>
                    </div>
                  </c:when>
                  <c:otherwise>
                    <form action='<c:url value="/placeanotherbet.html"/>' method="post">
                    <div class="span-5 last right">
                        <c:choose>
                        <c:when test="${competition.competitionType == 'POOL_BETTING'}">
                            <input type="text" id="myStake" size="3" name="stake"/> ${competition.currency}&nbsp;
                        </c:when>
                        <c:otherwise>
                            ${competition.fixedStake} ${competition.currency}&nbsp;
                        </c:otherwise>
                        </c:choose>
                        <input type="hidden" name="competitionId" value="${competition.id}"/>
                        <input type="hidden" name="alternativeId" value="${altFromList.id}"/>
                        <input type="submit" class="greenButton90" value="Place bet"/>
                    </div>
                    </form>
                  </c:otherwise>
                </c:choose>
                </div>
            </div>
        </li>
        </c:forEach>
        </ul>
        &nbsp;
    </div>
    <p>&nbsp;</p>
</div>
