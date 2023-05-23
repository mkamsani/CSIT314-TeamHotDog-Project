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

    <?php
//                    get monthly reports
//    $monthlyReportsCh = curl_init();
//    curl_setopt($monthlyReportsCh, CURLOPT_URL, "http://localhost:8000/api/cinemaRoom/read/allActiveCinemaRoomIds");
//    curl_setopt($monthlyReportsCh, CURLOPT_RETURNTRANSFER, 1);
//    $mReports = curl_exec($monthlyReportsCh);
//    curl_close($monthlyReportsCh);
//    $mReports = json_decode($mReports, true);
//    print_r($mReports);

    //                    get daily reports
    //    $dailyReportsCh = curl_init();
    //    curl_setopt($dailyReportsCh, CURLOPT_URL, "http://localhost:8000/api/cinemaRoom/read/allActiveCinemaRoomIds");
    //    curl_setopt($dailyReportsCh, CURLOPT_RETURNTRANSFER, 1);
    //    $dReports = curl_exec($dailyReportsCh);
    //    curl_close($dailyReportsCh);
    //    $dReports = json_decode($dReports, true);
    //    print_r($dReports);

    ?>
</tr>
</table>
</body>
</html>