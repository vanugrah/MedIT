/**
 * Created by Anthony Tsou on 4/12/2016.
 */
// Settings Controller
angular.module('medIT.settings', [])

  .controller('SettingsCtrl', function($scope, $http, $localstorage) {

    $scope.$on('$ionicView.loaded', function() {
      $scope.settings = {
        push: true,
        sms: true,
        email: true,
        weather: true,
        traffic: true
      };
      $localstorage.setObject("settings", $scope.settings);
    });

    // Sets the settings when the user enters settings page
    $scope.$on('$ionicView.beforeEnter', function(){
      $scope.settings = $localstorage.getObject("settings");
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

    });

    $scope.$on('$ionicView.beforeLeave', function(){
      $localstorage.setObject("settings", $scope.settings);
      //console.log($scope.settings.push);
      //console.log($scope.settings.sms);
      //console.log($scope.settings.email);
      //console.log($scope.settings.weather);
      //console.log($scope.settings.traffic);
    });

    //$scope.getWeather = function() {
    //  $http.get("http://api.wunderground.com/api/e6fd20d04846f5be/hourly/q/GA/Atlanta.json")
    //    .success(function(data) {
    //      alert(JSON.stringify(data));
    //    })
    //    .error(function(data) {
    //      alert("You messed up");
    //    });
    //};
  });
