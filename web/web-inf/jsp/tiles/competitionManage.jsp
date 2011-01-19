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
        jQuery('#settleCompetitionForm').submit();
	} 
	
    jQuery(document).ready(function() {
        jQuery("#competitionDeadline").datetimepicker({ 
        	dateFormat: 'yy-mm-dd', 
        	minDate: 0, 
        	onClose: function(datetext, inst) {
        		 jQuery('#competitionForm').submit();} 
        });
        jQuery("#settlingDeadline").datetimepicker({ 
        	dateFormat: 'yy-mm-dd', 
        	minDate: 0, 
            onClose: function(datetext, inst) {
                jQuery('#competitionForm').submit();} 
        });

        jQuery("#confirmDelete").dialog({
            resizable: false,
            autoOpen: false,
            height:160,
            modal: true,
            buttons: {
                "Delete": function() {
                    jQuery('#deleteCompetitionForm').submit();
                    $( this ).dialog( "close" );
                },
                Cancel: function() {
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
                "Void": function() {
                    jQuery('#voidAlternativeForm').submit();
                    $( this ).dialog( "close" );
                },
                Cancel: function() {
                    $( this ).dialog( "close" );
                }
            }
        });

    });

   
</script>
<!-- TODO: All strings to message resources -->
<div>
    <h2 class="dark">Manage competition</h2>
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
            Deadline for competition
        </div>
        <div class="span-10 last">
            <form:input path="deadline" id="competitionDeadline"/>
        </div>
    </div>
    <div class="span-12">
        <div class="span-2 labelDiv">
            Deadline for settling
        </div>
        <div class="span-10 last">
            <form:input path="settlingDeadline" id="settlingDeadline"/>
        </div>
    </div>
    <p>&nbsp;</p>
    <h4>Alternatives</h4>
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
                        <c:if test="${empty altFromList.bets}"><span class="detailTitle">No bets</span></c:if>
                        <c:forEach items="${altFromList.bets}" var="bet">
                            <span class="detailTitle"><c:choose><c:when test="${bet.ownerId == competition.ownerId}">You</c:when><c:otherwise>${bet.ownerName}</c:otherwise></c:choose> (${bet.stake} ${competition.currency})</span><br/>
                        </c:forEach>
                        </div>
                        <div class="span-2 altListPriority right">
                            <input type="radio" value="${altFromList.id}" name="correctAlternative"/> Correct
                        </div>
                        <div class="span-3 last altListPriority right"><button class="whiteButton90" onclick="voidAlternative(${altFromList.id});return false">Void</button></div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="span-10 last altListDetails">
                        <div class="span-5 altListName">
                        ${altFromList.name}<br/>
                        <c:if test="${empty altFromList.bets}"><span class="detailTitle">No bets</span></c:if>
                        <c:forEach items="${altFromList.bets}" var="bet">
                            <span class="detailTitle"><c:choose><c:when test="${bet.ownerId == competition.ownerId}">You</c:when><c:otherwise>${bet.ownerName}</c:otherwise></c:choose> (${bet.stake} ${competition.currency})</span><br/>
                        </c:forEach>
                        </div>
                        <div class="span-2 altListPriority right">
                            <input type="radio" value="${altFromList.id}" name="correctAlternative"/> Correct
                        </div>
                        <div class="span-3 last altListPriority right"><button class="whiteButton90" onclick="voidAlternative(${altFromList.id});return false;">Void</button></div>
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
    <button class="whiteButton140" onclick="deleteCompetition();">Delete competition</button>
    <button class="greenButton140" onclick="settleCompetition();">Settle competition</button>
    <p>&nbsp;</p>
</div>
<div id="confirmDelete" title="Delete competition?">
    <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>This competition will be deleted.<br/> Are you sure?</p>
</div>
<div id="confirmVoid" title="Void alternative?">
    <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>This alternative will be void. <br/>Are you sure?</p>
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
</form>
