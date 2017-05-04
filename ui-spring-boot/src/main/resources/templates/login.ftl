<!DOCTYPE HTML>
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

<div class="navbar navbar-default" role="navigation">
    <div class="navbar-header">
        <a class="" href="index.html"><span class="navbar-brand"><span class="fa fa-paper-plane"></span>&nbsp;&nbsp;&nbsp;GFish Resource Library Manager</span></a>
    </div>
    <div class="navbar-collapse collapse" style="height: 1px;">
    </div>
</div>
</div>
<div class="dialog">
    <div class="panel panel-default">
        <p class="panel-heading no-collapse">请登录</p>
        <div class="panel-body">
            <form action="${springMacroRequestContext.contextPath}/login" method="POST">
                <div class="form-group">
                    <span>${errMsg!}</span>
                </div>
                <div class="form-group">
                    <label>用户名</label>
                    <input name="username" type="text" class="form-control span12">
                </div>
                <div class="form-group">
                    <label>密码</label>
                    <input name="password" type="password" class="form-control span12 form-control">
                </div>
                <input type="hidden" name="redirectUrl" value="${redirectUrl}">
                <button class="btn btn-primary pull-right" type="submit">登录</button>
                <div class="clearfix"></div>
            </form>
        </div>
    </div>
    <p class="pull-right" style=""><a href="#" target="blank" style="font-size: .75em; margin-top: .25em;">Design by
        Portnine</a></p>
</div>

<#include "common/tail.ftl">
<script type="text/javascript">
    $("[rel=tooltip]").tooltip();
    $(function () {
        $('.demo-cancel-click').click(function () {
            return false;
        });
    });
</script>
</body>
</html>
