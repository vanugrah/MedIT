/**
 * Created by Anthony Tsou on 4/18/2016.
 */
// Home Controller
angular.module('medIT.apptDetails', [])

  .controller('ApptDetailsCtrl', function($scope, $localstorage, $ionicPopup, $state) {

    $scope.$on('$ionicView.beforeEnter', function() {
      $scope.appt = [];
      $scope.apptID = $localstorage.getObject('apptID');
      $scope.appts = $localstorage.getObject('appts');
      $scope.appt[0] = $scope.appts[$scope.apptID];

      console.log($scope.appt);
    });

    $scope.cancelAppt = function() {
      var confirmPopup = $ionicPopup.confirm({
        title: 'Cancel Appointment',
        template: 'Are you sure you want to cancel this appointment?'
      });

      confirmPopup.then(function(res) {
        if(res) {
          $scope.appt[0].isCancelled = true;
          $scope.appts[$scope.apptID].isCancelled = true;
          $localstorage.setObject('appts', $scope.appts);
          //console.log(JSON.stringify($scope.appts));

          $scope.cancelResult();
        } else {
          console.log('You are not sure');
        }
      });
    };

    // An alert dialog
    $scope.cancelResult = function() {
      var alertPopup = $ionicPopup.alert({
        title: 'Cancel Appointment',
        template: 'Appointment has been cancelled successfully!'
      });

      alertPopup.then(function(res) {
        $state.go('app.home');
      });
    };

  });
