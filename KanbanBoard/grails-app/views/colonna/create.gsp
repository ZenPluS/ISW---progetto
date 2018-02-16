<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Crea colonna</title>
</head>
<body>
    <div class="nav" role="navigation">
        <ul>
            <li><a class="home" href="${createLink(uri: '/board/dashboard')}">Dashboard</a></li>
            <li><a class="back" href="/board/show/${currentBoard.id}">Torna a: ${currentBoard.name}</a></li>
        </ul>
    </div>
<h1 class="sub-title">Crea una nuova colonna</h1>
    <div class="form-section">
        <div class="form">
        <g:form controller="colonna"  action="create">
            <g:if  test="${flash.message}">
                <p class="inputError">${flash.message}</p>
            </g:if>
            <label for="nomeColonna">Nome della colonna: </label>
            <span class="required-indicator">*</span>
            <g:textField name="nomeColonna" required="true"/><br/>
            <g:actionSubmit class="custombutton , form-submit" value="Crea" action="createColumn"/>
        </g:form>
        </div>
    </div>
</body>
</html>