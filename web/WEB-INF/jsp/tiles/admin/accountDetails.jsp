<%@include file="includes.jsp"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<script type="text/javascript">
	jQuery(document).ready(function() {
	
	    jQuery("tr", "#activeBetsTable tbody").hover(
	         function () {
	           jQuery(this).addClass("palsTableHover clickable");
	         }, 
	         function () {
	           jQuery(this).removeClass("palsTableHover clickable");
	         }
	    );
	
	    jQuery("tr", "#activeBetsTable tbody").click(
	         function () {
	           var userId = jQuery(".betIdCell", this).text();
	           var competitionId = jQuery(".competitionIdCell", this).text();
	           jQuery('#competitionToManage', '#viewActiveCompetitionForm').val("/ongoingcompetition.html?competitionId=" + competitionId);
	           jQuery('#viewActiveCompetitionForm').submit();
	         } 
	    );
	    
	    jQuery("tr", "#settledBetsTable tbody").hover(
	         function () {
	           jQuery(this).addClass("palsTableHover clickable");
	         }, 
	         function () {
	           jQuery(this).removeClass("palsTableHover clickable");
	         }
	    );
	
	    jQuery("tr", "#settledBetsTable tbody").click(
	         function () {
	           var userId = jQuery(".betIdCell", this).text();
	           var competitionId = jQuery(".competitionIdCell", this).text();
	           jQuery('#competitionToManage', '#viewSettledCompetitionForm').val("/settledcompetition.html?competitionId=" + competitionId);
	           jQuery('#viewSettledCompetitionForm').submit();
	         } 
	    );
	
	});

   function setAsDefaultAccount() {
       jQuery('#defaultAccountForm').submit();
   } 

   function viewSettledCompetition(competitionId) {
       jQuery('#competitionToManage', '#viewSettledCompetitionForm').val("/settledcompetition.html?competitionId=" + competitionId);
       jQuery('#viewSettledCompetitionForm').submit();
   } 

   function viewActiveCompetition(competitionId) {
       jQuery('#competitionToManage', '#viewActiveCompetitionForm').val("/ongoingcompetition.html?competitionId=" + competitionId);
       jQuery('#viewActiveCompetitionForm').submit();
   } 

</script>
<div>
    <h2 class="dark_long"><spring:message code="account.details.title"/> (${account.currency}) - ${userProfile.fullName}</h2>
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
                <form action='<c:url value="/admin/accountdeposit.html"/>' method="post">
                    <input type="text" name="amount" size="10"/>
                    <input type="hidden" name="accountId" value="${account.id}"/>
                    <button class="whiteButton90" onclick="submit();"><spring:message code="account.deposit"/></button>
                </form>
                </td>
                <td class="buttonCell">
                <form action='<c:url value="/admin/accountwithdraw.html"/>' method="post">
                    <input type="text" name="amount" size="10"/>
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
         <display:table requestURI="/admin/accountdetails.html#transactions" name="account.transactions" 
        id="transactionListTable" class="palsTable" pagesize="20"
        decorator="se.telescopesoftware.betpals.web.decorators.TransactionTableDecorator" >
     <display:setProperty name="paging.banner.item_name" value="transaction"/>
     <display:setProperty name="paging.banner.items_name" value="transactions"/>
         <display:column class="userListColumn" property="id" sortable="true"/>
         <display:column class="userListColumn" property="transactionType" sortable="true"/>
         <display:column class="userListColumn" property="description"/>
         <display:column class="userListColumn" property="amount" sortable="true"/>
         <display:column class="userListColumn" property="transactionDate" sortable="true"/>
    </display:table>
        <p>&nbsp;</p>
    </div>
    <div id="activeBets" class="contentDiv">
     <display:table requestURI="/admin/accountdetails.html#activeBets" name="activeBets" 
        id="activeBetsTable" class="palsTable" pagesize="20"
        decorator="se.telescopesoftware.betpals.web.decorators.BetTableDecorator" >
     <display:setProperty name="paging.banner.item_name" value="bet"/>
     <display:setProperty name="paging.banner.items_name" value="bets"/>
         <display:column class="betIdCell userListColumn" property="id" sortable="true"/>
         <display:column class="userListColumn" property="stake" sortable="true"/>
         <display:column class="userListColumn" property="details"/>
         <display:column class="userListColumn" property="placed" sortable="true"/>
         <display:column class="competitionIdCell" property="alternative.event.competition.id" style="display: none;" title=""/>
    </display:table>
        <p>&nbsp;</p>
    </div>
    <div id="settledBets" class="contentDiv">
     <display:table requestURI="/admin/accountdetails.html#settledBets" name="settledBets" 
        id="settledBetsTable" class="palsTable" pagesize="20"
        decorator="se.telescopesoftware.betpals.web.decorators.BetTableDecorator" >
     <display:setProperty name="paging.banner.item_name" value="bet"/>
     <display:setProperty name="paging.banner.items_name" value="bets"/>
         <display:column class="betIdCell userListColumn" property="id" sortable="true"/>
         <display:column class="userListColumn" property="stake" sortable="true"/>
         <display:column class="userListColumn" property="details"/>
         <display:column class="userListColumn" property="placed" sortable="true"/>
         <display:column class="userListColumn" property="profitOrLoss" sortable="true"/>
         <display:column class="userListColumn" property="settled" sortable="true"/>
         <display:column class="competitionIdCell" property="alternative.event.competition.id" style="display: none;" title=""/>
    </display:table>
        <p>&nbsp;</p>
    </div>
</div>
<form action='<c:url value="/admin/accountsetdefault.html"/>' method="post" id="defaultAccountForm">
    <input type="hidden" name="accountId" value="${account.id}"/>
</form>
<form action='<c:url value="/j_spring_security_switch_user"/>' method="post" id="viewActiveCompetitionForm">
    <input type="hidden" name="j_username" value="${userProfile.user.username}"/>
    <input type="hidden" name="targetUrl" value="" id="competitionToManage"/>
    <input type="hidden" name="returnUrl" value="/admin/accountdetails.html?accountId=${account.id}#activeBets" />
</form>
<form action='<c:url value="/j_spring_security_switch_user"/>' method="post" id="viewSettledCompetitionForm">
    <input type="hidden" name="j_username" value="${userProfile.user.username}"/>
    <input type="hidden" name="targetUrl" value="" id="competitionToManage"/>
    <input type="hidden" name="returnUrl" value="/admin/accountdetails.html?accountId=${account.id}#settledBets" />
</form>


