<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%
	String context = request.getContextPath();
	String js = context + "/assets/js";
	String images = context + "/assets/images";
%>

<div class="clearfix document-reader">
	<span class="triangle-corner"></span>
	<p class="title">Source Document</p>
	<span class="doc-action-icons"> <a href="javascript:void(0)"
		title="PDF"><i><img src="<%=images%>/svg/pdf.svg" class="icon16x"></i></a> <a href="edit-reference.html"
		title="Edit References"><i><img src="<%=images%>/svg/edit.svg"
				class="icon16x"></i></a>
	</span>
	<div class="form-horizontal">
		<div class="form-group">
			<div class="col-sm-12">
				<div class="col-sm-12">
					<label class="control-label">Document Description:</label>
					<div class="form-control-static">Non-final Rejection</div>
				</div>
				<div class="col-sm-6">
					<label class="control-label">Mailing Date</label>
					<div class="form-control-static">Dec 10, 2015</div>
				</div>
				<div class="col-sm-6">
					<label class="control-label">Family ID</label>
					<div class="form-control-static">F2340769</div>
				</div>
				<div class="col-sm-6">
					<label class="control-label">Jurisdiction</label>
					<div class="form-control-static">US</div>
				</div>
				<div class="col-sm-6">
					<label class="control-label">Application #</label>
					<div class="form-control-static">13/367,505</div>
				</div>
			</div>
		</div>
	</div>
</div>