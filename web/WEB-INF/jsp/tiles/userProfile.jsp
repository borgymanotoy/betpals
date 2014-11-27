<%@include file="includes.jsp"%>
<div>
    <h2 class="dark"><spring:message code="profile.view.title"/></h2>
</div>
<div class="rbDiv contentDiv">
    <h4><spring:message code="profile.view.header"/></h4>
    <div class="span-12">
        <div class="span-2 labelDiv"><img class="userPic" src='<c:url value="/users/images/${userProfile.userId}.jpeg"/>'/></div>
        <div style="padding-top: 5px; margin-bottom: 10px;" class="span-10 last">
            <h5>${userProfile.fullName}</h5>
            <spring:message code="${userProfile.country}"/>
           <br/>
           <p>${userProfile.bio}</p>
        </div>
    </div>
    <p>&nbsp;</p>
    <h4><spring:message code="profile.reputation"/></h4>
    <table class="palsTable reputationTable zeroCellSpacingPadding">
         <tr>
             <th>&nbsp;<spring:message code="profile.competitions.created"/></th>
             <th><spring:message code="profile.competitions.participated"/></th>
         </tr>
         <tr class="odd">
             <td>&nbsp;${totalCompetitions}</td>
             <td>${totalBets}</td>
         </tr>
     </table>
    <p>&nbsp;</p>
	<div class="span-2 last left userControlDiv">
	<c:if test="${user.userId != friendProfile.userId && !friendProfile.friendWithCurrentUser}">
		<form action='<c:url value="/invitefriend.html"/>' method="post">
			<input type="hidden" name="friendId" value="${friendProfile.userId}"/>
			<button class="addAsFriendButton" onclick="submit();"><spring:message code="button.add.friend"/></button>
		</form>
	</c:if>
	</div>
    <p>&nbsp;</p>
</div>
