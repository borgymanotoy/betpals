<%@include file="includes.jsp"%>
<div id="searchFriendsDiv">
    <h2>Search result</h2>
    <form action='<c:url value="/searchfriends.html"/>' name="search_form" method="post">
        <input type="text" name="query" value="Search again" onfocus="this.value=''"/>
        <button id="searchButton" onclick="document.search_form.submit();">&nbsp;</button>
    </form>
</div>
<div class="ui-widget-content ui-corner-all contentDiv">
    <h4>Friends</h4>
    <ul id="friendList">
    <c:forEach items="${friendsList}" var="friend">
        <li>
	        <div class="span-12">
	            <div class="span-2 userPicDiv">
	                <img class="userPic" src='<c:url value="/images/users/${friend.userId}.jpg"/>'/>
	            </div>
	            <div class="span-10 last userNameDiv">
	                <div class="span-8">
	                    <h5>${friend.fullName}</h5>
	                </div>
	                <div class="span-2 last right userControlDiv">
				        <form action='<c:url value="/addfriend.html"/>' method="post">
				            <input type="hidden" name="friendId" value="${friend.userId}"/>
				            <button class="addFriendButton" onclick="submit();">Add</button>
				        </form>
	                </div>
	            </div>
	        </div>
        </li>
    </c:forEach>
    </ul>
    <p>&nbsp;</p>
</div>