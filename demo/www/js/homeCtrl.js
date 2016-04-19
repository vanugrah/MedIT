// Home Controller
angular.module('medIT.home', [])

  .controller('HomeCtrl', function($scope, $localstorage, $ionicPopup) {

    $scope.$on('$ionicView.loaded', function() {
      $scope.apptReminder = false;
      $localstorage.setObject('apptReminder', $scope.apptReminder);

      $scope.appts = [
        {
          id: 0,
          location: "Children's Healthcare of Atlanta - Egleston",
          address: "1405 Clifton Road NE",
          city: "Atlanta",
          state: "GA",
          zip: "30322",
          date: "21 April 2016",
          time: "4:00pm",
          physician: "Dr. Batisky",
          patient: "Johnny Doe Jr.",
          patientID: "0",
          notes: "Follow-up appointment",
          isCancelled: false,
          color: "Yellow"
        },
        {
          id: 1,
          location: "Children's Healthcare of Atlanta - Egleston",
          address: "1405 Clifton Road NE",
          city: "Atlanta",
          state: "GA",
          zip: "30322",
          date: "22 April 2016",
          time: "1:00pm",
          physician: "Dr. Omojokun",
          patient: "Johanna Doe",
          patientID: "1",
          notes: "6-month check-up",
          isCancelled: false,
          color: "Blue"
        },
        {
          id: 2,
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
          notes: "N/A",
          color: "Yellow"
        }
      ];

      $localstorage.setObject('appts', $scope.appts)
    });

    $scope.$on('$ionicView.beforeEnter', function() {
      //$scope.appts = $localstorage.getObject('appts');
      $scope.apptReminder = $localstorage.getObject('apptReminder');
    });

    $scope.$on('$ionicView.afterEnter', function() {
      $scope.appts = $localstorage.getObject('appts');

      if($scope.apptReminder) {
        setTimeout(function() {
          $scope.reminder();
        }, 6000);
      }
    });

    $scope.reminder = function() {
      var confirmPopup = $ionicPopup.confirm({
        title: '<strong>Upcoming Appointment Reminder</strong>',
        template: "Johanna has an appointment tomorrow " +
          "<strong>4/22/16</strong> " +
          "at <strong>3:15pm</strong> " +
          "at <strong>Children's Healthcare of Atlanta - Egleston.</strong><br>" +
          "<br>Will she be able to make the appointment?<br>" +
          "<br>(If unsure, please select 'Yes')",
        cancelText: 'No',
        okText: 'Yes'
      });

      confirmPopup.then(function(res) {
        var yesTitle = "<strong>Confirmed</strong>";
        var yesTemplate = "Thank you for your response! <br>" +
                          "<br>The clinic has been informed of your expected attendance!";
        var noTitle = "<strong>Cancelled</strong>";
        var noTemplate = "Thank you for your response! <br>" +
                         "<br>The appointment has been cancelled.";

        if(res) {
          $scope.result(yesTitle, yesTemplate);
        } else {
          $scope.result(noTitle, noTemplate);
          $scope.appts[1].isCancelled = true;
          $localstorage.setObject('appts', $scope.appts);
        }
      });
    };

    // An alert dialog
    $scope.result = function(title, template) {
      var alertPopup = $ionicPopup.alert({
        title: title,
        template: template
      });

      alertPopup.then(function(res) {
        $localstorage.setObject('apptReminder', false);
        $scope.apptReminder = false;
      });
    };

    $scope.viewAppt = function(appt) {
      $localstorage.setObject('apptID', appt.id);
    };
  });
