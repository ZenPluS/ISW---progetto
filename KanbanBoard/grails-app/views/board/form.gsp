<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main"/>
        <title>Crea nuova Board</title>
    </head>
    <body>
    <div class="nav" role="navigation">
        <ul>
            <li><a class="home" href="${createLink(uri: '/board/dashboard')}">Dashboard</a></li>
        </ul>
    </div>
    <h1 class="sub-title">Crea nuova Board</h1>
    <div class="form-section">
        <div class="form">
        <g:form controller="board" action="create">
            <g:if  test="${flash.message}">
                <p class="inputError">${flash.message}</p>
            </g:if>
            <label for="nomeBoard">Nome della board: </label>
            <span class="required-indicator">*</span>
            <g:textField name="nomeBoard" required="true"/><br/>
            <g:actionSubmit class="custombutton , form-submit" value="Crea" id="createBoard" action="create"/>
        </g:form>
        </div>
    </div>
    </body>
</html>