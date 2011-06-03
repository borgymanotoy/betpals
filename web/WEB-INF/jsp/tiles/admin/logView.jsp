<%@include file="includes.jsp"%>
<script type="text/javascript">
    jQuery(document).ready(function() {
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
    
</script>
<div>
    <h2 class="dark_long"><spring:message code="log.view.title"/></h2>
</div>
<div class="rbDiv contentDiv">
    <div style="border-bottom: 1px solid #d0d0d0; padding-top: 5px; padding-bottom: 5px; margin-bottom: 10px;">
        <span>
            <a href='<c:url value="/admin/viewlatestlog.html"/>'>0</a>&nbsp;
            <a href='<c:url value="/admin/viewlatestlog.html?file=1"/>'>1</a>&nbsp;
            <a href='<c:url value="/admin/viewlatestlog.html?file=2"/>'>2</a>&nbsp;
            <a href='<c:url value="/admin/viewlatestlog.html?file=3"/>'>3</a>&nbsp;
            <a href='<c:url value="/admin/viewlatestlog.html?file=4"/>'>4</a>&nbsp;
            <a href='<c:url value="/admin/viewlatestlog.html?file=5"/>'>5</a>&nbsp;
            <a href='<c:url value="/admin/viewlatestlog.html?file=6"/>'>6</a>&nbsp;
            <a href='<c:url value="/admin/viewlatestlog.html?file=7"/>'>7</a>&nbsp;
            <a href='<c:url value="/admin/viewlatestlog.html?file=8"/>'>8</a>&nbsp;
            <a href='<c:url value="/admin/viewlatestlog.html?file=9"/>'>9</a>&nbsp;&nbsp;
            <a href='<c:url value="/admin/viewlatestlog.html?level=error"/>' class="logLevelERROR">ERROR</a>&nbsp;
            <a href='<c:url value="/admin/viewlatestlog.html?level=warn"/>' class="logLevelWARN">WARN</a>&nbsp;
            <a href='<c:url value="/admin/viewlatestlog.html?level=info"/>' class="logLevelINFO">INFO</a>&nbsp;
            <a href='<c:url value="/admin/viewlatestlog.html?level=debug"/>' class="logLevelDEBUG">DEBUG</a>&nbsp;
        </span>
    </div>
	<ul class="logList">
	    <c:forEach items="${logEntryList}" var="entry">
	        <li class="logEntry logLevel${entry.level} clickable">
	            <fmt:formatDate value="${entry.date}" pattern="yyyy-MM-dd HH:mm"/> <c:out value="${entry.level}"/> - <c:out value="${entry.shortMessage}"/>
	            <div class="logInfo">
	            <span class="logFieldTitle">Date</span>: <fmt:formatDate value="${entry.date}" pattern="yyyy-MM-dd HH:mm:ss"/><br/>
	            <span class="logFieldTitle">Source</span>: <c:out value="${entry.source}"/><br/>
	            <span class="logFieldTitle">Message</span>: <c:out value="${entry.message}"/><br/>
	            <span class="logFieldTitle">Additional info</span>: <c:out value="${entry.additionalInfo}"/>
	            </div>
	        </li>
	    </c:forEach>
	</ul>&nbsp;
</div>
