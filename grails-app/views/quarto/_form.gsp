<%@ page import="assyncinsert.Quarto" %>



<div class="fieldcontain ${hasErrors(bean: quartoInstance, field: 'nome', 'error')} ">
	<label for="nome">
		<g:message code="quarto.nome.label" default="Nome" />
		
	</label>
	<g:textField name="nome" value="${quartoInstance?.nome}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: quartoInstance, field: 'unidades', 'error')} ">
	<label for="unidades">
		<g:message code="quarto.unidades.label" default="Unidades" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${quartoInstance?.unidades?}" var="u">
    <li><g:link controller="unidade" action="show" id="${u.id}">${u?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="unidade" action="create" params="['quarto.id': quartoInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'unidade.label', default: 'Unidade')])}</g:link>
</li>
</ul>

</div>

