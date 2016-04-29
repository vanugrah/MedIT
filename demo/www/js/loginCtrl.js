angular.module('medIT.login', [])

.controller('LoginCtrl', function($scope, LoginService, $ionicPopup, $state, $timeout, $http, $localstorage) {

    $scope.$on('$ionicView.beforeEnter', function() {
      $scope.data = {};
    });

    $scope.login = function() {

      var data = {
        MessageType: "AuthRequest",
        Username: $scope.data.username,
        Password: $scope.data.password
      };

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






