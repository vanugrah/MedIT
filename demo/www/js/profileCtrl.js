/**
 * Created by Anthony Tsou on 4/19/2016.
 */

angular.module('medIT.profile', [])
  .controller('ProfileCtrl', function($scope, $localstorage, $ionicPopup) {

    $scope.$on('$ionicView.loaded', function() {
      $scope.isCheckingIn = false;
      //$localstorage.setObject('isCheckingIn', false);
    });

    $scope.$on('$ionicView.afterEnter', function() {
      $scope.johnnyColor = $localstorage.getObject('johnnyColor');
      $scope.johannaColor = $localstorage.getObject('johannaColor');
      $scope.isCheckingIn = $localstorage.getObject('isCheckingIn');

      //$http.get("http://127.0.0.1/getPatients")
      //  .success(function(data) {
      //    $scope.patients = data;
      //  })
      //  .error(function(data) {
      //    console.log("You messed up yo");
      //  });
      //console.log($scope.isCheckingIn);

      $scope.user = {
        name: "John Doe",
        email: "jdoe@gatech.edu",
        phone: "404-915-3496",
        address: "733 Techwood Dr. Atlanta",
        city: "Atlanta",
        state: "GA",
        zip: "30313"
      };

      $scope.patients = [
        {
          name: "Johnny Doe Jr",
          age: "8",
          gender: "Male",
          insuranceID: "902933285",
          insuranceProvider: "BCBS Georgia",
          color: "pac-blue",
          photo: "img/boy-child.jpg"
        },
        {
          name: "Johanna Doe",
          age: "5",
          gender: "Female",
          insuranceID: "902933251",
          insuranceProvider: "BCBS Georgia",
          color: "green",
          photo: "img/girl-child.jpg"
        }
      ];
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

