/**
 * Created by Anthony Tsou on 4/18/2016.
 */
// Home Controller
angular.module('medIT.apptDetails', [])

  .controller('ApptDetailsCtrl', function($scope, $localstorage, $ionicPopup, $state) {

    $scope.$on('$ionicView.loaded', function() {
      $scope.hasCheckedIn = false;
      $localstorage.setObject('hasCheckedIn', false);
      $localstorage.setObject('firstAlert', true);
    });

    $scope.$on('$ionicView.beforeEnter', function() {
      $scope.hasCheckedIn = $localstorage.getObject('hasCheckedIn');
      $scope.appt = [];
      $scope.apptID = $localstorage.getObject('apptID');
      $scope.appts = $localstorage.getObject('appts');
      $scope.appt[0] = $scope.appts[$scope.apptID];

      //if($scope.appt[0].id === 1 && $localstorage.getObject('firstAlert')) {
      //  setTimeout(function() {
      //    var traffictTitle = "<strong>Traffic Alert</strong>";
      //    var trafficTemplate = 'There has been an accident on I-85 Southbound, ' +
      //      'causing delays of up to: ' +
      //      '<br><strong><center>15 minutes</center></strong> <br>' +
      //      'To reach your appointment on time, you should leave by: ' +
      //      '<br> <strong><center>3:30pm</center></strong>';
      //
      //    var weatherTitle = "<strong>Severe Weather Alert</strong>";
      //    var weatherTemplate = 'There is heavy rain in Atlanta, ' +
      //      'causing delays of up to: ' +
      //      '<br><strong><center>20 minutes</center></strong> <br>' +
      //      'To reach your appointment on time, you should leave by: ' +
      //      '<br> <strong><center>3:25pm</center></strong>';
      //
      //    $scope.notification(traffictTitle, trafficTemplate);
      //  }, 6000);
      //}
    });

    $scope.cancelAppt = function() {
      var confirmPopup = $ionicPopup.confirm({
        title: '<strong>Cancel Appointment</strong>',
        template: 'Are you sure you want to cancel this appointment?',
        cancelText: 'No',
        okText: 'Yes'
      });

      confirmPopup.then(function(res) {
        if(res) {
          $scope.appt[0].isCancelled = true;
          $scope.appts[$scope.apptID].isCancelled = true;
          $localstorage.setObject('appts', $scope.appts);

          $scope.cancelResult();
        }
      });
    };

    // An alert dialog
    $scope.cancelResult = function() {
      var alertPopup = $ionicPopup.alert({
        title: '<strong>Cancel Appointment</strong>',
        template: 'Appointment has been cancelled successfully!'
      });

      alertPopup.then(function(res) {
        $localstorage.setObject('apptReminder', true);
        $state.go('app.home');
      });
    };
    //
    //// Traffic Notification
    //$scope.notification = function(title, template) {
    //  var update = $ionicPopup.alert({
    //    title: title,
    //    template: template
    //  });
    //
    //  update.then(function(res) {
    //    $localstorage.setObject('firstAlert', false);
    //  });
    //};

    $scope.checkinAppt = function(res) {
      if (res === 0) {
        if ($scope.appt[0].id == 0) {
          // Can check in for appointment.
          $localstorage.setObject('isCheckingIn', true);
          console.log($localstorage.getObject('isCheckingIn'));
          $state.go('app.profile');

        } else {
          // Cannot check in for appointment.
          var alertPopup = $ionicPopup.alert({
            title: 'Check-in Error',
            template: 'You may only check in an hour before your appointment.'
          });
        }
      } else {
        var alertPopup = $ionicPopup.alert({
          title: '<strong>Check In</strong>',
          template: 'You have already checked into this appointment.'
        });

        alertPopup.then(function(res) {

        });
      }
    };
  });
