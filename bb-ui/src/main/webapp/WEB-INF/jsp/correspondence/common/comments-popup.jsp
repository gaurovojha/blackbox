<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>



<div id="commentsPopup" class="popup-msg">

	 <div class="text-right"><a class="close" href="#" onclick="hideCommentsPopup()">&times;</a></div>
		<div class="content">
			<p class="msg">
			 </p>
		</div>
		<div class="content">
			<p class="comments"> </p>
			<textarea name="userComment" id="userComment" class="form-control" rows="4" cols="4"></textarea>
			 
		</div>
		<div class="modal-footer">
                <button type="button" class="btn btn-submit btnDeleteYes" data-dismiss="modal" onclick="hideCommentsPopup();">YES</button>
                <button type="button" class="btn btn-cancel" onclick="hideCommentsPopup();">NO</button>
        </div>
	</div>  
	
	<script>
	function showCommentsPopup()
	{
		$('#commentsPopup').show();
		$('#commentsPopup').wrap("<div class='overlay'>");
	}
	function hideCommentsPopup()
	{
		$('#commentsPopup').hide();
		$('#commentsPopup').unwrap("<div class='overlay'>");
	}
	</script>