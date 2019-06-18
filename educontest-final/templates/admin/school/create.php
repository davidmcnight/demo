<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 4/3/17
 * Time: 2:55 PM
 */
require_once "templates/layout/header.php"
?>

<div class="container col-xs-9 col-xs-offset-1 mt2" id="main"
     ng-app="edu" ng-controller="registrationController">
    <div class="row p1 col-xs-12 ">
        <div class="col-xs-12 col-md-12">
            <form name="user_reg_form" >
                <div class="col-xs-12 row">
                    <h3 class="col-xs-12">School Information</h3>
                    <div class="col-md-2 mb1"  ng-init="getAllLeagues()">
                        <label class="control-label">League</label>
                        <select class="form-control" ng-model="school.league_id" name="registration_league"
                                ng-options="league.id as league.description for league in leagues">
                        </select>
                    </div>
                    <!-- School Name -->
                    <div class="col-xs-12 col-md-3 mb1">
                        <label class="control-label">Name</label>
                        <input ng-model="school.name" name="school_name"
                               type="text" class="form-control" placeholder="Greenville High" >
                    </div>
                    <!-- School Contact -->
                    <div class="col-xs-12 col-md-2 mb1">
                        <label class="control-label">Contact</label>
                        <input ng-model="school.contact" name="school_contact"
                               type="text" class="form-control" placeholder="Jane Doe" >
                    </div>

                    <div class="col-xs-12 col-md-2 mb1">
                        <label class="control-label">Billing Name</label>
                        <input ng-model="school.billing_name" name="school_billing_name"
                               type="text" class="form-control" placeholder="Jane Doe" >
                    </div>

                    <!-- School Phone -->
                    <div class="col-xs-12 col-md-2 mb1">
                        <label class="control-label">Phone</label>
                        <input ng-model="school.phone" name="school_phone"
                               type="tel" class="form-control" placeholder="" >
                    </div>
                    <!-- School Fax -->
                    <div class="col-xs-12 col-md-2 mb1">
                        <label class="control-label">Fax</label>
                        <input ng-model="school.fax" name="school_fax"
                               type="tel" class="form-control" placeholder="" >
                    </div>

                    <!-- School Email -->
                    <div class="col-xs-12 col-md-2 mb1">
                        <label class="control-label">Email</label>
                        <input class="form-control" ng-model="school.school_email"
                               name="school_email"
                               type="email" class="form-control"/>
                    </div>

                    <!-- School Purchase Order -->
                    <div class="col-xs-12 col-md-2 mb1">
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
                    <div class="col-xs-12 col-md-4">
                        <label class="control-label">Address</label>
                        <input ng-model="shipping.address1" name="registration_shipping_address1"
                               type="text" class="form-control col-xs-12" placeholder="" >
                    </div>
                    <!-- School Shipping Address 2-->
                    <div class="col-xs-12 col-md-2">
                        <label class="control-label">Address 2</label>
                        <input ng-model="shipping.address2" name="registration_shipping_address2"
                               type="text" class="form-control col-xs-12" placeholder="" >
                    </div>
                    <!-- School Shipping City-->
                    <div class="col-xs-12 col-md-2">
                        <label class="control-label">City</label>
                        <input ng-model="shipping.city" name="registration_shipping_city"
                               type="text" class="form-control col-xs-12" placeholder="" >
                    </div>
                    <div class="col-xs-12 col-md-2">
                        <label class="control-label">State</label>
                        <input ng-model="shipping.state" name="registration_shipping_state"
                               type="text" class="form-control col-xs-12" placeholder="" >
                    </div>
                    <div class="col-xs-12 col-md-2">
                        <label class="control-label">Zip</label>
                        <input ng-model="shipping.zip" name="registration_shipping_address2"
                               type="text" class="form-control col-xs-12" placeholder="" >
                    </div>
                </div>
                <div class="col-xs-12">
                    <button ng-disabled="!(!!shipping.address1 &&
                                !!shipping.city &&
                                !!shipping.state &&
                                !!shipping.zip)" ng-click="sameAddress()" class="btn btn-success mb1">
                        Click to Use the Same Address For Billing
                    </button>
                </div>
                <div class="col-xs-12 row mb2">
                    <h3 class="col-xs-12">Billing Information</h3>
                    <!--                     School Shipping Address 1-->
                    <div class="col-xs-12 col-md-4 ">
                        <label class="control-label">Address</label>
                        <input ng-model="billing.address1" name="registration_billing_address1"
                               type="text" class="form-control col-xs-12" placeholder="" >
                    </div>
                    <!--                     School Shipping Address 2-->
                    <div class="col-xs-12 col-md-2">
                        <label class="control-label">Address 2</label>
                        <input ng-model="billing.address2" name="billing_address2"
                               type="text" class="form-control col-xs-12" placeholder="" >
                    </div>
                    <!--                     School Shipping City-->
                    <div class="col-xs-12 col-md-2">
                        <label class="control-label">City</label>
                        <input ng-model="billing.city" name="billing_city"
                               type="text" class="form-control col-xs-12" placeholder="" >
                    </div>
                    <div class="col-xs-12 col-md-2 ">
                        <label class="control-label">State</label>
                        <input ng-model="billing.state" name="billing_state"
                               type="text" class="form-control col-xs-12" placeholder="" >
                    </div>
                    <div class="col-xs-12 col-md-2">
                        <label class="control-label">Zip</label>
                        <input ng-model="billing.zip" name="billing_address2"
                               type="text" class="form-control col-xs-12" placeholder="" >
                    </div>
                </div>
                <div class="col-xs-12 control-container">
                    <button
                        class="btn btn-primary"
                        ng-click="registerUserAdmin(school, shipping, billing, user)">
                        CreateSchool
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

