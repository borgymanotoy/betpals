<%@include file="includes.jsp"%>
<script type="text/javascript">
   function saveAndExit() {
       jQuery('#manageCompetitionForm').submit();
   } 

   function goBack() {
	   jQuery('#createCompetitionForm').submit();
   } 
   
   function goToNextStep() {
       jQuery('#confirmCompetitionForm').submit();
   } 
   
</script>
<!-- TODO: All strings to message resources -->
<div>
    <h2 class="dark">Create competition</h2>
</div>
<div class="rbDiv contentDiv">
    <div class="span-12">
        <div class="span-2 userPicDiv">
            <img class="userPic" src='<c:url value="/competition/images/${competition.id}.jpeg"/>'/>
        </div>
        <div class="span-10 last invitationDiv">
            <h5>${competition.name}</h5>
            ${competition.description}
        </div>
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
                        <div class="span-8 altListName">${altFromList.name}</div>
                        <div class="span-2 last">&nbsp;</div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="span-10 last altListDetails">
                        <div class="span-8 altListName">${altFromList.name}</div>
                        <div class="span-2 last">&nbsp;</div>
                    </div>
                </c:otherwise>
                </c:choose>
            </div>
        </li>
        </c:forEach>
        </ul>
        &nbsp;
    </div>
    <div class="span-12">
        <div class="span-5 formSectionDiv">Deadline: <span class="bold"><fmt:formatDate value="${competition.deadline}" pattern="yyyy-MM-dd HH:mm"/></span></div>
        <div class="span-7 last">&nbsp;</div>
    </div>
    <div class="span-12">
        <div class="span-5 formSectionDiv">Stake: <span class="bold">${competition.fixedStake} ${competition.currency}</span></div>
        <div class="span-7 last">&nbsp;</div>
    </div>
    <p>This competition is private. Just the people you choose in the next step will be possible participants.</p>
    <button class="blueButton110" onclick="goBack();">Back</button>
    <button class="whiteButton90" onclick="goHome();">Cancel</button>
    <button class="greenButton110" onclick="saveAndExit();">Save and exit</button>
    <button class="blueButton110" onclick="goToNextStep();">Confirm</button>
    <p>&nbsp;</p>
</div>
<form action='<c:url value="/competitionalternatives.html"/>' method="post" id="createCompetitionForm">
    <input type="hidden" name="competitionId" value="${competition.id}"/>
</form>
<form action='<c:url value="/confirmcompetition.html"/>' method="post" id="confirmCompetitionForm">
    <input type="hidden" name="competitionId" value="${competition.id}"/>
</form>
<form action='<c:url value="/managecompetitions.html"/>' method="post" id="manageCompetitionForm">
    <input type="hidden" name="competitionId" value="${competition.id}"/>
</form>
