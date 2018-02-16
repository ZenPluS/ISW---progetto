<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>
        <g:layoutTitle default="Grails"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    <asset:stylesheet src="application.css"/>

    <g:layoutHead/>
</head>
<body>
    <sec:ifNotLoggedIn>
        <div class="header">
            <div class="login-bar">
                <a href="/login" id="login" class="dropdown-toggle, custombutton" role="button" aria-haspopup="true" aria-expanded="false">Login</a>
                <a href="/Utenti/create" id="registra" class="dropdown-toggle, custombutton" role="button" aria-haspopup="true" aria-expanded="false">Registra</a>
            </div>
        </div>
    </sec:ifNotLoggedIn>
    <sec:ifLoggedIn>
        <div class="header">
            <div class="login-bar">
                <a href="/board/dashboard/<sec:loggedInUserInfo field="id"/>" id="loggedIn" class="dropdown-toggle, custombutton , account-button" role="button" aria-haspopup="true" aria-expanded="false"><sec:loggedInUserInfo field="username" /></a>
                <a href="/logout" class="dropdown-toggle, custombutton , logout-button" id="logout" role="button" aria-haspopup="true" aria-expanded="false">Logout</a>
            </div>
        </div>
    </sec:ifLoggedIn>
    <g:layoutBody/>
    <div id="spinner" class="spinner" style="display:none;">
        <g:message code="spinner.alt" default="Loading&hellip;"/>
    </div>
    <asset:javascript src="application.js"/>
</body>
</html>
