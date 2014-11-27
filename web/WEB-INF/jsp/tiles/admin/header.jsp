<%@include file="includes.jsp"%>
<div class="span-18 colorDiv">
    &nbsp;
    <a href="javascript:changeBgColor('#242b21', 'logo1');"><img src='<c:url value="/i/c1.jpg"/>'/></a>
    <a href="javascript:changeBgColor('#001d44', 'logo1');"><img src='<c:url value="/i/c2.jpg"/>'/></a>
    <a href="javascript:changeBgColor('#67b116', 'logo1');"><img src='<c:url value="/i/c3.jpg"/>'/></a>
    <a href="javascript:changeBgColor('#297085', 'logo1');"><img src='<c:url value="/i/c4.jpg"/>'/></a>
</div>
<div class="span-3 right" style="padding-top: 43px;">
    <a class="whiteDotLink white noline" href='<c:url value="/j_spring_security_logout"/>'><spring:message code="logout"/></a>&nbsp;
</div>
<div class="span-3 last left" style="padding-top: 42px;">
    <select id="langSelector" style="background-color: #67b116; color: white; margin: 0; font-size: 12px;" onchange="changeLanguage();">
        <option value="en" <c:if test="${pageContext.response.locale == 'en'}">selected="selected"</c:if>>English</option>
        <option value="sv" <c:if test="${pageContext.response.locale == 'sv'}">selected="selected"</c:if>>Svenska</option>
    </select>
</div>
<div class="span-24 headerPane logo1" id="headerPane">
	<div id="divHeaderLogo" onclick="goHome();">
		<img src="../i/transparent.gif" alt="Mybetpals Logo" />
	</div>
</div>
