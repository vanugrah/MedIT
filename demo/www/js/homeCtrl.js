/**
 * Created by Anthony Tsou on 4/10/2016.
 *
 * Controller for the home screen that lists the user's upcoming appointments
 */
angular.module('medIT.home', [])

  .controller('HomeCtrl', function($scope, $localstorage, $ionicPopup, $spinner, $http, $state) {

    $scope.$on('$ionicView.afterEnter', function() {
      $spinner.show();

      // Get logged-in user information
      $scope.user = $localstorage.getObject('user');

      $scope.getAppts();
    });

    // Get list of upcoming appointments
    $scope.getAppts = function() {
      var data = {
        MessageType: "AppointmentsQuery",
        Username: $scope.user.Username
      };

      // HTTP Request that returns list of appointments and their information
      $http({
        method: "POST",
        url: "http://localhost/",
        data: data
      }).then(function successCallback(response) {
        $spinner.hide();
        if (response.data.MessageType === "Error") {
          alert("An error has occurred. Please try again.");
        } else {
          $scope.appts = response.data.Appointments;
        }
      }, function errorCallback(response) {
        alert("An error has occurred. Please try again.");
      });
    };

    // View details about specific appointment
    $scope.viewApptDetails = function(appt) {
      $localstorage.setObject('appt', appt);
      $state.go('app.apptDetails');
    };


  });
