<%@include file="includes.jsp"%>
<script type="text/javascript">
   function manageCompetition(competitionId) {
       jQuery('#competitionToManage').val(competitionId);
       jQuery('#manageCompetitionForm').submit();
   } 
   
</script>
<div>
    <h2 class="dark"><spring:message code="competition.new.list.title"/></h2>
</div>
<div class="rbDiv contentDiv">
    <button style="margin-bottom: 10px;" class="blueButton90" onclick="manageCompetition(); return false;"><spring:message code="button.create.competition"/></button>
    <h4><spring:message code="competition.new.list.header"/></h4>
    <ul id="friendList">
    <c:forEach items="${competitionList}" var="competition">
        <li>
	        <div class="span-12">
	            <div class="span-2 userPicDiv">
	                <img class="userPic" src='<c:url value="/competition/images/${competition.id}.jpeg"/>'/>
	            </div>
	            <div class="span-10 last competitionDiv">
                    <h5 class="clickable" onclick="manageCompetition(${competition.id});">${competition.name}</h5>
                    <span class="detailTitle"><spring:message code="competition.status"/>: ${competition.status}</span><br/>
	            </div>
	        </div>
        </li>
    </c:forEach>
    </ul>
    <p>&nbsp;</p>
</div>
<form action='<c:url value="/competitionview.html"/>' method="post" id="manageCompetitionForm">
    <input type="hidden" name="competitionId" value="" id="competitionToManage"/>
</form>
