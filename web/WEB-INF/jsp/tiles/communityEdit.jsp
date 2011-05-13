<%@include file="includes.jsp"%>
<script type="text/javascript">
	function backToCommunityList() {
	    window.location.href = '<c:url value="/mycommunities.html"/>';
	} 

	function deleteCommunity() {
		jQuery("#deleteCommunityForm").submit();
	} 
</script>
<div>
    <h2 class="dark"><spring:message code="community.edit.title"/></h2>
</div>
<div class="rbDiv contentDiv">
<c:url var="editCommunityURL" value="/editcommunity.html"/>
<form:form commandName="community" action="${editommunityURL}" method="post" enctype="multipart/form-data">
<form:hidden path="id"/>
<form:hidden path="ownerId"/>
    <div class="span-12">
        <div class="span-2 labelDiv"><img class="userPic" src='<c:url value="/communities/images/${community.id}.jpg"/>'/></div>
        <div style="padding-top: 15px; margin-bottom: 10px;" class="span-10 last formSectionDiv">
            <spring:message code="community.edit.add.picture"/><br/> 
            <input type="file" name="imageFile"/>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv" style="padding-top: 8px;"><spring:message code="community.edit.name"/></div>
        <div class="span-10 last">
            <form:input path="name" size="45"/>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv" style="padding-top: 8px;"><spring:message code="community.edit.description"/></div>
        <div class="span-10 last">
            <form:textarea path="description" style="width: 374px;"/>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="formSectionDiv">
            <input type="radio" name="accessType" value="PUBLIC"><spring:message code="access.type.public"/></input><br/>
            <input type="radio" name="accessType" value="PRIVATE"><spring:message code="access.type.private"/></input>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv">&nbsp;</div>
        <div class="span-10 last">
           <input type="submit" class="greenButton110" value='<spring:message code="button.save"/>'/>&nbsp;
           <button class="blueButton110" onclick="backToCommunityList(); return false;"><spring:message code="button.cancel"/></button>&nbsp;
           <button class="whiteButton110" onclick="deleteCommunity(); return false;"><spring:message code="button.delete"/></button>
        </div>
    </div>
</form:form>
             <p>&nbsp;</p>
</div>
<form id="deleteCommunityForm" action='<c:url value="/deletecommunity.html"/>' method="post">
    <input type="hidden" name="communityId" value="${community.id}"/>
</form>
