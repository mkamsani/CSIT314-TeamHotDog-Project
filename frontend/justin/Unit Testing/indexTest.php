<?php
use PHPUnit\Framework\TestCase;



class indexTest extends TestCase
{



   public function testLoginTrue()
    {


        $data = array(
            'userId' => 'jim',
            'password' => 'password-employee'

        );


       $ch = curl_init();
       curl_setopt($ch, CURLOPT_URL, 'http://localhost:8000/api/admin/user-account/read/');
       curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
       $result = curl_exec($ch);
       $data = json_decode($result, true);
       $user = $data[0];
       curl_close($ch);

     }


}

?>

