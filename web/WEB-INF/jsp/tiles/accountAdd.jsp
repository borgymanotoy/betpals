<%@include file="includes.jsp"%>
<div>
    <h2 class="dark"><spring:message code="account.page.title"/></h2>
</div>
<div class="rbDiv contentDiv">
    <h4><spring:message code="account.add.new"/></h4>
    <p><spring:message code="account.add.new.message"/></p>
    <c:choose>
        <c:when test="${empty supportedCurrencies}">
            <p><spring:message code="account.already.have.message"/></p>
        </c:when>
        <c:otherwise>
		    <c:url var="addAccountURL" value="/addaccount.html"/>
		    <form:form commandName="account" action="${addAccountURL}" method="post">
		        <span id="selectCurrencySpan"><spring:message code="account.currency"/></span>
		        <form:select path="currency" id="selectCurrencyInput">
		            <form:options items="${supportedCurrencies}"/>
		        </form:select>
		    <input type="submit" class="blueButton110" value="<spring:message code='account.add.new'/>"/>
		    </form:form>
        </c:otherwise>
    </c:choose>
    <p>&nbsp;</p>
    <p>&nbsp;</p>
</div>