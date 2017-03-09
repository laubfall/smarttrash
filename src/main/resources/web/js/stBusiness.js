/**
 * Contains methods to reach business endpoints that renders some parts of the application.
 * Also contains methods that provides business functionality.
 */

/**
 * 
 */
function createFlowOverview() {
	doRequestToEndpoint("createFlowOverview",function(markup){
		$("div#flowOverview").empty().append($(markup));
	})
}

function editCreateFlowAndUpdateView() {
	createFlowOverview();
	closeModal();
}

/**
 * 
 */
function createLatestNotesView() {
	
}

/**
 * Technical function.
 * 
 */
function doRequestToEndpoint(endpoint, markupAction,jsAction){
	 $.get(endpoint, function(response){
		 evaluateATResponse(response, markupAction,jsAction)
      },'html');
}