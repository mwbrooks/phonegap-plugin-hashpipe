(function() {
    var Store = function() {
        // In order to 'just work' with the Lawnchair BlackBerryPersistentStore adaptor,
        // the public API must be 'get', 'getAll', 'put', 'remove', 'nuke'
        return {
            get: function(successCallback, errorCallback, key) {
                var successWrapper = function(value) {
                    successCallback( (value === 'null') ? null : value );
                };
                
                PhoneGap.exec(successWrapper, errorCallback, 'Store', 'get', [key]);
            },
            
            getAll: function(successCallback, errorCallback) {
                PhoneGap.exec(successCallback, errorCallback, 'Store', 'all', []);
            },
            
            put: function(successCallback, errorCallback, key, value) {
                PhoneGap.exec(successCallback, errorCallback, 'Store', 'put', [key, value]);
            },
            
            remove: function(successCallback, errorCallback, key) {
                PhoneGap.exec(successCallback, errorCallback, 'Store', 'remove', [key]);
            },
            
            nuke: function(successCallback, errorCallback) {
                PhoneGap.exec(successCallback, errorCallback, 'Store', 'nuke', []);
            }
        };
    };
    
    PhoneGap.addConstructor(function() {
        // add plugin to window.plugins
        PhoneGap.addPlugin('store', new Store());
        
        // register plugin on native side
        phonegap.PluginManager.addPlugin('Store', 'com.phonegap.plugins.Store');
        
        // Stub the persistent storage adaptor
        if (!window.navigator) window.navigator = {};
        window.navigator.store = window.plugins.store;
    });
})();