/**
 * Asks the server for the content of a modal. The wanted modal is defined by
 * the given parameter.
 * 
 * @param name
 *            The name of the Thymeleaf template that contains the modals
 *            content.
 * @returns nothing.
 */
function modal(name) {
	$.get('/modal/' + name, function(data) {
		$('#modal').empty().append(data)
		$('#stModal').modal('show');
		
		$("#stModalForm").submit(function(event) {
			// TODO generischen Mechanismus für AJAX Posts etablieren (JSon als Response).
			 $.post($(this).attr('action'), $(this).serialize(), function(response){
				 // change the "replaceable" part. This is done because of components with js appended inside the modal (e.g. the submit button).
				 evaluateATResponse(response, function(appendableMarkup) {
					 $('#replaceable').empty().append($(appendableMarkup).find('#replaceable'))
				 })
		      },'html');
		      return false;
		});
	})
}

function closeModal() {
	$('#stModal').modal('hide');
}