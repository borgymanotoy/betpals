<%@include file="includes.jsp"%>
<div>
    <h2 class="dark">Edit user profile</h2>
</div>
<div class="rbDiv contentDiv">
    <c:url var="editProfileURL" value="/editprofile.html"/>
            <form:form commandName="userProfile" action="${editProfileURL}" method="post" enctype="multipart/form-data">
            <form:hidden path="userId"/>
            First name
            <input type="file" name="userImageFile"/>
            First name
            <form:input path="name"/>
            Last name
            <form:input path="surname"/>
            Address
            <input type="text" name="email"/>
            Town/City
            <input type="text" name="email"/>
            ZIP
            <input type="text" name="email"/>
            Country
            <input type="text" name="email"/>
            Bio
            <textarea name="bio"></textarea>
            <input type="submit" class="addButton" value="Save"/>
            </form:form>
             <p>&nbsp;</p>
</div>