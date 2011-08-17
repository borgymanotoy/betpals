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
<div>
    <h2 class="dark"><spring:message code="competition.create.title"/></h2>
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
    <h4><spring:message code="competition.alternatives.header"/></h4>
    <div class="formSectionDiv">
        <ul id="friendList">
        <c:forEach items="${competition.defaultEvent.sortedAlternatives}" var="altFromList" varStatus="status">
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
        <div class="span-5 formSectionDiv"><spring:message code="competition.deadline"/>: <span class="bold"><fmt:formatDate value="${competition.deadline}" pattern="yyyy-MM-dd HH:mm"/></span></div>
        <div class="span-7 last">&nbsp;</div>
    </div>
    <div class="span-12">
        <div class="span-5 formSectionDiv"><spring:message code="competition.stake"/>: <span class="bold">${competition.fixedStake} ${competition.currency}</span></div>
        <div class="span-7 last">&nbsp;</div>
    </div>
    <!-- <p><spring:message code="competition.private"/></p> -->
    <button class="blueButton110" onclick="goBack();"><spring:message code="button.back"/></button>
    <button class="whiteButton90" onclick="goHome();"><spring:message code="button.cancel"/></button>
    <button class="greenButton110" onclick="saveAndExit();"><spring:message code="button.save.and.exit"/></button>
    <button class="blueButton110" onclick="goToNextStep();"><spring:message code="button.confirm"/></button>
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
