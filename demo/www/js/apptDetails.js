/**
 * Created by Anthony Tsou on 4/18/2016.
 */
// Home Controller
angular.module('medIT.apptDetails', [])

  .controller('ApptDetailsCtrl', function($scope, $localstorage, $ionicPopup, $state, $http, $spinner) {

    $scope.$on('$ionicView.loaded', function() {
      $scope.hasCheckedIn = false;
      $localstorage.setObject('hasCheckedIn', false);
      $localstorage.setObject('firstAlert', true);
    });

    $scope.$on('$ionicView.beforeEnter', function() {
      $scope.appt = $localstorage.getObject('appt');
      $scope.hasCheckedIn = $localstorage.getObject('hasCheckedIn');
      //$scope.appt = [];
      $scope.apptID = $localstorage.getObject('apptID');
      $scope.appts = $localstorage.getObject('appts');
      //$scope.appt[0] = $scope.appts[$scope.apptID];
    });

    // Try to cancel appointment
    $scope.cancelAppt = function() {
      var confirmPopup = $ionicPopup.confirm({
        title: '<strong>Cancel Appointment</strong>',
        template: 'Are you sure you want to cancel this appointment?',
        cancelText: 'No',
        okText: 'Yes'
      });

      confirmPopup.then(function(res) {
        if(res) {
          $spinner.show();

          var data = {
            MessageType: "CancelAppointment",
            AppointmentID: $scope.appt.AppointmentID
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
              $scope.cancelResult();
            }
          }, function errorCallback(response) {
            alert("An error has occurred. Please try again.");
          });
        }
      });
    };

    $scope.cancelResult = function() {
      var alertPopup = $ionicPopup.alert({
        title: '<strong>Cancel Appointment</strong>',
        template: 'Appointment has been cancelled successfully!'
      });

      alertPopup.then(function(res) {
        $state.go('app.home');
      });
    };

    // Attempt to check into the appointment
    $scope.checkinAppt = function() {
      // Check if appt has been checked in already
      if ($scope.appt.CheckedIn === false) {
        // Current Date + Time
        var now = new Date();
        now.setHours(now.getHours() + 1);

        // Check if within 1 hour of appointment
        if (now < $scope.appt.Date) {
        //if (true) { // TODO: Implement the check for within 1 hr of appt
          // Can check in for appointment.
          $localstorage.setObject('isCheckingIn', true);
          $state.go('app.profile');
        } else {
          // Cannot check in for appointment.
          $ionicPopup.alert({
            title: 'Check-in Error',
            template: 'You may only check in an hour before your appointment.'
          });
        }
      } else {
        // Already checked in
        $ionicPopup.alert({
          title: '<strong>Check In</strong>',
          template: 'You have already checked into this appointment.'
        });
      }
    };
  });
