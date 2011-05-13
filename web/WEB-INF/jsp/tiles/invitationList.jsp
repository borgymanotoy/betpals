<%@include file="includes.jsp"%>
<script type="text/javascript">
    function getInvitationDetails(invitationId) {
        jQuery('input', '#invitationDetailsForm').val(invitationId);
        jQuery('#invitationDetailsForm').submit();
    } 
</script>
<div>
    <h2 class="dark"><spring:message code="invitation.list.title"/></h2>
</div>
<div class="rbDiv contentDiv">
    <h4><spring:message code="invitation.list.header"/></h4>
    <ul id="friendList">
    <c:forEach items="${invitationList}" var="invitation">
        <li>
	        <div class="span-12">
	            <div class="span-2 userPicDiv">
	                <img class="userPic" src='<c:url value="/competition/images/${invitation.competitionId}.jpeg"/>'/>
	            </div>
	            <div class="span-10 last invitationDiv">
                    <h5 onclick="getInvitationDetails(${invitation.id});">${invitation.competitionName}</h5>
                    <span class="detailTitle"><spring:message code="invitation.deadline"/>: </span><fmt:formatDate value="${invitation.deadline}" pattern="yyyy-MM-dd HH:mm"/><br/>
                    <span class="detailTitle"><spring:message code="invitation.owner"/>: </span><a href='<c:url value="/viewprofile/${invitation.ownerId}.html"/>'>${invitation.ownerName}</a>
	            </div>
	        </div>
        </li>
    </c:forEach>
    </ul>
    <p>&nbsp;</p>
</div>
<form action='<c:url value="/invitation.html"/>' method="post" id="invitationDetailsForm">
    <input type="hidden" name="invitationId" value=""/>
</form>
