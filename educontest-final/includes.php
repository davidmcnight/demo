<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 9/29/16
 * Time: 4:22 PM
 */

require_once "config/Config.php";
require_once "db/Database.php";
require_once "libs/FluentPDO/FluentPDO.php";
require_once "app/domain/factory/Factory.php";
require_once "app/domain/models/IEduObject.php";
require_once "app/domain/services/IRestService.php";
require_once "app/helpers/Encryption.php";
require_once "app/helpers/Login.php";

//MODELS AND SERVICES

//School Objects and Service Class. All these models are used in the school service. 
//All the models are either the school or a child model of school. (i.e. School->billAddress)
//models
require_once "vendor/autoload.php";

require_once "app/domain/models/School.php";
require_once "app/domain/models/SchoolYear.php";
require_once "app/domain/models/SchoolTest.php";

//
//require_once "app/domain/models/User.php";
//require_once "app/domain/models/League.php";
//
require_once "app/domain/models/League.php";
require_once "app/domain/models/ShippingAddress.php";
require_once "app/domain/models/BillingAddress.php";
require_once "app/domain/models/SchoolCompetition.php";
require_once "app/domain/models/Competition.php";
require_once "app/domain/models/Test.php";
require_once "app/domain/models/Year.php";
require_once "app/domain/models/Grade.php";
require_once "app/domain/models/Student.php";
//require_once "app/domain/models/User.php";
//require_once "app/domain/models/LumenSchool.php";


//SERVICES
require_once "app/domain/services/LeagueService.php";
require_once "app/domain/services/SchoolService.php";
require_once "app/domain/services/CompetitionService.php";
require_once "app/domain/services/ResultsService.php";
require_once "app/domain/services/StudentService.php";
require_once "app/domain/services/TestService.php";
require_once "app/domain/services/YearService.php";




//API -- NOT IN USE FOR NOW

