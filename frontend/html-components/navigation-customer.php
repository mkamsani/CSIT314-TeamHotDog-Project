<!-- for use with <use> -->
<svg xmlns="http://www.w3.org/2000/svg" hidden>
<symbol id="arrow" viewbox="0 0 16 16">
<polyline points="4 6, 8 10, 12 6" stroke="#000" stroke-width="2" fill="transparent" stroke-linecap="round"/>
</symbol>
</svg>
<!-- In the real world, all hrefs would have go to real, unique URLs, not a "#" -->
<nav id="site-navigation" class="site-navigation" aria-label="Site Navigation">
<ul class="main-menu clicky-menu no-js">

<li>
<a href="http://localhost:8000">Home</a>
</li>

<li>
<?php echo parentLabel('#', 'Them Movies'); ?>
<ul>
<li><a href="#">Purchase a ticket</a></li>
<li><a href="#">Purchase a food combo</a></li>
<li><a href="#">Accessibility</a></li>
<li><a href="#">Content Strategy</a></li>
<li><a href="#">Training</a></li>
</ul>
</li>

<li>
<a href="#">
My Account

<svg aria-hidden="true" width="16" height="16">
<use xlink:href="#arrow"/>
</svg>
</a>
<ul>
<li><a href="#">Update my particulars</a></li>
<li><a href="#">Request for account deletion</a></li>
<li><a href="#">Request for reset password</a></li>
<li><a href="#">Logout</a></li>
</ul>
</li>

</ul>
</nav>
<script><?php include 'navigation.js'; ?></script>
<?php
/** @return string - the HTML for the parent label. */
function parentLabel($url, $label)
{
return <<<EOF
<a href="$url">$label
<svg aria-hidden="true" width="16" height="16">
<use xlink:href="#arrow"/></svg></a>
EOF;
}

?>
