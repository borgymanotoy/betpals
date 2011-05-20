<%@include file="includes.jsp"%>
<script type="text/javascript">
   function viewCompetition(competitionId) {
       jQuery('#competitionToManage').val(competitionId);
       jQuery('#viewCompetitionForm').submit();
   } 
   
</script>
<div>
    <h2 class="dark"><spring:message code="competition.settled.list.title"/></h2>
</div>
<div class="rbDiv contentDiv">
    <h4><spring:message code="competition.settled.list.header"/></h4>
    <ul id="friendList">
    <c:forEach items="${competitionList}" var="competition">
        <li>
	        <div class="span-12">
	            <div class="span-2 userPicDiv">
	                <img class="userPic" src='<c:url value="/competition/images/${competition.id}.jpeg"/>'/>
	            </div>
	            <div class="span-10 last competitionDiv">
                    <h5 class="clickable" onclick="viewCompetition(${competition.id});">${competition.name}</h5>
                    <span class="detailTitle"><spring:message code="competition.competition.settled.deadline"/>: </span><fmt:formatDate value="${competition.deadline}" pattern="yyyy-MM-dd HH:mm"/><br/>
                    <span class="detailTitle"><spring:message code="competition.participants"/>: </span>${competition.numberOfParticipants}<br/>
                    <span class="detailTitle"><spring:message code="competition.turnover"/>: </span><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${competition.turnover}"/>  ${competition.currency} <br/>
	            </div>
	        </div>
        </li>
    </c:forEach>
    </ul>
    <p>&nbsp;</p>
</div>
<form action='<c:url value="/settledcompetition.html"/>' method="post" id="viewCompetitionForm">
    <input type="hidden" name="competitionId" value="" id="competitionToManage"/>
</form>
