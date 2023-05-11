<?php
use PHPUnit\Framework\TestCase;

class indexTest extends TestCase
{
    public function testLoginTrue()
    {
        interface HttpRequest
        {
            public function setOption($name, $value);
            public function execute();
            public function getInfo($name);
            public function close();
        }

        class CurlRequest implements HttpRequest
        {
            private $handle = null;

            public function __construct($url) {
                $this->handle = curl_init($url);
            }

            public function setOption($name, $value) {
                curl_setopt($this->handle, $name, $value);
            }

            public function execute() {
                return curl_exec($this->handle);
            }

            public function getInfo($name) {
                return curl_getinfo($this->handle, $name);
            }

            public function close() {
                curl_close($this->handle);
            }
        }
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

?>