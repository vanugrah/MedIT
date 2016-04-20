/**
 * Created by Anthony Tsou on 4/19/2016.
 */

angular.module('medIT.profile', [])
  .controller('ProfileCtrl', function($scope, $localstorage, $ionicPopup) {

    $scope.$on('$ionicView.loaded', function() {
      $scope.isCheckingIn = true;
    });

    $scope.$on('$ionicView.afterEnter', function() {
      $scope.johnnyColor = $localstorage.getObject('johnnyColor');
      $scope.johannaColor = $localstorage.getObject('johannaColor');
      $scope.isCheckingIn = $localstorage.getObject('isCheckingIn');
    });

    $scope.checkIn = function(res) {
      var title = "Checking In";
      var template = "";
      if (res === 0) {
        template = "Thank you for checking in! <br>" +
          "<br>Please see the front desk upon arrival in order to update your information!";
      } else {
        template = "Thank you for checking in! <br>" +
          "<br>You will be added to the waiting queue upon arrival.";
      }
      $scope.result(title, template);
    };

    $scope.result = function(title, template) {
      var alertPopup = $ionicPopup.alert({
        title: title,
        template: template
      });

      alertPopup.then(function(res) {
        $scope.isCheckingIn = false;
        $localstorage.setObject('isCheckingIn', false);
        $localstorage.setObject('hasCheckedIn', true);
      });
    };
  });

