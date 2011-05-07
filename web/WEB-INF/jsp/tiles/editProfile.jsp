<%@include file="includes.jsp"%>
<div>
    <h2 class="dark">Edit user profile</h2>
</div>
<div class="rbDiv contentDiv">
<c:url var="editProfileURL" value="/editprofile.html"/>
<form:form commandName="userProfile" action="${editProfileURL}" method="post" enctype="multipart/form-data">
<form:hidden path="userId"/>
    <h4>Personal information</h4>
    <div class="span-12">
        <div class="span-2 labelDiv"><img class="userPic" src='<c:url value="/images/users/${userProfile.userId}.jpg"/>'/></div>
        <div style="padding-top: 15px; margin-bottom: 10px;" class="span-10 last formSectionDiv">
            Add picture<br/> 
            <input type="file" name="userImageFile"/>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv" style="padding-top: 8px;">First name</div>
        <div class="span-10 last">
            <form:input path="name"/>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv" style="padding-top: 8px;">Last name</div>
        <div class="span-10 last">
            <form:input path="surname"/>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv" style="padding-top: 8px;">Address</div>
        <div class="span-10 last">
            <form:input path="address" style="width: 300px;"/>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv" style="padding-top: 8px;">Town/City</div>
        <div class="span-10 last">
            <form:input path="city" style="width: 300px;"/>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv" style="padding-top: 8px;">ZIP</div>
        <div class="span-10 last">
            <form:input path="postalCode" size="7"/>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv" style="padding-top: 8px;">Country</div>
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
        <div class="span-2 labelDiv" style="padding-top: 8px;">Bio</div>
        <div class="span-10 last">
            <form:textarea path="bio" id="userBioTextArea"/>
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv">&nbsp;</div>
        <div class="span-10 last">
           <input type="submit" class="greenButton110" value="Save changes"/>
           <button class="whiteButton90" onclick="goHome();return false;">Cancel</button> 
        </div>
    </div>
</form:form>
             <p>&nbsp;</p>
</div>