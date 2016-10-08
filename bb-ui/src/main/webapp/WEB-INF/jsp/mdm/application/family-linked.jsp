<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="form-group">
	<div class="col-sm-6">
		<label class="control-label">Customer No. <span
			class="required">*</span></label> <input type="text" class="form-control"
			value="12345678">
	</div>
	<div class="col-sm-6">
		<label class="control-label">Attorney Docket No.<span
			class="required">*</span></label> <input type="text" class="form-control"
			value="ACF / 144">
	</div>
</div>
<div class="form-group">
	<div class="col-sm-6">
		<label class="control-label">Assignee <span class="required">*</span></label>
		<input type="text" class="form-control" value="Apple">
	</div>
	<div class="col-sm-6">
		<label class="control-label">Entity <span class="required">*</span></label>
		<input type="text" class="form-control" value="XXXXXX">
	</div>
</div>
<div class="divider"></div>
<div class="form-group">
	<div class="col-sm-6">
		<label class="control-label">Filing Date <span
			class="required">*</span></label>
		<div class='input-group date datepickercontrol'>
			<input type='text' class="form-control" /> <span
				class="input-group-addon"> <i data-alt="calendar"><img
					src="images/svg/calender.svg" class="icon20x"></i>
			</span>
		</div>
	</div>
	<div class="col-sm-6">
		<label class="control-label">Confirmation No <span
			class="required">*</span></label> <input type="text" class="form-control"
			value="XXXXXX">
	</div>
</div>
<div class="form-group">
	<div class="col-sm-6">
		<label class="control-label">Title <span class="required">*</span></label>
		<input type="text" class="form-control"
			value="Down hale mud acturated hammer">
	</div>
	<div class="col-sm-6">
		<div class="control-label">&nbsp;</div>
		<input type="checkbox" id="usExport" checked=""><label
			class="control-label" for="usExport">US Export Controlled</label>
	</div>
</div>
<div class="divider"></div>
<div class="form-group">
	<div class="col-sm-6">
		<label class="control-label">Publication Number</label> <input
			type="text" class="form-control" value="">
	</div>
	<div class="col-sm-6">
		<label class="control-label">Publication Date</label>
		<div class='input-group date datepickercontrol'>
			<input type='text' class="form-control" /> <span
				class="input-group-addon"> <i data-alt="calendar"><img
					src="images/svg/calender.svg" class="icon20x"></i>
			</span>
		</div>
	</div>
</div>
<div class="form-group">
	<div class="col-sm-6">
		<label class="control-label">Patent Number</label> <input type="text"
			class="form-control" value="">
	</div>
	<div class="col-sm-6">
		<label class="control-label">Issue Date</label>
		<div class='input-group date datepickercontrol'>
			<input type='text' class="form-control" /> <span
				class="input-group-addon"> <i data-alt="calendar"><img
					src="images/svg/calender.svg" class="icon20x"></i>
			</span>
		</div>
	</div>
</div>