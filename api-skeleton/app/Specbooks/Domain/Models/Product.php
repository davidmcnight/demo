<?php
/**
 * Created by PhpStorm.
 * User: specbooks
 * Date: 10/23/18
 * Time: 10:52 AM
 */

namespace Specbooks\Domain\Models;


use Sgpatil\Orientdb\Eloquent\Model;

class Product extends Model
{
    protected $table = "Product";
    protected $connection = "oreintdb";

}