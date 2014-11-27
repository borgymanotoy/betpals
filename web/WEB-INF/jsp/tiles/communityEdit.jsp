<%@include file="includes.jsp"%>
<script type="text/javascript">
	function backToCommunityList() {
	    window.location.href = '<c:url value="/mycommunities.html"/>';
	} 

	function deleteCommunity() {
		jQuery("#deleteCommunityForm").submit();
	} 
	
   jQuery(document).ready(function() {
        var thumb = $('img#thumb'); 

        new AjaxUpload('#imageUpload', {
            action: '<c:url value="/savetempcommunityimage.html"/>',
            name: 'imageFile',
            onSubmit: function(file, extension) {
                $('div.preview').addClass('loading');
                jQuery('#filenameDiv').text('<spring:message code="generating.thumbnail"/>');
            },
            onComplete: function(file, response) {
                thumb.load(function(){
                    $('div.preview').removeClass('loading');
                    thumb.unbind();
                });

                try {
                    var responseJson = jQuery.parseJSON(response);
                    if (responseJson.success == 'true') {
                      var newSrc = '<c:url value="/communities/images/"/>';
                      var date = new Date();
                      thumb.attr('src', newSrc + responseJson.filename + ".jpg?v=" + date.getTime());
                      jQuery('#filenameDiv').text(file);
                    } else {
                        jQuery('#filenameDiv').text('<spring:message code="could.not.process.image"/>');
                    }
                } catch (exception) {
                    jQuery('#filenameDiv').text('<spring:message code="could.not.process.image"/>');
                }
            }
        });
        
   });
	
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
        <div class="span-2 labelDiv">
            <img class="userPic" src='<c:url value="/communities/images/${community.id}.jpeg"/>' id="thumb"/>
        </div>  
        <div class="span-10 last">
            <div id="filenameDiv" style="padding-top: 8px; padding-bottom: 10px;">&nbsp;</div>
            <button class="whiteButton110" id="imageUpload">
                <spring:message code="button.add.picture"/> 
            </button>
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
            <input type="radio" name="accessType" value="PUBLIC" <c:if test="${community.accessType == 'PUBLIC'}">checked="checked"</c:if>/><spring:message code="access.type.public"/><br/>
            <input type="radio" name="accessType" value="PRIVATE" <c:if test="${community.accessType == 'PRIVATE'}">checked="checked"</c:if>/><spring:message code="access.type.private"/>
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
