<%@include file="includes.jsp"%>
<script type="text/javascript">
    jQuery(document).ready(function() {
        $(".positive_number_only").numeric({ negative: false }, function() { alert("No negative values"); this.value = ""; this.focus(); });
        jQuery("#informBet").dialog({
            resizable: false,
            autoOpen: false,
            width: 410,
            height: 180,
            modal: true,
            buttons: {
                "<spring:message code='button.ok'/>": function() {
                    submitStake($( this ).data("invitationId"), $( this ).data("alternativeId"), $( this ).data("stake"));
                    $( this ).dialog( "close" );
                }
            }
        });
    });

    var validateAlternativeBet = function(e, invitationId, alternativeId){
        clearZeroStakeError();
        if(e.keyCode == 13){
            validateAlternativeBetSubmit(invitationId, alternativeId);
        }
    };
    var validateAlternativeBetSubmit = function(invitationId, alternativeId){
        $("#stake_"+alternativeId).val($("#myStake_"+alternativeId).val());
        var sValue = $("#stake_"+alternativeId).val();
        var fSValue = $("#fixedStake").val();
        var value = (sValue!=null && sValue.length > 0) ? sValue : fSValue;
        if(value.length > 0){
            var stake = (value.length > 0) ? Number(value) : 0;
            if(stake > 0){
                showBetConfirmation(invitationId, alternativeId, stake);
            }else{
                showHideZeroStakeError(true);
            }
        }
    };
    var showBetConfirmation = function(invitationId, alternativeId, stake){
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
            .data('invitationId',invitationId)
            .data('competitionName',competitionName)
            .data('alternativeId',alternativeId)
            .data('currency',competitionCurrency)
            .data('stake',stake)
            .dialog('open');
    };
    function submitStake(invitationId, alternativeId, stake){
        jQuery('#invitationId_'+alternativeId).val(invitationId);
        jQuery('#alternativeId_'+alternativeId).val(alternativeId);
        jQuery('#stake_'+alternativeId).val(stake);
        jQuery('#placeBetForm_'+alternativeId).submit();
    }
</script>
<div>
    <h2 class="dark"><spring:message code="invitation.view.title"/></h2>
</div>
<div class="rbDiv contentDiv">
    <div class="formSectionDiv">
        <img src='<c:url value="/i/ex.png"/>' style="vertical-align: bottom;"/>&nbsp;
        <a href='<c:url value="/viewprofile/${invitation.ownerId}.html"/>'>${invitation.ownerName}</a>&nbsp;<spring:message code="invitation.owner.wants.you.to.join"/><br/>
    </div>
    <div class="formSectionDiv">
	    <img class="userPic" src='<c:url value="/competition/images/${invitation.competitionId}.jpeg"/>'/>
	    <div class="inlineDiv">
    	    <h5>${competition.name}</h5>
	       ${competition.description}<br/>
	    </div>
	    <div style="padding-top: 10px;">
		    <span class="detailTitle"><spring:message code="invitation.deadline"/>: </span><fmt:formatDate value="${competition.deadline}" pattern="yyyy-MM-dd HH:mm"/><br/>
		    <span class="detailTitle"><spring:message code="invitation.resolved"/>: </span><fmt:formatDate value="${competition.settlingDeadline}" pattern="yyyy-MM-dd HH:mm"/><br/>
	    </div>
    </div>
    
    <ul id="friendList">
    <c:if test="${not empty param.invalidStake}">
        <p class="error"><spring:message code="error.invalid.stake"/></p>
    </c:if>
    <c:if test="${not empty param.noAccount}">
        <p class="error"><spring:message code="error.no.account"/>&nbsp;<a href='<c:url value="/addaccount.html"/>'><spring:message code="button.add.account"/></a></p>
    </c:if>
    <span class="error hide" id="spn_error_stake0"><spring:message code='error.zero.stake'/></span>
    <c:forEach items="${competition.events}" var="event">
        <c:forEach items="${event.alternatives}" var="alternative">
            <li>
            <div class="span-12">
                <div class="span-2 userPicDiv">
                    <img class="userPic" src='<c:url value="/competition/images/alt_${alternative.id}.jpeg"/>'/>
                </div>
                <div class="span-5 invViewDiv">
                    ${alternative.name}<br/>
                    <span class="detailTitle">${alternative.turnover} ${competition.currency} <spring:message code="competition.on.this.alternative"/></span>
                </div>
		        <c:choose>
		          <c:when test="${alternative.taken}">
	                <div class="span-5 last invViewDiv right">
			              <spring:message code="alternative.already.taken.by"/> <br/><a href='<c:url value="/viewprofile/${alternative.participantId}.html"/>'>${alternative.participantName}</a>
	                </div>
		          </c:when>
		          <c:when test="${competition.deadlineReached}">
                    <div class="span-5 last right">
                          <spring:message code="betting.is.closed"/>
                    </div>
                  </c:when>
                  <c:otherwise>
                    <form id="placeBetForm_${alternative.id}" action='<c:url value="/placebet.html"/>' method="post">
                        <input type="hidden" id="invitationId_${alternative.id}" name="invitationId" value="${invitation.id}"/>
                        <input type="hidden" id="competitionId_${alternative.id}" name="competitionId" value="${competition.id}"/>
                        <input type="hidden" id="competitionName_${alternative.id}" name="competitionName" value="${competition.name}"/>
                        <input type="hidden" id="competitionCurrency_${alternative.id}" name="competitionCurrency" value="${competition.currency}"/>
                        <input type="hidden" id="alternativeId_${alternative.id}" name="alternativeId" value="${alternative.id}"/>
                        <input type="hidden" id="stake_${alternative.id}" name="stake" value=""/>
                    </form>
                    <div class="span-5 last invViewButtonDiv right">
                        <c:choose>
                        <c:when test="${competition.competitionType == 'POOL_BETTING'}">
                            <c:if test="${alternative.id == alternativeId}">
                                <input type="text" id="myStake_${alternative.id}" size="3" maxlength="3" value="${betValue}" class="positive_number_only" onkeypress="validateAlternativeBet(event, '${invitation.id}', ${alternative.id}, this.value);" onchange="clearZeroStakeError();" /> ${competition.currency}&nbsp;
                            </c:if>
                            <c:if test="${alternative.id != alternativeId}">
                                <input type="text" id="myStake_${alternative.id}" size="3" maxlength="3" class="positive_number_only" onkeypress="validateAlternativeBet(event, '${invitation.id}', ${alternative.id}, this.value);" onchange="clearZeroStakeError();" /> ${competition.currency}&nbsp;
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <input type="hidden" id="fixedStake" value="${competition.fixedStake}"/>
                            ${competition.fixedStake} ${competition.currency}&nbsp;
                        </c:otherwise>
                        </c:choose>
                        <input type="submit" id="placeBetButton_${alternative.id}" class="greenButton90" value="<spring:message code='button.place.bet'/>" onclick="validateAlternativeBetSubmit('${invitation.id}', ${alternative.id});"/>
                    </div>
                  </c:otherwise>
                </c:choose>
            </div>
            </li>
        </c:forEach>
    </c:forEach>
    </ul>
    <p>&nbsp;</p>
</div>
<div id="informBet" title="<spring:message code='link.ongoing.competitions'/>">
    <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span><span id="betInformMessage"><spring:message code="confirmation.bet.redirect.after"/></span></p>
</div>
