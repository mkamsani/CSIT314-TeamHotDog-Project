<nav id="site-navigation" class="site-navigation" aria-label="Site Navigation">
<ul class="main-menu clicky-menu no-js">

<li><a href="http://localhost:8000">Home</a></li>

<li>
<?php echo parentLabel('Movies'); ?>
<ul>
<li><a href="#">Purchase a ticket</a></li>
<li><a href="#">Add a rating and review</a></li>
</ul>
</li>

<li>
<?php echo parentLabel('My Account'); ?>
<ul>
<li><a href="#">Update my particulars</a></li>
<li><a href="#">View my tickets</a></li>
<li><a href="#">View my loyalty points</a></li>
<li><a href="#">View my food orders</a></li>
<li><a href="#">Request for account deletion</a></li>
<li><a href="#">Request for reset password</a></li>
<li><a href="#">Logout</a></li>
</ul>
</li>

</ul>
</nav>
<script><?php include 'navigation.js'; /* Minified JavaScript. */ ?></script>

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