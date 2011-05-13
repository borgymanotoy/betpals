<%@include file="includes.jsp"%>
<div>
    <h2 class="dark"><spring:message code="profile.view.title"/></h2>
</div>
<div class="rbDiv contentDiv">
    <h4><spring:message code="profile.view.header"/></h4>
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
    <h4><spring:message code="profile.reputation"/></h4>
    <table class="palsTable reputationTable">
         <tr>
             <th>&nbsp;<spring:message code="profile.competitions.created"/></th>
             <th><spring:message code="profile.competitions.participated"/></th>
             <th><spring:message code="profile.satisfaction"/></th>
         </tr>
         <tr class="odd">
             <td>&nbsp;${totalCompetitions}</td>
             <td>${totalBets}</td>
             <td>100%</td>
         </tr>
     </table>
    
    <p>&nbsp;</p>
</div>
