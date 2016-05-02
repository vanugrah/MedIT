// Home Controller
angular.module('medIT.home', [])

  .controller('HomeCtrl', function($scope, $localstorage, $ionicPopup, $spinner, $http, $state) {
    $scope.$on('$ionicView.afterEnter', function() {
      // Setup the loader
      $spinner.show();
      $scope.user = $localstorage.getObject('user');
      $scope.getAppts();
    });

    $scope.getAppts = function() {
      var data = {
        MessageType: "AppointmentsQuery",
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
          $scope.appts = response.data.Appointments;
        }
      }, function errorCallback(response) {
        alert("An error has occurred. Please try again.");
      });
    };

    $scope.viewApptDetails = function(appt) {
      $localstorage.setObject('appt', appt);
      $state.go('app.apptDetails');
    };


  });
