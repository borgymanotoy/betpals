<%@include file="includes.jsp"%>
<script type="text/javascript">
	jQuery(document).ready(function() {
		var searchField = jQuery("#searchFriendsField"); 
		searchField.val(searchField.attr('title'));
		searchField.focus( function() {
			jQuery(this).val("");
		});
		searchField.blur( function() {
			if (jQuery(this).val() == "") {
				jQuery(this).val(jQuery(this).attr('title'));
			}
		});
		
		var selectedTab = "${tab}";
		if (selectedTab == "groups") {
			jQuery("#tabs").tabs("select", 1);
		}
	});
	
	function searchFriends() {
		var queryValue = jQuery("#searchFriendsField").val();
		if ( queryValue != "" && queryValue != jQuery("#searchFriendsField").attr('title')) {
		    document.search_form.submit();
		}
	}
</script>
<div id="searchFriendsDiv">
    <h2>Friends, communities and groups</h2>
    <form action='<c:url value="/searchfriends.html"/>' name="search_form" method="post">
        <input id="searchFriendsField" type="text" name="query" value="" title="Search new friends and communities"/>
        <button id="searchButton" onclick="searchFriends(); return false;">&nbsp;</button>
    </form>
</div>
<div id="tabs">
    <ul class="palsTabs">
        <li><a href="#friends">My friends</a></li>
        <li><a href="#groups">My groups</a></li>
        <li><a href="#communities">My communities</a></li>
    </ul>
    <div id="friends" class="contentDiv">
	    <ul id="friendList">
	    <c:forEach items="${friendsList}" var="friend">
	        <li>
		        <div class="span-12">
			        <div class="span-2 userPicDiv">
			         <a href='<c:url value="/viewprofile/${friend.userId}.html"/>'>
    			        <img class="userPic" src='<c:url value="/images/users/${friend.userId}.jpg"/>'/>
    			     </a>
			        </div>
			        <div class="span-10 last userNameDiv">
				        <div class="span-9">
		       	            <h5><a class="noline" href='<c:url value="/viewprofile/${friend.userId}.html"/>'>${friend.fullName}</a></h5>
			            </div>
			            <div class="span-1 last right">
					        <form action='<c:url value="/deletefriend.html"/>' method="post">
					            <input type="hidden" name="friendId" value="${friend.userId}"/>
					            <button class="deleteButton" onclick="submit();">&nbsp;</button>
					        </form>
				        </div>
			        </div>
		        </div>
	        </li>
	    </c:forEach>
	    </ul>
	    <p>&nbsp;</p>
        <c:if test="${not empty userRequestList}">
        <h4>Awaiting confirmation</h4>
        <ul id="friendList">
        <c:forEach items="${userRequestList}" var="userRequest">
            <li>
                <div class="span-12">
                    <div class="span-2 userPicDiv">
                        <a href='<c:url value="/viewprofile/${userRequest.inviteeId}.html"/>'>
                            <img class="userPic" src='<c:url value="/images/users/${userRequest.inviteeId}.jpg"/>'/>
                        </a>
                    </div>
                    <div class="span-10 last userNameDiv">
                        <div class="span-9">
                            <h5><a class="noline" href='<c:url value="/viewprofile/${userRequest.inviteeId}.html"/>'>${userRequest.inviteeName}</a></h5>
                        </div>
                        <div class="span-1 last right">
                        &nbsp;
                        </div>
                    </div>
                </div>
            </li>
        </c:forEach>
        </ul>
        <p>&nbsp;</p>
        </c:if>
    </div>
    <div id="groups" class="contentDiv">
        <div class="right"><a class="blueDotLink" href='<c:url value="/editgroup.html"/>'>Create new group</a></div>
        <ul id="friendList">
        <c:forEach items="${groupList}" var="group">
            <li>
                <div class="span-12 userNameDiv">
                    <div class="span-11">
                        <h5><a class="noline" href='<c:url value="/editgroup.html?groupId=${group.id}"/>'>${group.name}</a></h5>
                    </div>
                    <div class="span-1 last right">
                        <form action='<c:url value="/deletegroup.html"/>' method="post">
                            <input type="hidden" name="groupId" value="${group.id}"/>
                            <button class="deleteButton" onclick="submit();">&nbsp;</button>
                        </form>
                    </div>
                </div>
            </li>
        </c:forEach>
        </ul>
        <p>&nbsp;</p>
    </div>
    <div id="communities" class="contentDiv">
        <div class="right"><a class="blueDotLink" href='<c:url value="/createcommunity.html"/>'>Create new community</a></div>
        <ul id="communityList">
        
        </ul>
        <p>&nbsp;</p>
    </div>
</div>
