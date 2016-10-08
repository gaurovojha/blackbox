,<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<input type="hidden" id="recordsTotal" value="${referenceDestination.totalElements}" />
<input type="hidden" id="recordsFiltered" value="${referenceDestination.totalElements}"/>

<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>

<script type="text/javascript">
	function loadDestinationData(){
		var sourceAttrb = jQuery("#selectedSourceAttr option:selected").attr('value');
		var data = {};
		var target = "";
		
		data = {
				"dbId" : 	sourceAttrb				
		};
			target = $('#contextPath').val() + '/reference/flowRule/reviewFamily/dest';
		//alert(dbId);
		$.ajax({
			type : "POST",
			url : target,
			data : data,
			success : function(result) {
				$('#familyRuleEditData').html(result);
				//var hi = $('#destListSize').val();
				
			}
		});
	}
	
	function switchData(destId){
		var sourceAttrb = jQuery("#selectedSourceAttr option:selected").attr('value');
		document.getElementById("sourceApp").value = sourceAttrb;
		document.getElementById("targetApp").value = destId;
	}
	
	
	$(function(){
		$(document).on("click",".on-off input", function(){
			if($(this).is(":checked")){
				$('#confirmStatusChange').modal('show');
			}
			else{
				$('#confirmStatusChange').modal('show');
			}	
		});
		
	});
</script>

<%-- <jsp:include page="reference.family.link.data.jsp"></jsp:include> --%>

<c:forEach items="${referenceDestination.content}" var="familyDestdata" varStatus="status" begin="0" end="0">
	<tr class="odd">
		<td colspan="3">
				<select class="selectpicker form-control" id="selectedSourceAttr" data-live-search="true" onchange="loadDestinationData()">
				<c:forEach items="${referenceSource.content}" var="familySourcedata">
					<option id="" value="${familySourcedata.dbId}">${familySourcedata.jurisdiction} ${familySourcedata.applicationNumber} ${familySourcedata.attorneyDocket}</option>
				</c:forEach>
			</select>
		</td>
		<td colspan="3"></td>
		<td>${familySourcedata.jurisdiction}</td>
		<td>${familySourcedata.jurisdiction}</td>
		<td>${familySourcedata.jurisdiction}</td>
	</tr>
</c:forEach>

<div id="familyRuleEditData"><jsp:text /></div>