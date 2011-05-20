<%@include file="includes.jsp"%>
<script type="text/javascript">
   function setAsDefaultAccount() {
       jQuery('#defaultAccountForm').submit();
   } 

   function viewSettledCompetition(competitionId) {
       jQuery('input', '#viewSettledCompetitionForm').val(competitionId);
       jQuery('#viewSettledCompetitionForm').submit();
   } 

   function viewActiveCompetition(competitionId) {
       jQuery('input', '#viewActiveCompetitionForm').val(competitionId);
       jQuery('#viewActiveCompetitionForm').submit();
   } 

</script>
<div>
    <h2 class="dark"><spring:message code="account.details.title"/> (${account.currency})</h2>
</div>
<div id="tabs" class="rbDiv">
    <div class="contentDiv">
        <h4>${account.currency} <spring:message code="account"/> 
        <c:choose>
            <c:when test="${account.defaultAccount}">(<spring:message code="account.default"/>)</c:when>
            <c:otherwise>(<a href="#" onclick="setAsDefaultAccount()"><spring:message code="account.make.default"/></a>)</c:otherwise>
        </c:choose>
        </h4>
        <table id="accountDetailsTable" class="palsTable">
            <tr>
                <th><spring:message code="account.balance"/></th>
                <th><spring:message code="account.available"/></th>
                <th></th>
                <th></th>
            </tr>
            <tr>
                <td>${account.balance}</td>
                <td>${account.available}</td>
                <td class="buttonCell">
                <form action='<c:url value="/accountdepositview.html"/>' method="post">
                    <input type="hidden" name="accountId" value="${account.id}"/>
                    <button class="whiteButton90" onclick="submit();"><spring:message code="account.deposit"/></button>
                </form>
                </td>
                <td class="buttonCell">
                <form action='<c:url value="/accountwithdrawview.html"/>' method="post">
                    <input type="hidden" name="accountId" value="${account.id}"/>
                    <button class="whiteButton90" onclick="submit();"><spring:message code="account.withdraw"/></button>
                </form>
                </td>
            </tr>
        </table>
    </div>
    <ul class="palsTabs">
        <li><a href="#transactions"><spring:message code="account.transactions"/></a></li>
        <li><a href="#activeBets"><spring:message code="account.active.bets"/></a></li>
        <li><a href="#settledBets"><spring:message code="account.settled.bets"/></a></li>
    </ul>
    <div id="transactions" class="contentDiv">
        <table id="transactionTable" class="palsTable altRows">
            <tr>
                <th><spring:message code="account.transaction.table.id"/></th>
                <th><spring:message code="account.transaction.table.type"/></th>
                <th><spring:message code="account.transaction.table.details"/></th>
                <th><spring:message code="account.transaction.table.amount"/></th>
                <th><spring:message code="account.transaction.table.created"/></th>
            </tr>
            <c:forEach items="${account.transactions}" var="transaction">
            <tr>
                <td>${transaction.id}</td>
                <td>${transaction.transactionType}</td><!-- //TODO: change to message resource -->
                <td>${transaction.description}</td>
                <td><fmt:formatNumber value="${transaction.amount}" maxFractionDigits="2" minFractionDigits="2"/></td>
                <td><fmt:formatDate value="${transaction.transactionDate}" pattern="yyyy-MM-dd HH:mm"/></td>
            </tr>
            </c:forEach>
        </table>
        <p>&nbsp;</p>
    </div>
    <div id="activeBets" class="contentDiv">
        <table id="transactionTable" class="palsTable altRows">
            <tr>
                <th><spring:message code="account.transaction.table.id"/></th>
                <th><spring:message code="account.transaction.table.stake"/></th>
                <th><spring:message code="account.transaction.table.details"/></th>
                <th><spring:message code="account.transaction.table.placed"/></th>
            </tr>
            <c:forEach items="${activeBets}" var="activeBet">
            <tr>
                <td>${activeBet.id}</td>
                <td><fmt:formatNumber value="${activeBet.stake}" maxFractionDigits="2" minFractionDigits="2"/></td>
                <td><span class="clickable" onclick="viewActiveCompetition(${activeBet.alternative.event.competition.id});">${activeBet.details}</span></td>
                <td><fmt:formatDate value="${activeBet.placed}" pattern="yyyy-MM-dd HH:mm"/></td>
            </tr>
            </c:forEach>
        </table>
        <p>&nbsp;</p>
    </div>
    <div id="settledBets" class="contentDiv">
        <table id="transactionTable" class="palsTable altRows">
            <tr>
                <th><spring:message code="account.transaction.table.id"/></th>
                <th><spring:message code="account.transaction.table.stake"/></th>
                <th><spring:message code="account.transaction.table.details"/></th>
                <th><spring:message code="account.transaction.table.placed"/></th>
                <th><spring:message code="account.transaction.table.profitloss"/></th>
                <th><spring:message code="account.transaction.table.settled"/></th>
            </tr>
            <c:forEach items="${settledBets}" var="settledBet">
            <tr>
                <td>${settledBet.id}</td>
                <td><fmt:formatNumber value="${settledBet.stake}" maxFractionDigits="2" minFractionDigits="2"/></td>
                <td><span class="clickable" onclick="viewSettledCompetition(${settledBet.alternative.event.competition.id});">${settledBet.details}</span></td>
                <td><fmt:formatDate value="${settledBet.placed}" pattern="yyyy-MM-dd HH:mm"/></td>
                <td><fmt:formatNumber value="${settledBet.profitOrLoss}" maxFractionDigits="2" minFractionDigits="2"/></td>
                <td><fmt:formatDate value="${settledBet.settled}" pattern="yyyy-MM-dd HH:mm"/></td>
            </tr>
            </c:forEach>
        </table>
        <p>&nbsp;</p>
    </div>
</div>
<form action='<c:url value="/accountsetdefault.html"/>' method="post" id="defaultAccountForm">
    <input type="hidden" name="accountId" value="${account.id}"/>
</form>
<form action='<c:url value="/settledcompetition.html"/>' method="post" id="viewSettledCompetitionForm">
    <input type="hidden" name="competitionId" value="" id="competitionToManage"/>
</form>
<form action='<c:url value="/ongoingcompetition.html"/>' method="post" id="viewActiveCompetitionForm">
    <input type="hidden" name="competitionId" value="" id="competitionToManage"/>
</form>

