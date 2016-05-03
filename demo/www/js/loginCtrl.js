/**
 * Created by Anthony Tsou on 4/10/2016.
 *
 * Controller that handles login for users in database
 */
angular.module('medIT.login', [])

.controller('LoginCtrl', function($scope, $ionicPopup, $state, $timeout, $http, $localstorage) {

    $scope.$on('$ionicView.beforeEnter', function() {
      // Username and Password entered by user will be in this
      $scope.data = {};
    });

    // Logging into the application
    $scope.login = function() {

      var data = {
        MessageType: "AuthRequest",
        Username: $scope.data.username,
        Password: $scope.data.password
      };

      // HTTP Request using login credentials
      $http({
        method: "POST",
        url: "http://localhost/",
        data: data
      }).then(function successCallback(response) {
        if (response.data.MessageType === "Error") {
          alert("Server error. " + response.data.Message);
        } else {
          if(response.data.Success) {
            $localstorage.setObject('user', response.data.User);

            // Redirect to homepage
            $state.go('app.home');
          } else {
            var alertPopup = $ionicPopup.alert({
                title: 'Login failed!',
                template: 'Please check your credentials!'
            });
          }
        }
      }, function errorCallback(response) {
        alert("Unable to connect to server!");
      });
    };
});






