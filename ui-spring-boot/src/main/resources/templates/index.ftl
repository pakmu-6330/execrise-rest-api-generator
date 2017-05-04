<!doctype html>
<html lang="zh-cn">
<head>
<#include "common/header.ftl">
</head>

<!--[if lt IE 7 ]>
<body class="ie ie6"> <![endif]-->
<!--[if IE 7 ]>
<body class="ie ie7 "> <![endif]-->
<!--[if IE 8 ]>
<body class="ie ie8 "> <![endif]-->
<!--[if IE 9 ]>
<body class="ie ie9 "> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->
<body class="theme-blue">
<!--<![endif]-->

<#include "common/navigator.ftl">

<div style="margin-top:50px;" id="main-container-box">
    <div class="sidebar-nav">
        <ul>
            <li><a href="#" data-target=".dashboard-menu" class="nav-header chinese-font" data-toggle="collapse"><i
                    class="fa fa-fw fa-dashboard"></i> 基本功能<i class="fa fa-collapse"></i></a></li>
            <li>
                <ul class="dashboard-menu nav nav-list collapse in">
                    <li><a href="javascript:void(0);" class="chinese-font home-link"><span
                            class="fa fa-caret-right"></span> 主页</a></li>
                    <li><a class="chinese-font stat-link" href="javascript:void(0);"><span
                            class="fa fa-caret-right"></span> 统计信息</a></li>
                </ul>
            </li>

            <li><a href="#" data-target=".accounts-menu" class="nav-header collapsed chinese-font"
                   data-toggle="collapse"><i class="fa fa-fw fa-briefcase"></i> 工程信息<i class="fa fa-collapse"></i></a>
            </li>
            <li>
                <ul class="accounts-menu nav nav-list collapse">
                    <li><a class="chinese-font create-project-link" href="javascript:void(0);"><span
                            class="fa fa-caret-right"></span> &nbsp;创建工程</a></li>
                    <li><a class="chinese-font my-project-link" href="javascript:void(0);"><span
                            class="fa fa-caret-right"></span> &nbsp;我的工程</a></li>
                </ul>
            </li>

            <li><a href="#" data-target=".legal-menu" class="nav-header collapsed chinese-font"
                   data-toggle="collapse"><i class="fa fa-fw fa-legal"></i> 作业信息<i class="fa fa-collapse"></i></a></li>
            <li>
                <ul class="legal-menu nav nav-list collapse">
                    <li><a class="chinese-font submit-job-link" href="javascript:void(0);"><span
                            class="fa fa-caret-right"></span> 提交作业</a>
                    </li>
                    <li><a class="chinese-font submitted-job-link" href="javascript:void(0);"><span
                            class="fa fa-caret-right"></span>
                        已提交的作业</a></li>
                </ul>
            </li>
        </ul>
    </div>
    <div class="content" id="page-container">
    <#include "home.ftl">
    </div>
</div>

<#include "common/tail.ftl">
<script type="text/javascript">
    $("[rel=tooltip]").tooltip();
    var mainContainerBox = $("#main-container-box");
    var contentDiv = $(".content");

    window.onresize = function () {
        var newHeight = (document.documentElement.clientHeight - 50) + "px";
        mainContainerBox.css("height", newHeight);
        contentDiv.css("height", newHeight);
    };

    var pageContainer = $("#page-container");

    $(function () {
        var newHeight = (document.documentElement.clientHeight - 50) + "px";
        mainContainerBox.css("height", newHeight);
        contentDiv.css("height", newHeight);

        $(".home-link").bind("click", function() {
            contentDiv.load("${springMacroRequestContext.contextPath}/function/home")
        });

        $(".my-project-link").bind("click", function() {
              contentDiv.load("${springMacroRequestContext.contextPath}/page/projects")
        })
    });

</script>


</body>
</html>
