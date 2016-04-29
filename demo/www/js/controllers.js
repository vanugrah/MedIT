angular.module('medIT.controllers', [])

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
})

.controller('AppCtrl', function($scope, $ionicModal, $timeout, $ionicLoading) {

  // Form data for the login modal
  $scope.loginData = {};

  // Perform the login action when the user submits the login form
  $scope.doLogin = function() {
    console.log('Doing login', $scope.loginData);

    // // Simulate a login delay. Remove this and replace with your login
    // $timeout(function() {
    //   $scope.closeLogin();
    //   $ionicLoading.hide();
    // }, 2000);
  };
});






