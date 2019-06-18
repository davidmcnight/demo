/**
 * Created by dmcnight on 10/4/16.
 */
edu.controller('SchoolController', ['$scope','$http', function($scope, $http) {

    $scope.signedUpCompetition = [];
    $scope.notSignedUpCompetition = [];
    $scope.signedUpTest = [];
    $scope.notsignedUpTest = [];

    $scope.currentPage = 1;
    $scope.pageSize = 50;


    $scope.invoicePayments = {};

    $scope.sample_pdf = {
        '3rd Grade': 'm3-11.pdf',
        '4th Grade': 'm4-11.pdf',
        '5th Grade': 'm5-11.pdf',
        '6th Grade': 'm6-11.pdf',
        '7th Grade': 'm7-11.pdf',
        'Pre-Algebra': 'pre-11.pdf',
        'Algebra 1': 'a1-11.pdf',
        'Algebra 2': 'a2-11.pdf',
        'Geometry': 'gt-11.pdf',
        'Advanced Math': 'amt-11.pdf'
    }

    $scope.states = ["AK","AL","AR","AZ","CA","CO","CT","DC","DE",
        "FL","GA","GU","HI","IA","ID", "IL","IN","KS","KY","LA",
        "MA","MD","ME","MH","MI","MN","MO","MS","MT","NC","ND",
        "NE","NH","NJ","NM","NV","NY", "OH","OK","OR","PA","PR",
        "PW","RI","SC","SD","TN","TX","UT","VA","VI","VT","WA",
        "WI","WV","WY"];

    $scope.syButtonDisabled = false;


    $scope.showClasses = false;

    $scope.classRegSuccess = false;
    $scope.classTestSuccess = false;


    //ALERTS STUDENTS
    $scope.studentCreatedAlert = false;
    $scope.studentDeletedAlert = false;
    

    //SCHOOL
    $scope.getSchool = function (id) {
        $http.get("/api/School/" + id).success(function (response) {
            $scope.school = response;
        });
    }

    $scope.updateSchool = function (school) {
        var data = {};
        data["school"] = school;
        data["billing_address"] = school.billing_address;
        data["shipping_address"] = school.shipping_address;
        $http.put("/api/School", data).success(function (response) {
           location.reload();
        }, function errorCallback(response) {
            console.log(response);
        });

    }

    $scope.updatePassword = function (school_id) {
        var data = {};
        data["update_password"] = 1;
        data["school_id"] = school_id;
        $http.put("/api/School", data).success(function (response) {
            $scope.getAllSchoolsPw();
        }, function errorCallback(response) {
            console.log(response);
        });

    }

    $scope.getSchoolMin = function (id) {
        $http({
            url: "/api/find/School",
            method: "GET",
            params:  {"min": 1, "id": id}
        }).then(function successCallback(response){
           $scope.school = response.data;
        });
    }

    $scope.getCurrentSchools = function () {
        $http({
            url: "/api/find/School",
            method: "GET",
            params:  {"getCurrentSchools": 1}
        }).then(function successCallback(response){
            $scope.schools = response.data;
        });
    }

    $scope.getAllSchools = function () {
        $http.get("/api/School/").success(function (response) {
            $scope.schools = response;
        });
    }

    $scope.getAllSchoolsPw = function () {
            $http({
                url: "/api/find/School",
                method: "GET",
                params:  {"getAllSchoolsPassword": 1}
            }).then(function successCallback(response){
                $scope.schools = response.data;
            });

        }

    //SCHOOL YEAR
    $scope.isSchoolSignedUp = function (schoolId) {
        $http({
            url: "/api/find/Year",
            method: "GET",
            params:  {"isSchoolSignedUp": 1, "school_id": schoolId}
        }).then(function successCallback(response){
            $scope.is_school_signed_up = response.data.is_school_signed_up;
            if($scope.is_school_signed_up == true){
                $scope.schoolYear = response.data.activeSchoolYear;
                $scope.getCompetitionListsForSchool();
                $scope.getTestListsForSchool();
            }
        });

    }

    $scope.createSchoolYear = function () {
        var data = {};
        data["school"] = $scope.school;
        data["year"] = $scope.activeYear;
        data["createSchoolYear"] = 1;
        $http.post("/api/Year", data).success(function (response) {
            window.location.href= '/users/sign-up/' + $scope.school.id;
            $scope.syButtonDisabled = true;
        }, function errorCallback(response) {
            console.log("Fail");
        });

    }

    $scope.createSchoolYearAdmin = function () {
        var data = {};
        data["school"] = $scope.school;
        data["year"] = $scope.activeYear;
        data["createSchoolYear"] = 1;
        $http.post("/api/Year", data).success(function (response) {
            window.location.href= '/admin/school/dashboard/' + $scope.school.id;
            $scope.syButtonDisabled = true;
        }, function errorCallback(response) {
            console.log("Fail");
        });

    }



    //YEAR
    $scope.getActiveYearMin = function () {
        $http({
            url: "/api/find/Year",
            method: "GET",
            params:  {"getActiveYear": 1}
        }).then(function successCallback(response){
            $scope.activeYear = response.data;
           
        });

    }

    $scope.getActiveSchoolYear = function (school_id) {
        $http({
            url: "/api/find/Year",
            method: "GET",
            params:  {"getActiveSchoolYear": 1, "school_id": school_id}
        }).then(function successCallback(response){
            $scope.activeYear = response.data.active_year;
            $scope.schoolYear = response.data.school_year;
        });

    }
    
    
    $scope.getActiveYear = function () {
        $http({
            url: "/api/find/Year",
            method: "GET",
            params:  {"getActiveYear": 1}
        }).then(function successCallback(response){
            $scope.activeYear = response.data;
        });

    }

    //COMPETITION
    $scope.createSchoolCompetition = function (competition) {
        var data = {};
        data["school_year_id"] = $scope.schoolYear;
        data["competition"] = competition;
        data["createSchoolCompetition"] = 1;
        $http.post("/api/Competition", data).success(function (response) {
            $scope.getCompetitionListsForSchool();
            $scope.classRegSuccess = true;
        }, function errorCallback(response) {
            console.log("Fail");
        });

    }

    $scope.createSchoolTest = function (test) {
        var data = {};
        data["school_year_id"] = $scope.schoolYear;
        data["test"] = test;
        data["createSchoolTest"] = 1;
        $http.post("/api/Test", data).success(function (response) {
            $scope.getTestListsForSchool();
            $scope.classTestSuccess = true;
        }, function errorCallback(response) {
            console.log("Fail");
        });

    }

    $scope.deleteSchool = function (school_id) {

        $http({
            url: "/api/find/School",
            method: "GET",
            params:  {"deleteSchool": 1,
                "school_id": school_id
            }
        }).then(function successCallback(response){
            $scope.getAllSchools();
        });


    }


    $scope.deleteSchoolTest = function (school_test) {

        $http({
            url: "/api/find/Test",
            method: "GET",
            params:  {"deleteSchoolTest": 1,
                "school_test": school_test
            }
        }).then(function successCallback(response){
            $scope.getTestListsForSchool();
        });


    }

    $scope.deleteSchoolCompetition= function (school_competition) {

        $http({
            url: "/api/find/Competition",
            method: "GET",
            params:  {"deleteSchoolCompetition": 1,
                "school_competition": school_competition
            }
        }).then(function successCallback(response){
            $scope.getCompetitionListsForSchool();
        });


    }




    $scope.getRegisteredClasses = function () {
        $http({
            url: "/api/find/Competition",
            method: "GET",
            params:  {"getRegisteredClasses": 1,
                "school_id": $scope.school_id
            }
        }).then(function successCallback(response){
            $scope.registeredClasses = response.data;
        });

    };
    
    $scope.getTestListsForSchool = function () {
        $http({
            url: "/api/find/Test",
            method: "GET",
            params:  {"getSchoolTestLists": 1,
                "school_year_id": $scope.schoolYear
            }
        }).then(function successCallback(response){
            $scope.signedUpTest = response.data.signedUpTest;
            $scope.notSignedUpTest = response.data.notSignedUpTest;
        });
        
    }

    $scope.getCompetitionListsForSchool = function () {
        $http({
            url: "/api/find/Competition",
            method: "GET",
            params:  {
                "getSchoolCompetitionLists": 1,
                "school_year_id": $scope.schoolYear
            }
        }).then(function successCallback(response){
            $scope.signedUpCompetition = response.data.signedUpCompetition;
            $scope.notSignedUpCompetition = response.data.notSignedUpCompetition;
        });

    };

    $scope.getSchoolCompetition = function (scId) {
        $http({
            url: "/api/find/Competition",
            method: "GET",
            params:  {"getSchoolCompetition": 1,
                "school_competition_id": scId
            }
        }).then(function successCallback(response){
            $scope.schoolCompetition = response.data;
            console.log($scope.schoolCompetition);
            //$scope.getStudentsBySchoolCompetition($scope.schoolCompetition.id)
        });

    };

    $scope.updateSchoolCompetitionPublic = function (sc) {
        var data = {};
        data["updateSchoolCompetitionPublic"] = 1;
        data["school_competition"] = sc;
        $http.put("/api/Competition", data).success(function (response) {
           location.reload();
        }, function errorCallback(response) {
            console.log(response);
        });

        console.log(sc);
    }

    $scope.getStudentsBySchoolCompetition = function (scId) {
        $http({
            url: "/api/find/Student",
            method: "GET",
            params:  {"school_competition_id": scId}
        }).then(function successCallback(response){
            $scope.students = response.data;
            console.log($scope.students);
        });

    }
    $scope.createStudent = function (student, scId) {
        var data = {};
        student["school_competition_id"] = scId;
        data["student"] = student;
        $http.post("/api/Student", data).success(function (response) {
            $scope.getSchoolCompetition(scId);
            $scope.studentCreatedAlert = true;
            $scope.student = {};
        }, function errorCallback(response) {
              console.log(response);
        });

    }


    $scope.updateAllStudents = function(students){
        angular.forEach(students, function(value) {
           $scope.updateStudent(value);
        });
        location.reload();
    };

    $scope.updateStudent = function (student) {
        var data = {};
        data["student"] = student;
        $http.put("/api/Student", data).success(function (response) {
            $scope.getSchoolCompetition(student.school_competition_id);
            $scope.studentCreatedAlert = true;
        }, function errorCallback(response) {
            console.log(response);
        });

    }

    $scope.deleteStudent = function (student) {
        $http.delete("/api/Student/" + student.id).success(function (response) {
            $scope.getSchoolCompetition(student.school_competition_id);
            $scope.studentDeletedAlert = true;
        }, function errorCallback(response) {
            console.log(response);
        });
    }

    $scope.getAllLeagues = function () {
        $http.get("/api/League/").success(function (response) {
            $scope.leagues = response;
            console.log($scope.leagues)
        });
    }

    //INVOICES
    $scope.getAllInvoices = function (value) {
        $http({
            url: "/api/find/Year",
            method: "GET",
            params:  {"schoolsInvoice": 1}
        }).then(function successCallback(response){
            $scope.schoolYears = response.data;
        });

    }

    $scope.getPaidInvoices = function () {
        console.log("ENTERING PAID");
        $http({
            url: "/api/find/Year",
            method: "GET",
            params:  {"schoolsInvoice": 1}
        }).then(function successCallback(response){
            var invoices = [];
            var invoice = response.data;
            for (var i = 0; i < invoice.length; i++) {
                var totalCost = 6.00;
                //check league
                if (invoice[i].school.league.id == 1) {
                    //70
                    totalCost = totalCost
                        + (invoice[i].school_competitions.length * 70)
                        + (invoice[i].school_tests.length * 20);

                } else {
                    totalCost = totalCost
                        + (invoice[i].school_competitions.length * 80)
                        + (invoice[i].school_tests.length * 20);
                }
                // printNicely($totalCost);
                console.log(totalCost);
                if (totalCost - invoice[i].amount_paid <= 0) {
                    invoices.push(invoice[i]);
                }

            }
            $scope.schoolYears = invoices;
        });

    }

    $scope.getUnpaidInvoices = function () {

        $http({
            url: "/api/find/Year",
            method: "GET",
            params:  {"schoolsInvoice": 1}
        }).then(function successCallback(response){
            var invoices = [];
            var invoice = response.data;
            for (var i = 0; i < invoice.length; i++) {
                var totalCost = 6.00;
                //check league
                if (invoice[i].school.league.id == 1) {
                    //70
                    totalCost = totalCost
                        + (invoice[i].school_competitions.length * 70)
                        + (invoice[i].school_tests.length * 20);

                } else {
                    totalCost = totalCost
                        + (invoice[i].school_competitions.length * 80)
                        + (invoice[i].school_tests.length * 20);
                }
                // printNicely($totalCost);
                console.log(totalCost);
                if (totalCost - invoice[i].amount_paid > 0) {
                    invoices.push(invoice[i]);
                }

            }
            $scope.schoolYears = invoices;
        });

    }

    $scope.makeAPayment = function (school_year, amount){

        var data = {};
        data["school_year"] = school_year;
        data["makeAPayment"] = 1;
        data["amount"] = amount;
        $http.put("/api/Year", data).success(function (response) {
            $scope.invoicePayments ={};
            $scope.getAllInvoices();
        }, function errorCallback(response) {
            console.log("Fail");
        });

    }
    
    //divisions
    $scope.current_division_league_id = 0;
    $scope.getSchoolsForDivision = function (league_id) {
        $scope.current_division_league_id = league_id;
        $http({
            url: "/api/find/Competition",
            method: "GET",
            params:  {"getSchoolsForDivision": 1, "league_id": league_id}
        }).then(function successCallback(response){
            $scope.schoolCompetitionsDivisions = response.data;
        });

    }


    //

    $scope.updateDivision = function (sc){


        var data = {};
        data["school_competition"] = sc;
        data["updateDivision"] = 1;
        console.log(data);
        $http.put("/api/Competition", data).success(function (response) {
            $scope.getSchoolsForDivision($scope.current_division_league_id);
        }, function errorCallback(response) {

            console.log("Fail");
        });

    }

    $scope.downloadTest = function (url) {
        window.open(
            '/files/' + url,
            '_blank' // <- This is what makes it open in a new window.
        );
    }

    //results
    $scope.showStudentResults = false;
    $scope.showSchoolResults = false;
    $scope.result_filter = {};
    $scope.studentTestResults = {};
    $scope.student_results_page_size = 50;
    $scope.student_results_current_page =1;


    $scope.schoolTestResults = {};
    $scope.school_results_page_size = 50;
    $scope.school_results_current_page =1;

    $scope.getStudentTestResults = function (filters) {
        $http({
            url: "/api/find/Student",
            method: "GET",
            params:  {"studentResults": 1, "filters": filters}
        }).then(function successCallback(response){
            $scope.showStudentResults = true;
            $scope.showSchoolResults = false;
            $scope.studentTestResults = response.data;
        });

    }

    $scope.getSchoolTestResults = function (filters) {
        $http({
            url: "/api/find/Competition",
            method: "GET",
            params:  {"schoolResults": 1, "filters": filters}
        }).then(function successCallback(response){
            $scope.showStudentResults = false;
            $scope.showSchoolResults = true;
            $scope.schoolTestResults = response.data;
        });

    }

    //SETTINGS
    $scope.getAllYears = function () {
        $http.get("/api/Year/").success(function (response) {
            $scope.years = response;
        });
    }

    $scope.updateActiveYear = function (id) {
        var data = {};
        data["updateActiveYear"] = 1;
        data["activeYearId"] = id;
        $http.put("/api/Year", data).success(function (response) {
            location.reload();
        }, function errorCallback(response) {
            console.log(response);
        });

    }

    $scope.updateOpenTest = function (openTest) {
        var data = {};
        data["updateOpenTest"] = 1;
        data["openTest"] = openTest;
        $http.put("/api/Year", data).success(function (response) {
            location.reload();
        }, function errorCallback(response) {
            console.log(response);
        });

    }

    $scope.updateYearDates = function (test1, test2, test3, test4) {
        var data = {};
        data["updateTestYears"] = 1;
        data["test1"] = test1;
        data["test2"] = test2;
        data["test3"] = test3;
        data["test4"] = test4;
        $http.put("/api/Year", data).success(function (response) {
            location.reload();
        }, function errorCallback(response) {
            console.log(response);
        });

    }



}]).directive('convertToNumber', function() {
    return {
        require: 'ngModel',
        link: function(scope, element, attrs, ngModel) {
            ngModel.$parsers.push(function(val) {
                return parseInt(val, 10);
            });
            ngModel.$formatters.push(function(val) {
                return '' + val;
            });
        }
    };
});