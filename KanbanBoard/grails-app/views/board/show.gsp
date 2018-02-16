<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'board.label', default: 'Board')}" />
    <title>Board: ${board.name}</title>
</head>
<body>
<a href="#show-board" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
<div class="nav" role="navigation">
    <ul>
        <li><g:link class="home" action="dashboard">Dashboard</g:link></li>
        <li><g:link controller="Board" class="adduser" action="edit" var="${board.id}" params="[boardID: board.id]">Gestisci</g:link></li>
        <li><g:link controller="Colonna" class="create" action="create" var="${board.id}">Aggiungi colonna</g:link></li>
    </ul>

</div>
<div id="show-board" class="content scaffold-show" role="main">
    <h1 class="sub-title"> ${board.name}</h1>
</div>
<div id="show-create-c" class="content scaffold-show" role="main">
    <div class="column-container">
        <g:each in="${(board.colonne).sort{a,b-> a.id.compareTo(b.id)}}" var="column">
            <g:if test="${column.name == "Card non assegnate"}">
                <g:set var="idCNA" value="${column.id}"/>
                <div class="emptyColumn">
                        <p class="emptyColumn-title"> ${column.name}</p>



                <g:each in="${(column.cards).sort{c,d-> c.id.compareTo(d.id)}}">
                    <g:link controller="card" action="edit" id="${it.id}" params="[idCNA: column.id  , currentCol: column.id]" >
                    <div class="card-container">
                        <p class="card-titolo">${it.titolo}</p>
                        <g:form controller="card" resource="${it}"  params="[currentCard: it.id , idCNA: column.id , currentCol: column.id]" name="deleteCardByID" value="${column.id}" action="delete">
                            <input class="deleteCard" type="submit" value=" " onclick="return confirm('Sei sicuro di voler cancellare la Card ${it.titolo}? ');" />
                        </g:form>
                        <p class="card-descrizione">${it.descrizione}</p>
                        <p class="card-scadenza"><g:formatDate format="yyyy-MM-dd" date="${it.scadenza}"/></p>
                    </div>
                    </g:link>
                </g:each>

                <g:link controller="card" action="create" params="[columnId: column.id]">
                    <p class="column-addcard , emptyColumn-button"> Aggiungi Card </p>
                </g:link>
            </div>
                
            </g:if>
        </g:each>
        <g:each in="${(board.colonne).sort{a,b-> a.id.compareTo(b.id)}}" var="column">
            <g:if test="${column.name != "Card non assegnate"}">
                <div class="column">
                    <g:link controller="colonna" action="edit" resource="${column}" params="[idCNA: idCNA ]">
                        <p class="column-title"> ${column.name}</p>
                        <g:form resource="${column}" name="deleteColByID" value="${column.id}" action="delete">
                            <input class="deleteColumn" type="submit" value=" " onclick="return confirm('Sei sicuro di voler cancellare la colonna ${column.name}? Le cards al suo interno verranno salvate sulla colonna Cards non Assegnate');" />
                        </g:form>
                    </g:link>


                    <g:each in="${(column.cards).sort{c,d-> c.id.compareTo(d.id)}}">
                        <g:link controller="card" action="edit" id="${it.id}" params="[currentCard: it.id , currentCol: column.id]" >
                            <div class="card-container">
                                <p class="card-titolo">${it.titolo}</p>
                                <g:form controller="card" resource="${it}"  params="[currentCard: it.id , currentCol: column.id]" name="deleteCardByID" value="${column.id}" action="delete">
                                    <input class="deleteCard" type="submit" value=" " onclick="return confirm('Sei sicuro di voler cancellare la Card ${it.titolo}? ');" />
                                </g:form>
                                <p class="card-descrizione">${it.descrizione}</p>
                                <p class="card-scadenza"><g:formatDate format="yyyy-MM-dd" date="${it.scadenza}"/></p>
                            </div>
                        </g:link>
                    </g:each>
                    <g:link controller="card" action="create" params="[columnId: column.id]">
                        <p class="column-addcard"> Aggiungi Card </p>
                    </g:link>
                </div>
            </g:if>
        </g:each>

    </div>
</div>
</body>
</html>
