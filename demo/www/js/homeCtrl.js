// Home Controller
angular.module('medIT.home', [])

  .controller('HomeCtrl', function($scope, $localstorage) {

    $scope.$on('$ionicView.loaded', function() {
      $scope.appts = [
        {
          id: 1,
          location: "Children's Healthcare of Atlanta - Egleston",
          address: "1405 Clifton Road NE",
          city: "Atlanta",
          state: "GA",
          zip: "30322",
          date: "21 April 2016",
          time: "11:00am",
          physician: "Dr. Batisky",
          patient: "Johnny Doe Jr.",
          patientID: "0",
          notes: "Follow-up appointment"
        },
        {
          id: 2,
          location: "Children's Healthcare of Atlanta - Egleston",
          address: "1405 Clifton Road NE",
          city: "Atlanta",
          state: "GA",
          zip: "30322",
          date: "28 April 2016",
          time: "1:00pm",
          physician: "Dr. Omojokun",
          patient: "Johanna Doe",
          patientID: "1",
          notes: "6-month check-up"
        },
        {
          id: 3,
          location: "Children's Healthcare of Atlanta - Egleston",
          address: "1405 Clifton Road NE",
          city: "Atlanta",
          state: "GA",
          zip: "30322",
          date: "05 May 2016",
          time: "8:00am",
          physician: "Dr. Menagarishvili",
          patient: "Johnny Doe Jr.",
          patientID: "0",
          notes: "N/A"
        }
      ];

      $localstorage.setObject('appts', $scope.appts)
    });

    $scope.$on('$ionicView.beforeEnter', function() {
      $scope.appts = $localstorage.getObject('appts');
    });

    $scope.viewAppt = function(appt) {
      $localstorage.setObject('appt', appt);
    };
  });
