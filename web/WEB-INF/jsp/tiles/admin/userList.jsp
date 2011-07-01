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
     <display:table requestURI="/admin/listusers.html" name="userList" 
        id="userListTable" class="palsTable"  
        decorator="se.telescopesoftware.betpals.web.decorators.UserTableDecorator">
         <display:column class="userIdCell userListColumn" property="id" title="Id" sortable="true"/>
         <display:column class="userListColumn" property="name" title="Name" sortable="true"/>
         <display:column class="userListColumn" property="surname" title="Surname" sortable="true"/>
         <display:column class="userListColumn" property="email" title="Email" sortable="true"/>
         <display:column class="userListColumn" property="city" title="City" sortable="true"/>
         <display:column class="userListColumn" title="Country" sortable="true"><spring:message code="${userListTable.country}"/></display:column>
         <display:column class="userListColumn" title="">
         <c:if test="${userListTable.user.enabled == false}"><img src='<c:url value="/i/lock.gif"/>'/></c:if>
         </display:column>
     </display:table>
   
</div>
<form action='<c:url value="/admin/viewuser.html"/>' method="post" id="viewUserForm">
    <input type="hidden" name="userId" value="" id="userIdToView"/>
</form>
