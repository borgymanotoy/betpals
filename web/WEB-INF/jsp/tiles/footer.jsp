<%@include file="includes.jsp"%>
<div id="footerPane" class="span-24">
    <div class="prepend-4 span-4" style="padding-top: 9px;">
        <span class="arrowLink clickable" onclick="provideFeedback();"><spring:message code="feedback.link"/></span>
    </div>
    <div class="span-16 last"></div>
&nbsp;
</div>
<div class="span-24 versionInfo">
    <spring:eval expression="@buildProperties['build.number']" var="buildNumber"/>
    <spring:eval expression="@buildProperties['build.date']" var="buildDate"/>
    <spring:eval expression="@buildProperties['build.version']" var="buildVersion"/>
    Betpals version: <a class="white" href="Changes.txt">${buildVersion}</a>, build: ${buildNumber} at ${buildDate}
</div>
