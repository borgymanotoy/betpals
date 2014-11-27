<%@include file="includes.jsp"%>
<script type="text/javascript">
   jQuery(document).ready(function() {
        jQuery("#competitionDeadline").datetimepicker({ dateFormat: 'yy-mm-dd', minDate: 0, timeFormat: 'hh:mm' });
        jQuery("#settlingDeadline").datetimepicker({ dateFormat: 'yy-mm-dd', minDate: 0, timeFormat: 'hh:mm' });
        
        var thumb = $('img#thumb'); 

        new AjaxUpload('#imageUpload', {
            action: '<c:url value="/savetempcompetitionimage.html"/>',
            name: 'imageFile',
            onSubmit: function(file, extension) {
                $('div.preview').addClass('loading');
                jQuery('#filenameDiv').text('<spring:message code="generating.thumbnail"/>');
            },
            onComplete: function(file, response) {
                thumb.load(function(){
                    $('div.preview').removeClass('loading');
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
        
   });
</script>
<c:url value="/quickcompetition.html" var="actionURL"/>
<form:form commandName="quickCompetition" action="${actionURL}" method="post" enctype="multipart/form-data">
<div id="quickCompetitionDiv2">
    <h2><spring:message code="competition.quick.title"/></h2>
    <div class="headerContentDiv">
	    <span><spring:message code="competition.i.bet"/> </span><form:input id="quickCompetitionStake" path="stake"/>
	    <form:select path="accountId">
	    <c:forEach items="${accounts}" var="account">
	        <option value="${account.id}" <c:if test="${account.id == quickCompetition.accountId}">selected="selected"</c:if>>${account.currency}</option>
	        <form:option label="${account.currency}" value="${account.id}"/>
	    </c:forEach>
	    </form:select> 
	    <span><spring:message code="competition.bet.that"/> </span> 
	    <form:input id="quickCompetitionAlternative2" path="name"/>
    </div>
</div>
<div class="rbDiv contentDiv">
    <div class="formSectionDiv">
        <spring:message code="competition.edit.description"/><br/>
        <form:textarea path="description" id="quickCompetitionDescription"/>
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
    <p class="error">
	    <form:errors path="*" />
    </p>
    <button class="whiteButton90" onclick="goHome(); return false;"><spring:message code="button.cancel"/></button>
    <input type="submit" value="<spring:message code='button.next'/>" class="greenButton110"/>

    <p>&nbsp;</p>
</div>
</form:form>
