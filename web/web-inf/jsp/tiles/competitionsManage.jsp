<%@include file="includes.jsp"%>
<div>
    <h2 class="dark">Manage competitions</h2>
</div>
<div class="ui-widget-content ui-corner-all contentDiv">
    <h4>Competitions</h4>
    <ul id="friendList">
    <c:forEach items="${competitionList}" var="competition">
        <li>
	        <div class="span-12">
	            <div class="span-2 userPicDiv">
	                <img class="userPic" src='<c:url value="/competition/images/${competition.id}.jpeg"/>'/>
	            </div>
	            <div class="span-10 last competitionDiv">
                    <h5>${competition.name}</h5>
                    <span class="detailTitle">Deadline:</span> <fmt:formatDate value="${competition.deadline}" pattern="yyyy-MM-dd"/><br/>
                    <span class="detailTitle">Participants:</span> <br/>
                    <span class="detailTitle">Turnover:</span> <br/>
	            </div>
	        </div>
        </li>
    </c:forEach>
    </ul>
    <p>&nbsp;</p>
</div>