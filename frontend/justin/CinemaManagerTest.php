<html lang="">
<head>
<title></title> 
<meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
</head>
<body>
<table ="100%" style="border: 0">
<tr>
    <td><h1>CinemaManager Test</h1></td>
</tr>
</table>
<hr />
<?php
$moviesCh = curl_init();

curl_setopt($moviesCh, CURLOPT_URL, "http://localhost:8000/api/movie/read/allMovieTitles");
curl_setopt($moviesCh, CURLOPT_RETURNTRANSFER, 1);
$movies = curl_exec($moviesCh);
echo $movies;
curl_close($moviesCh);

$moviesDetailsCh = curl_init();

curl_setopt($moviesDetailsCh, CURLOPT_URL, "http://localhost:8000/api/movie/read/allMoviesDetails");
curl_setopt($moviesDetailsCh, CURLOPT_RETURNTRANSFER, 1);
$moviesDetails = curl_exec($moviesDetailsCh);
echo $moviesDetails;
curl_close($moviesDetailsCh);

?>






</body>
</html>