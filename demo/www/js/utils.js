/**
 * Created by Anthony Tsou on 3/9/2016.
 *
 * Utility module
 * Has different functions that can be used throughout the project,
 * usually for convenience and to prevent code repetition
 */
angular.module('ionic.utils', [])

  // Factory that handles setting and getting from localstorage
  // Handles any object <-> string conversions
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
  }])

  // Factory that handles the ionicLoading spinner
  .factory('$spinner', ['$ionicLoading', function($ionicLoading) {
    return {
      show: function() {
        $ionicLoading.show({
          template: '<h2>Loading...</h2> <p> <ion-spinner icon="android" class="spinner-positive" style="height: 50px !important;"></ion-spinner>',
          animation: 'fade-in',
          noBackdrop: false,
          maxWidth: 200
        });
      },

      hide: function() {
        $ionicLoading.hide();
      }
    }
  }]);
