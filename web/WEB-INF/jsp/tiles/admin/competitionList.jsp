<%@include file="includes.jsp"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<script type="text/javascript">
    jQuery(document).ready(function() {

        jQuery("tr", "#competitionListTable tbody").hover(
             function () {
               jQuery(this).addClass("palsTableHover clickable");
             }, 
             function () {
               jQuery(this).removeClass("palsTableHover clickable");
             }
        );

        jQuery("tr", "#competitionListTable tbody").click(
             function () {
               var userId = jQuery(".competitionIdCell", this).text();
               jQuery('#competitionIdToView').val(userId);
               jQuery('#viewCompetitionForm').submit();
             } 
        );

    });
    
</script>
<div>
    <h2 class="dark_long"><spring:message code="competition.list.title"/></h2>
</div>
<div class="rbDiv contentDiv">
     <display:table requestURI="/admin/listcompetitions.html" name="competitionList" 
        id="competitionListTable" class="palsTable"  
        decorator="se.telescopesoftware.betpals.web.decorators.CompetitionTableDecorator">
         <display:column class="userListColumn" property="name" title="Name" sortable="true"/>
         <display:column class="userListColumn" property="owner" title="Owner" sortable="true"/>
         <display:column class="userListColumn" property="status" title="Status" sortable="true"/>
         <display:column class="userListColumn" property="deadline" title="Deadline" sortable="true"/>
         <display:column class="userListColumn" property="settlingDeadline" title="Settling deadline" sortable="true"/>
         <display:column class="userListColumn" property="currency" title="Currency" sortable="true"/>
         <display:column class="userListColumn" property="competitionType" title="Type" sortable="true"/>
         <display:column class="userListColumn" property="numberOfParticipants" title="Participants" sortable="true"/>
         <display:column class="userListColumn" property="turnover" title="Turnover" sortable="true"/>
         <display:column class="competitionIdCell userListColumn" property="id" style="display: none;" title=""/>
     </display:table>
   
</div>
<form action='<c:url value="/admin/viewcompetition.html"/>' method="post" id="viewCompetitionForm">
    <input type="hidden" name="competitionId" value="" id="competitionIdToView"/>
</form>
