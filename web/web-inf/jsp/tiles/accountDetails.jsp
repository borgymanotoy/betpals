<%@include file="includes.jsp"%>
<div>
    <h2 class="dark">Account details (${account.currency})</h2>
</div>
<div id="tabs">
    <div class="contentDiv">
        <h4>${account.currency} account</h4>
        <table id="accountDetailsTable" class="palsTable">
            <tr>
                <th>Balance</th>
                <th>Available</th>
                <th></th>
                <th></th>
            </tr>
            <tr>
                <td>${account.balance}</td>
                <td>${account.available}</td>
                <td class="buttonCell">
                <form action='<c:url value="/accountdepositview.html"/>' method="post">
                    <input type="hidden" name="accountId" value="${account.id}"/>
                    <button class="whiteButton90" onclick="submit();">Deposit</button>
                </form>
                </td>
                <td class="buttonCell">
                <form action='<c:url value="/accountwithdrawview.html"/>' method="post">
                    <input type="hidden" name="accountId" value="${account.id}"/>
                    <button class="whiteButton90" onclick="submit();">Withdraw</button>
                </form>
                </td>
            </tr>
        </table>
    </div>
    <ul class="palsTabs">
        <li><a href="#transactions">Transactions</a></li>
        <li><a href="#activeBets">Active bets</a></li>
        <li><a href="#settledBets">Settled bets</a></li>
    </ul>
    <div id="transactions" class="contentDiv">
        <table id="transactionTable" class="palsTable altRows">
            <tr>
                <th>Id</th>
                <th>Type</th>
                <th>Details</th>
                <th>Amount</th>
                <th>Created</th>
            </tr>
            <c:forEach items="${account.transactions}" var="transaction">
            <tr>
                <td>${transaction.id}</td>
                <td>${transaction.transactionType}</td><!-- //TODO: change to message resource -->
                <td>${transaction.description}</td>
                <td>${transaction.amount}</td>
                <td><fmt:formatDate value="${transaction.transactionDate}" pattern="yyyy-MM-dd HH:mm"/></td>
            </tr>
            </c:forEach>
        </table>
        <p>&nbsp;</p>
    </div>
    <div id="activeBets" class="contentDiv">
        <table id="transactionTable" class="palsTable altRows">
            <tr>
                <th>Id</th>
                <th>Stake</th>
                <th>Details</th>
                <th>Placed</th>
            </tr>
            <c:forEach items="${activeBets}" var="activeBet">
            <tr>
                <td>${activeBet.id}</td>
                <td>${activeBet.stake}</td>
                <td>${activeBet.details}</td>
                <td><fmt:formatDate value="${activeBet.placed}" pattern="yyyy-MM-dd HH:mm"/></td>
            </tr>
            </c:forEach>
        </table>
        <p>&nbsp;</p>
    </div>
    <div id="settledBets" class="contentDiv">
        <table id="transactionTable" class="palsTable altRows">
            <tr>
                <th>Id</th>
                <th>Stake</th>
                <th>Details</th>
                <th>Placed</th>
                <th>Profit/Loss</th>
                <th>Settled</th>
            </tr>
        </table>
        <p>&nbsp;</p>
    </div>
</div>