<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 1/17/17
 * Time: 8:42 AM
 */

class Student extends \Illuminate\Database\Eloquent\Model implements IEduObject{
    
    
    protected $table = "student";
    
    

    public function schoolCompetition(){
        return $this->belongsTo("SchoolCompetition");
    }
    
    public function setAttributes($data){
        if(isset($data["id"])){
            $this->setAttribute("id", $data["id"]);
        }

        if(isset($data["school_competition_id"])){
            $this->setAttribute("school_competition_id", $data["school_competition_id"]);
        }

        if(isset($data["public"])){
            $this->setAttribute("public", $data["public"]);
        }

        if(isset($data["name"])){
            $this->setAttribute("name", $data["name"]);
        }

        if(isset($data["test1_score"])){
            $this->setAttribute("test1_score", $data["test1_score"]);
        }

        if(isset($data["test2_score"])){
            $this->setAttribute("test2_score", $data["test2_score"]);
        }

        if(isset($data["test3_score"])){
            $this->setAttribute("test3_score", $data["test3_score"]);
        }

        if(isset($data["test4_score"])){
            $this->setAttribute("test4_score", $data["test4_score"]);
        }


    }


}


?>


