<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
%>

<c:set var="pathImg" value="${contextPath}/assets/images/svg" scope="request" />

<div class="main-content container">
	<div class="page-header">
		<h2 class="page-heading">New Rule (Subject Matter Link)</h2>
	</div>
	
	<div class="row" id="newRuleContainer">
		<div class="col-sm-7 form-group">
			<label class="control-label">Search Primary Application</label>
		</div>
       	<div class="col-sm-7 mdm-right-pad">
       		<form class="form-horizontal">
        		<div id="familyLinkage">
	        		<div class="form-group">
	        			<div class="col-sm-6">
	        				<select class="form-control" id="familyLinkageSelect">
	        					<option value="application">Application#</option>
	        					<option value="attdocket">Attorney Docket#</option>
	        					<option value="familyid">Family Id</option>
	        				</select>
	        			</div>
	        		</div>
	        		<div class="form-group" id="applicationLinkageData">
        				<div class="col-sm-3">
	        				<label class="control-label">Jurisdiction</label>
	        				<input type="text" class="form-control" name='jurisCode'>
	        			</div>
	        			<div class="col-sm-3">
	        				<label class="control-label">Application #</label>
	        				<input type="text" class="form-control" name='appNo'>
	        			</div>
	        		</div>
	        		<div class="form-group" id="attorneyLinkageData">
        				<div class="col-sm-6">
	        				<label class="control-label">Attorney Docket#</label>
	        				<input type="text" class="form-control" name='attdocketNo'>
	        			</div>
	        		</div>
	        		<div class="form-group" id="familyLinkageData">
        				<div class="col-sm-6">
	        				<label class="control-label">Family Id</label>
	        				<input type="text" class="form-control" name='familyNo'>
	        			</div>
	        		</div>
	        		<div class="divider"></div>
	        		<div class="form-group">
	        			<div class="col-sm-12">
	        				<button type="button" class="btn btn-submit" id="showFamilyGrid" onclick="fetchApplications();">Search</button>
	        			</div>
	        		</div>
	        	</div>
	        	<p><span class="error">Select application from a different family to create Subject Matter Linkage</span> <p>
	        	<div id="divApplicationDetails">
	        		
				</div>
       		</form>
       	</div>
       </div>
       <div class="main-content container" id="linkFamilyContainer">
			<div class="form-group col-sm-offset-6">
				<label class="control-label">Subject Matter Link to</label>
			</div>
			<div class="row">
				<div id="linkFamily">
					
				</div>	
				<div class="col-sm-6">
					<div class="family-view-block form-horizontal">
						<a class="btn add-family-btn" href="#"><i><img
								src="${pathImg}/plus.svg" class="icon20x"></i> Add Target Application</a>
					</div>
				</div>
			</div>
			<div class="divider"></div>
			<button type="button" class="btn btn-cancel" id="cancelFamilyBtn">Cancel</button>
			<button type="button" class="btn btn-submit disabled" id="linkFamilyBtn">Link</button>
       </div>
</div>
<script>
function fetchApplications() {
	
	var applicationSearchBy = $('#familyLinkageSelect :selected').val()
	var data = {};
	var target = "";
	
	if(applicationSearchBy === 'application') {
		data = {
			"jurisdictionCode" : 	$("input[name='jurisCode']").val(),
			"applicationNumber" : $("input[name='appNo']").val()
		}
		target = $('#contextPath').val() + '/reference/flowrule/appDetailsByApplication';
	}
	if(applicationSearchBy === 'attdocket') {
		data = {
			"attdocketNo" : $("input[name='attdocketNo']").val(),
		}
		target = $('#contextPath').val() + '/reference/flowrule/appDetailsByAttDocket';
	}
	if(applicationSearchBy === 'familyid') {
		data = {
			"familyValue" : $("input[name='familyNo']").val(),
		}
		target = $('#contextPath').val() + '/reference/flowrule/appDetailsByFamily';
	}
		
	$.ajax({
		method : "POST",
		url : target,
		data : data,
		success : function(result) {
			$('#divApplicationDetails').html(result);
		}

	});
}

