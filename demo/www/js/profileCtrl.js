/**
 * Created by Anthony Tsou on 4/19/2016.
 */

angular.module('medIT.profile', [])
  .controller('ProfileCtrl', function($scope, $localstorage, $ionicPopup, $spinner, $http, $state) {

    $scope.$on('$ionicView.afterEnter', function() {
      $spinner.show();

      $scope.isCheckingIn = $localstorage.getObject('isCheckingIn');
      $scope.user = $localstorage.getObject('user');

      var data = {
        MessageType: "UserPatientsQuery",
        Username: $scope.user.Username
      };

      $http({
        method: "POST",
        url: "http://localhost/",
        data: data
      }).then(function successCallback(response) {
        $spinner.hide();
        if (response.data.MessageType === "Error") {
          alert("An error has occurred. Please try again.");
        } else {
          $scope.patients = response.data.Patients;
        }
      }, function errorCallback(response) {
        alert("An error has occurred. Please try again.");
      });
    });

    $scope.checkIn = function(res) {
      $spinner.show();

      var title = "Checking In";
      var template = "";
      if (res === 0) {
        template = "Thank you for checking in! <br>" +
          "<br>Please see the front desk upon arrival in order to update your information!";
      } else {
        template = "Thank you for checking in! <br>" +
          "<br>You will be added to the waiting queue.";
      }

      var appt = $localstorage.getObject('appt');
      var data = {
        MessageType: "CheckInForAppointment",
        AppointmentID: appt.AppointmentID
      };

      $http({
        method: "POST",
        url: "http://localhost/",
        data: data
      }).then(function successCallback(response) {
        $spinner.hide();
        if (response.data.MessageType === "Error") {
          alert("An error has occurred. Please try again.");
        } else {
          $scope.result();
        }
      }, function errorCallback(response) {
        alert("An error has occurred. Please try again.");
      });
    };

    $scope.result = function(title, template) {
      var alertPopup = $ionicPopup.alert({
        title: title,
        template: template
      });

      alertPopup.then(function(res) {
        $scope.isCheckingIn = false;
        $localstorage.setObject('isCheckingIn', false);
        $state.go('app.home');
      });
    };
  });

