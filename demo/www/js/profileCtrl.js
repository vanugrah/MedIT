/**
 * Created by Anthony Tsou on 4/19/2016.
 *
 * Controller for the Profile page
 */

angular.module('medIT.profile', [])
  .controller('ProfileCtrl', function($scope, $localstorage, $ionicPopup, $spinner, $http, $state) {

    $scope.$on('$ionicView.afterEnter', function() {
      $spinner.show();

      // Determines if the checkin material should show
      $scope.isCheckingIn = $localstorage.getObject('isCheckingIn');

      // Need user info to get profile info
      $scope.user = $localstorage.getObject('user');

      var data = {
        MessageType: "UserPatientsQuery",
        Username: $scope.user.Username
      };

      // HTTP request that gets user information
      // and information for all patients associated with that user
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

    // Check user into appointment
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

      // HTTP request to change checkedin status of appointment in DB
      $http({
        method: "POST",
        url: "http://localhost/",
        data: data
      }).then(function successCallback(response) {
        $spinner.hide();
        if (response.data.MessageType === "Error") {
          alert("An error has occurred. Please try again.");
        } else {
          $scope.result(title, template);
        }
      }, function errorCallback(response) {
        alert("An error has occurred. Please try again.");
      });
    };

    // Result of checkin attempt
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

