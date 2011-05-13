<%@include file="includes.jsp"%>
<script type="text/javascript">
	function nextStep() {
        jQuery('#nextStep').val(true);
        jQuery('#competitionForm').submit();
	} 
	
	function saveAndExit() {
        jQuery('#competitionForm').submit();
	} 
	
    jQuery(document).ready(function() {
        jQuery("#competitionDeadline").datetimepicker({ dateFormat: 'yy-mm-dd', minDate: 0, timeFormat: 'hh:mm' });
        jQuery("#settlingDeadline").datetimepicker({ dateFormat: 'yy-mm-dd', minDate: 0, timeFormat: 'hh:mm' });
    });

   
</script>
<div>
    <h2 class="dark"><spring:message code="competition.create.title"/></h2>
</div>
<div class="rbDiv contentDiv">
<c:url value="/savecompetition.html" var="actionURL"/>
<form:form commandName="competition" action="${actionURL}" method="post" enctype="multipart/form-data" id="competitionForm">
    <form:hidden path="id"/>
    <div class="formSectionDiv">
        <div class="span-12">
            <div class="span-2 labelDiv">
                <spring:message code="competition.edit.name"/>
            </div>
            <div class="span-10 last">
                <form:input path="name" size="36"/>
            </div>
        </div>
        <div class="span-12">
            <div class="span-2 labelDiv">
                <spring:message code="competition.edit.description"/>
            </div>
            <div class="span-10 last">
                <form:textarea path="description" id="competitionDescription"/>
            </div>
        </div>
        &nbsp;
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
    <div class="formSectionDiv">
        <spring:message code="competition.select.currency"/>
        <form:select path="accountId">
        <c:forEach items="${accounts}" var="account">
            <!-- TODO: mark selected element! -->
            <form:option label="${account.currency}" value="${account.id}"/>
        </c:forEach>
        </form:select> 
    </div>    
    <div class="formSectionDiv">
        <input type="radio" name="competitionType" value="POOL_BETTING"><spring:message code="competition.type.pool.betting"/></input><br/>
        <input type="radio" name="competitionType" value="FIXED_STAKE"><spring:message code="competition.type.fixed.stake"/></input>
        <form:input path="fixedStake" size="4"/>
    </div>
    <div class="formSectionDiv">
        <input type="checkbox" name="public"><spring:message code="competition.public"/></input>
        <form:hidden path="goToNextStep" id="nextStep"/>
    </div>
</form:form>
    <button class="whiteButton90" onclick="goHome();"><spring:message code="button.cancel"/></button>
    <button class="greenButton110" onclick="saveAndExit();"><spring:message code="button.save.and.exit"/></button>
    <button class="blueButton110" onclick="nextStep();"><spring:message code="button.next"/></button>

    <p><form:errors path="*"/></p>
    <p>&nbsp;</p>
</div>