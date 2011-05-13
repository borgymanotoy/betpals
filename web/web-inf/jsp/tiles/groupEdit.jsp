<%@include file="includes.jsp"%>
<script type="text/javascript">
	function backToGroupList() {
	    window.location.href = '<c:url value="/mygroups.html"/>';
	} 
</script>
<div>
    <h2 class="dark"><spring:message code="group.edit.title"/></h2>
</div>
<div class="rbDiv contentDiv">
<c:url var="editGroupURL" value="/editgroup.html"/>
<form:form commandName="group" action="${editGroupURL}" method="post">
<form:hidden path="id"/>
<form:hidden path="ownerId"/>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv" style="padding-top: 8px;"><spring:message code="group.edit.name"/></div>
        <div class="span-10 last">
            <form:input path="name" size="45"/>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv" style="padding-top: 8px;"><spring:message code="group.edit.friends"/></div>
        <div class="span-10 last selectionDiv115">
             <ul class="selectionList">
             <c:forEach items="${friendsList}" var="friend">
                 <li><form:checkbox path="membersIdSet" label="${friend.fullName}" value="${friend.id}"/></li>
             </c:forEach>
             </ul>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv">&nbsp;</div>
        <div class="span-10 last">
           <input type="submit" class="greenButton110" value="<spring:message code='button.save'/>"/>&nbsp;
           <button class="blueButton110" onclick="backToGroupList(); return false;"><spring:message code="button.cancel"/></button>
        </div>
    </div>
</form:form>
             <p>&nbsp;</p>
</div>