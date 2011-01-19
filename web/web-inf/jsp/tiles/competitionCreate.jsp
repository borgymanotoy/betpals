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
        jQuery("#competitionDeadline").datetimepicker({ dateFormat: 'yy-mm-dd', minDate: 0 });
        jQuery("#settlingDeadline").datetimepicker({ dateFormat: 'yy-mm-dd', minDate: 0 });
    });

   
</script>
<!-- TODO: All strings to message resources -->
<div>
    <h2 class="dark">Create competition</h2>
</div>
<div class="rbDiv contentDiv">
<c:url value="/savecompetition.html" var="actionURL"/>
<form:form commandName="competition" action="${actionURL}" method="post" enctype="multipart/form-data" id="competitionForm">
    <form:hidden path="id"/>
    <div class="formSectionDiv">
        <div class="span-12">
            <div class="span-2 labelDiv">
                Competition name
            </div>
            <div class="span-10 last">
                <form:input path="name" size="36"/>
            </div>
        </div>
        <div class="span-12">
            <div class="span-2 labelDiv">
                Description
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
            Add picture<br/> 
            <input type="file" name="imageFile"/>
        </div>
    </div>    
    <div class="formSectionDiv" style="padding-top: 5px; padding-bottom: 15px;">
        <div class="span-12">
            <div class="span-2 labelDiv">
                Deadline for competition
            </div>
            <div class="span-10 last">
                <form:input path="deadline" id="competitionDeadline"/>
            </div>
        </div>
        <div class="span-12">
            <div class="span-2 labelDiv">
                Deadline for settling
            </div>
            <div class="span-10 last">
                <form:input path="settlingDeadline" id="settlingDeadline"/>
            </div>
        </div>
        &nbsp;
    </div>    
    <div class="formSectionDiv">
        Select currency
        <form:select path="accountId">
        <c:forEach items="${accounts}" var="account">
            <!-- TODO: mark selected element! -->
            <form:option label="${account.currency}" value="${account.id}"/>
        </c:forEach>
        </form:select> 
    </div>    
    <div class="formSectionDiv">
        <form:radiobutton path="competitionType" value="POOL_BETTING" label="Pool betting"/><br/>
        <form:radiobutton path="competitionType" value="FIXED_STAKE" label="Fixed stake / One punter per selection"/>
        <form:input path="fixedStake" size="4"/>
    </div>
    <div class="formSectionDiv">
        <form:checkbox path="public" label="This competition is public"/>
        <form:hidden path="goToNextStep" id="nextStep"/>
    </div>
</form:form>
    <button class="whiteButton90" onclick="goHome();">Cancel</button>
    <button class="greenButton110" onclick="saveAndExit();">Save and exit</button>
    <button class="blueButton110" onclick="nextStep();">Next</button>

    <p><form:errors path="*"/></p>
    <p>&nbsp;</p>
</div>