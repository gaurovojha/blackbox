<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="modal custom fade" id="dropRefAllIdsPopUp" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" style="display: none;">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
        <h4 class="modal-title" id="myModalLabel">Drop Reference </h4>
      </div>
      <div class="modal-body">
      	<div class="form-horizontal">
      		<p>Do you want to drop the references from all IDS?</p>
      		<div class="form-group">
                <div class="col-sm-12">
                	<button class="btn btn-submit yes"  data-dismiss="modal">Yes</button>
                	<button class="btn btn-submit"  data-dismiss="modal">No</button>
                </div>
            </div>
	       	<div class="form-group">
		    	<div class="col-sm-12">
		    	</div>
		    </div>
		</div>
      </div>
    </div>
  </div>
</div>

<div class="modal custom fade" id="dropRefFromThisIdsPopUp" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" style="display: none;">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
        <h4 class="modal-title" id="myModalLabel">Drop From This IDS</h4>
      </div>
      <div class="modal-body">
      	<div class="form-horizontal">
      		<p>Do you want to drop the references from this IDS?</p>
      		<div class="form-group">
                <div class="col-sm-12">
                	<button class="btn btn-submit yes"  data-dismiss="modal">Yes</button>
                	<button class="btn btn-submit" data-dismiss="modal">No</button>
                </div>
            </div>
	       	<div class="form-group">
		    	<div class="col-sm-12">
		    	</div>
		    </div>
		</div>
      </div>
    </div>
  </div>
</div>