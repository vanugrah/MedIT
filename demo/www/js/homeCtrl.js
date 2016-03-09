/**
 * Created by Anthony Tsou on 3/7/2016.
 */
// Home Controller
angular.module('medIT.home', [])

  .controller('HomeCtrl', function($scope, $localstorage) {

    $scope.appts = [
      { title: 'Appointment 1', id: 1 },
      { title: 'Appointment 2', id: 2 },
      { title: 'Appointment 3', id: 3 },
      { title: 'Appointment 4', id: 4 },
      { title: 'Appointment 5', id: 5 },
      { title: 'Appointment 6', id: 6 }
    ];

    $scope.viewAppt = function(appt) {
      $localstorage.setObject('appt', appt);
      console.log($localstorage.getObject('appt'));
    };
  });
