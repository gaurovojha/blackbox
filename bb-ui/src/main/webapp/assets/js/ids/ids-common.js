$(function () {

    //table expand collapse js
    $(".hidden-row").hide();
    $(".has-hidden-row i.expand-collapse").on("click", function () {

      //if($(this).parents("tr"))
      if ($(this).find("img").attr("src")=="images/svg/plus.svg") {
         // $(this).parents(".has-hidden-row").addClass("active");
          $(this).find("img").attr("src","images/svg/minus.svg");

          $(this).parents(".has-hidden-row").nextAll("tr")
              .each(function () {
                  if ($(this).hasClass("hidden-row")) {
                     $(this).toggle();
                  } else {
                      return false;
                  }
              });
      } else if ($(this).find("img").attr("src")=="images/svg/minus.svg") {
        //  $(this).parents(".has-hidden-row").removeClass("active");
          $(this).find("img").attr("src","images/svg/plus.svg");
          $(this).parents(".has-hidden-row").nextAll("tr").each(function () {
              if ($(this).hasClass("hidden-row")) {
                  $(this).toggle();
              } else {
                  return false;
              }
          });
      }


    });

    // search expand collapse js
    var searchHeight = $('.search-dropdown').height();

    $(".search-control .icon-search").on("click", function(){
      $('.search-dropdown').slideToggle("show");
    });

    $("#hideSearch").on("click", function(){
      $(".search-dropdown").slideUp("hide");
    });

    // datepicker js
    $('.datepicker').datepicker({
      format: "M dd, yyyy"
    });

    //date range picker js
    $(".daterange-picker .date").daterangepicker({
        opens:'left',
         autoUpdateInput: false,
        locale: {
          format: 'MMM DD, YYYY',
          cancelLabel: 'Clear'
        }
    });

    $('.daterange-picker i').click(function() {
      $(this).parents(".daterange-picker").find('input').click();
    });

    $('.daterange-picker .date').on('cancel.daterangepicker', function(ev, picker) {
      $(this).parents(".daterange-picker").find('input').val('');
    });

    $('input[name="datefilter"]').on('apply.daterangepicker', function(ev, picker) {
      $(this).val(picker.startDate.format('MMM DD, YYYY') + ' - ' + picker.endDate.format('MMM DD, YYYY'));
    });

    $('input[name="datefilter"]').on('cancel.daterangepicker', function(ev, picker) {
      $(this).val('');
    });

    //reference record dropdown js   

    $(document).on('click', '#reference-search-control', function (e) {
            e.stopPropagation();
        });
     $("#reference-search-control .btn-cancel").on("click", function(){
            $("body").click();
        });


});

$(".DoNotActn").click(function(){
    var comment;
 //   comment = $('#cmntForApproval').attr('value');
    alert($('#cmntForApproval').attr('value'));
 });
 
 $("#includeBtn").click(function(){

	  var target =$('#contextPath').val() + "/ids/attorneyApproval/viewIncludeAllReferences"; 
	  var data = $('#appDetails').val();
	  $.ajax({
         type: "POST",
         url: target,
         data: {"appForm":data},
	     success: function(data) {
	        $('#accordion').html(data);
	     }
     });
 });
 
 
 $(document).on('click', '.moreUsers', function() {
		var $parent = $(this).parents('td');
		$parent.find('.moreUsers').hide();
		$parent.find('.spanMoreUsers').show();
});
 
$(".reviewed").hide();
$(".flag").on("click", function(){
	$(this).hide();
	$(".reviewed").show();
});
 
function popupMsgForReview(){
//	var refrenceID=($(event.target).attr('id')==='undefined'?'':$(event.target).attr('id'));
	
	$("#popupMsgReview").removeClass("hide");
	$("#popupMsgReview").show();
	$("#popupMsgReview").wrap("<div class='overlay'>");
	$("#popupMsgReview").closest('a .close').bind('click', function() {
		hideReviewPopUp()
	});
}

function hideReviewPopUp(){
	$("#popupMsgReview").addClass("hide");
	$("#popupMsgReview").unwrap("<div class='overlay'>");
}
