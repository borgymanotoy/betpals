<%@include file="includes.jsp"%>
<script type="text/javascript">
    function getAccountDetails(accountId) {
        jQuery('input', '#accountDetailsForm').val(accountId);
        jQuery('#accountDetailsForm').submit();
    } 

</script>
<div id="leftPane" class="span-5">
    <div class="span-2 right">
        <c:url var="userPictureURL" value="/user/images/${user.userId}.jpg"/>
        <img class="userPic" src="${userPictureURL}"/>
        <img src='<c:url value="/i/upbt.jpg"/>'/>
    </div>
    <div class="span-3 last">
        <h5><security:authentication property="principal.userProfile.name"/>&nbsp;</h5>
        <h5><security:authentication property="principal.userProfile.surname"/>&nbsp;</h5>
        <button id="editProfileButton" onclick="window.location = '<c:url value=""/>'">Edit profile</button>
    </div>
    <div id="leftBlock" class="span-5">
        <div class="leftPaneInside"><img src='<c:url value="/i/separator.jpg"/>'/></div>
        <!-- 
        <form action='<c:url value="/accountdetails.html"/>' method="post" id="accountDetailsForm">
            <input type="hidden" name="accountId" value=""/>
        </form>
	    <table id="accountInfoTable" class="palsTable altRows">
	        <tr>
	            <th></th>
	            <th>Balance</th>
	            <th>Available</th>
	        </tr>
	    <c:forEach items="${accounts}" var="account">
	        <tr onclick="getAccountDetails(${account.id});">
	            <td class="currencyCell">${account.currency}</td>
	            <td><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2">${account.balance}</fmt:formatNumber></td>
	            <td><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2">${account.available}</fmt:formatNumber></td>
	        </tr>
	    </c:forEach>    
	    </table>
 -->
	    <br/>
	    <div class="orangeTitle">&nbsp;
	    </div>
	    <div class="panelNoPadding">
	    &nbsp;
	    <br/>
	    <br/>
	    <br/>
	    <br/>
	    </div>
	    <div class="panelFooter">&nbsp;</div>
	    <div class="greenTitle">&nbsp;</div>
	    <div class="panel">
	        &nbsp;<br/>
	        &nbsp;<br/>
	        &nbsp;<br/>
	        &nbsp;<br/>
	        &nbsp;<br/>
	        &nbsp;<br/>
	    </div>
	    <div class="panelFooter">&nbsp;</div>
    </div>
</div>