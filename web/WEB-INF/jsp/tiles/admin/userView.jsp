<%@include file="includes.jsp"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<script type="text/javascript">
    jQuery(document).ready(function() {

        jQuery("tr", "#accountsTable tbody").hover(
                function () {
                  jQuery(this).addClass("palsTableHover clickable");
                }, 
                function () {
                  jQuery(this).removeClass("palsTableHover clickable");
                }
           );

    	jQuery("#confirmDelete").dialog({
            resizable: false,
            autoOpen: false,
            height:160,
            modal: true,
            buttons: {
                "<spring:message code='button.delete'/>": function() {
                    //jQuery('#deleteUserForm').submit();
                    $( this ).dialog( "close" );
                    alert("Delete user is not yet implemented, because it's not clear what to do with user accounts and bets. This should be clarified.");
                },
                "<spring:message code='button.cancel'/>": function() {
                    $( this ).dialog( "close" );
                }
            }
        });

        jQuery(".logEntry").click(function() {
            jQuery(".logInfo", this).toggle();
            jQuery(this).toggleClass("bold");
        });
        
        jQuery(".logEntry").hover(
              function () {
                jQuery(this).addClass("logHover");
              }, 
              function () {
                jQuery(this).removeClass("logHover");
              }
            );
    	
    });

    function deleteUser() {
        jQuery("#confirmDelete").dialog('open');
    } 
    
    function blockUser() {
        jQuery('#blockUserForm').submit();
    } 
    
    function switchUser() {
        jQuery('#switchUserForm').submit();
    } 
    
    function getAccountDetails(accountId) {
        jQuery('input', '#accountDetailsForm').val(accountId);
        jQuery('#accountDetailsForm').submit();
    } 
    
</script>
<div>
    <h2 class="dark_long"><spring:message code="user.view.title"/>&nbsp;<c:out value="${userProfile.fullName}"/>&nbsp;<span style="font-weight: normal; font-size: 11px;">(<spring:message code="profile.last.login.date"/>: <fmt:formatDate value="${userProfile.lastLoginDate}" pattern="yyyy-MM-dd HH:mm"/>)</span></h2>
</div>
<div class="rbDiv contentDiv">
    <div class="span-16">
         <div class="span-2 labelDiv"><img class="userPic" src='<c:url value="/users/images/${userProfile.userId}.jpeg"/>'/></div>
         <div class="span-14 last">
             <div class="span-7">
                <spring:message code="profile.edit.name"/>: ${userProfile.name}<br/>
                <spring:message code="profile.edit.surname"/>: ${userProfile.surname}<br/>
                <spring:message code="profile.edit.email"/>: ${userProfile.email}<br/>
                <spring:message code="profile.registration.date"/>: <fmt:formatDate value="${userProfile.registrationDate}" pattern="yyyy-MM-dd HH:mm"/>
             </div>
             <div class="span-7 last">
                <spring:message code="profile.edit.address"/>: ${userProfile.address}<br/>
                <spring:message code="profile.edit.city"/>: ${userProfile.city}<br/>
                <spring:message code="profile.edit.zip"/>: ${userProfile.postalCode}<br/>
                <spring:message code="profile.edit.country"/>: <spring:message code="${userProfile.country}"/><br/>   
             </div>
         </div>
    </div>
    <div class="span-16 formSectionSlimDiv" style="padding-top: 10px;">
       <spring:message code="profile.edit.bio"/>: ${userProfile.bio}
    </div>
    <div class="span-16 formSectionSlimDiv" style="padding-top: 10px;">
         <button class="blueButton90" onclick="blockUser();return false;">
         <c:choose>
            <c:when test="${userProfile.user.enabled == false}"><spring:message code="button.unblock"/></c:when>
            <c:otherwise><spring:message code="button.block"/></c:otherwise>
         </c:choose>
            
         </button> 
         <c:if test="${userProfile.user.enabled == true}">
         <button class="greenButton110" onclick="switchUser();return false;"><spring:message code="button.login.as.user"/></button>
         </c:if> 
         <button class="whiteButton90" onclick="deleteUser();return false;"><spring:message code="button.delete"/></button> 
    </div>
    <div class="span-16 formSectionSlimDiv" style="padding-top: 16px;">
       <h4><spring:message code="admin.user.accounts.title"/></h4>
       <table class="palsTable altRows" id="accountsTable">
        <thead>
            <tr>
                <th><spring:message code="account.currency"/>&nbsp;</th>
                <th><spring:message code="account.balance"/></th>
                <th><spring:message code="account.available"/></th>
            </tr>
         </thead>
        <c:forEach items="${userAccountList}" var="account">
            <tr onclick="getAccountDetails(${account.id});">
                <td class="currencyCell">${account.currency}</td>
                <td><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2">${account.balance}</fmt:formatNumber></td>
                <td><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2">${account.available}</fmt:formatNumber></td>
            </tr>
        </c:forEach>    
        </table>
       <p>&nbsp;</p>
    </div>
    <div class="span-16 formSectionSlimDiv" style="padding-top: 16px;">
       <h4><spring:message code="admin.user.log.title"/></h4>
    <ul class="logList">
        <c:forEach items="${userLog}" var="entry">
            <li class="logEntry clickable">
                <fmt:formatDate value="${entry.created}" pattern="yyyy-MM-dd HH:mm"/> <c:out value="${entry.shortMessage}"/>
                <div class="logInfo">
                <span class="logFieldTitle">Date</span>: <fmt:formatDate value="${entry.created}" pattern="yyyy-MM-dd HH:mm:ss"/><br/>
                <span class="logFieldTitle">Message</span>: <c:out value="${entry.message}"/><br/>
                </div>
            </li>
        </c:forEach>
    </ul>&nbsp;
    </div>   
   <p>&nbsp;</p>
</div>
<div id="confirmDelete" title="<spring:message code='confirmation.delete.user.title'/>">
    <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span><spring:message code="confirmation.delete.user" arguments="${userProfile.fullName}"/></p>
</div>

<form action='<c:url value="/admin/deleteuser.html"/>' method="post" id="deleteUserForm">
    <input type="hidden" name="userId" value="${userProfile.userId}"/>
</form>
<form action='<c:url value="/j_spring_security_switch_user"/>' method="post" id="switchUserForm" target="_blank">
    <input type="hidden" name="j_username" value="${userProfile.user.username}"/>
</form>
<form action='<c:url value="/admin/blockuser.html"/>' method="post" id="blockUserForm">
    <input type="hidden" name="userId" value="${userProfile.userId}"/>
</form>
<form action='<c:url value="/admin/accountdetails.html"/>' method="post" id="accountDetailsForm">
    <input type="hidden" name="accountId" value=""/>
</form>

