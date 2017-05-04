$(function () {
    var contentBox = $("#content");

    $(".nav-item-my-project").bind('click', function () {
        contentBox.load("/page/projects");
    });
});