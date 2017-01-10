/**
 * Functions to evaluate the json of AjaxTriggeredResponse.
 */

/**
 * 
 * @response jsonified AjaxTriggeredResponse as string.
 * @markup_action function called if only the markup is of interest.
 * @js_action function called if only the js is of interest. If not defined we use simpleEval if action is JS_ONLY or BOTH.
 * @both_action function called if markup and js are of interest.
 */
function evaluateATResponse(response, markup_action, js_action, both_action) {
	var jsonObj = JSON.parse(response);
	var action = jsonObj.usage;
	switch (action) {
	case "MARKUP_ONLY":
		markup_action(jsonObj.appendableMarkup);
		break;
	case "JS_ONLY":
		if(!js_action) {
			simpleEval(jsonObj.evaluatableJS)
		} else {
			js_action(jsonObj.evaluatableJS)
		}
		
		break;
	}
}

function simpleEval(js) {
	eval(js)
}