<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<%
	String context = request.getContextPath();
	String css = context+"/assets/css";
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>

<html>
<head>
	<title><spring:message code="title.blackbox" /></title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">

	<link rel="stylesheet" type="text/css" href="<%=css%>/bootstrap.min.css">
	<!-- <link rel="stylesheet" type="text/css" href="css/https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.5.0/css/bootstrap-datepicker.css"> -->
	<link rel="stylesheet" type="text/css" href="<%=css%>/daterangepicker.css">
	<link rel="stylesheet" type="text/css" href="<%=css%>/main.css">
	<link rel="stylesheet" type="text/css" href="<%=css%>/datatables.min.css">

	
</head>
<body>
<header class="header">
		<div class="container">
			<a href="javascript:void(0)" class="logo"><img src="<%=images%>/logo_site.gif" alt="Torry | Morris"></a>
			<div class="header-right">
				<div class="dropdown user-login">
				  <button class="btn btn-default dropdown-toggle" type="button" id="userdropdown" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
				    John Doe 
				    <span class="caret"></span>
				  </button>
				  <ul class="dropdown-menu" aria-labelledby="userdropdown">
				    <li><a href="<%=context%>/logout">Log Out</a></li>
				  </ul>
				</div>
				 <img src="<%=images%>/logo_ids.gif" alt="IDS">
			</div>
		</div>
	</header>
	<script type="text/javascript" src="<%=js%>/jquery.min.js"></script>
	<script type="text/javascript" src="<%=js%>/moment.js"></script>		
	<script type="text/javascript" src="<%=js%>/bootstrap-datepicker.js"></script>
	<script type="text/javascript" src="<%=js%>/bootstrap-datepicker.js"></script>
	<script type="text/javascript" src="<%=js%>/multiselect.min.js"></script>
	<script type="text/javascript" src="<%=js%>/bootstrap.min.js"></script>	
	<script type="text/javascript" src="<%=js%>/datatables.min.js"></script>

	<!-- Pagination Data Table -->
	
	<script type="text/javascript">

    //Plug-in to fetch page data 
	jQuery.fn.dataTableExt.oApi.fnPagingInfo = function ( oSettings )
	{
		return {
			"iStart":         oSettings._iDisplayStart,
			"iEnd":           oSettings.fnDisplayEnd(),
			"iLength":        oSettings._iDisplayLength,
			"iTotal":         oSettings.fnRecordsTotal(),
			"iFilteredTotal": oSettings.fnRecordsDisplay(),
			"iPage":          oSettings._iDisplayLength === -1 ?
				0 : Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength ),
			"iTotalPages":    oSettings._iDisplayLength === -1 ?
				0 : Math.ceil( oSettings.fnRecordsDisplay() / oSettings._iDisplayLength )
		};
	};

	
	function change(value){
		
		alert(value);
	}
$(document).ready(function() {

	$("#userlist").dataTable( {
		
		
        "bProcessing": true,
        "bServerSide": true,
        "sort": "position",
        //bStateSave variable you can use to save state on client cookies: set value "true" 
        "bStateSave": false,
        //Default: Page display length
        "iDisplayLength": 10,
        //We will use below variable to track page number on server side(For more information visit: http://legacy.datatables.net/usage/options#iDisplayStart)
        "iDisplayStart": 0,
        "fnDrawCallback": function () {
            //Get page numer on client. Please note: number start from 0 So
            //for the first page you will see 0 second page 1 third page 2...
            //Un-comment below alert to see page number
        	//alert("Current page number: "+this.fnPagingInfo().iPage);    
        },
        "columnDefs": [
                       {
                           // The `data` parameter refers to the data for the cell (defined by the
                           // `data` option, which defaults to the column being worked with, in
                           // this case `data: 0`.
                           "render": function ( data, type, row ) {

                        	   return "<a href='#' onclick='change(\"" +  data + "\")'>"+'Edit Setup |  '+"</a>"+"<a href='#' onclick='change(\"" +  data + "\")'>"+'Edit Business Rule'+"</a>";

                               //return data +' test data';
                           },
                           "targets": 2
                       }
                   ],
        "sAjaxSource": "/users",
        "aoColumns": [
            { "mData": "name" },
            { "mData": "position" },
            { "mData": "office" },
            { "mData": "phone" },
            { "mData": "start_date" },
            { "mData": "salary" },
             
        ]
    } );

} );

</script>
	
	
</body>
</html>