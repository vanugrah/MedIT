// Home Controller
angular.module('medIT.home', [])

  .controller('HomeCtrl', function($scope, $localstorage, $ionicPopup, $http) {

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

    //$scope.$on('$ionicView.beforeEnter', function() {
    //  //$scope.appts = $localstorage.getObject('appts');
    //  //$scope.apptReminder = $localstorage.getObject('apptReminder');
    //
    //  var data = {
    //    MessageType: "AppointmentsQuery",
    //    Username: 'atsou3'
    //  };
    //
    //  $http.post("http://localhost/", data)
    //    .success(function(data) {
    //      if (data.MessageType === "Error") {
    //        alert("Error");
    //      } else {
    //        $scope.appts = data.Appointments;
    //      }
    //    })
    //    .error(function(data) {
    //      alert("You messed up");
    //    });
    //
    //});

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
        console.log(JSON.stringify(response));
        if (response.MessageType === "Error") {
          alert("Error");
        } else {
          $scope.appts = response.data.Appointments;
        }
      }, function errorCallback(response) {
        alert("You messed up");
      });

    };
    //
    //$scope.$on('$ionicView.afterEnter', function() {
    //  //$scope.appts = $localstorage.getObject('appts');
    //
    //  if ($localstorage.getObject('hasCheckedIn') !== null) {
    //    console.log($localstorage.getObject('hasCheckedIn'));
    //    $scope.appts[0].notCheckedIn = !$localstorage.getObject('hasCheckedIn');
    //  }
    //
    //  //if($scope.apptReminder) {
    //  //  setTimeout(function() {
    //  //    $scope.reminder();
    //  //  }, 6000);
    //  //}
    //});

    //$scope.reminder = function() {
    //  var confirmPopup = $ionicPopup.confirm({
    //    title: '<strong>Upcoming Appointment Reminder</strong>',
    //    template: "Johanna has an appointment tomorrow " +
    //      "<strong>4/22/16</strong> " +
    //      "at <strong>3:15pm</strong> " +
    //      "at <strong>Children's Healthcare of Atlanta - Egleston.</strong><br>" +
    //      "<br>Will she be able to make the appointment?<br>" +
    //      "<br>(If unsure, please select 'Yes')",
    //    cancelText: 'No',
    //    okText: 'Yes'
    //  });
    //
    //  confirmPopup.then(function(res) {
    //    var yesTitle = "<strong>Confirmed</strong>";
    //    var yesTemplate = "Thank you for your response! <br>" +
    //                      "<br>The clinic has been informed of your expected attendance!";
    //    var noTitle = "<strong>Cancelled</strong>";
    //    var noTemplate = "Thank you for your response! <br>" +
    //                     "<br>The appointment has been cancelled.";
    //
    //    if(res) {
    //      $scope.result(yesTitle, yesTemplate);
    //    } else {
    //      $scope.result(noTitle, noTemplate);
    //
    //      $scope.appts[1].isCancelled = true;
    //      $localstorage.setObject('appts', $scope.appts);
    //
    //      //$http.post("http://127.0.0.1/{REST OF THE URL PLS}", $scope.appts)
    //      //  .success(function(data) {
    //      //    $scope.result();
    //      //  })
    //      //  .error(function(data) {
    //      //    alert("You messed up");
    //      //  });
    //    }
    //  });
    //};

    // An alert dialog
    //$scope.result = function(title, template) {
    //  var alertPopup = $ionicPopup.alert({
    //    title: title,
    //    template: template
    //  });
    //
    //  alertPopup.then(function(res) {
    //    $localstorage.setObject('apptReminder', false);
    //    $scope.apptReminder = false;
    //  });
    //};
    //
    //$scope.viewAppt = function(appt) {
    //  $localstorage.setObject('apptID', appt.id);
    //};
  });
