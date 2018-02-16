<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'colonna.label', default: 'Colonna')}" />
        <title>Modifica Colonna</title>
    </head>
    <body>
        <a href="#edit-colonna" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/board/dashboard')}">Dashboard</a></li>
                <li><a class="back" href="/board/show/${currentBoard.id}">Torna a: ${currentBoard.name}</a></li>
                <li><g:link class="custombutton , deleteButtonBar" action="delete" onclick="return confirm('Sei sicuro di voler cancellare la colonna ${currentColumn.name}? Le cards al suo interno verranno salvate sulla colonna Cards non Assegnate');">Elimina Colonna</g:link> </ul>

        </div>
        <div id="edit-colonna" class="content scaffold-edit" role="main">
            <h1 class="sub-title">Modifica Colonna</h1>

            <div class="form-section">
                <div class="form">

                    <g:form controller="colonna"  action="update">
                        <g:if  test="${flash.message}">
                            <p class="inputError">${flash.message}</p>
                        </g:if>
                        <g:hiddenField name="idColonna" value="${currentColumn.id}" />
                        <div class="form-label">
                            <label for="nomeColonna" class="form-titolo">Nome: </label><span class="required-indicator">* </span>
                        </div>
                        <div class="form-input , form-input-editBoard">
                        <g:textField class="gselect_inner" name="nomeColonna" value="${currentColumn.name}" required="true"/><br/>
                        </div>
                        <div class="form-label">
                            <label class="form-users">Elimina Card: </label>
                        </div>
                        <div class="form-input , gselect">
                        <g:select class="gselect_inner"
                                  name="currentCards"
                                  from="${currentColumn.cards}"
                                  optionKey="id"
                                  optionValue="titolo"
                                  multiple="true"
                        />
                        </div>
                        <g:if test="${colonnaCNA != null}">
                            <g:hiddenField name="idCNA" value="${colonnaCNA.id}" />
                        <div class="form-label">
                        <label class="form-users">Aggiungi Card:  </label>
                    </div>
                        <div class="form-input , gselect">
                        <g:select class="gselect_inner"
                                  name="CNACards"
                                  from="${colonnaCNA.cards}"
                                  optionKey="id"
                                  optionValue="titolo"
                                  multiple="true"
                        />
                        </div>
                        </g:if>
                        <br/>
                        <g:actionSubmit class="custombutton , form-submit" value="Accetta" action="update"/>
                    </g:form>
                </div>
            </div>

        </div>
    </body>
</html>
