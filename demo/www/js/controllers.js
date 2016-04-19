angular.module('medIT.controllers', [])

.controller('LoginCtrl', function($scope, LoginService, $ionicPopup, $state, $timeout, $ionicLoading) {
    $scope.data = {};

    $scope.login = function() {
        LoginService.loginUser($scope.data.username, $scope.data.password).success(function(data) {
            // Redirect to homepage
            $state.go('app.home');

            // Setup the loader
            $ionicLoading.show({
              template: '<h3>Loading...</h3> <ion-spinner icon="spiral" class="spinner-positive"></ion-spinner>',
              animation: 'fade-in',
              showBackdrop: true,
              maxWidth: 200,
              duration: 2000
            });

        }).error(function(data) {
            var alertPopup = $ionicPopup.alert({
                title: 'Login failed!',
                template: 'Please check your credentials!'
            });
        });
    }

    // Simulate a login delay. Remove this and replace with your login
    $timeout(function() {
      $scope.closeLogin();
    }, 2000);
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


  



