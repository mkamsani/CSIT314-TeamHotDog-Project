<?php
session_start();
?>
<html>
<head>
<title></title> 
<meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
</head>
<body>
<table width="100%" style="border: 0">
<tr>
    <td><h1>Monthly Report</h1></td>
	<?php
	echo "<table border='1' table  class = 'subjects'>\n";
		echo "<tr><th>Date From</th>" .
				"<th>Date To</th>" .
				"<th>Monthly revenue</th>" .
				"<th>Expediture</th></tr>\n";
		/* while (($Row = mysqli_fetch_assoc($result)) != FALSE) {
			echo "<tr><td>{$Row['SubjectCode']}</td>";
			echo "<td>{$Row['Name']}</td>";
			echo "<td>{$Row['Lecturer']}</td>";
			echo "<td>{$Row['Venue']}</td></tr>\n";
		}; */
		echo "</table>\n";
		?>
</tr>
</table>
</body>
</html>