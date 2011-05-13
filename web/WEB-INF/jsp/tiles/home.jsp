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
        
        jQuery("#quickCompetitionStake").each(function() {
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
        jQuery("#quickCompetitionAlternative").each(function() {
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
        jQuery("#quickCompetitionForm").submit(function() {
            var field = jQuery(this).find('#quickCompetitionAlternative');
            var queryValue = jQuery.trim(field.val());
            if ( queryValue == "" || queryValue == field.attr('title')) {
                return false;
            }
        }); 

    });
    
</script>
<div id="quickCompetitionDiv">
    <h2><spring:message code="competition.quick.title"/></h2>
    <c:choose>
    <c:when test="${not empty accounts}">
        <form action='<c:url value="/quickcompetitionview.html"/>' method="post" id="quickCompetitionForm">
	    <span style="padding-left: 16px;"><spring:message code="competition.i.bet"/> </span>
	    <div class="formInlineDiv">
	    <input id="quickCompetitionStake" type="text" name="stake" title="<spring:message code='competition.quick.default.amount'/>"/>
	    <select name="accountId">
	    <c:forEach items="${accounts}" var="account">
	        <option value="${account.id}">${account.currency}</option>
	    </c:forEach>
	    </select> 
	    </div>
	    <span><spring:message code="competition.bet.that"/> </span> 
	    <input id="quickCompetitionAlternative" type="text" name="alternative" title="<spring:message code='competition.quick.placeholder'/>"/>
	    <input type="submit" class="clickable" value="<spring:message code='button.next'/>" id="quickCompetitionButton"/>
        </form>
    </c:when>
    <c:otherwise><span style="padding-left: 16px;"><spring:message code="competition.account.required"/></span></c:otherwise>
    </c:choose>
</div>
<div class="wallInputDiv">
    <h3><spring:message code="activities.title"/></h3>
    <form id="activityForm" action='<c:url value="/activities.html"/>' method="post">
        <input type="text" class="activityField" name="message" value="" title="<spring:message code='wall.activity.placeholder'/>"/>
    </form>
</div>
<div class="rbDiv">
    <ul id="activitiesList">
    <c:forEach items="${activitiesList}" var="activity">
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
		                  <input type="text" class="commentField" name="message" value="" title='<spring:message code="wall.comment.placeholder"/>'/>
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
</div>