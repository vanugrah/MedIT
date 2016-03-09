/**
 * Created by Anthony Tsou on 3/9/2016.
 */
angular.module('ionic.utils', [])

  .factory('$localstorage', ['$window', function($window) {
    return {
      // Saves a string value in localstorage with specified key
      set: function(key, value) {
        $window.localStorage[key] = value;
      },
      // Returns a string value from localstorage associated with specified key
      get: function(key, defaultValue) {
        return $window.localStorage[key] || defaultValue;
      },
      // Saves an obj in localstorage with specified key
      setObject: function(key, value) {
        $window.localStorage[key] = JSON.stringify(value);
      },
      // Returns an obj from localstorage associated with specified key
      getObject: function(key) {
        return JSON.parse($window.localStorage[key] || '{}');
      }
    }
  }]);
