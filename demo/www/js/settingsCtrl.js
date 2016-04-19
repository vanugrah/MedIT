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

      $scope.appts = $localstorage.getObject('appts');

      $scope.johnnyColor = "Yellow";
      $scope.johannaColor = "Blue";

      $localstorage.setObject('johnnyColor', $scope.johnnyColor);
      $localstorage.setObject('johannaColor', $scope.johannaColor);

      $scope.colors = ["Blue", "Yellow", "Red", "Green"];
    });

    // Sets the settings when the user enters settings page
    $scope.$on('$ionicView.beforeEnter', function(){
      $scope.settings = $localstorage.getObject("settings");
      $scope.appts = $localstorage.getObject('appts');
      $scope.johnnyColor = $localstorage.getObject('johnnyColor');
      $scope.johannaColor = $localstorage.getObject('johannaColor');

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

      for (i = 0; i < $scope.appts.length; i++) {
        if($scope.appts[i].patientID === "0") {
          $scope.appts[i].color = $scope.johnnyColor;

        } else if ($scope.appts[i].patientID === "1") {
          $scope.appts[i].color = $scope.johannaColor;
        }
      }

      $localstorage.setObject('appts', $scope.appts);
      $localstorage.setObject('johnnyColor', $scope.johnnyColor);
      $localstorage.setObject('johannaColor', $scope.johannaColor);
    });

    $scope.changeJohnnyColor = function(color) {
      $scope.johnnyColor = color;
      $localstorage.setObject('johnnyColor', $scope.johnnyColor);
    };

    $scope.changeJohannaColor = function(color) {
      $scope.johannaColor = color;
      $localstorage.setObject('johannaColor', $scope.johannaColor);
    };

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
