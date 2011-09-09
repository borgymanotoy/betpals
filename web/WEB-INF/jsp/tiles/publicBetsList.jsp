<%@include file="includes.jsp"%>
<script type="text/javascript">
	jQuery(document).ready(function() {
	    var searchField = jQuery("#searchCompetitionsField"); 
	    searchField.val(searchField.attr('title'));
	    searchField.focus( function() {
	        jQuery(this).val("");
	    });
	    searchField.blur( function() {
	        if (jQuery(this).val() == "") {
	            jQuery(this).val(jQuery(this).attr('title'));
	        }
	    });
	    
	});


    function searchCompetitions() {
        var queryValue = jQuery("#searchCompetitionsField").val();
        if ( queryValue != "" && queryValue != jQuery("#searchCompetitionsField").attr('title')) {
            document.search_form.submit();
        }
    }
    
</script>
<div id="searchFriendsDiv">
    <h2><spring:message code="public.bets.list.title"/></h2>
    <form action='<c:url value="/searchpubliccompetitions.html"/>' name="search_form" method="post">
        <input id="searchCompetitionsField" type="text" name="query" value="" title="<spring:message code="competition.search.placeholder"/>"/>
        <button id="searchButton" onclick="searchCompetitions(); return false;">&nbsp;</button>
    </form>
</div>
<div class="rbDiv contentDiv">
    <h4><spring:message code="competition.public.list.header"/></h4>
    <ul id="friendList">
    <c:forEach items="${competitionList}" var="competition">
        <li>
            <div class="span-12">
                <div class="span-2 userPicDiv">
                    <img class="userPic" src='<c:url value="/competition/images/${competition.id}.jpeg"/>'/>
                </div>
                <div class="span-10 last competitionDiv">
                    <h5 class="clickable" onclick="viewPublicCompetition(${competition.id});">${competition.name}</h5>
                    <span class="detailTitle"><spring:message code="competition.creator"/>: </span><a href='<c:url value="/viewprofile/${competition.ownerId}.html"/>'>${competition.owner.fullName}</a><br/>
                    <span class="detailTitle"><spring:message code="competition.status"/>: ${competition.status} (<spring:message code="competition.deadline"/> <fmt:formatDate value="${competition.deadline}" pattern="yyyy-MM-dd HH:mm"/>)</span><br/>
                </div>
            </div>
        </li>
    </c:forEach>
    </ul>
    <c:if test="${numberOfPages > 0}">
    <p>&nbsp;number of pages: ${numberOfPages}, current page: ${currentPage}</p>
    <div class="span-12">
        <div class="span-6 left">
            <c:if test="${currentPage < numberOfPages}">
                <form name="prevPageForm" action='<c:url value="/publiccompetitions.html"/>' method="post">
                    <input type="hidden" name="pageId" value="${currentPage + 1}"/>
                </form>
                <button class="blueButton90" onclick="prevPageForm.submit();"><spring:message code="button.previous"/></button>
            </c:if>
        </div>
        <div class="span-6 last right">
            <c:if test="${currentPage > 0}">
                <form name="nextPageForm" action='<c:url value="/publiccompetitions.html"/>' method="post">
                    <input type="hidden" name="pageId" value="${currentPage - 1}"/>
                </form>
                <button class="greenButton90" onclick="nextPageForm.submit();"><spring:message code="button.next"/></button>
            </c:if>
        </div>
    </div>
    </c:if>
    
    <p>&nbsp;</p>
    
</div>
