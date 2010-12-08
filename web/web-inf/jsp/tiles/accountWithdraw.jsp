<%@include file="includes.jsp"%>
<div>
    <h2 class="dark">Account Withdraw</h2>
</div>
<div class="ui-widget-content ui-corner-all contentDiv">
    <h4>Withdraw from account</h4>
    <p>Here you can withdraw money from your account. This is a temporary page, and should be integrated with some payment system. </p>
    <c:url var="accountWithdrawURL" value="/accountwithdraw.html"/>
    <form action="${accountWithdrawURL}" method="post">
        <span id="selectCurrencySpan">Amount</span>
        <input type="hidden" name="accountId" value="${accountId}"/>
        <input type="text" name="amount"/>
        <input type="submit" class="blueButton110" value="Withdraw from account"/>
    </form>
   <p>&nbsp;</p>
    <p>&nbsp;</p>
</div>