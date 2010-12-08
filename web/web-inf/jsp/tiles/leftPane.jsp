<%@include file="includes.jsp"%>
<script type="text/javascript">
    function getAccountDetails(accountId) {
        jQuery('input', '#accountDetailsForm').val(accountId);
        jQuery('#accountDetailsForm').submit();
    } 

</script>
<div id="leftPane" class="span-5">
    <div class="span-2 right">
        <c:url var="userPictureURL" value="/images/users/${user.userId}.jpg"/>
        <img class="userPic" src="${userPictureURL}"/>
        <img src='<c:url value="/i/upbt.jpg"/>'/>
    </div>
    <div class="span-3 last">
        <h5><security:authentication property="principal.userProfile.name"/>&nbsp;</h5>
        <h5><security:authentication property="principal.userProfile.surname"/>&nbsp;</h5>
        <button id="editProfileButton" onclick="window.location = '<c:url value="/editprofile.html"/>'">Edit profile</button>
    </div>
    <div id="leftBlock" class="span-5">
        <div class="leftPaneInside"><img src='<c:url value="/i/separator.jpg"/>'/></div>
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
	            <td>${account.balance}</td>
	            <td>${account.available}</td>
	        </tr>
	    </c:forEach>    
	    </table>
	    <div class="leftPaneInside"><a class="greenDotLink" href='<c:url value="/addaccount.html"/>'>Add account</a></div>
	    <br/>
	    <div class="orangeTitle">Invites and competitions</div>
	    <div class="panelNoPadding">
	        <ul id="invitesAndCompetitions">
	           <li><a href='<c:url value="/home.html"/>'>Active invites</a> (0)</li>
               <li><a href='<c:url value="/home.html"/>'>Ongoing competitions</a> (0)</li>
               <li><a href='<c:url value="/home.html"/>'>Manage competitions</a> (0)</li>
               <li><a href='<c:url value="/home.html"/>'>Search public competitions</a></li>
	        </ul>
	    </div>
	    <div class="panelFooter">&nbsp;</div>
	    <div class="greenTitle">Friends and groups</div>
	    <div class="panel">
	        &nbsp;<br/>
	        <a class="greenDotLink" href='<c:url value="/allfriends.html"/>'>All friends</a><br/>
	        <ul id="friendsSideList">
	        <c:forEach items="${friendsSideList}" var="friend">
	           <li>
	               <table id="friendsTable">
	                   <tr>
		                   <td rowspan="2" width="56px">
		                       <img class="userPic" src='<c:url value="/images/users/${friend.userId}.jpg"/>'/>
		                   </td>
		                   <td class="bottom">
	    	                   ${friend.name}&nbsp;
	    	               </td>
	                   </tr>
	                   <tr>
	                       <td class="top">
	                          ${friend.surname}&nbsp;
	                       </td>
                       </tr>
	               </table>
	           </li>
	        </c:forEach>
	        </ul>
	    </div>
	    <div class="panelFooter">&nbsp;</div>
    </div>
</div>