<?php
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, "http://localhost:8000/api/customer/screening/read/all");
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
$screenings = curl_exec($ch); // e.g. {"movie":"Spider-Man","showTime":"afternoon","showDate":"2023-01-01","cinemaRoom":42}
$screenings = json_decode($screenings, true);
$moviesDistinct = array();
foreach ($screenings as $screening) {
if (!in_array($screening['movie'], $moviesDistinct))
$moviesDistinct[] = $screening['movie'];
}
sort($moviesDistinct);
curl_close($ch);
?>

<!DOCTYPE html>
<html lang="en">
<head>
<title>HOTDOG CINEMAS</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3.org/StyleSheets/Core/Steely" type="text/css">
</head>
<body>
<header>HOTDOG CINEMAS</header>
<main>

<label for="movie">Movie</label>
<select name="movie" id="movie">
<option value="">Select a movie</option>
<?php
foreach ($moviesDistinct as $movie) echo sprintf("<option value=\"%s\">%s</option>", $movie, $movie);
?>
</select>
<p id="placeholder">Available screenings will be displayed below after you select a movie.</p>

<?php
foreach ($screenings as $screening) {

// A hidden field is sent in the POST request to the next page.
// A disabled field is not sent in the POST request to the next page.
// The disabled field is used to display the values of the hidden field in a readable format.

echo sprintf('<form hidden="hidden" action="%s" method="POST" class="%s">', $_SERVER['PHP_SELF'], $screening['movie']);
echo sprintf('<input hidden="hidden" readonly name="movie" value="%s">', $screening['movie']);

$showDateReadable = date_create($screening['showDate'])->format('d M (D)');
echo sprintf('<input hidden="hidden" readonly name="showDate" value="%s">&nbsp;', $screening['showDate']);
echo sprintf('<input disabled name="showDateReadable" value="%s">&nbsp;', $showDateReadable);

echo sprintf('<input hidden="hidden" readonly name="showTime" value="%s">&nbsp;', $screening['showTime']);
echo sprintf('<input disabled name="showTimeCapitalized" value="%s">&nbsp;', ucfirst($screening['showTime']));

echo sprintf('<input hidden="hidden" readonly name="cinemaRoom" value="%s">&nbsp;', $screening['cinemaRoom']);
echo sprintf('<input disabled name="cinemaRoomReadable" value="Cinema Room %s">&nbsp;', $screening['cinemaRoom']);

$url = "http://localhost:8000/api/customer/screening/seats/read/{$screening['showDate']}/{$screening['showTime']}/{$screening['cinemaRoom']}";
$ch = curl_init($url); // e.g. {"row":"A","column":"1","concat":"A1"}
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
$seats = curl_exec($ch);
curl_close($ch);
$seats = json_decode($seats, true);
echo sprintf('<input hidden="hidden" readonly name="totalSeats" value="%s">&nbsp;', count($seats));
echo sprintf('<input disabled name="totalSeatsReadable" value="%s seats available.">&nbsp;', count($seats) === 280 ? 'All' : count($seats));
echo sprintf('<select name="seatNumber">');
foreach ($seats as $seat) echo sprintf("<option value=\"%s\">%s</option>", $seat['concat'], $seat['concat']);
echo sprintf('</select>');

echo sprintf('&nbsp;<input type="submit" name="action" value="Proceed to purchasing">');
echo sprintf('</form>');
}

?>
</main>
<script>
// This script is used to show the form for the selected movie.
document.getElementById('movie').addEventListener(
    'change', (event) => {
        document.querySelectorAll('form').forEach(
            (form) => form.hidden = form.className !== event.target.value
        );
        document.getElementById('placeholder').hidden = document.getElementById('movie').value !== '';
    }
);
</script>
</body>
</html>