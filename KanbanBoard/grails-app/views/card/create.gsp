<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main"/>
        <title>Crea Card</title>
    </head>
    <body>
    <div class="nav" role="navigation">
        <ul>
            <li><a class="home" href="${createLink(uri: '/board/dashboard')}">Dashboard</a></li>
            <li><a class="back" href="/board/show/${currentBoard.id}">Torna a: ${currentBoard.name}</a></li>
        </ul>
    </div>
    <h1 class="sub-title">Crea nuova Card</h1><br/>

    <div class="form-section">
        <div class="form">
            <g:form controller="card"  action="createCard">
                <g:if  test="${flash.message}">
                    <p class="inputError">${flash.message}</p>
                </g:if>
                <div class="form-label">
                <label for="nomeCard" class="form-titolo">Titolo:</label><span class="required-indicator">* </span><br/>
                <label for="descrizione" class="form-descrizione">Descrizione:</label><span class="required-indicator">* </span><br/>
                </div>
                <div class="form-input">
                    <g:textField class="gselect_inner" name="nomeCard" required="true"/><br/>
                    <g:textArea class="gselect_inner" name="descrizione" required="true"></g:textArea><br/>

                </div>
                <div class="form-date-label">
                    <label class="form-scadenza">Scadenza:</label><br/>
                </div>
                <div class="form-date-input">
                        <g:datePicker id="datapicker" name="scadenza" precision="day"></g:datePicker>
                </div>
                <g:actionSubmit class="custombutton , form-submit" value="Crea" action="createCard"/>
            </g:form>
        </div>
    </div>
    </body>
</html>