<!DOCTYPE html>
<html lang="en">
<?php
?>
<head>



</head>
<body>
<nav class="navtop">
    <div>
        <h1>Reviews System</h1>
    </div>
</nav>
<div class="content home">
    <h2>Reviews</h2>
    <p>Take some time to review the movie you just watched.</p>
    <form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST" class="reviews">
    <textarea id="movieReview" name="movieReview" rows="4" cols="50">
    </textarea>
    <br>
    <input type="submit" name="submit" value="submit">
    <input type="submit" name="back" value="back">
    </form>
</div>
</body>
</html>
<?php
if(isset($_POST['submit'])){
$review = $_POST['movieReview'];
echo $review;
}
if(isset($_POST['back'])) {
	header("Location: movies.php");
}


//$reviewCh = curl_init();
//$review = $_POST['review'];
//$data = array('review' => $review);
//$data_json = json_encode($data);
//print_r($data_json);
//$reviewCh = curl_init("http://localhost:8000/api/ticketType/create/ticketType");
//curl_setopt($reviewCh, CURLOPT_POST, "1");
//curl_setopt($reviewCh, CURLOPT_POSTFIELDS, $data_json);
//curl_setopt($reviewCh, CURLOPT_RETURNTRANSFER, 1);
//curl_setopt($reviewCh, CURLOPT_HTTPHEADER, array('Content-Type:application/json'));
//
//$response = curl_exec($reviewCh);
//curl_close($reviewCh);
?>

