<%@include file="includes.jsp"%>
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
	    <table id="accountInfoTable">
	        <tr>
	            <th></th>
	            <th>Balance</th>
	            <th>Available</th>
	        </tr>
	        <tr>
	            <td class="currencyCell">EUR</td>
	            <td>300</td>
	            <td>240</td>
	        </tr>
	        <tr>
	            <td class="currencyCell">FUN</td>
	            <td>500</td>
	            <td>350</td>
	        </tr>
	    </table>
	    <div class="leftPaneInside"><a class="greenDotLink" href='<c:url value="/editaccounts.html"/>'>Edit accounts</a></div>
	    <br/>
	    <div class="orangeTitle">Invites and competitions</div>
	    <div class="panelNoPadding">
	        <ul id="invitesAndCompetitions">
	           <li><a href='<c:url value="/activeinvites.html"/>'>Active invites</a> (0)</li>
               <li><a href='<c:url value="/ongoingcompetitions.html"/>'>Ongoing competitions</a> (0)</li>
               <li><a href='<c:url value="/managecompetitions.html"/>'>Manage competitions</a> (0)</li>
               <li><a href='<c:url value="/searchcompetitions.html"/>'>Search public competitions</a></li>
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