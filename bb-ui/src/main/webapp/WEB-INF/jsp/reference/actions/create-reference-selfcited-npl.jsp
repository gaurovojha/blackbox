<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%
	String context = request.getContextPath();
	String images = context+"/assets/images";
%>

<div class="form-group">
    <div class="col-sm-12">
        <form:label class="control-label" path="title">Title<span class="required">*</span></form:label>
        <form:input class="form-control" id="nplTitle" path="title" placeholder="Start Typing here..."></form:input>
        <form:errors class="form-control error" path="title"></form:errors>
    </div>
    <div class="form-group" id="appSerialNo" style="disaplay:none;">
    <div class="col-sm-6"><label class="control-label">Application Serial Number 
			<span class="required">*</span></label> 
			<%-- <form:input id="nplApplicationSerialNumber" type="text" path="applicationSerialNumber" class="form-control" placeholder=""></form:input> --%>
			<input type="text"  id="nplApplicationSerialNumber">
		</div>
	</div>     
</div>
<div id="usUnpublishApplication">
	 <div class="form-group USCheck">
	    <div class="col-sm-6">
	        <form:label class="control-label" path="publicationDate">Publication Date<span class="required">*</span></form:label>
	        <div class="input-group date datepicker">
	            <form:input class="form-control" id="nplPublicationDate" path="publicationDate"></form:input>
	            <span class="input-group-addon">
				    <i class="glyphicon glyphicon-calendar"></i>
				</span>
				<form:errors class="form-control error" path="publicationDate"></form:errors>
	        </div>
	    </div>
	    <div class="col-sm-6">
	        <form:label class="control-label" path="author">Author</form:label>
	        <form:input id="nplAuthor" class="form-control" path="author"></form:input>
	        <form:errors class="form-control error" path="author"></form:errors>
	    </div>
	</div>
	<div class="form-group USCheck">
	    <div class="col-sm-6">
	        <form:label class="control-label" path="publicationDetail">Publication Details</form:label>
	        <form:input path="publicationDetail" id="nplPublicationDetails" class="form-control"></form:input>
	        <form:errors class="form-control error" path="publicationDetail"></form:errors>
	    </div>
	    <div class="col-sm-3">
	        <form:label class="control-label" path="relevantPages">Relevant Pages</form:label>
	        <form:input path="relevantPages" id="nplRelevantPages" class="form-control"></form:input>
	        <form:errors class="form-control error" path="relevantPages"></form:errors>
	    </div>
	    <div class="col-sm-3">
	        <form:label class="control-label" path="volumeNumber">Vol. / Issue No.</form:label>
	        <form:input id="nplIssueNo" class="form-control" path="volumeNumber"></form:input>
	        <form:errors class="form-control error" path="volumeNumber"></form:errors>
	    </div>
	</div>
	<div class="form-group USCheck">
	    <div class="col-sm-12">
	        <form:label class="control-label" path="URL">URL</form:label>
	        <form:input id="nplURL" class="form-control" path="URL"></form:input>
	        <p id="RetrievalDateError" class="has-error"><span class="error">Please Enter a valid URL</span></p>
	        <form:errors class="form-control error" path="URL"></form:errors>
	    </div>
	   <div class="col-sm-6" id="RetrievalDate">
	        <form:label class="control-label" path="retrivalDate">Retrieval Date</form:label>
	        <div class="input-group date datepicker">
	            <form:input id="nplRetrievalDate" class="form-control" path="retrivalDate"></form:input>
	            <span class="input-group-addon">
	            	<form:errors class="form-control error" path="retrivalDate"></form:errors>
	    			<i class="glyphicon glyphicon-calendar"></i>
				</span>
	        </div>
	    </div>
	</div>
	<div class="form-group npl-autodata">
	    <div class="col-sm-12">
	        <label class="control-label title">Auto Populated Field</label>
	        <div class="">
	            <p id="autoPupulatedData" class="content"></p>
	        </div>
	        <div class="text-right">
	            <form:button type="button" class="btn btn-submit" id="searchNplBtn">Search NPL database</form:button>
	        </div>
	    </div>
	</div>
	<div class="form-group">
   <div class="col-sm-6 text-left">
      <div class="form-control-static">
         <a href="javascript:document.getElementById('PdfFileNPLSelf').click()"><i><img src="<%=images %>/svg/add.svg" class="icon16x"></i>
         <form:input path="file" id="PdfFileNPLSelf" class="form-element" type="file" style="display:none" accept="application/pdf" /><label for="PdfFileNPLSelf">Add an Attachment (PDF Only)</label></a><div id="filenameNPLSelf"></div> 
      </div>
   </div>
   <div class="col-sm-6 text-right">
      <input type="checkbox" name="englishTranslation" class="form-element" id="engTranslation" />
      <label class="control-label" for="engTranslation">English Translation</label>
   </div>
</div>
</div>
<div class="divider"></div>