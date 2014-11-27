<%@include file="includes.jsp"%>
<script type="text/javascript">
jQuery(document).ready(function() {
    $(".positive_number_only").numeric({ negative: false }, function() { alert("No negative values"); this.value = ""; this.focus(); });
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

    jQuery("#informBet").dialog({
        resizable: false,
        autoOpen: false,
        width: 410,
        height: 180,
        modal: true,
        buttons: {
            "<spring:message code='button.ok'/>": function() {
                submitStake($( this ).data("competitionId"), $( this ).data("alternativeId"), $( this ).data("stake"));
                $( this ).dialog( "close" );
            }
        }
    });

});

var validateAlternativeBet = function(e, competitionId, alternativeId){
	clearZeroStakeError();
	if(e.keyCode == 13){
		validateAlternativeBetSubmit(competitionId, alternativeId);
	}
};
var validateAlternativeBetSubmit = function(competitionId, alternativeId){
	$("#stake_"+alternativeId).val($("#myStake_"+alternativeId).val());
	var sValue = $("#stake_"+alternativeId).val();
	var fSValue = $("#fixedStake").val();
	var value = (sValue!=null && sValue.length > 0) ? sValue : fSValue;
	if(value.length > 0){
		var stake = (value.length > 0) ? Number(value) : 0;
		if(stake > 0){
			showBetConfirmation(competitionId, alternativeId, stake);
		}else{
			showHideZeroStakeError(true);
		}
	}
};
var showBetConfirmation = function(competitionId, alternativeId, stake){
	var competitionName = $("#competitionName_"+alternativeId).val();
	competitionName = (competitionName.length > 0) ? competitionName : "N/A";
	var competitionCurrency = $("#competitionCurrency_"+alternativeId).val();
	competitionCurrency = (competitionCurrency.length > 0) ? competitionCurrency : "N/A";

	var inform_msg = $('#betInformMessage').html();
	inform_msg = inform_msg.replace(/~%BET%~/g, stake);
	inform_msg = inform_msg.replace(/~%CURRENCY%~/g, competitionCurrency);
	inform_msg = inform_msg.replace(/~%COMPETITION%~/g, "<b>" + competitionName+"</b>");
	$('#betInformMessage').html(inform_msg);

	jQuery("#informBet")
	  .data('competitionId',competitionId)
	  .data('competitionName',competitionName)
	  .data('alternativeId',alternativeId)
	  .data('currency',competitionCurrency)
	  .data('stake',stake)
	  .dialog('open');
};
function submitStake(competitionId, alternativeId, stake){
	jQuery('#competitionId_'+alternativeId).val(competitionId);
	jQuery('#alternativeId_'+alternativeId).val(alternativeId);
	jQuery('#stake_'+alternativeId).val(stake);
	jQuery('#placeAnotherBetForm_'+alternativeId).submit();
}

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
<div>
    <h2 class="dark"><spring:message code="competition.ongoing.title"/></h2>
</div>
<div class="rbDiv contentDiv">
    <div class="formSectionDiv">
        <img src='<c:url value="/i/ex.png"/>' style="vertical-align: bottom;"/>&nbsp;
        <a href='<c:url value="/viewprofile/${competition.ownerId}.html"/>'>${competition.owner.fullName}</a>&nbsp;<spring:message code="invitation.owner.wants.you.to.join"/><br/>
    </div>
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
    <span class="error hide" id="spn_error_stake0"><spring:message code='error.zero.stake'/></span>
    <div class="formSectionDiv">
        <ul id="friendList">
    <c:if test="${not empty param.invalidStake}">
        <p class="error"><spring:message code="error.invalid.stake"/></p>
    </c:if>
    <c:if test="${not empty param.noAccount}">
        <p class="error"><spring:message code="error.no.account"/>&nbsp;<a href='<c:url value="/addaccount.html"/>'><spring:message code="button.add.account"/></a></p>
    </c:if>
        <c:forEach items="${sortedAlternativeList}" var="altFromList" varStatus="status">
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
                  <c:when test="${competition.deadlineReached}">
                    <div class="span-5 last right">
                          <spring:message code="betting.is.closed"/>
                    </div>
                  </c:when>
                  <c:otherwise>
                    <form id="placeAnotherBetForm_${altFromList.id}" action='<c:url value="/placeanotherbet.html"/>' method="post">
                        <input type="hidden" id="competitionId_${altFromList.id}" name="competitionId" value="${competition.id}"/>
                        <input type="hidden" id="competitionName_${altFromList.id}" name="competitionName" value="${competition.name}"/>
                        <input type="hidden" id="competitionCurrency_${altFromList.id}" name="competitionCurrency" value="${competition.currency}"/>
                        <input type="hidden" id="alternativeId_${altFromList.id}" name="alternativeId" value="${altFromList.id}"/>
                        <input type="hidden" id="stake_${altFromList.id}" name="stake" value=""/>
                    </form>
                    <div class="span-5 last right">
                        <c:choose>
                        <c:when test="${competition.competitionType == 'POOL_BETTING'}">
                            <c:if test="${altFromList.id == alternativeId}">
                                <input type="text" id="myStake_${altFromList.id}" size="3" maxlength="3" value="${betValue}" class="positive_number_only" onkeypress="validateAlternativeBet(event, ${competition.id}, ${altFromList.id}, this.value);" onchange="clearZeroStakeError();" /> ${competition.currency}&nbsp;
                            </c:if>
                            <c:if test="${altFromList.id != alternativeId}">
                                <input type="text" id="myStake_${altFromList.id}" size="3" maxlength="3" class="positive_number_only" onkeypress="validateAlternativeBet(event, ${competition.id}, ${altFromList.id}, this.value);" onchange="clearZeroStakeError();" /> ${competition.currency}&nbsp;
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <input type="hidden" id="fixedStake" value="${competition.fixedStake}"/>
                            ${competition.fixedStake} ${competition.currency}&nbsp;
                        </c:otherwise>
                        </c:choose>
                        <input type="submit" id="placeBetButton_${altFromList.id}" class="greenButton90" value="<spring:message code='button.place.bet'/>" onclick="validateAlternativeBetSubmit(${competition.id}, ${altFromList.id});"/>
                    </div>
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
                              <input id="commentMessageField_${activity.id}" type="text" class="commentField" name="message" value="" onkeypress="submitCommentByKeyPress(event, ${activity.id});" title="<spring:message code='wall.comment.placeholder'/>"/>
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
<div id="confirmDelete" title="<spring:message code='confirmation.delete.activity.title'/>">
    <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span><spring:message code="confirmation.delete.activity"/></p>
</div>
<div id="informBet" title="<spring:message code='link.ongoing.competitions'/>">
    <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span><span id="betInformMessage"><spring:message code="confirmation.bet.redirect.after"/></span></p>
</div>
