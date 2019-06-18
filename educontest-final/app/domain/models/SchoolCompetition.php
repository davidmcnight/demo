<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 12/20/16
 * Time: 11:42 AM
 */



class SchoolCompetition extends \Illuminate\Database\Eloquent\Model implements IEduObject {

    protected $table = "school_competition";

    protected $fillable = ['division', 'public'];

    public function school_year(){
        return $this->belongsTo("SchoolYear");
    }

    public function competition(){
        return $this->belongsTo("competition");
    }

    public function students(){
        return $this->hasMany("student");
    }
    
    public function setAttributes($data){

        if(isset($data["id"])){
            $this->setAttribute("id", $data["id"]);
        }

        if(isset($data["school_year_id"])){
            $this->setAttribute("school_year_id", $data["school_year_id"]);
        }

        if(isset($data["competition"]["id"])){
            $this->setAttribute("competition_id", $data["competition"]["id"]);

        }

        if(isset($data["division"])){
            $this->setAttribute("division", $data["division"]);

        }

        if(isset($data["public"])){
            $this->setAttribute("public", $data["public"]);

        }

    }


}


?>


