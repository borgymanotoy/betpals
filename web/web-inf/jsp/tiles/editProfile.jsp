<%@include file="includes.jsp"%>
<script type="text/javascript">
    
   jQuery(document).ready(function() {
        var thumb = $('img#thumb'); 

        new AjaxUpload('#imageUpload', {
            action: '<c:url value="/savetempuserimage.html"/>',
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
                      var newSrc = '<c:url value="/users/images/"/>';
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
        
        jQuery("#emailField").change(function (){
        	jQuery("#emailPassword").show();
        });
   });
    
   function changePassword() {
	   var oldPassword = jQuery("#oldPassword").val();
	   var newPassword = jQuery("#newPassword").val();
	   var newPasswordRepeat = jQuery("#newPasswordRepeat").val();

	   if (oldPassword.length == 0 || newPassword.length == 0 || newPasswordRepeat.length == 0) {
		   var errorText = '<spring:message code="error.all.fields.are.mandatory"/>';
		   jQuery("#changePasswordError").html(errorText);
	   } else if (newPassword != newPasswordRepeat) {
           var errorText = '<spring:message code="error.passwords.mismatch"/>';
           jQuery("#changePasswordError").html(errorText);
	   } else {
		   jQuery("#changePasswordForm").submit();
	   }
   }
</script>

<div>
    <h2 class="dark"><spring:message code="profile.edit.title"/></h2>
</div>
<div class="rbDiv contentDiv">
<c:url var="editProfileURL" value="/editprofile.html"/>
<form:form commandName="userProfile" action="${editProfileURL}" method="post" enctype="multipart/form-data">
<form:hidden path="userId"/>
    <h4><spring:message code="profile.edit.header"/></h4>
    <div class="span-12">
        <div class="span-2 labelDiv">
            <img class="userPic" src='<c:url value="/users/images/${userProfile.userId}.jpeg"/>' id="thumb"/>
        </div>  
        <div class="span-10 last">
            <div id="filenameDiv" style="padding-top: 8px; padding-bottom: 10px;">&nbsp;</div>
            <button class="whiteButton110" id="imageUpload">
                <spring:message code="button.add.picture"/> 
            </button>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv" style="padding-top: 8px;"><spring:message code="profile.edit.name"/></div>
        <div class="span-10 last">
            <form:input path="name"/>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv" style="padding-top: 8px;"><spring:message code="profile.edit.surname"/></div>
        <div class="span-10 last">
            <form:input path="surname"/>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv" style="padding-top: 8px;"><spring:message code="profile.edit.email"/></div>
        <div class="span-10 last">
            <form:input path="email" id="emailField"/>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv" style="display: none;" id="emailPassword">
        <div class="span-2 labelDiv" style="padding-top: 8px;"><spring:message code="profile.edit.password"/></div>
        <div class="span-10 last">
            <span class="error"><spring:message code="profile.edit.email.password"/></span><br/>
            <form:password path="password"/>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv" style="padding-top: 8px;"><spring:message code="profile.edit.address"/></div>
        <div class="span-10 last">
            <form:input path="address" style="width: 300px;"/>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv" style="padding-top: 8px;"><spring:message code="profile.edit.city"/></div>
        <div class="span-10 last">
            <form:input path="city" style="width: 300px;"/>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv" style="padding-top: 8px;"><spring:message code="profile.edit.zip"/></div>
        <div class="span-10 last">
            <form:input path="postalCode" size="7"/>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv" style="padding-top: 8px;"><spring:message code="profile.edit.country"/></div>
        <div class="span-10 last">
            <form:select path="country" style="width: 310px; height: 30px;">
                <form:option value=""></form:option>
                <c:forEach items="${countryList}" var="country">
                <form:option value="${country}"><spring:message code="${country}"/></form:option>
                </c:forEach>
            </form:select>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv" style="padding-top: 8px;"><spring:message code="profile.edit.bio"/></div>
        <div class="span-10 last">
            <form:textarea path="bio" id="userBioTextArea"/>
        </div>
    </div>
    <p class="error"><form:errors path="*"/></p>
    <c:if test="${alreadyExist}"><p class="error"><spring:message code="profile.edit.user.exists"/></p></c:if>
    <c:if test="${wrongPassword}"><p class="error"><spring:message code="profile.edit.wrong.password"/></p></c:if>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv">&nbsp;</div>
        <div class="span-10 last">
           <input type="submit" class="greenButton110" value="<spring:message code='button.save'/>"/>
           <button class="whiteButton90" onclick="goHome();return false;"><spring:message code="button.cancel"/></button> 
        </div>
    </div>
</form:form>
    <p>&nbsp;</p>
    <p>&nbsp;</p>
    <h4><spring:message code="profile.change.password.header"/></h4>
    <form action='<c:url value="/changepassword.html"/>' method="post" id="changePasswordForm">
    <p class="error" id="changePasswordError">
    <c:if test="${wrongOldPassword}">
        <spring:message code="error.wrong.old.password"/>
    </c:if>
    </p>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv" style="padding-top: 8px;"><spring:message code="profile.password.old"/></div>
        <div class="span-10 last">
            <input type="password" name="oldPassword" id="oldPassword"/>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv" style="padding-top: 8px;"><spring:message code="profile.password.new"/></div>
        <div class="span-10 last">
            <input type="password" name="newPassword" id="newPassword"/>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv" style="padding-top: 8px;"><spring:message code="profile.password.new.repeat"/></div>
        <div class="span-10 last">
            <input type="password" name="newPasswordRepeat" id="newPasswordRepeat"/>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv">&nbsp;</div>
        <div class="span-10 last">
           <button class="blueButton110" onclick="changePassword(); return false;"><spring:message code='button.change'/></button>
        </div>
    </div>
    </form>
    <p>&nbsp;</p>
</div>