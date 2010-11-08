<%@include file="includes.jsp"%>
<div id="searchFriendsDiv">
    <h2>Friends, communities and groups</h2>
    <form action='<c:url value="/searchfriends.html"/>' name="search_form" method="post">
        <input type="text" name="query" value="Search new friends and communities" onfocus="this.value=''"/>
        <button id="searchButton" onclick="document.search_form.submit();">&nbsp;</button>
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
    			        <img class="userPic" src='<c:url value="/images/users/${friend.userId}.jpg"/>'/>
			        </div>
			        <div class="span-10 last userNameDiv">
				        <div class="span-9">
		       	            <h5>${friend.fullName}</h5>
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
    </div>
    <div id="groups" class="contentDiv">
        <div class="right"><a class="blueDotLink" href='<c:url value="/creategroup.html"/>'>Create new group</a></div>
        <ul id="groupList">
        
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
