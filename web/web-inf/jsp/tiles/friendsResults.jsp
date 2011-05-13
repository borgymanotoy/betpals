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
    });
    
    function searchFriends() {
        var queryValue = jQuery("#searchFriendsField").val();
        if ( queryValue != "" && queryValue != jQuery("#searchFriendsField").attr('title')) {
            document.search_form.submit();
        }
    }
</script>

<div id="searchFriendsDiv">
    <h2><spring:message code="friends.search.result.title"/></h2>
    <form action='<c:url value="/searchfriends.html"/>' name="search_form" method="post">
        <input id="searchFriendsField" type="text" name="query" value="" title='<spring:message code="friends.search.again.placeholder"/>'/>
        <button id="searchButton" onclick="searchFriends(); return false;">&nbsp;</button>
    </form>
</div>
<div class="rbDiv contentDiv">
    <c:if test="${not empty friendsList}">
    <h4><spring:message code="friends.search.results.friends.header"/></h4>
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
	                <div class="span-8">
	                    <h5><a class="noline" href='<c:url value="/viewprofile/${friend.userId}.html"/>'>${friend.fullName}</a></h5>
	                </div>
	                <div class="span-2 last right userControlDiv">
				        <form action='<c:url value="/invitefriend.html"/>' method="post">
				            <input type="hidden" name="friendId" value="${friend.userId}"/>
				            <button class="addFriendButton" onclick="submit();"><spring:message code="button.add"/></button>
				        </form>
	                </div>
	            </div>
	        </div>
        </li>
    </c:forEach>
    </ul>
    <p>&nbsp;</p>
    </c:if>
    <c:if test="${not empty communityList}">
    <h4><spring:message code="friends.search.results.communities.header"/></h4>
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
	                <div class="span-8">
	                    <h5><a class="noline" href='<c:url value="/communities/${community.id}.html"/>'>${community.name}</a></h5>
	                </div>
	                <div class="span-2 last right userControlDiv">
				        <form action='<c:url value="/joincommunity.html"/>' method="post">
				            <input type="hidden" name="communityId" value="${community.id}"/>
				            <button class="addFriendButton" onclick="submit();"><spring:message code="button.join"/></button>
				        </form>
	                </div>
	            </div>
	        </div>
        </li>
    </c:forEach>
    </ul>
    </c:if>
    <p>&nbsp;</p>
</div>