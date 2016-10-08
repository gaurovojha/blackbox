$(function () {
    $(".hidden-row").hide();
    $(".has-hidden-row span").on("click", function () {

        if ($(this).hasClass("icon-plus")) {
            $(this).parents(".has-hidden-row").addClass("active");
            $(this).removeClass("icon-plus").addClass("icon-minus");
            $(this).parents(".has-hidden-row").siblings(".hidden-row").toggle();
        } else if ($(this).hasClass("icon-minus")) {
            $(this).parents(".has-hidden-row").removeClass("active");
            $(this).removeClass("icon-minus").addClass("icon-plus");
            $(this).parents(".has-hidden-row").siblings(".hidden-row").toggle();
        }


    });


    $('.icon-calendar').click(function () {
        $(document).ready(function () {
            $("#config-demo").daterangepicker({
                opens: 'left'
            }).focus();
        });
    });

    $(".header .search-input .form-control").on("focus", function () {
        $(this).parents(".search-input").addClass("active");
    });
    $(".header .search-input .form-control").on("focusout", function () {
        $(this).parents(".search-input").removeClass("active");
    });


    $("#gotoSearch").on("click", function () {
        window.location.assign("search.html");
    });


    var searchHeight = $('.search-dropdown').height();
    //	$('.search-dropdown').css("marginTop", -searchHeight);

    $(".header .search-input .form-control").on("focus", function () {
        //$(this).parents(".search-input").addClass("active");
        //$('.search-dropdown').animate({"marginTop": 0, "toggle": "height"}, 1500);
        $('.search-dropdown').slideToggle("show");
    });

    $("#hideSearch").on("click", function () {
        $(this).parents(".search-input").removeClass("active");
        $(".search-dropdown").slideUp("hide");
    });

    //Alt key shortcut combination 
    function KeyCode(e) {
        if (e.which == 9) {
            $(".a").focus();
        }
        if (e.shiftKey) {
            if (e.preventDefault) { //prevent default action that belongs to the event.
                e.preventDefault();
            } else {
                e.returnValue = false;
            }
            $(".shortcut-pic").toggle(); //show/hide shortcuts
        }

        var keycode = e.which; //get Keycode
        if (e.shiftKey && keycode == 88) {
            $('.nav-tabs a[href="#updateDocTab"]').tab('show');
        } else if (e.shiftKey && keycode == 89) {
            $('.nav-tabs a[href="#addDocTab"]').tab('show');
        } else if (e.shiftKey && keycode == 68) {
            $(location).attr('href', 'dashboard.html');
        } else if (e.shiftKey && keycode == 82) {
            $(location).attr('href', 'sent-requests.html');
        }
    }
    document.onkeydown = KeyCode;

});