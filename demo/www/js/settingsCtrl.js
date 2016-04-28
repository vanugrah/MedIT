/**
 * Created by Anthony Tsou on 4/12/2016.
 */
// Settings Controller
angular.module('medIT.settings', [])

  .controller('SettingsCtrl', function($scope, $http, $localstorage) {

    $scope.$on('$ionicView.loaded', function() {

      $scope.settings = {
        push: false,
        sms: false,
        email: false
      };

      $scope.patients = [ ];

      $scope.colors = ["blue", "yellow", "red", "green"];
    });

    $scope.getSettingsFromServer = function() {
      var data = {
        MessageType: "UserSettingsQuery",
        Username: 'atsou3'
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
    }
    
    $scope.getPatientsFromServer = function() {
      var data = {
        MessageType: "UserPatientsQuery",
        Username: 'atsou3'
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
    $scope.$on('$ionicView.beforeEnter', function(){
      $scope.getSettingsFromServer();
      
      $scope.getPatientsFromServer();

    });

    $scope.changeSettings = function() {
      var data = {
        MessageType: "UserSettingsChange",
        Username: 'atsou3',
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
    };

    $scope.changeJohnnyColor = function(color) {
      $scope.johnnyColor = color;
      $localstorage.setObject('johnnyColor', $scope.johnnyColor);
    };

    $scope.changeJohannaColor = function(color) {
      $scope.johannaColor = color;
      $localstorage.setObject('johannaColor', $scope.johannaColor);
    };
  });
