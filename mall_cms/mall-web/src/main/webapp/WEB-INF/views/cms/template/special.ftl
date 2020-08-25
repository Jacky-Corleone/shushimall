<div>

工程案例
<hr>
	<div>
		<@CmsCategoryListTag name="工程案例" delFlag="0">
			<#list categoryList as category>
				<div>
					<@CmsArticleListTag cityid="${category.id}" delFlag="0">
						<#list articleList as article>
							<span><a href="${result.prefix}${article.link}">${article.name}</a></span></br>	
						</#list>
					</@CmsArticleListTag>
				</div>
			</#list>
		</@CmsCategoryListTag>
	</div>
</div>
