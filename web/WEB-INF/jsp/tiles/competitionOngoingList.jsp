<%@include file="includes.jsp"%>
<script type="text/javascript">
   function viewCompetition(competitionId) {
       jQuery('#competitionToView').val(competitionId);
       jQuery('#viewCompetitionForm').submit();
   } 
   
</script>
<div>
    <h2 class="dark">Ongoing competitions</h2>
</div>
<div class="rbDiv contentDiv">
    <h4>Competitions</h4>
    <ul id="friendList">
    <c:forEach items="${competitionList}" var="competition">
        <li>
	        <div class="span-12">
	            <div class="span-2 userPicDiv">
	                <img class="userPic" src='<c:url value="/competition/images/${competition.id}.jpeg"/>'/>
	            </div>
	            <div class="span-10 last competitionDiv">
                    <h5 class="clickable" onclick="viewCompetition(${competition.id});">${competition.name}</h5>
                    <span class="detailTitle">Creator: </span><a href='<c:url value="/viewprofile/${competition.ownerId}.html"/>'>${competition.owner.fullName}</a><br/>
                    <span class="detailTitle">Status: ${competition.status} (deadline <fmt:formatDate value="${competition.deadline}" pattern="yyyy-MM-dd HH:mm"/>)</span><br/>
	            </div>
	        </div>
        </li>
    </c:forEach>
    </ul>
    <p>&nbsp;</p>
</div>
<form action='<c:url value="/ongoingcompetition.html"/>' method="post" id="viewCompetitionForm">
    <input type="hidden" name="competitionId" value="" id="competitionToView"/>
</form>

