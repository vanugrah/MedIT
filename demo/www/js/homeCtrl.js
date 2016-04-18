// Home Controller
angular.module('medIT.home', [])

  .controller('HomeCtrl', function($scope, $localstorage) {

    $scope.appts = [
      {
        id: 1,
        location: "Children's Healthcare of Atlanta",
        date: "21 April 2016",
        time: "11:00am",
        physician: "Dr. Batisky",
        patient: "Johnny Doe Jr."
      },
      {
        id: 2,
        location: "Children's Healthcare of Atlanta",
        date: "28 April 2016",
        time: "1:00pm",
        physician: "Dr. Omojokun",
        patient: "Johanna Doe"
      },
      {
        id: 3,
        location: "Children's Healthcare of Atlanta",
        date: "05 May 2016",
        time: "8:00am",
        physician: "Dr. Menagarishvili",
        patient: "Johnny Doe Jr."
      }
    ];

    $scope.viewAppt = function(appt) {
      $localstorage.setObject('appt', appt);
    };
  });
