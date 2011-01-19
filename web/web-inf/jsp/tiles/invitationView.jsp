<%@include file="includes.jsp"%>
<div>
    <h2 class="dark">Place bet</h2>
</div>
<div class="rbDiv contentDiv">
    <div class="formSectionDiv">
        <img src='<c:url value="/i/ex.png"/>' style="vertical-align: bottom;"/>&nbsp;
        <a href="#">${invitation.ownerName}</a> wants you to join this competition.<br/>
    </div>
    <div class="formSectionDiv">
	    <img class="userPic" src='<c:url value="/competition/images/${invitation.competitionId}.jpeg"/>'/>
	    <div class="inlineDiv">
    	    <h5>${competition.name}</h5>
	       ${competition.description}<br/>
	    </div>
	    <div style="padding-top: 10px;">
		    <span class="detailTitle">Deadline: </span><fmt:formatDate value="${competition.deadline}" pattern="yyyy-MM-dd"/><br/>
		    <span class="detailTitle">Resolved: </span><fmt:formatDate value="${competition.settlingDeadline}" pattern="yyyy-MM-dd"/><br/>
	    </div>
    </div>
    
    <ul id="friendList">
    <c:forEach items="${competition.events}" var="event">
        <c:forEach items="${event.alternatives}" var="alternative">
            <li>
            <div class="span-12">
                <div class="span-2 userPicDiv">
                    <img class="userPic" src='<c:url value="/competition/images/${competition.id}.jpeg"/>'/>
                </div>
                <div class="span-5 invViewDiv">
                    ${alternative.name}
                </div>
                <div class="span-2 invViewDiv">
		            ${competition.fixedStake} ${competition.currency}
		        </div>
		        <div class="span-3 last invViewButtonDiv">
		        <c:choose>
		          <c:when test="${alternative.taken}">
		              Already taken by<br/>
		              <a href="#">${invitation.ownerName}</a>
		          </c:when>
		          <c:otherwise>
		            <form action='<c:url value="/placebet.html"/>' method="post">
		                <input type="hidden" name="invitationId" value="${invitation.id}"/>
		                <input type="hidden" name="alternativeId" value="${alternative.id}"/>
		                <input type="submit" class="greenButton110" value="Place bet"/>
		            </form>
		          </c:otherwise>
		        </c:choose>
                </div>
            </div>
            </li>
        </c:forEach>
    </c:forEach>
    </ul>
    <p>&nbsp;</p>
</div>