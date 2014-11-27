<%@include file="includes.jsp"%>
<script type="text/javascript">
    function createCompetition() {
        jQuery('#goToCreateCompetitionForm').submit();
    } 

</script>
<div id="rightPane">
    <div class="blueTitle"><spring:message code="create.competitions.pane.title"/></div>
    <div class="panel nopadding">
        <button id="competitionButton" onclick="createCompetition();">&nbsp;</button>
    </div>
    <div class="panelFooter">&nbsp;</div>
    <div class="greyTitle"><spring:message code="public.bets.pane.title"/></div>
    <div class="panelNoPadding">
         <ul id="pbRightList">
         <c:forEach items="${topPublicCompetitions}" var="competition">
             <li>
                 <div class="pbRightOwnerDiv"><a href='<c:url value="/viewprofile/${competition.ownerId}.html"/>'>${competition.owner.fullName}</a></div>
                 <h5 class="clickable pbRightTitle" onclick="viewPublicCompetition(${competition.id});">${competition.name}</h5>
                 <p>
                 <span class="detailTitle"><spring:message code="competition.deadline"/>: </span><br/>
                 <fmt:formatDate value="${competition.deadline}" pattern="yyyy-MM-dd HH:mm"/><br/>
                 </p>
             </li>
         </c:forEach>
         </ul>
        <div style="padding-left: 10px; padding-bottom: 2px;"><a class="greenDotLink" href='<c:url value="/publiccompetitions.html"/>'><spring:message code="view.public.bets.link"/></a></div>
        &nbsp;<br/>
    </div>
    <div class="panelFooter">&nbsp;</div>
</div>
<form action='<c:url value="/competitionview.html"/>' method="post" id="goToCreateCompetitionForm">
    <input type="hidden" name="accountId" value=""/>
</form>
