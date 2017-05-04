<div class="row" style="text-align: center">
    <ul class="pagination">
    <#if page.number gt 3>
        <#assign minPage=page.number-3>
    <#else >
        <#assign minPage=1>
    </#if>

    <#if page.number lt page.totalPages-3>
        <#assign maxPage=page.totalPages-3>
    <#else >
        <#assign maxPage=page.totalPages>
    </#if>

    <#if page.first == false>
        <li><a href="#">«</a></li>
    </#if>
    <#list minPage..maxPage as i>
        <#if i == page.number+1>
            <li class="active"><a href="#">${i}</a></li>
        <#else >
            <li><a href="#">${i}</a></li>
        </#if>
    </#list>
    <#if page.last == false>
        <li><a href="#">»</a></li>
    </#if>
    </ul>
</div>