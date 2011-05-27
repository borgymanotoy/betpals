<%@include file="includes.jsp"%>
<script type="text/javascript">
   function manageCompetition(competitionId) {
       jQuery('#competitionToManage').val(competitionId);
       jQuery('#manageCompetitionForm').submit();
   } 
   
</script>
<div>
    <h2 class="dark"><spring:message code="competition.manage.list.title"/></h2>
</div>
<div class="rbDiv contentDiv">
    <h4><spring:message code="competition.manage.list.header"/></h4>
    <ul id="friendList">
    <c:forEach items="${competitionList}" var="competition">
        <li>
	        <div class="span-12">
	            <div class="span-2 userPicDiv">
	                <img class="userPic" src='<c:url value="/competition/images/${competition.id}.jpeg"/>'/>
	            </div>
	            <div class="span-10 last competitionDiv">
                    <h5 class="clickable" onclick="manageCompetition(${competition.id});">${competition.name}</h5>
                    <span class="detailTitle"><spring:message code="competition.deadline"/>: </span>
                    <fmt:formatDate value="${competition.deadline}" pattern="yyyy-MM-dd HH:mm"/>
                    <c:if test="${competition.status == 'CLOSE'}"><img src='<c:url value="/i/lock.gif"/>'/></c:if>
                    <br/>
                    <span class="detailTitle"><spring:message code="competition.participants"/>: </span>${competition.numberOfParticipants}<br/>
                    <span class="detailTitle"><spring:message code="competition.turnover"/>: </span><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${competition.turnover}"/>  ${competition.currency} <br/>
	            </div>
	        </div>
        </li>
    </c:forEach>
    </ul>
    <p>&nbsp;</p>
    <a class="greenDotLink" href='<c:url value="/settledcompetitions.html"/>'><spring:message code="link.settled.competitions"/> (${settledCompetitionCount})</a><br/>
    <p>&nbsp;</p>
</div>
<form action='<c:url value="/managecompetition.html"/>' method="post" id="manageCompetitionForm">
    <input type="hidden" name="competitionId" value="" id="competitionToManage"/>
</form>
