// Home Controller
angular.module('medIT.home', [])

  .controller('HomeCtrl', function($scope, $localstorage) {

    $scope.appts = [
      { title: 'Appointment 1', 
        id: 1,
        location: "Children's Healthcare of Atlanta",
        date: "15th April 2016",
        physician: "Dr. Batisky"
      },
      
    ];

    $scope.viewAppt = function(appt) {
      $localstorage.setObject('appt', appt);
      console.log($localstorage.getObject('appt'));
    };
  });
