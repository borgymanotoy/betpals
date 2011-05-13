<%@include file="includes.jsp"%>
<script type="text/javascript">
   function saveAndExit() {
       jQuery('#manageCompetitionForm').submit();
   } 

   function goBack() {
	   jQuery('#createCompetitionForm').submit();
   } 
   
   function goToNextStep() {
       jQuery('#confirmCompetitionForm').submit();
   } 
   
   function deleteAlternative(alternativeId) {
       jQuery('#alternativeToDelete').val(alternativeId);
       jQuery('#deleteAlternativeForm').submit();
   } 
   
</script>
<div>
    <h2 class="dark"><spring:message code="competition.create.title"/></h2>
</div>
<div class="rbDiv contentDiv">
<c:url value="/savealternative.html" var="actionURL"/>
<form:form commandName="alternative" action="${actionURL}" method="post" enctype="multipart/form-data" id="alternativeForm">
    <div>
        <div class="span-12">
            <div class="span-2 labelDiv">
                <spring:message code="alternative.edit.name"/>
            </div>
            <div class="span-10 last">
                <form:input path="name" size="36"/>
            </div>
        </div>
        <div class="span-12">
            <div class="span-2 labelDiv">&nbsp;</div>
            <div class="span-10 last formSectionDiv">
		        <img class="userPic" src='<c:url value="/images/competitions/question.jpg"/>'/>
		        <div style="display: inline-block;">
		            <spring:message code="alternative.add.picture"/><br/> 
		            <input type="file" name="imageFile"/>
		        </div>
            </div>
        </div>
        <div class="span-12">
            <div class="span-2 labelDiv">&nbsp;</div>
            <div class="span-10 last">
		        <form:hidden path="eventId"/> 
		        <form:hidden path="competitionId"/> 
		        <input type="submit" value="<spring:message code='button.add'/>" class="greenButton110"/>
            </div>
        </div>
        &nbsp;
    </div>    
    <p><form:errors path="*"/>&nbsp;</p>
</form:form>
    <h4><spring:message code="competition.create.alternatives.header"/></h4>
    <div class="formSectionDiv">
        <ul id="friendList">
        <c:forEach items="${alternativesList}" var="altFromList" varStatus="status">
        <li>
	        <div class="span-12">
		        <div class="span-2 userPicDiv">
		            <img class="userPic" src='<c:url value="/competition/images/alt_${altFromList.id}.jpeg"/>'/>
		        </div>
		        <c:choose>
		        <c:when test="${status.last}">
                    <div class="span-10 last altListDetails noborder">
                        <div class="span-5 altListName">${altFromList.name}</div>
                        <div class="span-2 altListPriority right">
                           <img src='<c:url value="/i/up.png"/>'/>
                           <img src='<c:url value="/i/down.png"/>'/>
                        </div>
                        <div class="span-3 last altListButton right"><button class="whiteButton90" onclick="deleteAlternative(${altFromList.id});"><spring:message code="button.delete"/></button></div>
                    </div>		        
                </c:when>
		        <c:otherwise>
			        <div class="span-10 last altListDetails">
	    		        <div class="span-5 altListName">${altFromList.name}</div>
		   	            <div class="span-2 altListPriority right">
		   	               <img src='<c:url value="/i/up.png"/>'/>
		   	               <img src='<c:url value="/i/down.png"/>'/>
		   	            </div>
			            <div class="span-3 last altListButton right"><button class="whiteButton90" onclick="deleteAlternative(${altFromList.id});"><spring:message code="button.delete"/></button></div>
			        </div>
		        </c:otherwise>
		        </c:choose>
	        </div>
        </li>
        </c:forEach>
        </ul>
        &nbsp;
    </div>
    <button class="blueButton110" onclick="goBack();"><spring:message code="button.back"/></button>
    <button class="whiteButton90" onclick="goHome();"><spring:message code="button.cancel"/></button>
    <button class="greenButton110" onclick="saveAndExit();"><spring:message code="button.save.and.exit"/></button>
    <button class="blueButton110" onclick="goToNextStep();"><spring:message code="button.next"/></button>
    <p>&nbsp;</p>
</div>
<form action='<c:url value="/competitionview.html"/>' method="post" id="createCompetitionForm">
    <input type="hidden" name="competitionId" value="${alternative.competitionId}"/>
</form>
<form action='<c:url value="/competitionconfirmview.html"/>' method="post" id="confirmCompetitionForm">
    <input type="hidden" name="competitionId" value="${alternative.competitionId}"/>
</form>
<form action='<c:url value="/managecompetitions.html"/>' method="post" id="manageCompetitionForm">
    <input type="hidden" name="competitionId" value="${alternative.competitionId}"/>
</form>
<form action='<c:url value="/deletealternative.html"/>' method="post" id="deleteAlternativeForm">
    <input type="hidden" name="alternativeId" value="" id="alternativeToDelete"/>
    <input type="hidden" name="competitionId" value="${alternative.competitionId}"/>
</form>

