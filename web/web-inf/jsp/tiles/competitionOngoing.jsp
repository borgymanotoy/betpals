<%@include file="includes.jsp"%>
<script type="text/javascript">
jQuery(document).ready(function() {
    jQuery(".activityField").each(function() {
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
    jQuery("#activityForm").submit(function() {
        var field = jQuery(this).find('.activityField');
        var queryValue = jQuery.trim(field.val());
        if ( queryValue == "" || queryValue == field.attr('title')) {
            return false;
        }
    }); 
    
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
    <h2 class="dark"><spring:message code="competition.ongoing.title"/></h2>
</div>
<div class="rbDiv contentDiv">
    <div class="span-12">
        <div class="span-2 userPicDiv">
            <img class="userPic" src='<c:url value="/competition/images/${competition.id}.jpeg"/>'/>
        </div>
        <div class="span-10 last invitationDiv formSectionDiv">
            <h5>${competition.name}</h5>
            <p>${competition.description}</p>
        </div>
    </div>
    <div>
        <spring:message code="competition.competition.deadline"/>: <span class="bold"><fmt:formatDate value="${competition.deadline}" pattern="yyyy-MM-dd HH:mm"/></span>
    </div>
    <div>
        <spring:message code="competition.settling.deadline"/>: <span class="bold"><fmt:formatDate value="${competition.settlingDeadline}" pattern="yyyy-MM-dd HH:mm"/></span>
    </div>
    <p>&nbsp;</p>
    <h4><spring:message code="competition.alternatives.header"/></h4>
    <div class="formSectionDiv">
        <ul id="friendList">
        <c:forEach items="${competition.defaultEvent.alternatives}" var="altFromList" varStatus="status">
        <li>
            <div class="span-12">
                <div class="span-2 userPicDiv">
                    <img class="userPic" src='<c:url value="/competition/images/alt_${altFromList.id}.jpeg"/>'/>
                </div>
                <c:choose>
                <c:when test="${status.last}">
                <div class="span-10 last altListDetails noborder">
                </c:when>
                <c:otherwise>
                <div class="span-10 last altListDetails">
                </c:otherwise>
                </c:choose>
                <div class="span-5">
                    ${altFromList.name}<br/>
                    <c:if test="${empty altFromList.bets}"><span class="detailTitle"><spring:message code="competition.no.bets"/><br/></span></c:if>
                    <c:forEach items="${altFromList.bets}" var="bet">
                        <span class="detailTitle"><c:choose><c:when test="${bet.ownerId == competition.ownerId}"><spring:message code="competition.you"/></c:when><c:otherwise><a href='<c:url value="/viewprofile/${bet.ownerId}.html"/>'>${bet.ownerName}</a></c:otherwise></c:choose> (${bet.stake} ${competition.currency})</span><br/>
                    </c:forEach>
                    <span class="detailTitle">${altFromList.turnover} ${competition.currency} <spring:message code="competition.on.this.alternative"/></span>
                </div>
                <c:choose>
                  <c:when test="${altFromList.taken}">
                    <div class="span-5 last right">
                          <spring:message code="alternative.already.taken.by"/> <br/><a href='<c:url value="/viewprofile/${altFromList.participantId}.html"/>'>${altFromList.participantName}</a>
                    </div>
                  </c:when>
                  <c:otherwise>
                    <form action='<c:url value="/placeanotherbet.html"/>' method="post">
                    <div class="span-5 last right">
                        <c:choose>
                        <c:when test="${competition.competitionType == 'POOL_BETTING'}">
                            <input type="text" id="myStake" size="3" name="stake"/> ${competition.currency}&nbsp;
                        </c:when>
                        <c:otherwise>
                            ${competition.fixedStake} ${competition.currency}&nbsp;
                        </c:otherwise>
                        </c:choose>
                        <input type="hidden" name="competitionId" value="${competition.id}"/>
                        <input type="hidden" name="alternativeId" value="${altFromList.id}"/>
                        <input type="submit" class="greenButton90" value="<spring:message code='button.place.bet'/>"/>
                    </div>
                    </form>
                  </c:otherwise>
                </c:choose>
                </div>
            </div>
        </li>
        </c:forEach>
        </ul>
        &nbsp;
    </div>
    <p>&nbsp;</p>
    <h4><spring:message code="competition.activities.header"/></h4>
    <div id="activities">
        <form id="activityForm" action='<c:url value="/activities.html"/>' method="post">
            <input type="hidden" name="competitionId" value="${competition.id}"/>
            <input class="wallInputActivityField activityField" type="text" name="message" value="" title="<spring:message code='wall.activity.placeholder'/>"/>
        </form>
        <ul id="communityActivitiesList">
        <c:forEach items="${activityList}" var="activity">
            <li>
                <table>
                   <tr>
                    <td class="userPicCell">
                        <a href='<c:url value="/viewprofile/${activity.ownerId}.html"/>'>
                           <img class="userPic" src='<c:url value="/users/images/${activity.ownerId}.jpeg"/>'/>
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
                                <c:when test="${user.userId == activity.ownerId}"><a href="" onclick="document.activityForm_${activity.id}.submit();"><spring:message code="button.delete"/></a></c:when>
                                <c:otherwise><a href="" onclick="document.likeForm_${activity.id}.submit();"><spring:message code="button.like"/></a></c:otherwise>
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
                                   <spring:message code="wall.likes.this"/>
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
                                   <img class="userPic" src='<c:url value="/users/images/${comment.ownerId}.jpeg"/>'/>
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
                              <input type="text" class="commentField" name="message" value="" title="<spring:message code='wall.comment.placeholder'/>"/>
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
                    <button class="blueButton90" onclick="prevPageForm.submit();"><spring:message code="button.previous"/></button>
                </c:if>
            </div>
            <div class="span-6 append-1 last right">
                <c:if test="${currentPage > 0}">
                    <form name="nextPageForm" action='<c:url value="/home.html"/>' method="post">
                        <input type="hidden" name="pageId" value="${currentPage - 1}"/>
                    </form>
                    <button class="greenButton90" onclick="nextPageForm.submit();"><spring:message code="button.next"/></button>
                </c:if>
            </div>
        </div>
        </c:if>
        <p>&nbsp;</p>
       &nbsp;
    </div>
</div>

