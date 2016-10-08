<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

            <c:set var="context" value="${pageContext.request.contextPath}" />
    
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <link rel="stylesheet" type="text/css" href="${context}/assets/css/main.css"></link>
            <link rel="stylesheet" type="text/css" href="${context}/assets/css/datatables.min.css"></link>
            
        <div>
		<sec:authorize access="canAccessUrl('/notification/setUpNotification')" var="notificationSetup" />
		<sec:authorize access="canAccessUrl('/notification/editBussinessRules')" var="notifcationEditRules" />
		<sec:authorize access="canAccessUrl('/notification/dashBoard')" var="notifcationDashBoard" />
        </div>
           
           <input type="hidden" id="setupNotificationAccess" value="${notificationSetup}"/>
           <input type="hidden" id="editNotificationRulesAccess" value="${notifcationEditRules}"/>
           <input type="hidden" id="dashBoardAccess" value="${notifcationDashBoard}"/>
            
            <div class="main-content container">
                <div class="tab-container">
                    <ul class="tab-actions pull-right">
                        <li>
                            <div class="inner-search notificationSerach">
                                <select class="form-control tablesearchOption">
                                    <option id='all'>All</option>
                                    <option id='0'>Notification Name</option>
                                    <option id='1'>Notification Type</option>
                                    <option id='2'>Display on IDS Review</option>
                                    <option id='3'>Email Notification</option>
                                    <option id='4'>Reminder</option>
                                    <option id='5'>Escalation</option>
                                    <option id='6'>No of Business Rules</option>
                                </select>
                                <div class="input-group">
                                    <span class="input-group-btn">
								<button class="search"><span class="icon icon-search-inner"></span>
                                    </button>
                                    </span>
                                    <input type="text" placeholder="Search" id='notificationTableSearch'>
                                </div>
                            </div>
                        </li>
                        <li><a href="" class='export' ><span class="icon icon-export"></span>Export</a>
                        </li>

                    </ul>
                    <!-- Nav tabs -->
                    <ul class="nav nav-tabs custom-tabs" role="tablist">
                        <li role="presentation" class="active"><a href="#notificationTab" role="tab" data-toggle="tab">Workflow Setup</a>
                        </li>
                        <li role="presentation"><a href="#adminTab" role="tab" data-toggle="tab">Workflow Admin</a>
                        </li>
                    </ul>
                    <!-- Tab panes -->
                    <div class="tab-content">
                        <div role="tabpanel" class="tab-pane active" id="notificationTab">
                            <div class="tab-heading clearfix">
                                <h3 class="pull-left">List of Notification</h3>
                                <!-- 	<button type="button" class="btn btn-submit pull-right"  data-toggle="modal" id="createNotification">Create Notification</button> -->
                            </div>
                            <table style="border: 3px;">
                                <tr>
                                    <td>
                                        <table id="notificationTable" class="table custom-table" >
                                            <thead>
                                                <tr>
                                                    <th>Notification Name</th>
                                                    <th>Notification Type</th>
                                                    <th>Display on IDS Review</th>
                                                    <th>Email Notification</th>
                                                    <th>Reminder</th>
                                                    <th>Escalation</th>
                                                    <th>No of Business Rules</th>
                                                    <th>Actions</th>
                                                </tr>

                                            </thead>
                                        </table>
                                    </td>
                                </tr>
                            </table>

                        </div>
                        <div role="tabpanel" class="tab-pane" id="adminTab">
                            <h2>Admin data</h2>
                        </div>
                    </div>

                </div>
            </div>

            <!-- create new role modal -->
            <div class="modal custom fade modal-wide" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                            </button>
                            <h4 class="modal-title" id="myModalLabel">Select Role</h4>
                        </div>
                        <div class="modal-body">
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <div class="col-sm-3 col-lg-2">
                                        <label class="control-label">Type of Role</label>
                                    </div>
                                    <div class="col-sm-5 col-lg-3">
                                        <select class="form-control" id="selectRole">
                                            <option>Select</option>
                                            <option value="duplicate role">Duplicate Role</option>
                                            <option value="create new role">Create New Roles</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="clearfix">
                                <div id="duplicateRoleContainer">
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-sm-3 col-lg-3">
                                                <div class="input-group inner-search">
                                                    <span class="input-group-btn"><button class="search"><span class="icon icon-search-inner"></span>
                                                    </button>
                                                    </span>
                                                    <input type="text" placeholder="Search the Role">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <table class="table custom-table">
                                        <thead>
                                            <tr>
                                                <th>Role Name</th>
                                                <th>Access Profile</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr class="odd">
                                                <td><span class="icon icon-info"></span> Role 1</td>
                                                <td>By Correspondance</td>
                                                <td><a href="javascript:void(0)">Select</a>
                                                </td>
                                            </tr>
                                            <tr class="even">
                                                <td><span class="icon icon-info"></span> Role 2</td>
                                                <td>By MDM</td>
                                                <td><a href="javascript:void(0)">Select</a>
                                                </td>
                                            </tr>
                                            <tr class="odd">
                                                <td><span class="icon icon-info"></span> Role 3</td>
                                                <td>By Reference</td>
                                                <td><a href="javascript:void(0)">Select</a>
                                                </td>
                                            </tr>
                                        </tbody>

                                    </table>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-cancel" data-dismiss="modal">Cancel</button>
                            <button type="button" class="btn btn-submit" id="duplicateAcessProfile">Submit</button>
                        </div>
                    </div>
                </div>
            </div>
            <script type="text/javascript" src="${context}/assets/js/notification/dashboard.js"></script>
           
            