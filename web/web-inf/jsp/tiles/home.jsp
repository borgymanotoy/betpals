<%@include file="includes.jsp"%>
<div id="quickCompetitionDiv">
    <h2>Quick competition</h2>
    <c:choose>
    <c:when test="${not empty accounts}">
        <form action='<c:url value="/quickcompetitionview.html"/>' method="post">
	    <span style="padding-left: 16px;">I bet </span>
	    <div class="formInlineDiv">
	    <input id="quickCompetitionStake" type="text" name="stake"/>
	    <select name="accountId">
	    <c:forEach items="${accounts}" var="account">
	        <option value="${account.id}">${account.currency}</option>
	    </c:forEach>
	    </select> 
	    </div>
	    <span>that </span> 
	    <input id="quickCompetitionAlternative" type="text" name="alternative"/>
	    <input type="submit" value="Next" id="quickCompetitionButton"/>
        </form>
    </c:when>
    <c:otherwise><span style="padding-left: 16px;">You need a valid <a href='<c:url value="/addaccount.html"/>'>account</a> to create competition. </span></c:otherwise>
    </c:choose>
</div>
<div id="wallInputDiv">
    <h3>Activities</h3>
    <form action='<c:url value="/activities.html"/>' method="post">
        <input type="text" name="message" value="What's on your mind?" onfocus="this.value=''"/>
    </form>
</div>
<div class="ui-widget-content ui-corner-all">
    <ul id="activitiesList">
    <c:forEach items="${activitiesList}" var="activity">
        <li>
	        <table>
	           <tr>
	            <td class="userPicCell">
	                <img class="userPic" src='<c:url value="/images/users/${activity.ownerId}.jpg"/>'/>
	            </td>
	            <td class="">
	               <h5>${activity.ownerName}</h5>
	               <p>${activity.message}</p>
	               <form name="likeForm_${activity.id}" action='<c:url value="/activitylike.html"/>' method="post">
                   <input type="hidden" name="activityId" value="${activity.id}"/>
                   <table class="wallControlTable">
                    <tr>
                        <td class="nowrap">${activity.timeSinceCreated}</td>
                        <td class="nowrap">&nbsp;-&nbsp;</td>
                        <td><a href="" onclick="document.likeForm_${activity.id}.submit();">Like</a></td>
                        <td width="100%">&nbsp;</td>
                    </tr>
                   </table>
	               </form>
	               <c:forEach items="${activity.likes}" var="like">
	               <table class="wallLikeTable">
	                   <tr class="topTr"><td colspan="2"></td></tr>
	                   <tr>
		                   <td class="nowrap">
			                   <h5>${like.ownerName}:</h5>
		                   </td>
		                   <td width="100%">
			                   likes this.
		                   </td>
	                   </tr>
	                   <tr class="bottomTr"><td colspan="2" class="nopadding"></td></tr>
	               </table>
	               </c:forEach>
	               <c:forEach items="${activity.comments}" var="comment">
	               <table class="wallCommentTable">
	                   <tr class="topTr"><td colspan="2"></td></tr>
	                   <tr>
			               <td class="userPicCellComment">
			                   <img class="userPic" src='<c:url value="/images/users/${comment.ownerId}.jpg"/>'/>
			               </td>
		                   <td class="">
			                   <h5>${comment.ownerName}</h5>
			                   <p>${comment.message}</p>
		                   </td>
	                   </tr>
	                   <tr class="bottomTr"><td colspan="2" class="nopadding"></td></tr>
	               </table>
	               </c:forEach>
	               
		           <form action='<c:url value="/activitycomment.html"/>' method="post">
	                   <input type="hidden" name="activityId" value="${activity.id}"/>
                   <table class="wallCommentTable">
                       <tr class="topTr"><td colspan="2"></td></tr>
                       <tr>
                        <td>
		                  <input type="text" name="message" value="Write a comment" onfocus="this.value=''"/>
		                </td>         
		                <td class="nopadding"><button class="commentButton" onclick="submit();">&nbsp;</button></td>  
                       </tr>
                       <tr class="bottomTr"><td colspan="2" class="nopadding"></td></tr>
                   </table>
		           </form>
	            </td>
	            </tr>
	        </table>
        </li>
    </c:forEach>
    </ul>
    <p>&nbsp;</p>
</div>