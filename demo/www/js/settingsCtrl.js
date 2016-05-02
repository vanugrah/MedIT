/**
 * Created by Anthony Tsou on 4/12/2016.
 */
// Settings Controller
angular.module('medIT.settings', [])

  .controller('SettingsCtrl', function($scope, $http, $localstorage, $spinner, $ionicPopup) {

    $scope.$on('$ionicView.loaded', function() {
      $scope.patients = [];
      $scope.settings = [];
      $scope.colors = ["blue", "yellow", "red", "green"];
    });

    $scope.getSettingsFromServer = function() {

      var data = {
        MessageType: "UserSettingsQuery",
        Username: $scope.user.Username
      };

      $http({
        method: "POST",
        url: "http://localhost/",
        data: data
      }).then(function successCallback(response) {
        if (response.data.MessageType === "Error") {
          alert("Server error. " + response.data.Message);
        } else {
          $scope.settings.push = response.data.GetsPush;
          $scope.settings.sms = response.data.GetsSMS;
          $scope.settings.email = response.data.GetsEmail;
        }
      }, function errorCallback(response) {
        alert("Unable to connect to server!");
      });
    };

    $scope.getPatientsFromServer = function() {
      var data = {
        MessageType: "UserPatientsQuery",
        Username: $scope.user.Username
      };

      $http({
        method: "POST",
        url: "http://localhost/",
        data: data
      }).then(function successCallback(response) {
        if (response.data.MessageType === "Error") {
          alert("Server error. " + response.data.Message);
        } else {
          $scope.patients = [];
          for (var i = 0; i < response.data.Patients.length; i++) {
            patient = {
              pid: response.data.Patients[i].PatientID,
              name: (response.data.Patients[i].FirstName + " " + response.data.Patients[i].LastName),
              color: response.data.Patients[i].Color
            };
            $scope.patients.push(patient);
          }
        }
      }, function errorCallback(response) {
        alert("Unable to connect to server!");
      });
    }

    // Sets the settings when the user enters settings page
    $scope.$on('$ionicView.afterEnter', function(){
      $spinner.show();
      $scope.user = $localstorage.getObject('user');
      $scope.getSettingsFromServer();
      $scope.getPatientsFromServer();
      $spinner.hide();
    });

    $scope.changeSettings = function() {
      $spinner.show();
      var data = {
        MessageType: "UserSettingsChange",
        Username: $scope.user.Username,
        GetsPush: $scope.settings.push,
        GetsSMS: $scope.settings.sms,
        GetsEmail: $scope.settings.email
      };

      $http({
        method: "POST",
        url: "http://localhost/",
        data: data
      }).then(function successCallback(response) {
        if (response.data.MessageType === "Error") {
          alert("Server error. " + response.data.Message);
        }
      }, function errorCallback(response) {
        alert("Unable to connect to server!");
      });

      for(var i = 0; i < $scope.patients.length; i++) {
        data = {
          MessageType: "SaveColorForPatient",
          PatientID: $scope.patients[i].pid,
          Color: $scope.patients[i].color
        };

        $http({
          method: "POST",
          url: "http://localhost/",
          data: data
        }).then(function successCallback(response) {
          if (response.data.MessageType === "Error") {
            alert("Server error. " + response.data.Message);
          }
        }, function errorCallback(response) {
          alert("Unable to connect to server!");
        });
      }
      $spinner.hide();
      $scope.saveResult();
    };

    $scope.saveResult = function() {
      $ionicPopup.alert({
        title: 'Settings',
        template: 'Your settings have been saved!'
      });
    };
  });
