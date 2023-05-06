<?php
?>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Page Title</title>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
</head>
<body>
<?php
// Code for update
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
$json = json_encode($_POST);
$ch = curl_init("http://localhost:8000/api/user-profile/update");
curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "PUT");
curl_setopt($ch, CURLOPT_POSTFIELDS, $json);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json'));
$result = curl_exec($ch);
curl_close($ch);
echo <<<HTML
<section>
<h2>Result</h2>
<p>$result</p>
</section>
HTML;
}
?>
<label for="title-selector">Select the user profile to update:</label>
<select name="title-selector" id="title-selector">
<?php
$ch = curl_init("http://localhost:8000/api/user-profile/read/active-user-profiles");
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
$user_profiles = curl_exec($ch); // [{"privilege":"customer","title":"customer"},...,{"privilege":"admin","title":"chief information officer"}]
curl_close($ch);
$user_profiles = json_decode($user_profiles, true);
foreach ($user_profiles as $user_profile) {
if ($user_profile['title'] === 'customer') continue;
$titleCapitalized = ucwords($user_profile['title']);
$privilegeCapitalized = ucwords($user_profile['privilege']);
// if {$user_profile['title']}, move to the next iteration
echo <<<EOF
<option value="{$user_profile['title']}">$privilegeCapitalized: $titleCapitalized</option>\n
EOF;
}
?>
</select>
<form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST" style="display: grid; grid-template-columns: 1fr 4fr 6fr; grid-gap: 1rem;">
<label>Title:</label>
<input  type="text" required placeholder="Original Value" id="title" name="target-title"  disabled>
<input  type="text" required placeholder="Updated Value"             name="title">
<label>Privilege:</label>
<input  type="text" required placeholder="Original Value" id="privilege"  disabled>
<input  type="text" required placeholder="Updated Value"  name="privilege">
<button type="submit">Submit</button>
</form>
<script>
const titleSelector = document.getElementById('title-selector');
let title = document.getElementById('title');
// We add an event selector so that titleSelector changes, we perform a GET request to the API to get the title and then we set the value of the title input to the title we got from the API.
titleSelector.addEventListener('change', () => {
    // find from the using query select an option with the value of titleSelector.value
    // set the value of title to the value of the option
    title.value = titleSelector.value;
    // do a for loop across all <option tags>
    for (let i = 0; i < titleSelector.options.length; i++) {
        if (titleSelector.options[i].value === titleSelector.value) {
            // if the value of the option is equal to titleSelector.value
            // log its label also
            let privilege = titleSelector.options[i].label;
            privilege = privilege.substring(0, privilege.indexOf(':')).toLowerCase();
            console.log(titleSelector.options[i].value);
            console.log(privilege);
            document.getElementById('privilege').placeholder = privilege;
        }
    }
});
</script>
</body>
</html>