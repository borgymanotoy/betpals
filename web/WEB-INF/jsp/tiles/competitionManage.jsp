<%@include file="includes.jsp"%>
<script type="text/javascript">
	
	function deleteCompetition() {
		jQuery("#confirmDelete").dialog('open');
	} 
	
	function voidAlternative(alternativeId) {
		jQuery("#alternativeToVoid").val(alternativeId);
		jQuery("#confirmVoid").dialog('open');
	} 
	
	function settleCompetition() {
		
		var alternativeId;
		jQuery('input[name=correctAlternative]:radio').each(function() {
			if (jQuery(this).attr('checked')) {
				alternativeId = jQuery(this).val();
			}
		});
		if (alternativeId) {
		    jQuery("#winningAlternative").val(alternativeId);
		    jQuery('#settleCompetitionForm').submit();
        } else {
        	jQuery("#selectDialog").dialog('open');
        }
	} 
	
    jQuery(document).ready(function() {
        jQuery("#competitionDeadline").datetimepicker({ 
        	dateFormat: 'yy-mm-dd', 
        	minDate: 0, 
        	timeFormat: 'hh:mm',
        	onClose: function(datetext, inst) {
        		 jQuery('#competitionForm').submit();} 
        });
        jQuery("#settlingDeadline").datetimepicker({ 
        	dateFormat: 'yy-mm-dd', 
        	minDate: 0, 
            timeFormat: 'hh:mm',
            onClose: function(datetext, inst) {
                jQuery('#competitionForm').submit();} 
        });

        jQuery("#confirmDelete").dialog({
            resizable: false,
            autoOpen: false,
            height:160,
            modal: true,
            buttons: {
                "<spring:message code='button.delete'/>": function() {
                    jQuery('#deleteCompetitionForm').submit();
                    $( this ).dialog( "close" );
                },
                "<spring:message code='button.cancel'/>": function() {
                    $( this ).dialog( "close" );
                }
            }
        });

        jQuery("#confirmVoid").dialog({
            resizable: false,
            autoOpen: false,
            height:160,
            modal: true,
            buttons: {
            	"<spring:message code='button.void'/>": function() {
                    jQuery('#voidAlternativeForm').submit();
                    $( this ).dialog( "close" );
                },
                "<spring:message code='button.cancel'/>": function() {
                    $( this ).dialog( "close" );
                }
            }
        });

        jQuery("#selectDialog").dialog({
            resizable: false,
            autoOpen: false,
            height:140,
            modal: true,
            buttons: {
            	"<spring:message code='button.ok'/>": function() {
                    $( this ).dialog( "close" );
                }
            }
        });

    });

   
</script>
<div>
    <h2 class="dark"><spring:message code="competition.manage.title"/></h2>
</div>
<div class="rbDiv contentDiv">
<c:url value="/updatecompetition.html" var="actionURL"/>
<form:form commandName="competition" action="${actionURL}" method="post" id="competitionForm">
    <form:hidden path="id"/>
    <div class="span-12">
        <div class="span-2 userPicDiv">
            <img class="userPic" src='<c:url value="/competition/images/${competition.id}.jpeg"/>'/>
        </div>
        <div class="span-10 last invitationDiv formSectionDiv">
            <h5>${competition.name}</h5>
            <p>${competition.description}</p>
        </div>
    </div>
    <div class="span-12">
        <div class="span-2 labelDiv">
            <spring:message code="competition.competition.deadline"/>
        </div>
        <div class="span-10 last">
            <form:input path="deadline" id="competitionDeadline"/>
        </div>
    </div>
    <div class="span-12">
        <div class="span-2 labelDiv">
            <spring:message code="competition.settling.deadline"/>
        </div>
        <div class="span-10 last">
            <form:input path="settlingDeadline" id="settlingDeadline"/>
        </div>
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
                        <div class="span-5 altListName">
                        ${altFromList.name}<br/>
                        <c:if test="${empty altFromList.bets}"><span class="detailTitle"><spring:message code="competition.no.bets"/></span></c:if>
                        <c:forEach items="${altFromList.bets}" var="bet">
                            <span class="detailTitle"><c:choose><c:when test="${bet.ownerId == competition.ownerId}"><spring:message code="competition.you"/></c:when><c:otherwise>${bet.ownerName}</c:otherwise></c:choose> (${bet.stake} ${competition.currency})</span><br/>
                        </c:forEach>
                        </div>
                        <div class="span-2 altListPriority right">
                            <input type="radio" value="${altFromList.id}" name="correctAlternative"/> <spring:message code="alternative.correct"/>
                        </div>
                        <div class="span-3 last altListPriority right"><button class="whiteButton90" onclick="voidAlternative(${altFromList.id});return false"><spring:message code="alternative.void"/></button></div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="span-10 last altListDetails">
                        <div class="span-5 altListName">
                        ${altFromList.name}<br/>
                        <c:if test="${empty altFromList.bets}"><span class="detailTitle"><spring:message code="competition.no.bets"/></span></c:if>
                        <c:forEach items="${altFromList.bets}" var="bet">
                            <span class="detailTitle"><c:choose><c:when test="${bet.ownerId == competition.ownerId}"><spring:message code="competition.you"/></c:when><c:otherwise>${bet.ownerName}</c:otherwise></c:choose> (${bet.stake} ${competition.currency})</span><br/>
                        </c:forEach>
                        </div>
                        <div class="span-2 altListPriority right">
                            <input type="radio" value="${altFromList.id}" name="correctAlternative"/> <spring:message code="alternative.correct"/>
                        </div>
                        <div class="span-3 last altListPriority right"><button class="whiteButton90" onclick="voidAlternative(${altFromList.id});return false;"><spring:message code="alternative.void"/></button></div>
                    </div>
                </c:otherwise>
                </c:choose>
            </div>
        </li>
        </c:forEach>
        </ul>
        &nbsp;
    </div>
</form:form>
    <button class="whiteButton140" onclick="deleteCompetition();"><spring:message code="competition.delete"/></button>
    <button class="greenButton140" onclick="settleCompetition();"><spring:message code="competition.settle"/></button>
    <p>&nbsp;</p>
</div>
<div id="confirmDelete" title="<spring:message code='confirmation.delete.competition.title'/>">
    <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span><spring:message code="confirmation.delete.competition"/></p>
</div>
<div id="confirmVoid" title="<spring:message code='confirmation.void.alternative.title'/>">
    <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span><spring:message code="confirmation.void.alternative"/></p>
</div>
<div id="selectDialog" title="<spring:message code='confirmation.select.alternative.title'/>">
    <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span><spring:message code="confirmation.select.alternative"/></p>
</div>

<form action='<c:url value="/deletecompetition.html"/>' method="post" id="deleteCompetitionForm">
    <input type="hidden" name="competitionId" value="${competition.id}"/>
</form>
<form action='<c:url value="/voidalternative.html"/>' method="post" id="voidAlternativeForm">
    <input type="hidden" name="competitionId" value="${competition.id}"/>
    <input type="hidden" name="alternativeId" value="" id="alternativeToVoid"/>
</form>
<form action='<c:url value="/settlecompetition.html"/>' method="post" id="settleCompetitionForm">
    <input type="hidden" name="competitionId" value="${competition.id}"/>
    <input type="hidden" name="alternativeId" value="" id="winningAlternative"/>
</form>
