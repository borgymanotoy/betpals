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
    <h2 class="dark">Edit community</h2>
</div>
<div class="rbDiv contentDiv">
<c:url var="editCommunityURL" value="/editcommunity.html"/>
<form:form commandName="community" action="${editommunityURL}" method="post" enctype="multipart/form-data">
<form:hidden path="id"/>
<form:hidden path="ownerId"/>
    <div class="span-12">
        <div class="span-2 labelDiv"><img class="userPic" src='<c:url value="/communities/images/${community.id}.jpg"/>'/></div>
        <div style="padding-top: 15px; margin-bottom: 10px;" class="span-10 last formSectionDiv">
            Add picture<br/> 
            <input type="file" name="imageFile"/>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv" style="padding-top: 8px;">Name</div>
        <div class="span-10 last">
            <form:input path="name" size="45"/>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv" style="padding-top: 8px;">Description</div>
        <div class="span-10 last">
            <form:textarea path="description" style="width: 374px;"/>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="formSectionDiv">
            <form:radiobutton path="accessType" value="PUBLIC" label="Open to public"/><br/>
            <form:radiobutton path="accessType" value="PRIVATE" label="Private"/>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv">&nbsp;</div>
        <div class="span-10 last">
           <input type="submit" class="greenButton110" value="Save"/>&nbsp;
           <button class="blueButton110" onclick="backToCommunityList(); return false;">Cancel</button>&nbsp;
           <button class="whiteButton110" onclick="deleteCommunity(); return false;">Delete</button>
        </div>
    </div>
</form:form>
             <p>&nbsp;</p>
</div>
<form id="deleteCommunityForm" action='<c:url value="/deletecommunity.html"/>' method="post">
    <input type="hidden" name="communityId" value="${community.id}"/>
</form>
