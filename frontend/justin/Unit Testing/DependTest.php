<?php

require_once "phpwebdriver/WebDriver.php";

$webdriver = new WebDriver("localhost", "4444");

$webdriver->connect("firefox");

$webdriver->get("http://google.com");
$element = $webdriver->findElementBy(LocatorStrategy::name, "q");

if ($element) {

    $element->sendKeys(array("BrowserStack") );

    $element->submit();

}

$webdriver->close();

?>