<?php

/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 10/3/16
 * Time: 3:59 PM
 */
class Encryption{

    public static function ENCRYPT_PASSWORD($password){
        return base64_encode(mcrypt_encrypt(MCRYPT_RIJNDAEL_256, md5(ENCRYPTION_KEY),
            $password, MCRYPT_MODE_CBC, md5(md5(ENCRYPTION_KEY))));
    }

    public static function DECRYPT_PASSWORD($encrypted){
        return rtrim(mcrypt_decrypt(MCRYPT_RIJNDAEL_256,
            md5(ENCRYPTION_KEY), base64_decode($encrypted),
            MCRYPT_MODE_CBC, md5(md5(ENCRYPTION_KEY))), "\0");
    }

}