/**
 * Created by Anthony Tsou on 4/12/2016.
 */
// Settings Controller
angular.module('medIT.settings', [])

  .controller('SettingsCtrl', function($scope, $http) {

    $scope.push = true;
    $scope.sms = true;
    $scope.email = true;
    $scope.weather = true;
    $scope.traffic = true;

    // HTTP GET request that will retrieve values
    $scope.$on('$ionicView.beforeEnter', function(){

        //$http.get("http://127.0.0.1/{REST OF THE URL PLS}")
        //  .success(function(data) {
        //    $scope.push = data.push;
        //    $scope.sms = data.sms;
        //    $scope.email = data.email;
        //    $scope.weather = data.weather;
        //    $scope.traffic = data.traffic;
        //  })
        //  .error(function(data) {
        //    alert("You messed up");
        //  });

      // Retrieve all preferences from DB using GET request
      //alert("Settings: "
      //  + "\nPush: " + $scope.push
      //  + "\nSMS: " + $scope.sms
      //  + "\nEmail: " + $scope.email
      //  + "\nWeather: " + $scope.weather
      //  + "\nTraffic: " + $scope.traffic);
    });

    $scope.setPreference = function(preference) {
      switch(preference) {
        case "push":
          $scope.push = !$scope.push;
          break;
        case "sms":
          $scope.sms = !$scope.sms;
          break;
        case "weather":
          $scope.weather = !$scope.weather;
          break;
        case "traffic":
          $scope.traffic = !$scope.traffic;
          break;
      }
    };

    $scope.$on('$ionicView.beforeLeave', function(){
      alert("Settings: "
        + "\nPush: " + $scope.push
        + "\nSMS: " + $scope.sms
        + "\nEmail: " + $scope.email
        + "\nWeather: " + $scope.weather
        + "\nTraffic: " + $scope.traffic);

      // Save all settings in DB using POST req
    });
  });
