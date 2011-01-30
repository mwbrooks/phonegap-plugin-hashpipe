//
// An almost blind copy & paste of store.js from phonegap/phonegap-blackberry
// This will fail horribly, but at least the public functions will now exist
//
(function() {
    function Store() {
        this.save_success = null;
        this.save_error = null;
        this.loadAll_success = null;
        this.loadAll_error = null;
        this.load_success = null;
        this.load_error = null;
        this.remove_success = null;
        this.remove_error = null;
        this.nuke_success = null;
        this.nuke_error = null;
    }

    Store.prototype.getAll = function(successCallback,errorCallback) {
        // Wrap the success callback with a little object parses that decodes the keys and composes into a new JSON obj.
        // We do this because we encode all keys as they go into offline storage, so we have to decode on the way back.
        this.loadAll_success = function(obj){
            var trueObj = {}, prop;
            for (prop in obj) {
                if (obj.hasOwnProperty(prop)) {
                    trueObj[decodeURIComponent(prop)] = obj[prop];
                }
            }
            successCallback(trueObj);
        };
        this.loadAll_error = errorCallback;
        //PhoneGap.exec("store",["loadAll"]);
        PhoneGap.exec(this.loadAll_success, this.loadAll_error, 'Store', 'all', [ ]);
    };

    Store.prototype.put = function(successCallback,errorCallback,key,data) {
        this.save_success = successCallback;
        this.save_error = errorCallback;
        // PhoneGap.exec("store",["save",encodeURIComponent(key),data]);
        PhoneGap.exec(this.save_success, this.save_error, 'Store', 'save', [ ]);
    };

    Store.prototype.get = function(successCallback,errorCallback,key) {
        this.load_success = successCallback;
        this.load_error = errorCallback;
        // PhoneGap.exec("store",["load",encodeURIComponent(key)]);
        PhoneGap.exec(this.load_success, this.load_error, 'Store', 'load', [ ]);
    };

    Store.prototype.remove = function(successCallback, errorCallback, key) {
        this.remove_success = successCallback;
        this.remove_error = errorCallback;
        // PhoneGap.exec("store", ["remove",encodeURIComponent(key)]);
        PhoneGap.exec(this.remove_success, this.remove_error, 'Store', 'remove', [ ]);
    };

    Store.prototype.nuke = function(successCallback, errorCallback) {
        this.nuke_success = successCallback;
        this.nuke_error = errorCallback;
        // PhoneGap.exec("store", ["nuke"]);
        PhoneGap.exec(this.nuke_success, this.nuke_error, 'Store', 'nuke', [ ]);
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