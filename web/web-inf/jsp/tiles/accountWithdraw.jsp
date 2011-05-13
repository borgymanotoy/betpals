<%@include file="includes.jsp"%>
<div>
    <h2 class="dark"><spring:message code="account.withdraw.title"/></h2>
</div>
<div class="rbDiv contentDiv">
    <h4><spring:message code="account.withdraw"/></h4>
    <p><spring:message code="account.withdraw.message"/></p>
    <c:url var="accountWithdrawURL" value="/accountwithdraw.html"/>
    <form action="${accountWithdrawURL}" method="post">
        <span id="selectCurrencySpan"><spring:message code="account.amount"/></span>
        <input type="hidden" name="accountId" value="${accountId}"/>
        <input type="text" name="amount"/>
        <input type="submit" class="blueButton110" value="<spring:message code='account.withdraw'/>"/>
    </form>
   <p>&nbsp;</p>
    <p>&nbsp;</p>
</div>