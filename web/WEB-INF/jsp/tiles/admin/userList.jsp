<%@include file="includes.jsp"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<script type="text/javascript">
    jQuery(document).ready(function() {

        jQuery("tr", "#userListTable tbody").hover(
             function () {
               jQuery(this).addClass("palsTableHover clickable");
             }, 
             function () {
               jQuery(this).removeClass("palsTableHover clickable");
             }
        );

        jQuery("tr", "#userListTable tbody").click(
             function () {
               var userId = jQuery(".userIdCell", this).text();
               jQuery('#userIdToView').val(userId);
               jQuery('#viewUserForm').submit();
             } 
        );

    });
    
</script>
<div>
    <h2 class="dark_long"><spring:message code="user.list.title"/></h2>
</div>
<div class="rbDiv contentDiv">
    <div style="border-bottom: 1px solid #d0d0d0; padding-top: 5px; padding-bottom: 5px; margin-bottom: 10px;">
       <form action='<c:url value="/admin/searchusers.html"/>' name="search_form" method="post">
        <input id="searchField" type="text" name="query"/>
        <button id="searchButton" type="submit">&nbsp;</button>
       </form>     
    </div>

     <display:table requestURI="/admin/listusers.html" name="userList" 
        id="userListTable" class="palsTable" pagesize="20" 
        >
         <display:column class="userListColumn" property="name" title="Name" sortable="true"/>
         <display:column class="userListColumn" property="surname" title="Surname" sortable="true"/>
         <display:column class="userListColumn" title="Last login" sortable="true">
            <fmt:formatDate value="${userListTable.lastLoginDate}" pattern="yyyy-MM-dd HH:mm"/>
         </display:column>
         <display:column class="userListColumn" title="Last placed bet" sortable="true">
            <fmt:formatDate value="${userListTable.lastBetDate}" pattern="yyyy-MM-dd HH:mm"/>
         </display:column>
         <display:column class="userListColumn" title="Last created bet" sortable="true">
            <fmt:formatDate value="${userListTable.lastCompetitionDate}" pattern="yyyy-MM-dd HH:mm"/>
         </display:column>
         <display:column class="userListColumn" property="numberOfVisits" title="logins" sortable="true"/>
         <display:column class="userListColumn" property="betsCount" title="placed bets" sortable="true"/>
         <display:column class="userListColumn" property="competitionsCount" title="created bets" sortable="true"/>
         <display:column class="userListColumn" title="">
         <c:if test="${userListTable.user.enabled == false}"><img src='<c:url value="/i/lock.gif"/>'/></c:if>
         </display:column>
         <display:column class="userIdCell userListColumn" property="id" style="display: none;" title=""/>
     </display:table>
   
</div>
<form action='<c:url value="/admin/viewuser.html"/>' method="post" id="viewUserForm">
    <input type="hidden" name="userId" value="" id="userIdToView"/>
</form>
