<button type="submit" value="submit" class="search-btn">
	<i class="icon-search" data-alt="search"><img
		src="${pathImg}/search.svg" class="icon20x"></i>
</button>
<div class="search-dropdown">
	<h5>Search By</h5>
	<ul class="nav nav-tabs custom-tabs" role="tablist">
		<li role="presentation" class="active"><a
			href="#applicationNoTab" role="tab" data-toggle="tab">Application#</a></li>
		<li role="presentation"><a href="#attorneyDocTab" role="tab"
			data-toggle="tab">Attorney Docket No.</a></li>
		<li role="presentation"><a href="#mailingDateTab" role="tab"
			data-toggle="tab">Family Id</a></li>
		<li role="presentation"><a href="#moreTab" role="tab"
			data-toggle="tab">More</a></li>
	</ul>

	<div class="tab-content">
		<div role="tabpanel" class="tab-pane active" id="applicationNoTab">
			<form class="form-horizontal">
				<div class="form-group">
					<div class="col-sm-12">
						<div class="col-sm-6">
							<label class="control-label">Application Number</label> <input
								type="text" id="txtApplicationNo" class="form-control">

						</div>
						<div class="col-sm-6">
							<label class="control-label">Jurisdiction</label> <input
								type="text" id="txtJurisdiction" class="form-control">
						</div>
					</div>
				</div>
			</form>
		</div>
		<div role="tabpanel" class="tab-pane" id="attorneyDocTab">
			<form class="form-horizontal">
				<div class="form-group">
					<div class="col-sm-12">
						<div class="col-sm-6">
							<label class="control-label">Attorney Docket Number</label> <input
								type="text"  id="txtAttorneyDocketNo" class="form-control">
						</div>
					</div>
				</div>
			</form>
		</div>
		<div role="tabpanel" class="tab-pane" id="mailingDateTab">
			<form class="form-horizontal">
				<div class="form-group">
					<div class="col-sm-12">
						<div class="col-sm-6">
							<label class="control-label">Family ID</label> <input type="text" id="txtFamilyId"
								class="form-control">
						</div>
					</div>
				</div>
			</form>
		</div>
		<div role="tabpanel" class="tab-pane" id="moreTab">
			<div class="form-horizontal">
				<div class="form-group">
					<div class="col-sm-12">
						<div class="col-sm-6">
							<label class="control-label">Jurisdiction</label> <input
								type="text" class="form-control">
						</div>
						<div class="col-sm-6">
							<label class="control-label">Application#</label> <input
								type="text" class="form-control">
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-12">
						<div class="col-sm-6">
							<label class="control-label">Upload Date</label>
							<div class="daterange-picker">
								<input type="text" class="form-control date" name="datefilterSearch" id="txtUploadedOn"> <span
									class="calendar"><i class="glyphicon glyphicon-calendar"></i></span>
							</div>

						</div>
						<div class="col-sm-6">
							<label class="control-label">Document Description</label> <input
								type="text" id = "txtDescription"class="form-control">
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-12">
						<div class="col-sm-6">
							<label class="control-label">Uploaded By</label> <input
								type="text" id="txtUploadedBy" class="form-control">
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="search-footer clearfix">
		<div class="text-left">
			<button class="btn btn-cancel" id="hideSearch">Cancel</button>
			<button class="btn btn-submit" id="gotoSearch">Search</button>
		</div>
	</div>
</div>