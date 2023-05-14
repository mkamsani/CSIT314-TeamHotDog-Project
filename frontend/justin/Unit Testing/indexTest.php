<?php
use PHPUnit\Framework\TestCase;

class indexTest extends TestCase
{
    private $handle = null;

    public function testLoginTrue()
    {
        // create our http client (Guzzle)
        $client = new Client('http://localhost:8000', array(
            'request.options' => array(
                'exceptions' => false,
            )
        ));

        $data = array(
            'userId' => 'jim',
            'password' => 'password-employee'

        );

        $request = $client->post('/api/user-account/login', null, json_encode($data));
        $response = $request->send();

        $this->assertEquals(201, $response->getStatusCode());
        $this->assertTrue($response->hasHeader('Location'));
        $data = json_decode($response->getBody(true), true);
        $this->assertArrayHasKey('nickname', $data);

     }


}

?>
