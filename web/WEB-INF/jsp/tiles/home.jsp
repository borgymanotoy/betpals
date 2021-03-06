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

        jQuery("#confirmDelete").dialog({
            resizable: false,
            autoOpen: false,
            width: 390,
            height: 175,
            modal: true,
            buttons: {
                "<spring:message code='button.delete'/>": function() {
                    removeActivityAction($( this ).data("activityId"));
                    $( this ).dialog( "close" );
                },
                "<spring:message code='button.cancel'/>": function() {
                    $( this ).dialog( "close" );
                }
            }
        });

    });

	function submitCommentByKeyPress(e, activityIdToComment){
		if(e.keyCode == 13){
			postComment(activityIdToComment);
			return false;
		}
	}

    function postComment(activityIdToComment) {
    	var url = '<c:url value="/activitycomment.html"/>';
    	var commentField = jQuery("#commentMessageField_" + activityIdToComment);
    	var commentMessage = jQuery.trim(commentField.val());
    	
        if ( commentMessage != "" && commentMessage != commentField.attr('title')) {
	    	jQuery.post(url, { activityId: activityIdToComment, message: commentMessage }, function(data) {
	    	     var activityId = data.activityId;
	    	     var viewProfileURL = '<c:url value="/viewprofile/"/>' + data.ownerId + '.html';
	    	     var userPicURL = '<c:url value="/users/images/"/>' + data.ownerId +'.jpeg';
	    	     var removeCommentURL = '<c:url value="/removecomment.html"/>';
	    	     var commentContent = '<table class="wallCommentTable">' +
	                 '<tr class="topTr"><td colspan="3"></td></tr>' +
	                 '<tr>' +
	                     '<td class="userPicCellComment">' +
	                       '<a href="' + viewProfileURL + '">' +
	                         '<img class="userPic" src="' + userPicURL + '"/>' +
	                       '</a>' +  
	                     '</td>' +
	                     '<td class="">' +
	                         '<h5><a class="noline" href="' + viewProfileURL + '">' + data.ownerName + '</a></h5>' +
	                         '<span class="commentDate">' + data.timeSinceCreated + '</span>' +
	                         '<p>' + data.message + '</p>' +
	                     '</td>' +
	                     '<td class="right">' +
	                         '<form action="' + removeCommentURL + '" method="post">' +
	                             '<input type="hidden" name="commentId" value="' + data.id + '"/>' +
	                             '<button class="deleteButton" onclick="submit();">&nbsp;</button>' +
	                         '</form>' +
	                     '</td>' +
	                 '</tr>' +
	                 '<tr class="bottomTr"><td colspan="3" class="nopadding"></td></tr>' +
	             '</table>';
	             
	    	     jQuery(".wallCommentTable", "#activityCell_" + activityId).last().before(commentContent);
	    	   });
        }
		clearCommentField(activityIdToComment);
    }
	var clearCommentField = function(activityIdToClear) {
		jQuery("#commentMessageField_" + activityIdToClear).val("");
	};
	function removeActivity(activityId){
		jQuery("#confirmDelete").data('activityId',activityId).dialog('open');
	}
	function removeActivityAction(activityId){
		jQuery('#activityForm_'+activityId).submit();
	}
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
	        <option value="${account.id}" <c:if test="${account.defaultAccount}">selected="selected"</c:if>>${account.currency}</option>
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
	                   <img class="userPic" src='<c:url value="/users/images/${activity.ownerId}.jpeg"/>'/>
	                </a>
	            </td>
	            <td id="activityCell_${activity.id}" class="">
	               <h5><a class="noline" href='<c:url value="/viewprofile/${activity.ownerId}.html"/>'>${activity.ownerName}</a></h5>
	               <p>${activity.message}</p>
	               <form id="likeForm_${activity.id}" name="likeForm_${activity.id}" action='<c:url value="/activitylike.html"/>' method="post">
                       <input type="hidden" name="activityId" value="${activity.id}"/>
	               </form>
	               <form id="activityForm_${activity.id}" name="activityForm_${activity.id}" action='<c:url value="/removeactivity.html"/>' method="post">
                       <input type="hidden" name="activityId" value="${activity.id}"/>
	               </form>
                   <table class="wallControlTable">
                    <tr>
                        <td class="nowrap">${activity.timeSinceCreated}</td>
                        <td class="nowrap">&nbsp;-&nbsp;</td>
                        <td>
                        <c:choose>
	                        <c:when test="${user.userId == activity.ownerId}"><a href="javascript:void(0);" onclick="removeActivity('${activity.id}');"><spring:message code="button.delete"/></a></c:when>
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
			                   <span class="commentDate">${comment.timeSinceCreated}</span>
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
	               
                   <table class="wallCommentTable">
                       <tr class="topTr"><td colspan="2"></td></tr>
                       <tr>
                        <td>
		                  <input id="commentMessageField_${activity.id}" type="text" class="commentField" name="message" value="" onkeypress="submitCommentByKeyPress(event, ${activity.id});" title='<spring:message code="wall.comment.placeholder"/>'/>
		                </td>         
		                <td class="nopadding"><button class="commentButton" onclick="postComment(${activity.id}); return false;">&nbsp;</button></td>  
                       </tr>
                       <tr class="bottomTr"><td colspan="2" class="nopadding"></td></tr>
                   </table>
	            </td>
	            </tr>
	        </table>
        </li>
    </c:forEach>
    </ul>
    <c:if test="${numberOfPages > 0}">
    <div class="span-14">
        <div class="prepend-1 span-6 left">
            <c:if test="${currentPage < numberOfPages - 1}">
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
<div id="confirmDelete" title="<spring:message code='confirmation.delete.activity.title'/>">
    <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span><spring:message code="confirmation.delete.activity"/></p>
</div>
