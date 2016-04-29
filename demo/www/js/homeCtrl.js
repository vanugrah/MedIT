// Home Controller
angular.module('medIT.home', [])

  .controller('HomeCtrl', function($scope, $localstorage, $ionicPopup, $spinner, $http, $state) {

    $scope.$on('$ionicView.loaded', function() {
      //$scope.apptReminder = false;
      //$localstorage.setObject('hasCheckedIn', false);
      //$localstorage.setObject('apptReminder', $scope.apptReminder);

      //$scope.appts = [
      //  {
      //    id: 0,
      //    location: "Children's Healthcare of Atlanta - Egleston",
      //    address: "1405 Clifton Road NE",
      //    city: "Atlanta",
      //    state: "GA",
      //    zip: "30322",
      //    date: "21 April 2016",
      //    time: "4:30pm",
      //    physician: "Dr. Batisky",
      //    patient: "Johnny Doe Jr.",
      //    patientID: "0",
      //    notes: "Follow-up appointment",
      //    isCancelled: false,
      //    color: "blue",
      //    notCheckedIn: true
      //  },
      //  {
      //    id: 1,
      //    location: "Children's Healthcare of Atlanta - Egleston",
      //    address: "1405 Clifton Road NE",
      //    city: "Atlanta",
      //    state: "GA",
      //    zip: "30322",
      //    date: "22 April 2016",
      //    time: "1:00pm",
      //    physician: "Dr. Omojokun",
      //    patient: "Johanna Doe",
      //    patientID: "1",
      //    notes: "6-month check-up",
      //    isCancelled: false,
      //    color: "green",
      //    notCheckedIn: true
      //  },
      //  {
      //    id: 2,
      //    location: "Children's Healthcare of Atlanta - Egleston",
      //    address: "1405 Clifton Road NE",
      //    city: "Atlanta",
      //    state: "GA",
      //    zip: "30322",
      //    date: "05 May 2016",
      //    time: "8:00am",
      //    physician: "Dr. Menagarishvili",
      //    patient: "Johnny Doe Jr.",
      //    patientID: "0",
      //    notes: "N/A",
      //    isCancelled: false,
      //    color: "blue",
      //    notCheckedIn: true
      //  }
      //];
      //
      //$localstorage.setObject('appts', $scope.appts)
    });

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
