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
        <button id="editProfileButton" onclick="window.location = '<c:url value="/editprofile.html"/>'"><spring:message code="button.edit.profile"/></button>
    </div>
    <div id="leftBlock" class="span-5">
        <div class="leftPaneInside"><img src='<c:url value="/i/separator.jpg"/>'/></div>
        <form action='<c:url value="/accountdetails.html"/>' method="post" id="accountDetailsForm">
            <input type="hidden" name="accountId" value=""/>
        </form>
	    <table id="accountInfoTable" class="palsTable altRows">
	        <tr>
	            <th></th>
	            <th><spring:message code="account.balance"/></th>
	            <th><spring:message code="account.available"/></th>
	        </tr>
	    <c:forEach items="${accounts}" var="account">
	        <tr onclick="getAccountDetails(${account.id});">
	            <td class="currencyCell">${account.currency}</td>
	            <td><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2">${account.balance}</fmt:formatNumber></td>
	            <td><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2">${account.available}</fmt:formatNumber></td>
	        </tr>
	    </c:forEach>    
	    </table>
	    <div class="leftPaneInside"><a class="greenDotLink" href='<c:url value="/addaccount.html"/>'><spring:message code="button.add.account"/></a></div>
	    <br/>
	    <div class="orangeTitle"><spring:message code="competitions.pane.title"/></div>
	    <div class="panelNoPadding">
	        <ul id="invitesAndCompetitions">
	           <li><a href='<c:url value="/invitations.html"/>'><spring:message code="link.invitations"/></a> (${myInvitationsCount})</li>
               <li><a href='<c:url value="/ongoingcompetitions.html"/>'><spring:message code="link.ongoing.competitions"/></a> (${myOngoingCompetitionsCount})</li>
               <li><a href='<c:url value="/managecompetitions.html"/>'><spring:message code="link.manage.competitions"/></a> (${myCompetitionsCount})</li>
               <li><a href='<c:url value="/home.html"/>'><spring:message code="link.search.competitions"/></a></li>
	        </ul>
	    </div>
	    <div class="panelFooter">&nbsp;</div>
	    <div class="greenTitle"><spring:message code="friends.pane.title"/></div>
	    <div class="panel">
	        &nbsp;<br/>
	        <a class="greenDotLink" href='<c:url value="/allfriends.html"/>'><spring:message code="link.all.friends"/></a><br/>
	        <a class="greenDotLink" href='<c:url value="/myrequests.html"/>'><spring:message code="link.new.requests"/> (${myRequestsCount})</a><br/>
	        <ul id="friendsSideList">
	        <c:forEach items="${friendsSideList}" var="friend">
	           <li>
	               <table id="friendsTable">
	                   <tr>
		                   <td rowspan="2" width="56px">
		                      <a href='<c:url value="/viewprofile/${friend.userId}.html"/>'>
		                       <img class="userPic" src='<c:url value="/images/users/${friend.userId}.jpg"/>'/>
		                      </a> 
		                   </td>
		                   <td class="bottom">
		                      <a class="noline" style="color: #88898A !important;" href='<c:url value="/viewprofile/${friend.userId}.html"/>'>
	    	                   ${friend.name}&nbsp;
	    	                  </a>
	    	               </td>
	                   </tr>
	                   <tr>
	                       <td class="top">
                              <a class="noline" style="color: #88898A !important;" href='<c:url value="/viewprofile/${friend.userId}.html"/>'>
	                          ${friend.surname}&nbsp;
	                          </a>
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