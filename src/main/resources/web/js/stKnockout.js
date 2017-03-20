// namespace definition
var stko = {}

templateFromUrlLoader = {
	loadTemplate : function(name, templateConfig, callback) {
		if (templateConfig.fromUrl) {
			// Uses jQuery's ajax facility to load the markup from a file

			$.get(templateConfig.fromUrl, function(markupString) {
				// We need an array of DOM nodes, not a string.
				// We can use the default loader to convert to the
				// required format.
				ko.components.defaultLoader.loadTemplate(name, markupString,
						callback);
			});
		} else {
			// Unrecognized config format. Let another loader handle it.
			callback(null);
		}
	}
};

viewModelCustomLoader = {
	loadViewModel : function(name, viewModelConfig, callback) {
		if (viewModelConfig.viaLoader) {
			
			var prom = $.ajax({
				url : "/data/TODO"
			});
			prom.done(function(data) {
				ko.components.defaultLoader.loadViewModel(name,
						function(params){return JSON.parse(data)}, callback);
			});
			
		} else {
			// Unrecognized config format. Let another loader handle it.
			callback(null);
		}
	}
};

// Register it
ko.components.loaders.unshift(templateFromUrlLoader);
ko.components.loaders.unshift(viewModelCustomLoader);

registerModalTemplate = function() {
	ko.components.register('fade', {
		viewModel : function(params) {
			this.flowId = "1234";
			this.esDocumentId = "lkldsf";
		},
		template : {
			fromUrl : '/modal/editCreateNoteKO'
		}
	});
};

registerKoComponent = function() {
	ko.components.register('flow', {
		viewModel : {
			viaLoader : function(params) {
				var res;
//				var prom = $.ajax({
//					url : "/data/TODO"
//				});
//				prom.done(function(data) {
//					res = JSON.parse(data);
//				});
				return "";
			}
		},
		template : {
			fromUrl : '/knockout/flowOverview.html'
		}
	});
};

KoViewModel = function KoViewModel() {
	this.modals = ko.observableArray();
	this.simpleFlowOverview = ko.observable(false);
}

KoViewModel.prototype.addModal = function() {
	this.modals.push("TODO")
}

var viewModel = new KoViewModel();

init = function() {
	registerKoComponent();
	registerModalTemplate();
	ko.applyBindings(viewModel);
};