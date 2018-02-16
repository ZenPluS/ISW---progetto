<%@ page import="kanbanboard.Utenti" %>
<!DOCTYPE html>
<html>

    <head>
        <meta name="layout" content="main"/>
        <title>Form aggiungi utenti</title>
    </head>
    <body>
    <a href="#edit-card" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
        <ul>
            <li><a class="home" href="${createLink(uri: '/board/dashboard')}">Dashboard</a></li>
            <li><a class="back" href="/board/show/${currentBoard.id}">Torna a: ${currentBoard.name}</a></li>
            <li><g:link id="deleteBoard" class="custombutton , deleteButtonBar" action="deleteBoard" onclick="return confirm('Sei sicuro di voler cancellare la board ${currentBoard.name}? Tutte le colonne e le card al suo interno verranno perse');">Elimina Board</g:link></li>
        </ul>
    </div>
    <h1 class="sub-title">Modifica Board</h1>
    <div class="form-section">
        <div class="form">
            <g:form controller="board" action="update" name="editColonna">
                <g:hiddenField name="idBoard" value="${currentBoard.id}" />
                <g:if  test="${flash.message}">
                    <p class="inputError">${flash.message}</p>
                </g:if>
                <div class="form-label">
                    <label for="nome" class="form-titolo">Nome: </label><span class="required-indicator">*</span>
                </div>
                <div class="form-input , form-input-editBoard">
                    <g:textField  class="gselect_inner" name="nome" required="true" value="${currentBoard.name}"/><br/>
                </div>
                <div class="form-label">
                    <label class="form-users">Aggiungi collaboratori:  </label>
                </div>
                <div class="form-input , gselect">
                <g:select class="gselect_inner"
                          name="utenteSelezionato"
                          from="${kanbanboard.Utenti.all - currentBoard.users}"
                          optionKey="id"
                          optionValue="username"
                          multiple="true"
                />
                </div>
                <div class="form-label">
                        <label class="form-users">Elimina collaboratori:  </label>
                </div>
                <div class="form-input , gselect">
                <g:select class="gselect_inner"
                          name="utenteSelezionatoRimuovi"
                          from="${currentBoard.users - kanbanboard.Utenti.findById(currentBoard.admin)}"
                          optionKey="id"
                          optionValue="username"
                          multiple="true"
                />
                </div>
                <g:actionSubmit controller="board" class="custombutton , form-submit" value="Accetta" action="aggiorna"/>
            </g:form>
        </div>
    </div>
    </body>

</html>
