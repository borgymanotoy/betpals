<%@include file="includes.jsp"%>
<div>
    <h2 class="dark"><spring:message code="request.list.title"/></h2>
</div>
<div class="rbDiv contentDiv">
    <h4><spring:message code="request.list.friends.header"/></h4>
    <ul id="friendList">
    <c:forEach items="${userRequestFriendList}" var="userRequest">
        <li>
	        <div class="span-12">
	            <div class="span-2 userPicDiv">
	               <a href='<c:url value="/viewprofile/${userRequest.ownerId}.html"/>'>
	                <img class="userPic" src='<c:url value="/images/users/${userRequest.ownerId}.jpg"/>'/>
	               </a>
	            </div>
	            <div class="span-10 last userNameDiv">
	                <div class="span-6">
	                    <h5><a class="noline" href='<c:url value="/viewprofile/${userRequest.ownerId}.html"/>'>${userRequest.ownerName}</a></h5>
	                    <spring:message code="request.user.wants.to.be.your.friend"/>
	                </div>
	                <div class="span-2 right userControlDiv">
				        <form action='<c:url value="/acceptrequest.html"/>' method="post">
				            <input type="hidden" name="friendId" value="${userRequest.ownerId}"/>
				            <input type="hidden" name="requestId" value="${userRequest.id}"/>
				            <button class="addFriendButton" onclick="submit();"><spring:message code="button.accept"/></button>
				        </form>
	                </div>
	                <div class="span-2 last right userControlDiv">
				        <form action='<c:url value="/rejectrequest.html"/>' method="post">
				            <input type="hidden" name="friendId" value="${userRequest.ownerId}"/>
				            <input type="hidden" name="requestId" value="${userRequest.id}"/>
				            <button class="addFriendButton" onclick="submit();"><spring:message code="button.reject"/></button>
				        </form>
	                </div>
	            </div>
	        </div>
        </li>
    </c:forEach>
    </ul>
    <p>&nbsp;</p>
    <c:if test="${not empty userRequestCommunityList}">
    <h4><spring:message code="request.list.communities.header"/></h4>
    <ul id="friendList">
    <c:forEach items="${userRequestCommunityList}" var="userRequest">
        <li>
	        <div class="span-12">
	            <div class="span-2 userPicDiv">
	               <a href='<c:url value="/viewprofile/${userRequest.ownerId}.html"/>'>
	                <img class="userPic" src='<c:url value="/images/users/${userRequest.ownerId}.jpg"/>'/>
	               </a>
	            </div>
	            <div class="span-10 last userNameDiv">
	                <div class="span-6">
	                    <h5><a class="noline" href='<c:url value="/viewprofile/${userRequest.ownerId}.html"/>'>${userRequest.ownerName}</a></h5>
	                    <spring:message code="request.user.wants.to.join.community"/> ${userRequest.extensionName}
	                </div>
	                <div class="span-2 right userControlDiv">
				        <form action='<c:url value="/acceptrequest.html"/>' method="post">
				            <input type="hidden" name="requestId" value="${userRequest.id}"/>
				            <button class="addFriendButton" onclick="submit();"><spring:message code="button.accept"/></button>
				        </form>
	                </div>
	                <div class="span-2 last right userControlDiv">
				        <form action='<c:url value="/rejectrequest.html"/>' method="post">
				            <input type="hidden" name="requestId" value="${userRequest.id}"/>
				            <button class="addFriendButton" onclick="submit();"><spring:message code="button.reject"/></button>
				        </form>
	                </div>
	            </div>
	        </div>
        </li>
    </c:forEach>
    </ul>
    <p>&nbsp;</p>
    </c:if>
</div>