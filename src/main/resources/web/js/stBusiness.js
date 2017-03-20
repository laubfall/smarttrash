/**
 * Contains methods to reach business endpoints that renders some parts of the application.
 * Also contains methods that provides business functionality.
 */

/**
 * 
 */
function createFlowOverview() {
	viewModel.simpleFlowOverview(true);

	// doRequestToEndpoint("createFlowOverview",function(markup){
	// $("div#flowOverview").empty().append($(markup));
	// })
}

function editCreateFlowAndUpdateView() {
	createFlowOverview();
	closeModal();
}

function showEditAndCreateNoteView(json) {
	modal('editCreateNoteKO', json);
}

function editCreateNoteAndUpdateView() {
	createLatestNotesView();
	closeModal();
}

/**
 * 
 */
function createLatestNotesView() {
	doRequestToEndpoint("createLatestNotesView", function(markup) {
		$("div#latestNotes").empty().append($(markup));
	});
}

/**
 * Technical function.
 * 
 */
function doRequestToEndpoint(endpoint, markupAction, jsAction) {
	$.get(endpoint, function(response) {
		evaluateATResponse(response, markupAction, jsAction)
	}, 'html');
}

$(document).ready(function() {
	init();
	createFlowOverview();
	
	// createLatestNotesView();
})