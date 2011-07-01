<%@include file="includes.jsp"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<script type="text/javascript">
    jQuery(document).ready(function() {

    });
    
</script>
<div>
    <h2 class="dark_long"><spring:message code="configuration.title"/></h2>
</div>
<div class="rbDiv contentDiv">

    <c:url var="editConfigurationURL" value="/admin/updateconfiguration.html"/>
    <form action="${editConfigurationURL}" method="post">

    <c:forEach items="${configuration}" var="parameter">
        <div class="span-16 formSectionSlimDiv" style="margin-bottom: 4px;">
            <div class="span-6 labelDiv" style="padding-top: 8px;"><spring:message code="${parameter.key}"/></div>
            <div class="span-10 last">
                <input type="text" name="${parameter.key}" value="${parameter.value}" size="40"/>           
            </div>
        </div>
    </c:forEach>
    <div class="span-16 formSectionSlimDiv">
        <div class="span-6 labelDiv">&nbsp;</div>
        <div class="span-10 last">
           <input type="submit" class="greenButton90" value='<spring:message code="button.save"/>'/>
        </div>
    </div>
    </form>  
    <p>&nbsp;</p>
</div>
