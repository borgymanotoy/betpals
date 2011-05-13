<%@include file="includes.jsp"%>
<script type="text/javascript">
    jQuery(document).ready(function() {
    });
    
</script>
<div>
    <h2 class="dark_long">Message Resources</h2>
</div>
<div class="rbDiv contentDiv">
	<span>
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=a"/>'>A</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=b"/>'>B</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=c"/>'>C</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=d"/>'>D</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=e"/>'>E</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=f"/>'>F</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=g"/>'>G</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=h"/>'>H</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=i"/>'>I</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=j"/>'>J</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=k"/>'>K</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=l"/>'>L</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=m"/>'>M</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=n"/>'>N</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=o"/>'>O</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=p"/>'>P</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=q"/>'>Q</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=r"/>'>R</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=s"/>'>S</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=t"/>'>T</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=u"/>'>U</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=v"/>'>V</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=w"/>'>W</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=x"/>'>X</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=y"/>'>Y</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}&letter=z"/>'>Z</a>&nbsp;
	    <a href='<c:url value="/admin/listmessageresources.html?lang=${listLanguage}"/>'>View all</a>&nbsp;
	    <a href='<c:url value="/admin/editmessageresource.html?lang=${listLanguage}"/>'>Create new</a>&nbsp;
	</span>
	<div style="border-bottom: 1px solid #d0d0d0; padding-top: 5px; padding-bottom: 5px; margin-bottom: 10px;">
       <form action='<c:url value="/admin/searchmessageresources.html"/>' name="search_form" method="post">
        <input type="hidden" name="lang" value="${listLanguage}"/>
        <input id="searchmessageResourceField" type="text" name="query"/>
        <button id="searchButton" type="submit">&nbsp;</button>
       </form>	   
	</div>
	<ul id="priority_list">
	    <c:forEach items="${messageResourceList}" var="item">
	        <c:url var="editURL" value="/admin/editmessageresource.html">
	            <c:param name="lang" value="${item.locale}"/>
	            <c:param name="key" value="${item.key}"/>
	        </c:url>
	        <li>
	            <a href="${editURL}" class="mt_text1">
	            ${item.locale}:${item.key} = ${item.value}</a>
	        </li>
	    </c:forEach>
	</ul>&nbsp;
</div>
