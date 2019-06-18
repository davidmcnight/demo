<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 10/12/16
 * Time: 1:11 PM
 */

class Login
{

    public function __construct()
    {
        // create/read session, absolutely necessary
       
        // check the possible login actions:
        // if user tried to log out (happen when user clicks logout button)
        if (isset($_GET["logout"])) {
            $this->doLogout();
        }
        // login via post data (if user just submitted a login form)
        elseif (isset($_POST["login"])) {
            $this->dologinWithPostData();
        }
    }
    /**
     * log in with post data
     */
    public function login($schoolId, $password)
    {
        // check login form contents
//        printNicely($username);
//        printNicely($password);

        // create a database connection, using the constants from config/db.php (which we loaded in index.php)
        $db = Database::getInstance();

        // database query, getting all the info of the selected user (allows login via email address in the
        // username field)
        //$password = Encryption::ENCRYPT_PASSWORD($password);

        $query = "SELECT * FROM school WHERE id = '$schoolId' AND password = '$password'";
        $stmt = $db->query($query);
        $result = $stmt->fetch(PDO::FETCH_ASSOC);
        
        // if this user exists
        if ($result) {
            // using PHP 5.5's password_verify() function to check if the provided password fits
            // the hash of that user's password// write user data into PHP SESSION (a file on your server)
            $_SESSION['password'] = $password;
            $_SESSION['logged_in'] = 1;
            $_SESSION['school_id'] = $result["id"];
            $_SESSION['admin'] = 0;
            return true;
        } else{
            return false;
        }

    }

    public function admin_login($username, $password)
    {
        // check login form contents
//        printNicely($username);
//        printNicely($password);

        // create a database connection, using the constants from config/db.php (which we loaded in index.php)
        $db = Database::getInstance();

        // database query, getting all the info of the selected user (allows login via email address in the
        // username field)
        //$password = Encryption::ENCRYPT_PASSWORD($password);

        $query = "SELECT * FROM user WHERE username = '$username' AND password = '$password'";
        $stmt = $db->query($query);
        $result = $stmt->fetch(PDO::FETCH_ASSOC);



        // if this user exists
        if ($result) {
            // using PHP 5.5's password_verify() function to check if the provided password fits
            // the hash of that user's password// write user data into PHP SESSION (a file on your server)
            $_SESSION['logged_in'] = 1;
            $_SESSION['admin'] = 1;
            return true;
        } else{
            return false;
        }

    }


    /**
     * perform the logout
     */
    public function logout($app)
    {


        // delete the session of the user
        //$_SESSION = array();
        session_destroy();

        // return a little feeedback message
    }
    /**
     * simply return the current state of the user's login
     * @return boolean user's login status
     */
    public function isUserLoggedIn()
    {

        if (isset($_SESSION['logged_in']) AND $_SESSION['logged_in'] == 1) {
            return true;
        }
        // default return
        return false;
    }

    public function isAdmin()
    {
        if (isset($_SESSION['admin']) AND $_SESSION['admin'] == 1) {
            return true;
        }
        // default return
        return false;
    }
}