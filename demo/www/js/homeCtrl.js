// Home Controller
angular.module('medIT.home', [])

  .controller('HomeCtrl', function($scope, $localstorage, $ionicPopup, $ionicLoading, $http) {

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

    $scope.$on('$ionicView.beforeEnter', function() {
      // Setup the loader
      $ionicLoading.show({
        template: '<h2>Loading...</h2> <p> <ion-spinner icon="android" class="spinner-positive" style="height: 50px !important;"></ion-spinner>',
        animation: 'fade-in',
        noBackdrop: false,
        maxWidth: 200
      });
      $scope.getAppts();
    });

    $scope.getAppts = function() {

      var data = {
        MessageType: "AppointmentsQuery",
        Username: 'atsou3'
      };

      $http({
        method: "POST",
        url: "http://localhost/",
        data: data
      }).then(function successCallback(response) {
        $ionicLoading.hide();
        if (response.data.MessageType === "Error") {
          alert("Error");
        } else {
          $scope.appts = response.data.Appointments;
        }
      }, function errorCallback(response) {
        alert("You messed up");
      });

    };
 
  });
