<%@include file="includes.jsp"%>
<script type="text/javascript">
   
</script>
<!-- TODO: All strings to message resources -->
<div>
    <h2 class="dark">Invite to competition</h2>
</div>
<div class="rbDiv contentDiv">
<c:url value="/invitetocompetition.html" var="actionURL"/>
<form:form commandName="invitationHelper" action="${actionURL}" method="post">
    <h4>Invite to competition</h4>
    <div class="span-12">
        <div class="span-2 labelDiv">
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            Invitation
        </div>
        <div class="span-10 last">
            <textarea name="invitation" id="competitionDescription">
<security:authentication property="principal.userProfile.fullName"/> is inviting you to a competition at myBetpals!

${competition.name}
${competition.description}

Do you wanna join?
https://www.mybetpals.com/ce12ceab21
            </textarea>
        </div>
    </div>
    <p>&nbsp;</p>
    <h4>Send to betPals</h4>
    <div>
        <form:radiobutton path="allFriends" label="All friends" value="true"/><br/>
        <form:radiobutton path="allFriends" label="Select from list" value="false"/>
        <p>&nbsp;</p>
    </div>
    <div class="span-12">
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
        <div class="span-4">
            <h6>Groups</h6>
            <div class="selectionDiv115">
            </div>
        </div>
        <div class="span-4 last">
            <h6>Communities</h6>
            <div class="selectionDiv115">
            </div>
        </div>
    </div>    
    <p>&nbsp;</p>
    <h4>Share this competition</h4>
    <div class="formSectionDiv">
        <form:checkbox path="mediaIdSet" label="Facebook" value="1"/>
        <form:checkbox path="mediaIdSet" label="Twitter" value="2"/>
        <form:hidden path="competitionId"/>
    </div>
    <p>&nbsp;</p>
    <input type="submit" value="Send competition" class="blueButton110"/>
    <p>&nbsp;</p>
</form:form>
</div>
