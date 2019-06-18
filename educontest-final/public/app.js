/**
 * Created by dmcnight on 9/30/16.
 */
var edu = angular.module('edu',['angularUtils.directives.dirPagination']);

   

edu.controller('registrationController', ['$scope','$http','$window', function($scope, $http,$window) {

    $scope.school = {};
    $scope.billing = {};
    $scope.shipping = {};
    $scope.user = {};

    $scope.admin_school = {};

    $scope.sign_in = {};
//     $scope.sign_in.school_id = "4";
//     $scope.sign_in.password = "12345";
//
//
//     $scope.school.league_id = 2;
//     $scope.school.name = "Greenville High";
//     $scope.school.contact = "Davey McNight";
//     $scope.school.billing_name = "Sarah";
//     $scope.school.phone = "864-654-256";
//     $scope.school.fax = "864-223-1234";
//     $scope.school.school_email = "davidmcnight@gmail.com";
//     $scope.school.purchase_order = "55673";
//     // $scope.school.league_id = 1;
// //
//     $scope.shipping.address1 = "125 N. Main St.";
//     $scope.shipping.address2 = "STE #2";
//     $scope.shipping.city = "Greenville";
//     $scope.shipping.state = "SC";
//     $scope.shipping.zip = "29601";
// //
//     $scope.billing.address1 = "125 N. Main St.";
//     $scope.billing.address2 = "STE #2";
//     $scope.billing.city = "Greenville";
//     $scope.billing.state = "SC";
//     $scope.billing.zip = "29601";
//
//     $scope.user.email = "davidmcnight@gmail.com";
//     $scope.user.password = "irishman1";
//     $scope.user.password_confirm = "irishman1";

    $scope.getAllLeagues = function () {
        $http.get("/api/League/").success(function (response) {
            $scope.leagues = response;
            console.log($scope.leagues)
        });
    }

    $scope.states = ["AK","AL","AR","AZ","CA","CO","CT","DC","DE",
        "FL","GA","GU","HI","IA","ID", "IL","IN","KS","KY","LA",
        "MA","MD","ME","MH","MI","MN","MO","MS","MT","NC","ND",
        "NE","NH","NJ","NM","NV","NY", "OH","OK","OR","PA","PR",
        "PW","RI","SC","SD","TN","TX","UT","VA","VI","VT","WA",
        "WI","WV","WY"];



    $scope.test = function (registration) {
        console.log(registration);
    }

    $scope.sameAddress = function () {
        $scope.billing.address1 = $scope.shipping.address1;
        $scope.billing.address2 = $scope.shipping.address2;
        $scope.billing.city = $scope.shipping.city;
        $scope.billing.state = $scope.shipping.state;
        $scope.billing.zip = $scope.shipping.zip;
    }



    $scope.registerUser = function (school, shipping, billing, user) {


        var data = {};
        data["school"] = school;
        data["shipping_address"] = shipping;
        data["billing_address"] = billing;
        //data["user"] = user;

        $http.post("/api/School", data).success(function (response) {
           console.log("Success");
            $window.location.href = '/users/thank-you';
        }, function errorCallback(response) {
            console.log("Fail");
        });
    }


    $scope.registerUserAdmin = function (school, shipping, billing) {


        var data = {};
        data["admin"] = 1;
        data["school"] = school;
        data["shipping_address"] = shipping;
        data["billing_address"] = billing;
        //data["user"] = user;

        $http.post("/api/School", data).success(function (response) {
            console.log("Success");
            console.log(response);
            $window.location.href = '/admin/school/created/?school_id=' + response.school_id + "&school_password="+response.password;
        }, function errorCallback(response) {
            console.log("Fail");
        });
    }

}]);


var compareTo = function() {
    return {
        require: "ngModel",
        scope: {
            otherModelValue: "=compareTo"
        },
        link: function(scope, element, attributes, ngModel) {

            ngModel.$validators.compareTo = function(modelValue) {
                return modelValue == scope.otherModelValue;
            };

            scope.$watch("otherModelValue", function() {
                ngModel.$validate();
            });
        }
    };
};

edu.directive("compareTo", compareTo);

//DIRECTIVES
    edu.directive('pwCheck', [function () {
        return {
            require: 'ngModel',
            link: function (scope, elem, attrs, ctrl) {
                var firstPassword = '#' + attrs.pwCheck;
                elem.add(firstPassword).on('keyup', function () {
                    scope.$apply(function () {
                        var v = elem.val()===$(firstPassword).val();
                        ctrl.$setValidity('pwmatch', v);
                    });
                });
            }
        }
    }]);


function OtherController($scope) {
    $scope.pageChangeHandler = function(num) {
        console.log('going to page ' + num);
    };
}

edu.controller('OtherController', OtherController);





// edu.config(['$interpolateProvider', function ($interpolateProvider) {
   //     $interpolateProvider.startSymbol('[{[');
   //     $interpolateProvider.endSymbol(']}]');
   // }]);

