/**
 * Asks the server for the content of a modal. The wanted modal is defined by the given parameter.
 * @param name The name of the Thymeleaf template that contains the modals content.
 * @returns nothing.
 */
function modal(name) {
	$.get('/modal/' + name, function(data) {
		$('#modal').empty().append(data)
		$('#stModal').modal('show');
	})
}