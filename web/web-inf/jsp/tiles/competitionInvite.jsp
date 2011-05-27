<%@include file="includes.jsp"%>
<script type="text/javascript">

	function shareToMedia(mediaId) {
        jQuery('#mediaId').val(mediaId);
	    jQuery("#confirmShare").dialog('open');
	} 

	jQuery(document).ready(function() {

		jQuery("#confirmShare").dialog({
            resizable: false,
            autoOpen: false,
            height:160,
            modal: true,
            buttons: {
                "<spring:message code='button.share'/>": function() {
                    jQuery('#invitationForm').submit();
                    $( this ).dialog( "close" );
                },
                "<spring:message code='button.cancel'/>": function() {
                    $( this ).dialog( "close" );
                }
            }
        });
		
	});
</script>
<div>
    <h2 class="dark"><spring:message code="competition.invite.title"/></h2>
</div>
<div class="rbDiv contentDiv">
<c:url value="/invitetocompetition.html" var="actionURL"/>
<form:form commandName="invitationHelper" action="${actionURL}" method="post" id="invitationForm">
    <h4><spring:message code="competition.invite.mail.header"/></h4>
    <p><spring:message code="competition.invite.mail.header.text"/></p>
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
    <p><spring:message code="competition.send.to.pals.header.text"/></p>
    <div>
        <input type="radio" name="allFriends" value="true"  <c:if test="${invitationHelper.allFriends}">checked="checked"</c:if>/><spring:message code="competition.all.friends"/><br/>
        <input type="radio" name="allFriends" value="false" <c:if test="${not invitationHelper.allFriends}">checked="checked"</c:if>/><spring:message code="competition.select.friends"/>
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
    <form:hidden path="competitionId"/>
    <input type="hidden" name="mediaIdSet" value="" id="mediaId"/>
    <input type="submit" value="<spring:message code='button.send'/>" class="blueButton110"/>
    <p>&nbsp;</p>
</form:form>
    <h4><spring:message code="competition.share.header"/></h4>
    <p><spring:message code="competition.share.header.text"/></p>
    <div class="formSectionDiv">
        <span class="clickable greenDotLink" onclick="shareToMedia('1');return false">Facebook</span>
    </div>
    <p>&nbsp;</p>
</div>
<div id="confirmShare" title='<spring:message code="confirmation.share.competition.title"/>'>
    <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span><spring:message code="confirmation.share.competition"/></p>
</div>

