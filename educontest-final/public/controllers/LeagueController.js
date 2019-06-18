/**
 * Created by dmcnight on 10/4/16.
 */



edu.controller('LeagueController', ['$scope','$http', function($scope, $http) {
    
    $scope.getAllLeagues = function () {
        $http.get("/api/League/").success(function (response) {
            $scope.leagues = response;
            console.log($scope.leagues)
        });
    }

}]);