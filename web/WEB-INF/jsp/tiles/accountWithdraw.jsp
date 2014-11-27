<%@include file="includes.jsp"%>
<script type="text/javascript">
	var validateWithdrawnAmount = function(e){
		var isIE = (navigator.appName == "Microsoft Internet Explorer");
		var value = $("#txtWithdrawAmount").val();
		if(e.keyCode == 13){
			if(value.length == 0){
				if(isIE){
					e.returnValue = false;
				}else{
					e.preventDefault();
					return false;
				}
			}else{
				value = value.replace(/,/g, '');
				$("#txtWithdrawAmount").val(value);
			}
		}
	};

	var validateWithdrawnAmountBySubmit = function(e, value){
		var isIE = (navigator.appName == "Microsoft Internet Explorer");
		if(value.length == 0){
			if(isIE){
				e.returnValue = false;
			}else{
				e.preventDefault();
				return false;
			}
		}else{
			value = value.replace(/,/g, '');
			$("#txtWithdrawAmount").val(value);
		}
		return true;
	};

	var formatDepositAmount = function(){
		$("#txtWithdrawAmount").autoNumeric({aSep: ',', aDec: '.'});
	};

	jQuery(document).ready(function() {
		$(".positive-currency").numeric({ negative: false }, function() { alert("No negative values"); this.value = ""; this.focus(); });
		formatDepositAmount();
	});
</script>
<div>
    <h2 class="dark"><spring:message code="account.withdraw.title"/></h2>
</div>
<div class="rbDiv contentDiv">
    <h4><spring:message code="account.withdraw"/></h4>
    <p><spring:message code="account.withdraw.message"/></p>
    <c:url var="accountWithdrawURL" value="/accountwithdraw.html"/>
    <form action="${accountWithdrawURL}" method="post">
		<table class="accountInfoTable">
			<tr>
				<td>
					<span id="selectCurrencySpan"><spring:message code="account.amount"/></span>
					<input type="hidden" name="accountId" value="${accountId}"/>
				</td>
				<td><input type="text" id="txtWithdrawAmount" name="amount" class="positive-currency" maxlength="10" onkeypress="validateWithdrawnAmount(event);" /></td>
				<td><input type="submit" class="blueButton110" value="<spring:message code='account.withdraw'/>" onclick="validateWithdrawnAmountBySubmit(event, $('#txtWithdrawAmount').val());"/></td>
			</tr>
		</table>
    </form>
   <p>&nbsp;</p>
    <p>&nbsp;</p>
</div>
