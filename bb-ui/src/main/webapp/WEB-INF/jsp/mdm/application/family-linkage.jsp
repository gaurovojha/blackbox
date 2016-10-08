<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="modal custom fade" id="popupEditFamily" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">

    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">Edit Family Linkage</h4>
      </div>

      <div class="modal-body">
     		<div id="divFamilySearchForm" class="familySearch">
				<jsp:include page="family-search-form.jsp" />
			</div>
			<div id="resultFamilySearch" class="familySearch"><jsp:text /></div>
      </div>
    </div>
  </div>
</div>