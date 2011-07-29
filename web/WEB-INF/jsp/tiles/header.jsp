<%@include file="includes.jsp"%>
<div class="span-18 colorDiv">
    &nbsp;
    <a href="javascript:changeBgColor('#242b21', 'logo4');"><img src='<c:url value="/i/c1.jpg"/>'/></a>
    <a href="javascript:changeBgColor('#001d44', 'logo3');"><img src='<c:url value="/i/c2.jpg"/>'/></a>
    <a href="javascript:changeBgColor('#67b116', 'logo1');"><img src='<c:url value="/i/c3.jpg"/>'/></a>
    <a href="javascript:changeBgColor('#297085', 'logo2');"><img src='<c:url value="/i/c4.jpg"/>'/></a>
</div>
<div class="span-3 right" style="padding-top: 43px;">
    <security:authorize ifAllGranted="ROLE_PREVIOUS_ADMINISTRATOR">
    <c:url value="/j_spring_security_exit_user" var="exitURL">
        <c:param name="targetUrl" value="${switchUserReturnUrl}"/>
        <c:param name="exitUserId" value="${user.userId}"/>
    </c:url>
    <a class="whiteDotLink white noline" href="${exitURL}"><spring:message code="exit"/></a>&nbsp;
    </security:authorize>
    <security:authorize ifNotGranted="ROLE_PREVIOUS_ADMINISTRATOR">
    <a class="whiteDotLink white noline" href='<c:url value="/j_spring_security_logout"/>'><spring:message code="logout"/></a>&nbsp;
    </security:authorize>
</div>
<div class="span-3 last left" style="padding-top: 42px;">
    <select id="langSelector" style="background-color: #67b116; color: white; margin: 0; font-size: 12px;" onchange="changeLanguage();">
        <option value="en" <c:if test="${pageContext.response.locale == 'en'}">selected="selected"</c:if>>English</option>
        <option value="sv" <c:if test="${pageContext.response.locale == 'sv'}">selected="selected"</c:if>>Svenska</option>
    </select>
</div>
<div class="span-24 headerPane logo1" id="headerPane" onclick="goHome();">
    &nbsp;
</div>