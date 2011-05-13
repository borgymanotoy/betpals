<%@include file="includes.jsp"%>
<div>
    <h2 class="dark"><spring:message code="profile.edit.title"/></h2>
</div>
<div class="rbDiv contentDiv">
<c:url var="editProfileURL" value="/editprofile.html"/>
<form:form commandName="userProfile" action="${editProfileURL}" method="post" enctype="multipart/form-data">
<form:hidden path="userId"/>
    <h4><spring:message code="profile.edit.header"/></h4>
    <div class="span-12">
        <div class="span-2 labelDiv"><img class="userPic" src='<c:url value="/images/users/${userProfile.userId}.jpg"/>'/></div>
        <div style="padding-top: 15px; margin-bottom: 10px;" class="span-10 last formSectionDiv">
            <spring:message code="profile.edit.add.picture"/><br/> 
            <input type="file" name="userImageFile"/>
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
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv">&nbsp;</div>
        <div class="span-10 last">
           <input type="submit" class="greenButton110" value="<spring:message code='button.save'/>"/>
           <button class="whiteButton90" onclick="goHome();return false;"><spring:message code="button.cancel"/></button> 
        </div>
    </div>
</form:form>
             <p>&nbsp;</p>
</div>