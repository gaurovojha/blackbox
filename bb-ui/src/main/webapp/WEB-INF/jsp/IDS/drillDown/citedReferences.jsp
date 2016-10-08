<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>

<div class="col-sm-5 trifolder-cited" id="citedReferences">
	<h3 class="tri-heading">Cited References</h3>
	<div class="panel-group user-management" id="accordion1" role="tablist" aria-multiselectable="true">
	<%-- <c:forEach items="${idsReferenceTypeCounts}" var=reference > --%>
		<div class="panel panel-default">
			<div class="panel-heading" role="tab" id="UsPatents">
				<h4 class="panel-title">
					<span></span>
					<a id ="PUS" role="button" class="collapse collapsed" data-toggle="collapse" data-parent="#accordion1" href="#collapseOne" aria-expanded="false" aria-controls="collapseOne">
						US Patent(0)
					
						<span class="selected"></span>
						<span class="icon icon-arrow-down"></span>
						<span class="icon icon-arrow-up"></span>
					</a>
				</h4>	
			</div>	
			<div style="height: 0px;" aria-expanded="false" id="collapseOne" class="panel-collapse collapse" role="tabpanel" aria-labelledby="UsPatents">
				<div class="panel-body">
					<div class="inner-search trisearch">
						<div class="input-group">
							<span class="input-group-btn">
								<button class="search"><span class="icon icon-search-inner"></span></button>
							</span>
							<input placeholder="Search" type="text">
						</div>
					</div>		
					<table class="table custom-table clickable" id="uspatent">
						<tbody>									
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<!-- US publication drop down -->
		<div class="panel panel-default">
			<div class="panel-heading" role="tab">
	    		<h4 class="panel-title">
	       			<a id ="USP" class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion1" href="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
	        			US Publication(0)
	        			<span class="selected"></span><span class="icon icon-arrow-down"></span><span class="icon icon-arrow-up"></span>
	     			</a>
	        	</h4>
	  		</div>
	   		<div id="collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-expanded="false">
	    		<div class="panel-body">
	        		<div class="inner-search trisearch">
						<div class="input-group">
							<span class="input-group-btn">
								<button class="search"><span class="icon icon-search-inner"></span>
								</button>
							</span>
							<input placeholder="Search" type="text">
						</div>
					</div>	
	         		<table class="table custom-table clickable" id="usPublication">
			 			<tbody>
							
						</tbody>
			 		</table>
				</div> 	
			</div>
		</div>	

		<!-- Foreign -->
	
		<div class="panel panel-default">
			<div class="panel-heading" role="tab">
	    		<h4 class="panel-title">
	        		<a id="FP" class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion1" href="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
	            		Foreign(0)
						
						 <span class="selected"></span><span class="icon icon-arrow-down"></span><span class="icon icon-arrow-up"></span>
	           		</a>
	        	</h4>
	    	</div>
	   		<div id="collapseThree" class="panel-collapse collapse" role="tabpanel" aria-expanded="false">
	    		<div class="panel-body">
	        		<div class="inner-search trisearch">
						<div class="input-group">
							<span class="input-group-btn">
								<button class="search"><span class="icon icon-search-inner"></span>
								</button>
							</span>
							<input placeholder="Search" type="text">
						</div>
					</div>
	            	<table class="table custom-table clickable" id="foreign">
						<tbody>
							
						</tbody>
					</table>
				</div>	
			</div>
		</div>
	
		<!-- NPL -->
		 <div class="panel panel-default">
	 		<div class="panel-heading" role="tab">
	    		<h4 class="panel-title">
	       		 	<a id="NPL_1" class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion1" href="#collapseFour" aria-expanded="false" aria-controls="collapseFour">
	        	   		NPL(0)
					 <span class="selected"></span><span class="icon icon-arrow-down"></span><span class="icon icon-arrow-up"></span>
	     	       </a>
	     		</h4>
	 		</div>
			<div id="collapseFour" class="panel-collapse collapse" role="tabpanel" aria-expanded="false">
	    		<div class="panel-body">
	        		<div class="inner-search trisearch">
						<div class="input-group">
							<span class="input-group-btn">
								<button class="search"><span class="icon icon-search-inner"></span>
								</button>
							</span>
							<input placeholder="Search" type="text">
						</div>
					</div>
	        		<table class="table custom-table clickable" id ="npl">
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<%-- </c:forEach> --%>
	</div>			
</div>


