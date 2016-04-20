angular.module('medIT.controllers', [])

.controller('LoginCtrl', function($scope, LoginService, $ionicPopup, $state, $timeout, $ionicLoading) {
    
    $scope.$on('$ionicView.beforeEnter', function() {
      $scope.data = {};
    });

    $scope.login = function() {
        LoginService.loginUser($scope.data.username, $scope.data.password).success(function(data) {
            // Redirect to homepage
            $state.go('app.home');

            // Setup the loader
            $ionicLoading.show({
              template: '<h2>Loading...</h2> <p> <ion-spinner icon="android" class="spinner-positive" style="height: 50px !important;"></ion-spinner>',
              animation: 'fade-in',
              noBackdrop: false,
              maxWidth: 200,
              duration: 2000
            });

        }).error(function(data) {
            var alertPopup = $ionicPopup.alert({
                title: 'Login failed!',
                template: 'Please check your credentials!'
            });
        });
    };

    // Simulate a login delay. Remove this and replace with your login
    //$timeout(function() {
    //  $scope.closeLogin();
    //}, 2000);
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






