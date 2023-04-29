<!DOCTYPE html>
<html lang="en">
<head>
<title>Home</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width">
<!-- http://localhost:8000/index.php?theme=dark will set the color scheme to dark. -->
<!-- This is just for fun, we are not going to implement dark mode. -->
<?php $colorScheme = isset($_GET['theme']) && $_GET['theme'] === 'dark' ? 'dark' : 'light'; ?>
<meta name="color-scheme" content="<?php echo $colorScheme; ?>">
</head>
<?php
date_default_timezone_set('Asia/Singapore');
$date = date('d/m/Y', time());
$time = date('h:i:s A', time());
?>
<body>
<h1>Home</h1>
<!-- PHP should display the current time. -->
<p>Current date is <?php echo $date; ?></p>
<p>Current time is <?php echo $time; ?></p>
</body>
</html>