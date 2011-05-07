<%@include file="includes.jsp"%>
<script type="text/javascript">
    jQuery(document).ready(function() {
    });
    
</script>
<div>
    <h2 class="dark_long">Message Resources</h2>
</div>
<div class="rbDiv contentDiv">
    <span><a href='<c:url value="/admin/deletemessageresource.html?key=${messageResource.key}&lang=${messageResource.locale}"/>' class="small_button"><img src='<c:url value="/i/delete.png"/>' alt=""/><spring:message code="button.delete"/></a></span>
    <c:url var="editMessageResourceURL" value="/admin/editmessageresource.html"/>
	<form action="${editMessageResourceURL}" method="post">
	    <fieldset  class="generalFieldset">
	        <ol>
	            <li>
	                <label for="language"><spring:message code="admin.cms.message.resource.language"/></label>
	                <select name="locale" id="language">
	                    <option value="all"><spring:message code="admin.cms.message.resource.language.all"/></option>
	                    <c:forEach items="${supportedLanguages}" var="lang">
	                    <option value="${lang}" <c:if test="${lang == messageResource.locale}">selected="true"</c:if>>${lang}</option>
	                    </c:forEach>
	                </select>
	            </li>
	            <li>
	                <label for="key"><spring:message code="admin.cms.message.resource.key"/></label>
	                <input id="key" name="key" value="${messageResource.key}" size="50"/>
	            </li>
	            <li>
	                <label for="value"><spring:message code="admin.cms.message.resource.value"/></label>
	                <input id="value" name="value" value="${messageResource.value}" size="50"/>
	            </li>
	        </ol>
	    </fieldset>
	    <input type="submit" value='<spring:message code="button.save"/>'/>
	</form>
&nbsp;
</div>
