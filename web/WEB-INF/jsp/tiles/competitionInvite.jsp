<%@include file="includes.jsp"%>
<script type="text/javascript">
   
</script>
<div>
    <h2 class="dark"><spring:message code="competition.invite.title"/></h2>
</div>
<div class="rbDiv contentDiv">
<c:url value="/invitetocompetition.html" var="actionURL"/>
<form:form commandName="invitationHelper" action="${actionURL}" method="post">
    <h4><spring:message code="competition.invite.header"/></h4>
    <div class="span-12">
            <textarea name="invitation" id="competitionDescription" style="height: 140px; width: 465px;">
<security:authentication property="principal.userProfile.fullName"/> <spring:message code="competition.user.is.inviting.you"/>

${competition.name}
${competition.description}

<spring:message code="competition.invitation.wanna.join"/>
https://www.mybetpals.com/join/${competition.encodedLink}
            </textarea>
    </div>
    <p>&nbsp;</p>
    <h4><spring:message code="competition.send.to.pals.header"/></h4>
    <div>
        <input type="radio" name="allFriends" value="true"><spring:message code="competition.all.friends"/></input><br/>
        <input type="radio" name="allFriends" value="false"><spring:message code="competition.select.friends"/></input>
        <p>&nbsp;</p>
    </div>
    <div class="span-12">
        <div class="span-4">
            <h6><spring:message code="competition.friends.header"/></h6>
            <div class="selectionDiv115">
                <ul class="selectionList">
                <c:forEach items="${friendList}" var="friend">
                    <li><form:checkbox path="friendsIdSet" label="${friend.fullName}" value="${friend.id}"/></li>
                </c:forEach>
                </ul>
            </div>
        </div>
        <div class="span-4">
            <h6><spring:message code="competition.groups.header"/></h6>
            <div class="selectionDiv115">
                <ul class="selectionList">
                <c:forEach items="${groupList}" var="group">
                    <li><form:checkbox path="groupIdSet" label="${group.name}" value="${group.id}"/></li>
                </c:forEach>
                </ul>
            </div>
        </div>
        <div class="span-4 last">
            <h6><spring:message code="competition.communities.header"/></h6>
            <div class="selectionDiv115">
                <ul class="selectionList">
                <c:forEach items="${communityList}" var="community">
                    <li><form:checkbox path="communityIdSet" label="${community.name}" value="${community.id}"/></li>
                </c:forEach>
                </ul>
            </div>
        </div>
    </div>    
    <p>&nbsp;</p>
    <h4><spring:message code="competition.share.header"/></h4>
    <div class="formSectionDiv">
        <form:checkbox path="mediaIdSet" label="Facebook" value="1"/>
        <form:checkbox path="mediaIdSet" label="Twitter" value="2"/>
        <form:hidden path="competitionId"/>
    </div>
    <p>&nbsp;</p>
    <input type="submit" value="<spring:message code='button.send'/>" class="blueButton110"/>
    <p>&nbsp;</p>
</form:form>
</div>
