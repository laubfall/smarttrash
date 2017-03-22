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
	ko.components.register('modal', {
		viewModel : function(params) {
			this.flowId = "1234";
			this.esDocumentId = "lkldsf";
		},
		template : {
			fromUrl : '/modal/editCreateNoteKO'
		}
	});
};

registerEditFlowTemplate = function() {
	ko.components.register('editcreateflowmodal', {
		viewModel : function(params) {
			this.esDocumentId = params.flowId;
		},
		template : {
			fromUrl : '/modal/editCreateFlow'
		}
	});
};

registerKoComponent = function() {
	ko.components.register('flow', {
		viewModel : {
			viaLoader : function(params) {
				return "";
			}
		},
		template : {
			fromUrl : '/knockout/flowOverview.html'
		}
	});
};

KoViewModel = function KoViewModel() {
	var self = this;
	this.modals = ko.observableArray();
	this.editCreateFlow = ko.observable(false);
	this.simpleFlowOverview = ko.observable(false);
	this.flowId = ko.observable("");
	
	this.showEditCreateFlowModal = function(data) {
		self.editCreateFlow(true);
		self.flowId=data.documentId;
		modalKO();
	}
	
	this.addModal = function() {
		this.modals.push("TODO")
	}
}

var viewModel = new KoViewModel();

init = function() {
	registerKoComponent();
	registerModalTemplate();
	registerEditFlowTemplate();
	ko.applyBindings(viewModel);
};