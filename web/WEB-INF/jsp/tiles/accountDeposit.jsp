<%@include file="includes.jsp"%>
<script type="text/javascript">
	var validateDepositAmount = function(e){
		var isIE = (navigator.appName == "Microsoft Internet Explorer");
		var value = $("#txtDepositAmount").val();
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
				$("#txtDepositAmount").val(value);
			}
		}
	};

	var validateDepositAmountBySubmit = function(e, value){
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
			$("#txtDepositAmount").val(value);
		}
		return true;
	};

	var formatDepositAmount = function(){
		$("#txtDepositAmount").autoNumeric({aSep: ',', aDec: '.'});
	};

	jQuery(document).ready(function() {
		$(".positive-currency").numeric({ negative: false }, function() { alert("No negative values"); this.value = ""; this.focus(); });
		formatDepositAmount();
	});
</script>
<div>
    <h2 class="dark"><spring:message code="account.deposit.title"/></h2>
</div>
<div class="rbDiv contentDiv">
    <h4><spring:message code="account.deposit"/></h4>
    <p><spring:message code="account.deposit.message"/></p>
    <c:url var="accountDepositURL" value="/accountdeposit.html"/>
    <form action="${accountDepositURL}" method="post">
		<table class="accountInfoTable">
			<tr>
				<td>
					<span id="selectCurrencySpan"><spring:message code="account.amount"/></span>
					<input type="hidden" name="accountId" value="${accountId}"/>
				</td>
				<td><input type="text" id="txtDepositAmount" name="amount" class="positive-currency" maxlength="10" onkeypress="validateDepositAmount(event);" /></td>
				<td><input type="submit" class="greenButton90" value="<spring:message code='button.place.bet'/>" onclick="validateDepositAmountBySubmit(event, $('#txtDepositAmount').val());"/></td>
			</tr>
        </table>
    </form>
   <p>&nbsp;</p>
    <p>&nbsp;</p>
</div>
