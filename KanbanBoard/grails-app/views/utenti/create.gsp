<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'utenti.label', default: 'Utenti')}" />
        <title>Registrazione</title>
    </head>
    <body>
    <div class="navH" role="navigation">
        Crea Account
    </div>
        <div id="create-utenti" class="content scaffold-create" role="main">
            <div class="form-section-registration">
            <div class="form">
            <g:form resource="${this.utenti}" method="POST">
                <g:if  test="${flash.message}">
                    <p class="inputError">${flash.message}</p>
                </g:if>
                    <f:field bean="utenti" property="username"/>
                    <f:field  bean="utenti" property="password"/>
                    <g:submitButton name="create" id="utenteCreate" class="registra-button , custombutton" value="Registra" />
            </g:form>
            </div>
            </div>
        </div>
    </body>
</html>
