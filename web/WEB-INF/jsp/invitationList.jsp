<%@include file="includes.jsp"%>
<script type="text/javascript">
    function getInvitationDetails(invitationId) {
        jQuery('input', '#invitationDetailsForm').val(invitationId);
        jQuery('#invitationDetailsForm').submit();
    } 
    
    function viewCompetition(competitionId) {
        jQuery('#competitionToView').val(competitionId);
        jQuery('#viewCompetitionForm').submit();
    } 
    
</script>
<div>
    <h2 class="dark"><spring:message code="invitation.list.title"/></h2>
</div>
<div class="rbDiv contentDiv">
    <h4><spring:message code="invitation.list.header"/></h4>
    <ul id="friendList">
    <c:forEach items="${invitationList}" var="invitation">
        <li>
	        <div class="span-12">
	            <div class="span-2 userPicDiv">
	                <img class="userPic" src='<c:url value="/competition/images/${invitation.competitionId}.jpeg"/>'/>
	            </div>
	            <div class="span-10 last invitationDiv">
                    <h5 onclick="getInvitationDetails(${invitation.id});">${invitation.competitionName}</h5>
                    <span class="detailTitle"><spring:message code="invitation.deadline"/>: </span><fmt:formatDate value="${invitation.deadline}" pattern="yyyy-MM-dd HH:mm"/><br/>
                    <span class="detailTitle"><spring:message code="invitation.owner"/>: </span><a href='<c:url value="/viewprofile/${invitation.ownerId}.html"/>'>${invitation.ownerName}</a>
	            </div>
	        </div>
        </li>
    </c:forEach>
    </ul>
    <p>&nbsp;</p>
    <h4><spring:message code="competition.ongoing.list.header"/></h4>
    <ul id="friendList">
    <c:forEach items="${competitionList}" var="competition">
        <li>
            <div class="span-12">
                <div class="span-2 userPicDiv">
                    <img class="userPic" src='<c:url value="/competition/images/${competition.id}.jpeg"/>'/>
                </div>
                <div class="span-10 last competitionDiv">
                    <h5 class="clickable" onclick="viewCompetition(${competition.id});">${competition.name}</h5>
                    <span class="detailTitle"><spring:message code="competition.creator"/>: </span><a href='<c:url value="/viewprofile/${competition.ownerId}.html"/>'>${competition.owner.fullName}</a><br/>
                    <span class="detailTitle"><spring:message code="competition.status"/>: ${competition.status} (<spring:message code="competition.deadline"/> <fmt:formatDate value="${competition.deadline}" pattern="yyyy-MM-dd HH:mm"/>)</span><br/>
                </div>
            </div>
        </li>
    </c:forEach>
    </ul>
    <p>&nbsp;</p>
    
</div>
<form action='<c:url value="/invitation.html"/>' method="post" id="invitationDetailsForm">
    <input type="hidden" name="invitationId" value=""/>
</form>
<form action='<c:url value="/ongoingcompetition.html"/>' method="post" id="viewCompetitionForm">
    <input type="hidden" name="competitionId" value="" id="competitionToView"/>
</form>

