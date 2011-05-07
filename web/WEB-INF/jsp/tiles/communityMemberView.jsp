<%@include file="includes.jsp"%>
<script type="text/javascript">
	function backToCommunityList() {
	    window.location.href = '<c:url value="/mycommunities.html"/>';
	} 
    jQuery(document).ready(function() {
        jQuery(".commentField").each(function() {
            jQuery(this).val(jQuery(this).attr('title'));
            jQuery(this).focus( function() {
                jQuery(this).val("");
            });
            jQuery(this).blur( function() {
                if (jQuery.trim(jQuery(this).val()) == "") {
                    jQuery(this).val(jQuery(this).attr('title'));
                }
            });
        }); 
        jQuery(".commentButton").each(function() {
            jQuery(this).click( function() {
                var form = jQuery(this).parents('form');
                var field = form.find('.commentField');
                var queryValue = jQuery.trim(field.val());
                if ( queryValue != "" && queryValue != field.attr('title')) {
                    form.submit();
                }
                return false;
            });
        }); 
    });

</script>
<div>
    <h2 class="dark">View community</h2>
</div>
<div id="tabs" class="rbDiv">
    <div class="contentDiv">
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
	        <c:choose>
            <c:when test="${user.userId == community.ownerId}">
                <form action='<c:url value="/editcommunity.html"/>'>
                    <input type="hidden" name="communityId" value="${community.id}"/>
                    <button class="greenButton90" onclick="submit();">Edit</button>
                </form>
	        </c:when>
	        <c:otherwise>
	            <form action='<c:url value="/unjoincommunity.html"/>' method="post">
	                <input type="hidden" name="communityId" value="${community.id}"/>
	                <button class="greenButton90" onclick="submit();">Unjoin</button>
	            </form>
	        </c:otherwise>
	        </c:choose>
	        </div>
	    </div>
	    <p>&nbsp;</p>
    </div>
	<ul class="palsTabs">
	    <li><a href="#activities">Activities</a></li>
	    <li><a href="#members">Members</a></li>
	    <li><a href="#competitions">Competitions</a></li>
	</ul>
	<div id="activities" class="contentDiv">
	    <form action='<c:url value="/activities.html"/>' method="post">
	        <input type="hidden" name="communityId" value="${community.id}"/>
	        <input class="wallInputActivityField" type="text" name="message" value="What's on your mind?" onfocus="this.value=''"/>
	    </form>
	    <ul id="communityActivitiesList">
	    <c:forEach items="${activityList}" var="activity">
	        <li>
	            <table>
	               <tr>
	                <td class="userPicCell">
	                    <a href='<c:url value="/viewprofile/${activity.ownerId}.html"/>'>
	                       <img class="userPic" src='<c:url value="/images/users/${activity.ownerId}.jpg"/>'/>
	                    </a>
	                </td>
	                <td class="">
	                   <h5><a class="noline" href='<c:url value="/viewprofile/${activity.ownerId}.html"/>'>${activity.ownerName}</a></h5>
	                   <p>${activity.message}</p>
	                   <form name="likeForm_${activity.id}" action='<c:url value="/activitylike.html"/>' method="post">
	                       <input type="hidden" name="activityId" value="${activity.id}"/>
	                   </form>
	                   <form name="activityForm_${activity.id}" action='<c:url value="/removeactivity.html"/>' method="post">
	                       <input type="hidden" name="activityId" value="${activity.id}"/>
	                   </form>
	                   <table class="wallControlTable">
	                    <tr>
	                        <td class="nowrap">${activity.timeSinceCreated}</td>
	                        <td class="nowrap">&nbsp;-&nbsp;</td>
	                        <td>
	                        <c:choose>
	                            <c:when test="${user.userId == activity.ownerId}"><a href="" onclick="document.activityForm_${activity.id}.submit();">Delete</a></c:when>
	                            <c:otherwise><a href="" onclick="document.likeForm_${activity.id}.submit();">Like</a></c:otherwise>
	                        </c:choose>
	                        </td>
	                        <td width="100%">&nbsp;</td>
	                    </tr>
	                   </table>
	                   <c:forEach items="${activity.likes}" var="like">
	                   <table class="wallLikeTable">
	                       <tr class="topTr"><td colspan="3"></td></tr>
	                       <tr>
	                           <td class="nowrap">
	                               <h5><a class="noline" href='<c:url value="/viewprofile/${like.ownerId}.html"/>'>${like.ownerName}</a>:</h5>
	                           </td>
	                           <td width="100%">
	                               likes this.
	                           </td>
	                           <td class="right">
	                           <c:if test="${user.userId == like.ownerId}">
	                               <form action='<c:url value="/removelike.html"/>' method="post">
	                                   <input type="hidden" name="likeId" value="${like.id}"/>
	                                   <button class="deleteButton" onclick="submit();">&nbsp;</button>
	                               </form>
	                           </c:if>
	                           </td>
	                       </tr>
	                       <tr class="bottomTr"><td colspan="3" class="nopadding"></td></tr>
	                   </table>
	                   </c:forEach>
	                   <c:forEach items="${activity.comments}" var="comment">
	                   <table class="wallCommentTable">
	                       <tr class="topTr"><td colspan="3"></td></tr>
	                       <tr>
	                           <td class="userPicCellComment">
	                             <a href='<c:url value="/viewprofile/${comment.ownerId}.html"/>'>
	                               <img class="userPic" src='<c:url value="/images/users/${comment.ownerId}.jpg"/>'/>
	                             </a>  
	                           </td>
	                           <td class="">
	                               <h5><a class="noline" href='<c:url value="/viewprofile/${comment.ownerId}.html"/>'>${comment.ownerName}</a></h5>
	                               <p>${comment.message}</p>
	                           </td>
	                           <td class="right">
	                           <c:if test="${user.userId == comment.ownerId}">
	                               <form action='<c:url value="/removecomment.html"/>' method="post">
	                                   <input type="hidden" name="commentId" value="${comment.id}"/>
	                                   <button class="deleteButton" onclick="submit();">&nbsp;</button>
	                               </form>
	                           </c:if>
	                           </td>
	                       </tr>
	                       <tr class="bottomTr"><td colspan="3" class="nopadding"></td></tr>
	                   </table>
	                   </c:forEach>
	                   
	                   <form action='<c:url value="/activitycomment.html"/>' method="post">
	                       <input type="hidden" name="activityId" value="${activity.id}"/>
	                   <table class="wallCommentTable">
	                       <tr class="topTr"><td colspan="2"></td></tr>
	                       <tr>
	                        <td>
	                          <input type="text" class="commentField" name="message" value="" title="Write a comment"/>
	                        </td>         
	                        <td class="nopadding"><button class="commentButton">&nbsp;</button></td>  
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
	    <c:if test="${numberOfPages > 0}">
	    <div class="span-14">
	        <div class="prepend-1 span-6 left">
	            <c:if test="${currentPage < numberOfPages}">
	                <form name="prevPageForm" action='<c:url value="/home.html"/>' method="post">
	                    <input type="hidden" name="pageId" value="${currentPage + 1}"/>
	                </form>
	                <button class="blueButton90" onclick="prevPageForm.submit();">Previous</button>
	            </c:if>
	        </div>
	        <div class="span-6 append-1 last right">
	            <c:if test="${currentPage > 0}">
	                <form name="nextPageForm" action='<c:url value="/home.html"/>' method="post">
	                    <input type="hidden" name="pageId" value="${currentPage - 1}"/>
	                </form>
	                <button class="greenButton90" onclick="nextPageForm.submit();">Next</button>
	            </c:if>
	        </div>
	    </div>
	    </c:if>
        <p>&nbsp;</p>
	   &nbsp;
	</div>
	<div id="members" class="contentDiv">
        <ul id="friendList">
        <c:forEach items="${community.members}" var="friend">
            <li>
                <div class="span-12">
                    <div class="span-2 userPicDiv">
                     <a href='<c:url value="/viewprofile/${friend.userId}.html"/>'>
                        <img class="userPic" src='<c:url value="/images/users/${friend.userId}.jpg"/>'/>
                     </a>
                    </div>
                    <div class="span-10 last userNameDiv">
                         <h5><a class="noline" href='<c:url value="/viewprofile/${friend.userId}.html"/>'>${friend.fullName}</a></h5>
                    </div>
                </div>
            </li>
        </c:forEach>
        </ul>
        <p>&nbsp;</p>
	</div>
	<div id="competitions" class="contentDiv">&nbsp;</div>
    <p>&nbsp;</p>
</div>
