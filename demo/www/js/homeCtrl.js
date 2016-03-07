/**
 * Created by Anthony Tsou on 3/7/2016.
 */
// Home Controller
angular.module('starter.home', [])

  .controller('HomeCtrl', function($scope) {
    $scope.appts = [
      { title: 'Appointment 1', id: 1 },
      { title: 'Appointment 2', id: 2 },
      { title: 'Appointment 3', id: 3 },
      { title: 'Appointment 4', id: 4 },
      { title: 'Appointment 5', id: 5 },
      { title: 'Appointment 6', id: 6 }
    ];
    //
    //$scope.viewAppt = function() {
    //  href="#/app/browse"
    //};
  });
