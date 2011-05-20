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
    <div class="greyTitle"><spring:message code="promotions.pane.title"/></div>
    <div class="panel">
        &nbsp;<br/>
        <img src='<c:url value="/images/promotion/banner2.jpg"/>'/>
        <img src='<c:url value="/images/promotion/banner1.gif"/>'/>
        &nbsp;<br/>
    </div>
    <div class="panelFooter">&nbsp;</div>
</div>
<form action='<c:url value="/competitionview.html"/>' method="post" id="goToCreateCompetitionForm">
    <input type="hidden" name="accountId" value=""/>
</form>