<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>

<div class="col-sm-2" id="filedOnRecords">
		<h3 class="tri-heading">IDS Filed on...</h3>
		 <ul class="nav triNav">
			<c:forEach items="${idsDrillDownInfoFilingDates}" var="item">
			<input type="hidden" value="${item.IDSId}" id="IDS${item.IDSId}"/>
			
				<li>
					<c:choose>
						<c:when test="${(item.idsFilingDate ne null) and (lastFiledOn ne null) and (item.idsFilingDate1 eq lastFiledOn)}">
							<c:choose>
								<c:when test="${item.count gt 1}">
									<a href="javascript:void(0)" class="active" data-idsid="${item.IDSId}" >${item.idsFilingDate}</a>(${item.count})
								</c:when>
								<c:otherwise>
									<a href="#" class="active">${item.idsFilingdate}</a>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${item.count gt 1}">
									<a href="javascript:void(0)">${item.idsFilingDate}</a>(${item.count})
								</c:when>
								<c:otherwise>
									<a href="#"  data-idsid="${item.IDSId}" id="idsFiledDate${item.IDSId}">${item.idsFilingDate}</a>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</li>
			</c:forEach>
	 	</ul>
</div>

<script type="text/javascript">

$(document).ready(function(){
    $('#filedOnRecords a').click(function(e) {
    e.preventDefault();
	var IDSId  = this.dataset.idsid;
	var appId = ${requestScope.app};
	var target = $("#contextPath").val() + "/ids/drillDown/citedReferences/"+appId;
	var data = {"idsID" : IDSId};
	 $.ajax({
		 	type: 'POST',
		 	url: target,
		 	dataType : 'json',
		 	data: data,
		 	success: function(result){
		 		var styleClass="";
		 		var allRefs = result["idsDrillDownInfoReferenceTypes"];
		 	    for ( var i = 0, len = allRefs.length; i < len; ++i) {
		 	    	var citedReference = allRefs[i];
		 	        if(i%2 == 0){
		 	        	styleClass = "even";
		 	         }else{
		 	        	 styleClass = "odd";
		 	         }
		 	         
		 	       	if(citedReference["referenceType"] == ('PUS')){
		 	      		 $('#citedReferences #uspatent').children('tbody').append("<tr class=\" clicked "+styleClass+"\" id = \"RefBase"+citedReference.referenceBaseId+"\" data-refBaseId = \""+citedReference.referenceBaseId+"\"><td>"
		 	      				 +citedReference.citeId+"</td>"+
		 	      				 "<td>"+citedReference.jurisdictionCode+"</td><td>"+citedReference.publicationNumber+"</td></tr>");
		 	       	}else if(citedReference["referenceType"] == ('US_PUBLICATION')){
		 	     		 $('#citedReferences #usPublication').children('tbody').append("<tr class=\" clicked "+styleClass+"\" id = \"RefBase"+citedReference.referenceBaseId+"\" data-refBaseId = \""+citedReference.referenceBaseId+"\"><td>"+citedReference.citeId+"</td>"+
		 	  				 "<td>"+citedReference.jurisdictionCode+"</td><td>"+citedReference.publicationNumber+"</td></tr>");
		 	   		}else if(citedReference["referenceType"] == ('FP')){
		 	        		 $('#citedReferences #foreign').children('tbody').append("<tr class=\" clicked "+styleClass+"\" id = \"RefBase"+citedReference.referenceBaseId+"\" data-refBaseId = \""+citedReference.referenceBaseId+"\"><td>"+citedReference.citeId+"</td>"+
		 	      				 "<td>"+citedReference.jurisdictionCode+"</td><td>"+citedReference.publicationNumber+"</td></tr>");
		 	       	}else if(citedReference["referenceType"] == ('NPL')){
		 	        		 $('#citedReferences #npl').children('tbody').append("<tr class=\" clicked "+styleClass+"\" id = \"RefBase"+citedReference.referenceBaseId+"\" data-refBaseId = \""+citedReference.referenceBaseId+"\"><td class=\"bdr-rt-none\">"+citedReference.citeId+"</td>"+
		 	      				 "<td class=\"bdr-rt-none\">"+citedReference.NPLString+"</td></tr>");
		 	       	}
		 	 	} 
		 	 	
		 		showReferencesCount(result["idsReferenceTypeCounts"]);
		     },	
	  		error: function(e){
		  		alert('Error: ' + e);
			}
	     });
    });
    bindRowClickEvent();
});


function showReferencesCount(result){
	for ( var i = 0, len = result.length; i < len; ++i) {
    	var citedReferenceCount = result[i];
    	
   		if(citedReferenceCount["referenceType"] == ('PUS')){
   			if($('#citedReferences #PUS').text().contains("US Patent")){
   				$('#citedReferences #PUS').html("US Patent ("+citedReferenceCount["count"]+")"
   						+"<span class=\"selected\"></span><span class=\"icon icon-arrow-down\"></span><span class=\"icon icon-arrow-up\"></span>");
   		}
    		else{
    			$('#citedReferences #PUS').text("US Patent ("+ 0 +")");
    		}
    	} 
    	
   		else if(citedReferenceCount["referenceType"] == ('US_PUBLICATION')){
    			if($('#citedReferences #USP').text().contains("US Publication")){
    			$('#citedReferences #USP').html("US Publication ("+citedReferenceCount["count"]+")"+
   						"<span class=\"selected\"></span><span class=\"icon icon-arrow-down\"></span><span class=\"icon icon-arrow-up\"></span>");
    		}
    		else{
    			$('#citedReferences #USP').text("US Publication ("+ 0 +")");
    		}
       	}
    	
   		else if(citedReferenceCount["referenceType"] == ('Foreign')){
       			if($('#citedReferences #FP').text().contains("Foreign")){
       			$('#citedReferences #FP').html("Foreign ("+citedReferenceCount["count"]+")"+
   						"<span class=\"selected\"></span><span class=\"icon icon-arrow-down\"></span><span class=\"icon icon-arrow-up\"></span>");
    		}else{
    			$('#citedReferences #FP').text("Foreign ("+ 0 +")");
    		}
      	}
    	
   		else if(citedReferenceCount["referenceType"] == ('NPL')){
      			if($('#citedReferences #NPL_1').text().contains("NPL")){
      			$('#citedReferences #NPL_1').html("NPL ("+citedReferenceCount["count"]+")"+
   						"<span class=\"selected\"></span><span class=\"icon icon-arrow-down\"></span><span class=\"icon icon-arrow-up\"></span>");
    		}else{
    			$('#citedReferences #NPL_1').text("NPL ("+ 0 +")");
    		}
        }
    	
    }
}

</script>