/**
 * Created by Anthony Tsou on 4/29/2016.
 *
 * Controller for the menu
 */
angular.module('medIT.menu', [])

.controller('MenuCtrl', function($scope, $localstorage) {
    $scope.user = $localstorage.getObject('user');
});
