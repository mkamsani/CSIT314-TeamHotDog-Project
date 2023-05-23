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

// TODO: conditional rendering of the menu items.

echo $closingBase;
}
