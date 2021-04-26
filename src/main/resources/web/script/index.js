$(document).ready(function() {
    $.ajax({
        url: "/getMaps",
        type: "GET",
        data: "jsonp",
        success: function(response) {
            $(".databaseInfo").text(response);
        }
    })
});

$("#uploadButton").click(function(e) {
    let osmFiles = new FormData(),
        url = '/uploadOSM';

    osmFiles.append('osmFile', $('#uploadFile')[0].files[0]);
    $.ajax({
        type: 'POST',
        url: url,
        processData: false,
        contentType: false,
        data: osmFiles,
        success: function (response) {
            console.log(response);
        },
        error: function(response) {
            console.log(response);
        }
    });
});

$('.menu')
    .on('click', '.item', function() {
        if(!$(this).hasClass('dropdown')) {
            $(this)
                .addClass('active')
                .siblings('.item')
                .removeClass('active');
        }
    });

function changePage(x) {
    let page = document.getElementsByClassName(x);
    $(page).addClass('activePage').removeClass('inactivePage');
    $(page).siblings('.page').removeClass('activePage').addClass('inactivePage');
}