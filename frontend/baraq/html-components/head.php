<?php
function head($title, $description)
{
return <<<EOF
<title>$title</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="icon" type="image/png" sizes="16x16" href="/favicon-16x16.png">
<link rel="icon" type="image/png" sizes="32x32" href="/favicon-32x32.png">
<link rel="manifest" href="/site.webmanifest">
<meta name="title" content="$title">
<meta name="description" content="$description">
EOF;
}