$(function(){
	//Family Linkage Select box 
	$("#familyLinkageData, #attorneyLinkageData, #familyGrid").hide();
	$("#familyLinkageSelect").on("change", function(){
		if($(this).val() == "application"){
			$("#applicationLinkageData").show();
			$("#attorneyLinkageData, #familyLinkageData").hide();
		}
		else if($(this).val() == "attdocket"){
			$("#attorneyLinkageData").show();
			$("#applicationLinkageData, #familyLinkageData").hide();
		}
		else if($(this).val() == "familyid"){
			$("#familyLinkageData").show();
			$("#applicationLinkageData, #attorneyLinkageData").hide();
		}
	});


	$("#linkFamilyContainer, .error").hide();
	
	//select click 
	var familyId, familyIdTemp1, familyIdTemp2;
	$("#newRuleContainer").on('click', 'a.selectLink', function(){
		if(familyIdTemp1 == "" || familyIdTemp1 == undefined){
			familyIdTemp1 = $(this).parents("td").siblings("td:first-child").text();
			familyId = familyIdTemp1;
		}else{
			familyIdTemp2 =$(this).parents("td").siblings("td:first-child").text();
			if(familyIdTemp1 == familyIdTemp2){
				$(".error").show();
				return false;
			}else{
				familyId = familyIdTemp2;
			}
		}
		$.ajax({
			url: $('#contextPath').val() + '/reference/flowRule/reviewFamily/selectedFamilyDetails',
			data: {'familyid': familyId},
			method: "POST",
			success:function(result){
				$("#linkFamily").append(result);
				familyIdTemp = "";
				checkFamilyLinkCondition();
			},
			error: function(e){
			}
		})
		$("#newRuleContainer").hide();
		$("#linkFamilyContainer").show();
	});
	
	// show/hide conditions
	function checkFamilyLinkCondition(){
		$(".error").hide();
		if($("#linkFamily .family-view-block").length == 2){
			$("a.add-family-btn").hide();
			$("a.removeFamilyView:eq(0)").css({"visibility": "hidden", opacity: 0});
			$("#linkFamilyBtn").removeClass("disabled");
		}else{
			if(!$("#linkFamilyBtn").hasClass("disabled")){
				$("#linkFamilyBtn").addClass("disabled");
			}
			$("a.add-family-btn").show();
			$("a.removeFamilyView:eq(0)").css({"visibility": "visible", opacity: 1});
		}
	}
	
	//add another family
	$("#linkFamilyContainer").on('click', 'a.add-family-btn', function(){
		$("#linkFamilyContainer").hide();
		$("#divApplicationDetails").html("");
		$("#newRuleContainer").show();
	});
	
	//remove added family
	$("#linkFamilyContainer").on('click', '.removeFamilyView', function(){
		if($("#linkFamily .family-view-block").length == 1){
			familyId= "", familyIdTemp1 = "", familyIdTemp2 = "";
		}
		$(this).parents(".col-sm-6").remove();
		checkFamilyLinkCondition();
	});
	
	//cancel Family Link
	$("#linkFamilyContainer").on('click', '#cancelFamilyBtn', function(){
		$("#linkFamily").html("");
		window.location.reload();
	});

	//Link family
	$("#linkFamilyContainer").on('click', '#linkFamilyBtn', function(){
		var sourceFamilyId, targetFamilyId, sourceAppId, targetAppId, sourceLinkage, targetLinkage;
		var ele = "#linkFamily .family-view-block";
		var data = {
			sourceFamilyId: $(ele+':eq(0) .familyId').val(),
			sourceAppId: $(ele+':eq(0) .dbId').val(),
			sourceLinkage: $(ele+':eq(0) .allFamilyCheckbox').is(":checked"),
			targetFamilyId: $(ele+':eq(1) .familyId').val(),
			targetAppId: $(ele+':eq(1) .dbId').val(),
			targetLinkage: $(ele+':eq(1) .allFamilyCheckbox').is(":checked")
		}
		
		$.ajax({
			url: $('#contextPath').val() + '/reference/flowRule/reviewFamily/newrule/link',
			data: data,
			method:"POST",
			success: function(result){
				window.location.reload(); 
			},
			error: function(e){
			}
		});
	});
});
</script>