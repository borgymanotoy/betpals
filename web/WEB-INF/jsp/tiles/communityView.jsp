<%@include file="includes.jsp"%>
<script type="text/javascript">
	function backToCommunityList() {
	    window.location.href = '<c:url value="/mycommunities.html"/>';
	} 
</script>
<div>
    <h2 class="dark">View community</h2>
</div>
<div class="rbDiv contentDiv">
    <div class="span-12">
        <div class="span-2 labelDiv"><img class="userPic" src='<c:url value="/communities/images/${community.id}.jpg"/>'/></div>
        <div style="padding-top: 4px; padding-bottom: 5px; margin-bottom: 5px;" class="span-10 last formSectionDiv">
            <div class="span-7 communityTitle">${community.name}</div>
            <div class="span-3 last right">Members: <span class="communityCount">${community.membersCount}</span></div>
        </div>
        <span>Creator: <a class="noline" href='<c:url value="/viewprofile/${community.ownerId}.html"/>'>${community.owner.fullName}</a></span>
    </div>
    <div class="span-12 formSectionSlimDiv" style="padding-top: 10px;">
        <div class="span-2 labelDiv">&nbsp;</div>
        <div class="span-10 last formSectionDiv">
            ${community.description}
        </div>
    </div>
    <div class="span-12 formSectionSlimDiv">
        <div class="span-2 labelDiv"></div>
        <div class="span-10 last">
            <form action='<c:url value="/joincommunity.html"/>' method="post">
                <input type="hidden" name="communityId" value="${community.id}"/>
                <button class="greenButton90" onclick="submit();">Join</button>
            </form>
        </div>
    </div>
    <p>&nbsp;</p>
</div>