<?php

/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 11/30/16
 * Time: 11:17 AM
 */
class SchoolYear extends \Illuminate\Database\Eloquent\Model implements IEduObject{

    protected $table = "school_year";

    protected $fillable = ['amount_paid', 'invoice_number'];

    public function school(){
        return $this->belongsTo('School');
    }

    public function school_competitions(){
        return $this->hasMany('SchoolCompetition');
    }


    public function school_competitions_pdf(){
        return $this->hasMany('SchoolCompetitionPDF');
    }

    public function school_tests(){
        return $this->hasMany('SchoolTest');
    }

    public function year(){
        return $this->belongsTo('Year');
    }

    public function setAttributes($data){
        
        if(isset($data["id"])){
            $this->setAttribute("id", $data["id"]);
        }
        
        if(isset($data["school"]["id"])){
            $this->setAttribute("school_id", $data["school"]["id"]);
        }
        
        if(isset($data["year"]["id"])){
            $this->setAttribute("year_id", $data["year"]["id"]);
        }

        if(isset($data["invoice_number"])){
            $this->setAttribute("invoice_number", $data["invoice_number"]);
        }
    }
}