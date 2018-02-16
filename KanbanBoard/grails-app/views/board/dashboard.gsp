<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'board.label', default: 'Board')}" />
    <title>Dashboard</title>
</head>

<body>
<div class="nav" role="navigation">
    <ul>
        <li><g:link class="create" action="form">Nuova Board</g:link></li>
    </ul>
</div>


<div id="list-board" class="content scaffold-list" role="main">
    <h1 class="sub-title">Dashboard</h1>
    <g:each in="${boards.sort{a,b-> b.id.compareTo(a.id)} }">
        <g:link class="board" action="show" resource="${it}">
            <div class="board">
                <p class="nomeBoard">
                    ${it.name}<br>
                    ${it.users.username}
                </p>
            </div>
        </g:link>
    </g:each>
</div>
</body>
</html>