/**
 * Created by Anthony Tsou on 4/18/2016.
 */
// Home Controller
angular.module('medIT.apptDetails', [])

  .controller('ApptDetailsCtrl', function($scope, $localstorage) {

    $scope.$on('$ionicView.beforeEnter', function() {
      $scope.appt = [];
      $scope.appt[0] = $localstorage.getObject('appt');

      console.log($scope.appt);
    });

  });
