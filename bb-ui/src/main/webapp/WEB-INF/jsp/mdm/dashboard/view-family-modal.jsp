<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="pathImg" value="${contextPath}/assets/images/svg" />

<div class="modal custom fade modal-wide" id="viewFamily" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel">
	<!-- Data through AJAX will be added here. -->

	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">View Family</h4>
			</div>
			<div class="modal-body" id="viewFamilyBody">
			
			</div>
		</div>
	</div>
</div>