<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var ="lastFiledOn" value="${lastFiledOn}" scope = "request"/>
<c:set var="pathImg" value="${contextPath}/assets/images/svg" scope="request" />
<c:set var="pathjs" value="${contextPath}/assets/js/ids" scope="request" />
<link rel="stylesheet" href="${contextPath}/assets/css/ids-style.css">


<div class="main-content container">
	<div class="notTopinfo">
		<div class="row">	
		<!-- <div class="col-xs-12 col-sm-6 div-drilldown-page-header" style="margin-left:185px;margin-right: 50px;margin-top: 50px; width:80%"> -->
        	<div class="col-sm-2">
            	<label class="control-label">Family ID</label>
                <div class="form-control-static">${idsDrillDownInfo.familyId }</div>
            </div>
            <div class="col-sm-2">
            	<label class="control-label">Jurisdiction</label>
                <div class="form-control-static">${idsDrillDownInfo.jurisdiction }</div>
            </div>           
            <div class="col-sm-2">
            	<label class="control-label">Application#</label>
            	<div class="form-control-static">${idsDrillDownInfo.applicationNo }</div>
            </div>                                          
            <div class="col-sm-2">
            	<label class="control-label">Filing Date</label>
                <div class="form-control-static">${idsDrillDownInfo.filingDate }</div>
            </div>
            <div class="col-sm-2">
            	<label class="control-label">Attorney Docket#</label>
                <div class="form-control-static">${idsDrillDownInfo.attorneyDocketNo }</div>
            </div>
            <div class="col-sm-2">
            	<label class="control-label"></label>
                <div class="form-control-static"><a href="javascript:void(0)"><i><img src="${pathImg}/download.svg" class="icon16x"></i>Download</a></div>
            </div>
    	</div>  
    </div>   
    <div class="pager-number clearfix hide">
		<div class="paging">
			<a href="#" class="active">1</a>
			<a href="#" class="">2</a>
		</div>
	</div>
	
	<div class="row">
		<jsp:include page="filedOnRecords.jsp"></jsp:include>
		<jsp:include page="citedReferences.jsp"></jsp:include>
		<jsp:include page="sourceDocument.jsp"></jsp:include>
	</div>
</div>	
            