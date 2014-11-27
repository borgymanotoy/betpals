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
		} else if (selectedTab == "communities") {
			jQuery("#tabs").tabs("select", 2);
		}
		
        jQuery("#confirmDeleteFriend").dialog({
            resizable: false,
            autoOpen: false,
            height:160,
            modal: true,
            buttons: {
                "<spring:message code='button.delete'/>": function() {
                    jQuery('#deleteFriendForm').submit();
                    $( this ).dialog( "close" );
                },
                "<spring:message code='button.cancel'/>": function() {
                    $( this ).dialog( "close" );
                }
            }
        });

		
	});
	
	function searchFriends() {
		var queryValue = jQuery("#searchFriendsField").val();
		if ( queryValue != "" && queryValue != jQuery("#searchFriendsField").attr('title')) {
		    document.search_form.submit();
		}
	}
	
    function deleteFriend(friendId) {
        jQuery("#friendToDelete").val(friendId);
        jQuery("#confirmDeleteFriend").dialog('open');
    } 
	
</script>
<div id="searchFriendsDiv" align="center">
    <h2 align="left"><spring:message code="friends.title"/></h2>
    <form action='<c:url value="/searchfriends.html"/>' name="search_form" method="post">
		<table id="tblSearchFriends">
			<tr>
				<td id="tdSearchInput"><input id="searchFriendsField" type="text" name="query" value="" title="<spring:message code="friends.search.placeholder"/>"/></td>
				<td id="tdSearchButton"><button id="searchButton" onclick="searchFriends(); return false;"></button></td>
			</tr>
		</table>
    </form>
</div>
<div id="tabs">
    <ul class="palsTabs">
        <li><a href="#friends"><spring:message code="friends.tab.friends"/></a></li>
        <li><a href="#groups"><spring:message code="friends.tab.groups"/></a></li>
        <li><a href="#communities"><spring:message code="friends.tab.communities"/></a></li>
    </ul>
    <div id="friends" class="contentDiv">
	    <ul id="friendList">
	    <c:forEach items="${friendsList}" var="friend">
	        <li>
		        <div class="span-12">
			        <div class="span-2 userPicDiv">
			         <a href='<c:url value="/viewprofile/${friend.userId}.html"/>'>
    			        <img class="userPic" src='<c:url value="/users/images/${friend.userId}.jpeg"/>'/>
    			     </a>
			        </div>
			        <div class="span-10 last userNameDiv">
				        <div class="span-9">
		       	            <h5><a class="noline" href='<c:url value="/viewprofile/${friend.userId}.html"/>'>${friend.fullName}</a></h5>
			            </div>
			            <div class="span-1 last right">
				            <button class="deleteButton" onclick="deleteFriend(${friend.userId});">&nbsp;</button>
				        </div>
			        </div>
		        </div>
	        </li>
	    </c:forEach>
	    </ul>
	    <p>&nbsp;</p>
        <c:if test="${not empty userRequestFriendList}">
        <h4><spring:message code="friends.awaiting.confirmation"/></h4>
        <ul id="friendList">
        <c:forEach items="${userRequestFriendList}" var="userRequest">
            <li>
                <div class="span-12">
                    <div class="span-2 userPicDiv">
                        <a href='<c:url value="/viewprofile/${userRequest.inviteeId}.html"/>'>
                            <img class="userPic" src='<c:url value="/users/images/${userRequest.inviteeId}.jpeg"/>'/>
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
        <div class="right"><a class="blueDotLink" href='<c:url value="/editgroup.html"/>'><spring:message code="friends.create.group"/></a></div>
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
        <div class="right"><a class="blueDotLink" href='<c:url value="/editcommunity.html"/>'><spring:message code="friends.create.community"/></a></div>
        <br/>
        <ul id="friendList">
        <c:forEach items="${communityList}" var="community">
            <li>
                <div class="span-12">
                    <div class="span-2 userPicDiv">
                     <a href='<c:url value="/communities/${community.id}.html"/>'>
                        <img class="userPic" src='<c:url value="/communities/images/${community.id}.jpg"/>'/>
                     </a>
                    </div>
                    <div class="span-10 last userNameDiv">
                        <h5><a class="noline" href='<c:url value="/communities/${community.id}.html"/>'>${community.name}</a></h5>
                    </div>
                </div>
            </li>
        </c:forEach>
        </ul>
        <p>&nbsp;</p>
        <c:if test="${not empty userRequestCommunityList}">
        <h4><spring:message code="friends.awaiting.confirmation"/></h4>
        <ul id="friendList">
        <c:forEach items="${userRequestCommunityList}" var="userRequest">
            <li>
                <div class="span-12">
                    <div class="span-2 userPicDiv">
                        <a href='<c:url value="/communities/${userRequest.extensionId}.html"/>'>
                            <img class="userPic" src='<c:url value="/communities/images/${userRequest.extensionId}.jpg"/>'/>
                        </a>
                    </div>
                    <div class="span-10 last userNameDiv">
                        <div class="span-9">
                            <h5><a class="noline" href='<c:url value="/communities/${userRequest.extensionId}.html"/>'>${userRequest.extensionName}</a></h5>
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
</div>
<div id="confirmDeleteFriend" title="<spring:message code='confirmation.delete.friend.title'/>">
    <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span><spring:message code="confirmation.delete.friend.text"/></p>
</div>
<form action='<c:url value="/deletefriend.html"/>' method="post" id="deleteFriendForm">
    <input type="hidden" name="friendId" value="" id="friendToDelete"/>
</form>

