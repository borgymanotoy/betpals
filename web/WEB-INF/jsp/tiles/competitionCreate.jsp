<%@include file="includes.jsp"%>
<script type="text/javascript">
	function nextStep() {
        jQuery('#nextStep').val(true);
        jQuery('#competitionForm').submit();
	} 
	
	function saveAndExit() {
		var size = $("#alternativeSize").val();
		if(size > 1){
			jQuery('#nextStep').val(false);
			jQuery('#competitionForm').submit();
		}
	} 
	
	var setSaveExitButtonAccordingToAlternativesCount = function(){
		var size = $("#alternativeSize").val();
		if(size > 1){
			$('#btnNewCompetitionSaveExit').show();
		}else{
			$('#btnNewCompetitionSaveExit').hide();
		}
	};
	
    jQuery(document).ready(function() {
		setSaveExitButtonAccordingToAlternativesCount();
        jQuery("#competitionDeadline").datetimepicker({ 
        	dateFormat: 'yy-mm-dd', 
        	minDate: 0, 
        	timeFormat: 'hh:mm',
        	onClose: function(dateText, inst) {
        	    jQuery.getJSON('<c:url value="/calculatesettlingdate.html"/>', 
        	    		{ deadlineDate: dateText }, 
        	    		function(json) {
        	    			if (json.success == 'true') {
        	    			   jQuery("#settlingDeadline").val(json.settlingDate);
        	    			}
        	    		});
       		}
        });
        jQuery("#settlingDeadline").datetimepicker({ dateFormat: 'yy-mm-dd', minDate: 0, timeFormat: 'hh:mm' });
        
        var thumb = jQuery('img#thumb'); 

        new AjaxUpload('#imageUpload', {
            action: '<c:url value="/savetempcompetitionimage.html"/>',
            name: 'imageFile',
            onSubmit: function(file, extension) {
                jQuery('div.preview').addClass('loading');
                jQuery('#filenameDiv').text('<spring:message code="generating.thumbnail"/>');
            },
            onComplete: function(file, response) {
                thumb.load(function(){
                    jQuery('div.preview').removeClass('loading');
                    thumb.unbind();
                });

                try {
                    var responseJson = jQuery.parseJSON(response);
	                if (responseJson.success == 'true') {
	                  var newSrc = '<c:url value="/competition/images/"/>';
	                  var date = new Date();
	                  thumb.attr('src', newSrc + responseJson.filename + ".jpg?v=" + date.getTime());
	                  jQuery('#filenameDiv').text(file);
	                } else {
	                    jQuery('#filenameDiv').text('<spring:message code="could.not.process.image"/>');
	                }
                } catch (exception) {
                    jQuery('#filenameDiv').text('<spring:message code="could.not.process.image"/>');
                }
            }
        });
        <c:if test="${competition.competitionType == 'FIXED_STAKE'}">
           jQuery('#fixedStakeDiv').show();
        </c:if>
    });

   
</script>
<div>
    <h2 class="dark"><spring:message code="competition.create.title"/></h2>
</div>

<input type="hidden" id="alternativeSize" value="${alternativeCount}"/>

<div class="rbDiv contentDiv">
<c:url value="/savecompetition.html" var="actionURL"/>
<form:form commandName="competition" action="${actionURL}" method="post" enctype="multipart/form-data" id="competitionForm">
    <form:hidden path="id"/>
    <form:hidden path="goToNextStep" id="nextStep"/>
    <div class="formSectionDiv">
        <div class="span-12">
            <div class="span-2 labelDiv">
                <spring:message code="competition.edit.name"/>
            </div>
            <div class="span-10 last">
                <form:input path="name" size="36"/>
            </div>
        </div>
        <div class="span-12">
            <div class="span-2 labelDiv">
                <spring:message code="competition.edit.description"/>
            </div>
            <div class="span-10 last">
                <form:textarea path="description" id="competitionDescription"/>
            </div>
        </div>
        &nbsp;
    </div>    
    <div class="formSectionDiv">
        <div class="span-12">
            <div class="span-2 labelDiv">
                <img class="userPic" src='<c:url value="/competition/images/${competition.id}.jpeg"/>' id="thumb"/>
            </div>  
            <div class="span-10 last">
                <div id="filenameDiv" style="padding-top: 8px; padding-bottom: 10px;">&nbsp;</div>
		        <button class="whiteButton110" id="imageUpload">
		            <spring:message code="button.add.picture"/> 
		        </button>
            </div>
        </div>
        &nbsp;
    </div>    
    <div class="formSectionDiv">
        <form:checkbox path="publicCompetition"/><spring:message code="competition.access.type.public"/>
    </div>
    <div class="formSectionDiv" style="padding-top: 5px; padding-bottom: 15px;">
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
        &nbsp;
    </div>    
    <div class="formSectionDiv">
        <spring:message code="competition.select.currency"/>
        <form:select path="accountId">
        <c:forEach items="${accounts}" var="account">
            <option value="${account.id}" <c:if test="${account.defaultAccount}">selected="selected"</c:if>>${account.currency}</option>
        </c:forEach>
        </form:select> 
    </div>    
    <div class="formSectionDiv">
        <input type="radio" name="competitionType" value="POOL_BETTING" onclick="jQuery('#fixedStake').val(''); jQuery('#fixedStakeDiv').hide();" <c:if test="${competition.competitionType == 'POOL_BETTING'}">checked="checked"</c:if>/><spring:message code="competition.type.pool.betting"/><br/>
        <input type="radio" name="competitionType" value="FIXED_STAKE" onclick="jQuery('#fixedStakeDiv').show();" <c:if test="${competition.competitionType == 'FIXED_STAKE'}">checked="checked"</c:if>/><spring:message code="competition.type.fixed.stake"/>
        <div style="padding-left: 20px; display: none;" id="fixedStakeDiv">
                <spring:message code="competition.stake"/>&nbsp;
                <form:input path="fixedStake" size="4" id="fixedStake"/>
        </div>
    </div>
    <p class="error">
        <form:errors path="*" />
    </p>
<!--
    <div class="formSectionDiv">
        <input type="checkbox" name="public"><spring:message code="competition.public"/></input>
    </div>
  -->    
</form:form>
	<button id="btnNewCompetitionCancel" class="whiteButton90" onclick="goHome();"><spring:message code="button.cancel"/></button>
	<button id="btnNewCompetitionSaveExit" class="greenButton110" onclick="saveAndExit();"><spring:message code="button.save.and.exit"/></button>
	<button id="btnNewCompetitionNext" class="blueButton110" onclick="nextStep();"><spring:message code="button.next"/></button>

    <p><form:errors path="*"/></p>
    <p>&nbsp;</p>
</div>
