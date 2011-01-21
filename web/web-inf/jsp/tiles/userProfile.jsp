<%@include file="includes.jsp"%>
<div>
    <h2 class="dark">User</h2>
</div>
<div class="rbDiv contentDiv">
    <h4>User</h4>
    <div class="span-12">
        <div class="span-2 labelDiv"><img class="userPic" src='<c:url value="/images/users/${userProfile.userId}.jpg"/>'/></div>
        <div style="padding-top: 5px; margin-bottom: 10px;" class="span-10 last">
            <h5>${userProfile.fullName}</h5>
            <spring:message code="${userProfile.country}"/>
           <br/>
           <p>${userProfile.bio}</p>
        </div>
    </div>
    <p>&nbsp;</p>
    <h4>Reputation</h4>
    <table class="palsTable reputationTable">
         <tr>
             <th>&nbsp;Competitions created</th>
             <th>Participated</th>
             <th>Satisfaction</th>
         </tr>
         <tr class="odd">
             <td>&nbsp;${totalCompetitions}</td>
             <td>${totalBets}</td>
             <td>100%</td>
         </tr>
     </table>
    
    <p>&nbsp;</p>
</div>
