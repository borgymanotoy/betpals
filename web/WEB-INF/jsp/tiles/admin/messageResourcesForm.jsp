<%@include file="includes.jsp"%>
<script type="text/javascript">
    jQuery(document).ready(function() {
    });
    
    function backToList() {
        window.location.href = '<c:url value="/admin/listmessageresources.html?lang="/>${messageResource.locale}';
    } 

    function deleteMessageResource() {
        jQuery("#deleteMessageResourceForm").submit();
    } 
    
</script>
<div>
    <h2 class="dark_long">Message Resources</h2>
</div>
<div class="rbDiv contentDiv" style="height: 300px;">
    <c:url var="editMessageResourceURL" value="/admin/editmessageresource.html"/>
	<form action="${editMessageResourceURL}" method="post">
		<div class="span-12 formSectionSlimDiv">
	        <div class="span-2 labelDiv" style="padding-top: 8px;"><spring:message code="admin.cms.message.resource.language"/></div>
	        <div class="span-10 last">
                 <select name="locale" id="language">
                     <option value="all"><spring:message code="admin.cms.message.resource.language.all"/></option>
                     <c:forEach items="${supportedLanguages}" var="lang">
                     <option value="${lang}" <c:if test="${lang == messageResource.locale}">selected="true"</c:if>>${lang}</option>
                     </c:forEach>
                 </select>
	        </div>
	    </div>
		<div class="span-12 formSectionSlimDiv">
	        <div class="span-2 labelDiv" style="padding-top: 8px;"><spring:message code="admin.cms.message.resource.key"/></div>
	        <div class="span-10 last">
                <input id="key" name="key" value="${messageResource.key}" size="50"/>	        
            </div>
	    </div>
		<div class="span-12 formSectionSlimDiv">
	        <div class="span-2 labelDiv" style="padding-top: 8px;"><spring:message code="admin.cms.message.resource.value"/></div>
	        <div class="span-10 last">
                <input id="value" name="value" value="${messageResource.value}" size="50"/>	        
            </div>
	    </div>
	    <div class="span-12 formSectionSlimDiv">
	        <div class="span-2 labelDiv">&nbsp;</div>
	        <div class="span-10 last">
	           <input type="submit" class="greenButton90" value='<spring:message code="button.save"/>'/>
	           <button class="blueButton90" onclick="backToList();return false;"><spring:message code="button.cancel"/></button> 
	           <button class="whiteButton90" onclick="deleteMessageResource();return false;"><spring:message code="button.delete"/></button> 
	        </div>
	    </div>
	</form>
<p>&nbsp;</p>
</div>
<form id="deleteMessageResourceForm" action='<c:url value="/admin/deletemessageresource.html"/>' method="post">
    <input type="hidden" name="key" value="${messageResource.key}"/>
    <input type="hidden" name="lang" value="${messageResource.locale}"/>
</form>

