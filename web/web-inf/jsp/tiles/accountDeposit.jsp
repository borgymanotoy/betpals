<%@include file="includes.jsp"%>
<div>
    <h2 class="dark">Account Deposit</h2>
</div>
<div class="ui-widget-content ui-corner-all contentDiv">
    <h4>Deposit to account</h4>
    <p>Here you can deposit some money to your account. This is a temporary page, and should be replaced with some payment site form. Paypal or similar.</p>
    <c:url var="accountDepositURL" value="/accountdeposit.html"/>
    <form action="${accountDepositURL}" method="post">
        <span id="selectCurrencySpan">Amount</span>
        <input type="hidden" name="accountId" value="${accountId}"/>
        <input type="text" name="amount"/>
        <input type="submit" class="blueButton110" value="Deposit to account"/>
    </form>
   <p>&nbsp;</p>
    <p>&nbsp;</p>
</div>