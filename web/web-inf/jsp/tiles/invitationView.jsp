<%@include file="includes.jsp"%>
<div>
    <h2 class="dark"><spring:message code="invitation.view.title"/></h2>
</div>
<div class="rbDiv contentDiv">
    <div class="formSectionDiv">
        <img src='<c:url value="/i/ex.png"/>' style="vertical-align: bottom;"/>&nbsp;
        <a href='<c:url value="/viewprofile/${invitation.ownerId}.html"/>'>${invitation.ownerName}</a>&nbsp;<spring:message code="invitation.owner.wants.you.to.join"/><br/>
    </div>
    <div class="formSectionDiv">
	    <img class="userPic" src='<c:url value="/competition/images/${invitation.competitionId}.jpeg"/>'/>
	    <div class="inlineDiv">
    	    <h5>${competition.name}</h5>
	       ${competition.description}<br/>
	    </div>
	    <div style="padding-top: 10px;">
		    <span class="detailTitle"><spring:message code="invitation.deadline"/>: </span><fmt:formatDate value="${competition.deadline}" pattern="yyyy-MM-dd HH:mm"/><br/>
		    <span class="detailTitle"><spring:message code="invitation.resolved"/>: </span><fmt:formatDate value="${competition.settlingDeadline}" pattern="yyyy-MM-dd HH:mm"/><br/>
	    </div>
    </div>
    
    <ul id="friendList">
    <c:forEach items="${competition.events}" var="event">
        <c:forEach items="${event.alternatives}" var="alternative">
            <li>
            <div class="span-12">
                <div class="span-2 userPicDiv">
                    <img class="userPic" src='<c:url value="/competition/images/alt_${alternative.id}.jpeg"/>'/>
                </div>
                <div class="span-5 invViewDiv">
                    ${alternative.name}<br/>
                    <span class="detailTitle">${alternative.turnover} ${competition.currency} <spring:message code="competition.on.this.alternative"/></span>
                </div>
		        <c:choose>
		          <c:when test="${alternative.taken}">
	                <div class="span-5 last invViewDiv right">
			              <spring:message code="alternative.already.taken.by"/> <br/><a href='<c:url value="/viewprofile/${alternative.participantId}.html"/>'>${alternative.participantName}</a>
	                </div>
		          </c:when>
		          <c:otherwise>
		            <form action='<c:url value="/placebet.html"/>' method="post">
                    <div class="span-5 last invViewButtonDiv right">
                        <c:choose>
                        <c:when test="${competition.competitionType == 'POOL_BETTING'}">
                            <input type="text" id="myStake" size="3" name="stake"/> ${competition.currency}&nbsp;
                        </c:when>
                        <c:otherwise>
                            ${competition.fixedStake} ${competition.currency}&nbsp;
                        </c:otherwise>
                        </c:choose>
		                <input type="hidden" name="invitationId" value="${invitation.id}"/>
		                <input type="hidden" name="alternativeId" value="${alternative.id}"/>
		                <input type="submit" class="greenButton90" value="<spring:message code="button.place.bet"/>"/>
                    </div>
		            </form>
		          </c:otherwise>
		        </c:choose>
            </div>
            </li>
        </c:forEach>
    </c:forEach>
    </ul>
    <p>&nbsp;</p>
</div>
