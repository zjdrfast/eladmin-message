var open_prototype = XMLHttpRequest.prototype.open,
    intercept_response = function (urlpattern, callback) {
        XMLHttpRequest.prototype.open = function () {
            arguments['1'] == urlpattern && this.addEventListener('readystatechange', function (event) {
                if (this.readyState === 4) {
                    console.log('is--hook---url--', urlpattern)
                    Object.defineProperty(this, 'response', {writable: true});
                    Object.defineProperty(this, 'responseText', {writable: true});
                    console.log('is--hook---url--responseText---',  event.target.responseText)
                    console.log('is--hook---url--response---',  event.target.response)
                    var response = callback(event.target.responseText);
                    this.response = this.responseText = response;

                }
            });
            return open_prototype.apply(this, arguments);
        };
    };
intercept_response('https://www.binance.com/bapi/asset/v2/private/asset-service/wallet/balance?needBalanceDetail=true', function (response) {
    console.log('is--hook---intercept_response--response--',  response)
    // var new_response = response.replace('banana', 'apple');
    return response;
});



(function() {
    // create XMLHttpRequest proxy object
    var oldXMLHttpRequest = XMLHttpRequest;
    console.log('1111111111')
    // define constructor for my proxy object
    XMLHttpRequest = function() {
        var actual = new oldXMLHttpRequest();
        var self = this;
        console.log('22222222222',self)
        this.onreadystatechange = null;

        // this is the actual handler on the real XMLHttpRequest object
        actual.onreadystatechange = function() {
            if (this.readyState == 4) {
                // actual.responseText is the ajax result
                console.log('actual.responseText-----',actual.responseText)
                // add your own code here to read the real ajax result
                // from actual.responseText and then put whatever result you want
                // the caller to see in self.responseText
                // this next line of code is a dummy line to be replaced
                self.responseText = '{"msg": "Hello"}';
            }
            if (self.onreadystatechange) {
                return self.onreadystatechange();
            }
        };

        // add all proxy getters
        ["status", "statusText", "responseType", "response",
            "readyState", "responseXML", "upload"].forEach(function(item) {
            Object.defineProperty(self, item, {
                get: function() {return actual[item];}
            });
        });

        // add all proxy getters/setters
        ["ontimeout, timeout", "withCredentials", "onload", "onerror", "onprogress"].forEach(function(item) {
            Object.defineProperty(self, item, {
                get: function() {return actual[item];},
                set: function(val) {actual[item] = val;}
            });
        });

        // add all pure proxy pass-through methods
        ["addEventListener", "send", "open", "abort", "getAllResponseHeaders",
            "getResponseHeader", "overrideMimeType", "setRequestHeader"].forEach(function(item) {
            Object.defineProperty(self, item, {
                value: function() {return actual[item].apply(actual, arguments);}
            });
        });
    }
})();