<%@include file="includes.jsp"%>
<div id="footerPane" class="span-24">
&nbsp;
</div>
<div class="span-24 versionInfo">
    <spring:eval expression="@buildProperties['build.number']" var="buildNumber"/>
    <spring:eval expression="@buildProperties['build.date']" var="buildDate"/>
    <spring:eval expression="@buildProperties['build.version']" var="buildVersion"/>
    Betpals version: ${buildVersion}, build: ${buildNumber} at ${buildDate}
</div>