<script type="text/javascript">
var corrId;
$('#citedReferences table').on('click', 'tr', function(){
	var refBaseId = this.dataset.refbaseid;
	var target = $("#contextPath").val() + "/ids/drillDown/referenceSourceDocument/";
	var data = {"refBaseId" : refBaseId};
	 $.ajax({
		 	type: 'POST',
		 	url: target,
		 	dataType : 'json',
		 	data: data,
		 	success: function(result){
		 		showSourceDoc(result);
		     },	
	  		error: function(e){
		  		alert('Error: ' + e);
			}
		});
});


/* $('#citedReferences table').on('click', 'tr', function(){
	var refBaseId = this.dataset.refbaseid;
	var target = $("#contextPath").val() + "/ids/drillDown/referenceSourceDocument/";
	var data = {"corrId" : corrId};
	 $.ajax({
		 	type: 'POST',
		 	url: target,
		 	dataType : 'json',
		 	data: data,
		 	success: function(result){
		 		//alert("I got the data.. Time to process");
		 		showOtherDocs(result);
		 		
		     },	
	  		error: function(e){
		  		alert('Error: ' + e);
			}
		});
}); */

function showSourceDoc(result){
	for ( var i = 0, len = result.length; i < len; ++i) {
    	var sourceDocument = result[i];
    	$('#docDescriptionSource').text(sourceDocument.documentDescription);
    	$('#mailDateSource').text(""+sourceDocument.mailingDate);
    	$('#familyIdSource').text(""+sourceDocument.familyId);
    	$('#jurisdictionSource').text(""+sourceDocument.jurisdictionCode);
    	$('#applicationNoSource').text(""+sourceDocument.applicationNo);
    	var corrId = sourceDocument.corrId;
    	var otherReferences = result["idsSourceDocOtherReferences"];
    	showOtherDocsAjax(corrId);
	}
}

function showOtherDocsAjax(corrId){
	var target = $("#contextPath").val() + "/ids/drillDown/otherReferences/"+corrId;
	$.ajax({
	 	type: 'POST',
	 	url: target,
	 	dataType : 'json',
	 	success: function(result){
	 		showOtherDocs(result);
	     },	
  		error: function(e){
	  		alert('Error: ' + e);
		}
	});
}

function showOtherDocs(result){

	for ( var i = 0, len = result.length; i < len; ++i) {
		var otherDoc = result[i];
		var formGroup = "<div class=\"form-group\"><div class=\"col-sm-6\"><label class=\"control-label\">Jurisdiction</label><div class=\"form-control-staticc\" id=\"jurisdictionOther"+otherDoc.referenceBaseId+"\"></div></div><div class=\"col-sm-6\"> <label class=\"control-label\">Publication Number</label><div class=\"form-control-static\" id=\"publicationOther"+otherDoc.referenceBaseId+"\"></div></div><div class=\"col-sm-12\"><a href=\"#\" title=\"More\"><small>More...</small></a></div><div class=\"col-sm-12\"><a href=\"#\" id=\"idsDateOther"+otherDoc.referenceBaseId+"\"></a></div></div>";
	 	var formGroupNPL = "<div class=\"form-group\"><div class=\"col-sm-12\"><label class=\"control-label\">NPL</label><div style = \"word-wrap : break-word\"id=\"nplString"+otherDoc.referenceBaseId+"\"></div></div><div class=\"col-sm-12\" ><p><a href=\"#\" id=\"idsDateOther"+otherDoc.referenceBaseId+"\"></a></p></div></div>";
	
    	if(otherDoc.referenceType == 'NPL'){
    		$('#mCSB_1_container').append(formGroupNPL);
    		$('#nplString'+otherDoc.referenceBaseId).text(""+otherDoc.nplstring);    			
    	}else{
    		$('#mCSB_1_container').append(formGroup);
    		$("#jurisdictionOther"+otherDoc.referenceBaseId).text(""+otherDoc.jurisdictionCode);
    		$("#publicationOther"+otherDoc.referenceBaseId).text(""+otherDoc.publicationNumber);
    	}
    	
    	if(otherDoc.refFlowStatus == 'CITED')
			$('#idsDateOther'+otherDoc.referenceBaseId).text(""+otherDoc.refFlowStatus+" in IDS:"+ otherDoc.rffilingDate);
		else
			$("#idsDateOther"+otherDoc.referenceBaseId).text(""+otherDoc.refFlowStatus);
    }
}

</script>