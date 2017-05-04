<div class="header">
    <h1 class="page-title">我的工程</h1>
    <ul class="breadcrumb">
        <li>主页</li>
        <li>工程信息</li>
        <li class="active">我的工程</li>
    </ul>
</div>
<div class="main-content">
    <table class="table">
        <thead>
        <tr>
            <th style="width: 15.0em">工程编号</th>
            <th>名称</th>
            <th style="width: 3.5em"></th>
            <th style="width: 3.5em"></th>
        </tr>
        </thead>
        <tbody>
        <#list page.content as project>
        <tr>
            <td>${project.projectId!}</td>
            <td>${project.displayName!}</td>
            <td>编辑</td>
            <td>删除</td>
        </tr>
        </#list>
        </tbody>
    </table>
<#include "../common/pagination.ftl">
</div>