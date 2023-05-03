<?php

/** @return string - HTML elements for the parent label. */
function parentLabel($label)
{
return <<<EOF
<a href="#">$label
<svg aria-hidden="true" width="16" height="16">
<polyline points="4 6, 8 10, 12 6" stroke="#000" stroke-width="2" fill="transparent" stroke-linecap="round"/>
</svg>
</a>
EOF;
}

function childLabel($label, $url)
{
$url = trim($url);
$label = trim($label);
return <<<EOF
<li><a href="$url">$label</a></li>
EOF;
}

function makeNavigation($urlCurrent)
{
$base = <<<EOF
<nav id="site-navigation" class="site-navigation" aria-label="Site Navigation">
<ul class="main-menu clicky-menu no-js">
<li><a href="http://localhost:8000">Home</a></li>
EOF;
$closingBase = <<<EOF
</ul>
</nav>
<script><?php include 'navigation.js'; /* Minified JavaScript. */ ?></script>
EOF;

echo $base;

if (strpos($urlCurrent, "/admin") !== false) {
$contents .= <<<EOF
<li>
{parentLabel('Movies')}
<ul>
<li><a href="#">Create user account</a></li>
<li><a href="#">Read user accounts</a></li>
<li><a href="#">Update user account</a></li>
<li><a href="#">Suspend user account</a></li>
</ul>
</li>

<li>
parentLabel('Movies');
<ul>
<li><a href="#">Create user profile</a></li>
<li><a href="#">Read user profiles</a></li>
<li><a href="#">Update user profile</a></li>
<li><a href="#">Suspend user profile</a></li>
</ul>
</li>


<li>
pare
<ul>
<li><a href="#">Update my particulars</a></li>
<li><a href="#">Logout</a></li>
</ul>
</li>
EOF;

} elseif (strpos($urlCurrent, "/user") !== false) {
// user page.
} elseif (strpos($urlCurrent, "/manager") !== false) {
// manager page.
} elseif (strpos($urlCurrent, "/owner") !== false) {
// owner page.
} else {
// home page, i.e. no login.
}

echo $closingBase;
}

?>

<nav id="site-navigation" class="site-navigation" aria-label="Site Navigation">
<ul class="main-menu clicky-menu no-js">

<li><a href="http://localhost:8000">Home</a></li>

</ul>
</nav>
<script><?php include 'navigation.js'; /* Minified JavaScript. */ ?></script>