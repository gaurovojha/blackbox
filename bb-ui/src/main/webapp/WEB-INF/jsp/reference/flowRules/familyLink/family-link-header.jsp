<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
%>
<%-- <script type="text/javascript" src="<%=js%>/reference/reference-flow-rule.js"></script> --%>

	<div class="form-control-static">
		<a href="#" data-target="#familyFlowExceptions" data-toggle="modal" id= "familyException">View Family Flow Exceptions</a>
	</div>
	<table id="referenceFlowFamilyLinkTbl"	class="table custom-table referenceTable">
		<tr>
			<td>
				<jsp:include page="family-link-source.jsp"/>
			</td>
			<td>
				<jsp:include page="family-link-destination.jsp"/>
			</td>
		</tr>
	</table>

	<!-- family flow exceptions -->
	<div class="modal custom fade modal-wide" id="familyFlowExceptions" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="myModalLabel">Family Flow Exceptions</h4>
	      </div>
	      	<div class="modal-body">
		      
        		<div class="form-horizontal">
        			<table id="familyFlowRuleExceptionTable" class="table custom-table">
						<thead>
						<tr class="small-height-row">
				            <th colspan="3" class="text-center">Source Application</th>
				            <th colspan="3" class="text-center">Destination Application</th>
				            <th></th>
				            <th></th>
				        </tr>
				        <tr>
				        	<th class="bdr-rt-none">Jurisdiction</th>
				            <th class="bdr-rt-none">Application #</th>
				            <th>Attorney Docket #</th>
				            <th class="bdr-rt-none">Jurisdiction</th>
				            <th class="bdr-rt-none">Application #</th>
				            <th>Attorney Docket #</th>
				            <th>Last Edit</th>
				            <th>Status Change Comment</th>
				        </tr>
						</thead>
						<tbody id="familyExceptionTbl"></tbody>
						</tbody>
					</table>
        		</div>
		     
      		</div>
	    </div>
	  </div>
	</div>

<div id="pageInfo"><jsp:text/></div>