<%@include file="includes.jsp"%>
<div>
    <h2 class="dark">Account</h2>
</div>
<div class="rbDiv contentDiv">
    <h4>Add new account</h4>
    <p>Here you can create new account in selected currency. And probably more descriptive text should be added here also.</p>
    <c:choose>
        <c:when test="${empty supportedCurrencies}">
            <p>You already have accounts in all supported currencies.</p>
        </c:when>
        <c:otherwise>
		    <c:url var="addAccountURL" value="/addaccount.html"/>
		    <form:form commandName="account" action="${addAccountURL}" method="post">
		        <span id="selectCurrencySpan">Currency</span>
		        <form:select path="currency" id="selectCurrencyInput">
		            <form:options items="${supportedCurrencies}"/>
		        </form:select>
		    <input type="submit" class="blueButton110" value="Add new account"/>
		    </form:form>
        </c:otherwise>
    </c:choose>
    <p>&nbsp;</p>
    <p>&nbsp;</p>
</div>