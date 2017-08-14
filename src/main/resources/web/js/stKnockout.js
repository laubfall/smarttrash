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
		if(viewModelConfig.createViewModel) {
			callback(viewModelConfig.createViewModel)
		} else if (viewModelConfig.viaLoader) {
			
			var prom = $.ajax({
				url : "/data/" + viewModelConfig.viewModelName + "/" + viewModelConfig.param
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


// Custom Bindings
ko.bindingHandlers.modal = {
	    init: function (element, valueAccessor) {
	        $(element).modal({
	            show: false
	        });
	        
	        var value = valueAccessor();
	        if (typeof value === 'function') {
	            $(element).on('hide.bs.modal', function() {
	               value(false);
	            });
	        }
	    },
	    update: function (element, valueAccessor) {
	        var value = valueAccessor();
	        if (ko.utils.unwrapObservable(value)) {
	            $(element).modal('show');
	        } else {
	            $(element).modal('hide');
	        }
	    }
	}

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
		viewModel : {
			createViewModel : function(params, componentInfo) {
				var docId = params.value().documentId;
				var prom = $.ajax({
					url : "/data/editCreateFlow/" + docId
				});
				
				var dataResult;
				prom.done(function(data) {
					dataResult = data;
				});
				
				return dataResult;
//				return {
//				param : params.flowId
//				viewModelName : "editCreateFlow"
//				}
			}
		},
		template : {
			fromUrl : '/modal/editCreateFlowKO'
		}
	});
};

registerKoComponent = function() {
	ko.components.register('flow', {
		viewModel : {
			viaLoader : true,
			viewModelName : "flowOverview",
			param : "unused"
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
	this.flowToEdit = ko.observable("");
	
	this.showEditCreateFlowModal = function(data) {
		self.editCreateFlow(true);
		self.flowToEdit(data);
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