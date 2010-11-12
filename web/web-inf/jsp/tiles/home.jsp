<%@include file="includes.jsp"%>
<div id="quickCompetitionDiv">
    <h2>Quick competition</h2>
</div>
<div id="wallInputDiv">
    <h3>Activities</h3>
    <form action='<c:url value="/activities.html"/>' method="post">
        <input type="text" name="message" value="What's on your mind?" onfocus="this.value=''"/>
    </form>
</div>
<div class="ui-widget-content ui-corner-all contentDiv">
    <ul id="activitiesList">
    <c:forEach items="${activitiesList}" var="activity">
        <li>
	        <div class="span-12">
	            <div class="span-2 userPicDiv">
	                <img class="userPic" src='<c:url value="/images/users/${activity.ownerId}.jpg"/>'/>
	            </div>
	            <div class="span-10 last userNameDiv">
	               <h5>${activity.ownerName}</h5>
	               <p>${activity.message}</p>
	               <span>${activity.created}</span> -  
	               <form action='<c:url value="/activitylike.html"/>' method="post">
	                   <input type="hidden" name="activityId" value="${activity.id}"/>
    	               <span onclick="commit();">Like</span>
	               </form>
	               <c:forEach items="${activity.comments}" var="comment">
		               <div class="span-2 userPicDiv">
		                   <img class="userPic" src='<c:url value="/images/users/${comment.ownerId}.jpg"/>'/>
		               </div>
	                   <div class="span-8 last">
		                   <h5>${comment.ownerName}</h5>
		                   <p>${comment.message}</p>
	                   </div>
	               </c:forEach>
	               
		           <form action='<c:url value="/activitycomment.html"/>' method="post">
	                   <input type="hidden" name="activityId" value="${activity.id}"/>
		               <input type="text" name="message" value="Write a comment" onfocus="this.value=''"/>           
		           </form>
	            </div>
	        </div>
        </li>
    </c:forEach>
    </ul>
    <p>&nbsp;</p>
    <p>&nbsp;</p>
    <p>&nbsp;</p>
    <p>&nbsp;</p>
    <p>&nbsp;</p>
</div>