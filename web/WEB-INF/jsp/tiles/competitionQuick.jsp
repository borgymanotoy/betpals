<%@include file="includes.jsp"%>
<script type="text/javascript">
   jQuery(document).ready(function() {
        jQuery("#competitionDeadline").datetimepicker({ dateFormat: 'yy-mm-dd', minDate: 0, timeFormat: 'hh:mm' });
        jQuery("#settlingDeadline").datetimepicker({ dateFormat: 'yy-mm-dd', minDate: 0, timeFormat: 'hh:mm' });
   });
</script>
<c:url value="/quickcompetition.html" var="actionURL"/>
<form:form commandName="quickCompetition" action="${actionURL}" method="post" enctype="multipart/form-data">
<div id="quickCompetitionDiv2">
    <h2><spring:message code="competition.quick.title"/></h2>
    <div class="headerContentDiv">
	    <span><spring:message code="competition.i.bet"/> </span><form:input id="quickCompetitionStake" path="stake"/>
	    <form:select path="accountId">
	    <c:forEach items="${accounts}" var="account">
	        <!-- TODO: mark selected element! -->
	        <form:option label="${account.currency}" value="${account.id}"/>
	    </c:forEach>
	    </form:select> 
	    <span><spring:message code="competition.bet.that"/> </span> 
	    <form:input id="quickCompetitionAlternative2" path="name"/>
    </div>
</div>
<div class="rbDiv contentDiv">
    <div class="formSectionDiv">
        <spring:message code="competition.edit.description"/><br/>
        <form:textarea path="description" id="quickCompetitionDescription"/>
    </div>    
    <div class="formSectionDiv">
        <img class="userPic" src='<c:url value="/images/competitions/empty.jpg"/>'/>
        <div style="display: inline-block;">
            <spring:message code="competition.add.picture"/><br/> 
            <input type="file" name="imageFile"/>
        </div>
    </div>    
    <div class="formSectionDiv" style="padding-top: 5px; padding-bottom: 15px;">
        <div class="span-12">
            <div class="span-2 labelDiv">
                <spring:message code="competition.competition.deadline"/>
            </div>
            <div class="span-10 last">
                <form:input path="deadline" id="competitionDeadline"/>
            </div>
        </div>
        <div class="span-12">
            <div class="span-2 labelDiv">
                <spring:message code="competition.settling.deadline"/>
            </div>
            <div class="span-10 last">
                <form:input path="settlingDeadline" id="settlingDeadline"/>
            </div>
        </div>
        &nbsp;
    </div>    
    <button class="whiteButton90" onclick="goHome(); return false;"><spring:message code="button.cancel"/></button>
    <input type="submit" value="<spring:message code='button.next'/>" class="greenButton110"/>

    <p><form:errors path="*"/></p>
    <p>&nbsp;</p>
</div>
</form:form>