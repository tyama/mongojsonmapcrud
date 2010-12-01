
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'fullMap.label', default: 'FullMap')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
			<div>
				<g:form name="list" action="list">
					<span>Search.</span> <g:textField name="query" value="${params.query?:''}" style="width:600;padding:4px"/>
				</g:form>
				<div style="margin:3px">Ex. [ name:searchString tel:000-000-0000] or [keyword_to_search]</div>
			</div>
            <div class="list">
                <table>
                    <thead>
                        <tr>
							<th>${message(code: 'fullMap.id.label', default: 'Id')}</th>
							<th>${message(code: 'fullMap.content.label', default: 'Content')}</th>
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${list}" status="i" var="map">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
							<g:set var="id" value="${map.remove('_id')}"/>
                            <td><g:link action="show" id="${id}">${id}</g:link></td>
                            <td>${map}</td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${count}" />
            </div>
        </div>
    </body>
</html>
