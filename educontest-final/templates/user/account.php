<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 10/13/16
 * Time: 4:38 PM
 */



require_once "templates/layout/header.php";
//printNicely($log);
//printNicely($_SESSION);
?>

<div class="container pt2 mt3" id="main" ng-app="edu" ng-controller="SchoolController"
     ng-init="getSchool(<?php echo $school_id; ?>)">
    <div class="col-xs-12 col-md-12 row">
        <form name="user_reg_form" class="p1">
            <div class="col-xs-12">
                <h2>Account Information</h2>
            </div>

            <div class="col-xs-12 row mb1">
                <h3 class="col-xs-12">School Information</h3>
                <!-- School Name -->
                <div class="col-xs-12 col-md-4 mb1">
                    <label class="control-label">School Name</label>
                    <input ng-model="school.name" name="school_name"
                           type="text" class="form-control" placeholder="Greenville High" >
                </div>
                <!-- School Contact -->
                <div class="col-xs-12 col-md-4 mb1">
                    <label class="control-label">School Contact</label>
                    <input ng-model="school.contact" name="school_contact"
                           type="text" class="form-control" placeholder="Jane Doe" >
                </div>

                <div class="col-xs-12 col-md-4 mb1">
                    <label class="control-label">Billing Name</label>
                    <input ng-model="school.billing_name" name="school_billing_name"
                           type="text" class="form-control" placeholder="Jane Doe" >
                </div>

                <!-- School Phone -->
                <div class="col-xs-12 col-md-4 mb1">
                    <label class="control-label">School Phone</label>
                    <input ng-model="school.phone" name="school_phone"
                           type="tel" class="form-control" placeholder="" >
                </div>
                <!-- School Fax -->
                <div class="col-xs-12 col-md-4 mb1">
                    <label class="control-label">School Fax</label>
                    <input ng-model="school.fax" name="school_fax"
                           type="tel" class="form-control" placeholder="" >
                </div>

                <!-- School Email -->
                <div class="col-xs-12 col-md-4 mb1">
                    <label class="control-label">School Email</label>
                    <input class="form-control" ng-model="school.school_email"
                           name="school_email"
                           type="email" class="form-control"/>
                </div>

                <!-- School Purchase Order -->


                <div class="col-xs-12 col-md-3 mb1">
                    <label class="control-label">Email 2</label>
                    <input class="form-control" ng-model="school.email_2"
                           name="school_email_1"
                           type="text" class="form-control"/>
                </div>
                <div class="col-xs-12 col-md-3 mb1">
                    <label class="control-label">Email 3</label>
                    <input class="form-control" ng-model="school.email_3"
                           name="school_email_3"
                           type="text" class="form-control"/>
                </div>
                <div class="col-xs-12 col-md-3 mb1">
                    <label class="control-label">Email 4</label>
                    <input class="form-control" ng-model="school.email_4"
                           name="school_email_4"
                           type="text" class="form-control"/>
                </div>

                <div class="col-xs-12 col-md-3 mb1">
                    <label class="control-label">PO # (For School Only)</label>
                    <input class="form-control" ng-model="school.purchase_order"
                           name="school_purchase_order"
                           type="text" class="form-control"/>
                </div>

                <!-- Invalid email Error -->
                <div class='msg-block mt1 mb1 col-xs-12' ng-show='user_reg_form.$error'>
                        <span class='msg-error alert alert-danger'
                              ng-show='user_reg_form.school_email.$error.email'>
                        Not a valid email address.
                        </span>
                </div>
            </div>

            <!--        SHIPPING        -->
            <div class="col-xs-12 row mb1">
                <h3 class="col-xs-12">Contact Information</h3>
                <!-- School Shipping Address 1-->
                <div class="col-xs-12 col-md-4 mt1">
                    <label class="control-label">Address</label>
                    <input ng-model="school.shipping_address.address1" name="registration_shipping_address1"
                           type="text" class="form-control col-xs-12" placeholder="" >
                </div>
                <!-- School Shipping Address 2-->
                <div class="col-xs-12 col-md-4  mt1">
                    <label class="control-label">Address 2</label>
                    <input ng-model="school.shipping_address.address2" name="registration_shipping_address2"
                           type="text" class="form-control col-xs-12" placeholder="" >
                </div>
                <!-- School Shipping City-->
                <div class="col-xs-12 col-md-4  mt1">
                    <label class="control-label">City</label>
                    <input ng-model="school.shipping_address.city" name="registration_shipping_city"
                           type="text" class="form-control col-xs-12" placeholder="" >
                </div>
                <div class="col-xs-12 col-md-4  mt1 ">
                    <label class="control-label">State</label>
                    <select class="form-control" ng-model="school.shipping_address.state"  ng-options="state as state for state in states">
                    </select>
                </div>
                <div class="col-xs-12 col-md-4  mt1 ">
                    <label class="control-label">Zip</label>
                    <input ng-model="school.shipping_address.zip" name="registration_shipping_address2"
                           type="text" class="form-control col-xs-12" placeholder="" maxlength="2">
                </div>
            </div>
            <div class="col-xs-12 row mb1">
                <h3 class="col-xs-12">Billing Information</h3>
                <!--                     School Shipping Address 1-->
                <div class="col-xs-12 col-md-4  mt1 ">
                    <label class="control-label">Address</label>
                    <input ng-model="school.billing_address.address1" name="registration_billing_address1"
                           type="text" class="form-control col-xs-12" placeholder="" >
                </div>
                <!--                     School Shipping Address 2-->
                <div class="col-xs-12 col-md-4  mt1 ">
                    <label class="control-label">Address 2</label>
                    <input ng-model="school.billing_address.address2" name="billing_address2"
                           type="text" class="form-control col-xs-12" placeholder="" >
                </div>
                <!--                     School Shipping City-->
                <div class="col-xs-12 col-md-4  mt1 ">
                    <label class="control-label">City</label>
                    <input ng-model="school.billing_address.city" name="billing_city"
                           type="text" class="form-control col-xs-12" placeholder="" >
                </div>
                <div class="col-xs-12 col-md-4  mt1 ">
                    <label class="control-label">State</label>
                    <select class="form-control" ng-model="school.billing_address.state"  ng-options="state as state for state in states">
                    </select>
                </div>

                <div class="col-xs-12 col-md-4 mt1 ">
                    <label class="control-label">Zip</label>
                    <input ng-model="school.billing_address.zip" name="billing_address2"
                           type="text" class="form-control col-xs-12" placeholder="" >
                </div>

            </div>
            <div class="col-xs-12 pb3">
                <button class="btn-success btn" ng-click="updateSchool(school)">Save</button>
            </div>
<!----></form>
        </div>
