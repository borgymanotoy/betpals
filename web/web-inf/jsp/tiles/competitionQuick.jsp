<%@include file="includes.jsp"%>
<script type="text/javascript">
   jQuery(document).ready(function() {
        jQuery("#competitionDeadline").datetimepicker({ dateFormat: 'yy-mm-dd', minDate: 0, timeFormat: 'hh:mm' });
        jQuery("#settlingDeadline").datetimepicker({ dateFormat: 'yy-mm-dd', minDate: 0, timeFormat: 'hh:mm' });
   });
</script>
<!-- TODO: All strings to message resources -->
<c:url value="/quickcompetition.html" var="actionURL"/>
<form:form commandName="quickCompetition" action="${actionURL}" method="post" enctype="multipart/form-data">
<div id="quickCompetitionDiv2">
    <h2>Quick competition</h2>
    <div class="headerContentDiv">
	    <span>I bet </span><form:input id="quickCompetitionStake" path="stake"/>
	    <form:select path="accountId">
	    <c:forEach items="${accounts}" var="account">
	        <!-- TODO: mark selected element! -->
	        <form:option label="${account.currency}" value="${account.id}"/>
	    </c:forEach>
	    </form:select> 
	    <span>that </span> 
	    <form:input id="quickCompetitionAlternative2" path="name"/>
    </div>
</div>
<div class="rbDiv contentDiv">
    <div class="formSectionDiv">
        Description<br/>
        <form:textarea path="description" id="quickCompetitionDescription"/>
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
    <div class="formSectionDiv span-12">
        <div class="span-4">
            <form:radiobutton path="allFriends" label="All friends" value="true"/><br/>
            <form:radiobutton path="allFriends" label="Select from list" value="false"/>
        </div>
        <div class="span-4">
            <h6>Friends</h6>
            <div class="selectionDiv115">
		        <ul class="selectionList">
		        <c:forEach items="${friendsSideList}" var="friend">
			        <li><form:checkbox path="friendsIdSet" label="${friend.fullName}" value="${friend.id}"/></li>
		        </c:forEach>
		        </ul>
            </div>
        </div>
        <div class="span-4 last">
            <h6>Groups</h6>
            <div class="selectionDiv115">
            </div>
        </div>
    </div>    
    <div class="formSectionDiv" style="padding-top: 5px; padding-bottom: 15px; margin-bottom: 15px;">
        <form:checkbox path="facebookPublish" label="Publish this bet on Facebook "/>
        <img src='<c:url value="/i/facebook.jpg"/>' style="vertical-align: bottom;"/>
    </div>    
    <input type="submit" value="Submit" class="greenButton110"/>

    <p><form:errors path="*"/></p>
    <p>&nbsp;</p>
</div>
</form:form>