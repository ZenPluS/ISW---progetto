<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'card.label', default: 'Card')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#edit-card" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/board/dashboard')}">Dashboard</a></li>
                <li><a class="back" href="/board/show/${currentBoard.id}">Torna a: ${currentBoard.name}</a></li>
                <li><g:link id="deleteCard" class="custombutton , deleteButtonBar" action="delete" params="[currentCard: currentCard.id , idCNA: currentColumn.id , currentCol: currentColumn.id]" onClick="return confirm('Sei sicuro di voler cancellare la card ${currentCard.titolo}?')">Elimina Card</g:link></li>
            </ul>
        </div>
    <h1 class="sub-title">Modifica Card</h1>
        <div id="edit-card" class="content scaffold-edit" role="main">


            <g:hasErrors bean="${this.card}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.card}" var="error">
                    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <div class="form-section">
                <div class="form">

                    <g:form controller="card"  action="update">
                        <g:if  test="${flash.message}">
                            <p class="inputError">${flash.message}</p>
                        </g:if>
                        <g:hiddenField name="idCard" value="${currentCard.id}" />
                        <div class="form-label">
                            <label class="form-titolo">Titolo: </label><span class="required-indicator">* </span><br/>
                            <label class="form-descrizione">Descrizione:</label><span class="required-indicator">* </span><br/>
                        </div>
                        <div class="form-input">
                            <g:textField  class="gselect_inner" name="titolo" required="true" value="${currentCard.titolo}"/><br/>
                            <g:textArea class="gselect_inner" name="descrizione" required="true"  value="${currentCard.descrizione}"></g:textArea><br/>

                        </div>
                        <div class="form-date-label">
                            <label class="form-scadenza">Scadenza:</label><br/>
                        </div>
                        <div class="form-date-input">
                            <g:datePicker class="gselect_inner" id="datapicker" name="scadenza" precision="day" value="${currentCard.scadenza}"></g:datePicker>
                        </div>
                        <br/>
                        <div class="form-label">
                            <label class="form-users">Aggiungi collaboratori: </label><br/>
                        </div>
                        <div class="form-input , gselect">
                            <g:select class="gselect_inner" name="utentiBoard"
                                      multiple="true"
                                      from="${currentBoard.users - currentCard.users}"
                                      optionKey="id"
                                      optionValue="username"
                            />
                        </div>

                        <div class="form-label">
                            <label class="form-users">Rimuovi collaboratori: </label><br/>
                        </div>
                        <div class="form-input , gselect">
                            <g:select class="gselect_inner" name="utentiCard"
                                      multiple="true"
                                      from="${currentCard.users}"
                                      optionKey="id"
                                      optionValue="username"
                            />
                        </div>

                        <div class="form-label">
                            <label class="form-users">Cambia colonna: </label><br/>
                        </div>
                        <div class="form-input , gselect">
                            <g:select class="gselect_inner" name="colonnaCard"
                                      noSelection="${['null' : "-Seleziona una colonna-"]}"
                                      from="${currentBoard.colonne.sort{a,b-> b.id.compareTo(a.id)} - currentColumn}"
                                      optionKey="id"
                                      optionValue="name"
                            />
                        </div>
                        <br/>
                        <g:actionSubmit class="custombutton , form-submit" value="Accetta" action="update"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>
