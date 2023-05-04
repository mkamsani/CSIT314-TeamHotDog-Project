<!DOCTYPE html>
<html lang="en">

<?php
session_start();
include('header.php');
include('idx_nav.php');
?>

<body>
<div class="container-fluid p-5 bg-primary text-white text-center">
    <h1>Registration</h1>
</div>

<<?php
// DB Config
$DB_Username = 'root';
$DB_Password = '';
$DB = 'isit307a2';

$link = new mysqli('localhost', $DB_Username, $DB_Password, $DB) or die("Unable to connect to server.");

// Variable, Error and Validation Defaults
$userId = $name = $surname = $phone = $email = $type = $position = "";
$userIdErr = $nameErr = $surnameErr = $phoneErr = $emailErr = $typeErr = $posErr = "";
$userIdValidated = $nameValidated = $surnameValidated = $phoneValidated = $emailValidated = $typeValidated = $posValidated = false;

if ($_SERVER["REQUEST_METHOD"] == "POST")
{
    // User ID Validation
    if (empty(strtoupper($_POST["userId"])))
    {
        $userIdErr = "User ID is required!";
    }
    elseif (!(preg_match('/AD\d\d\d/', strtoupper($_POST["userId"])) || preg_match('/ED\d\d\d/', strtoupper($_POST["userId"])) || preg_match('/ST\d\d\d/', strtoupper($_POST["userId"]))))
    {
        $userIdErr = "Incorrect User ID Format!";
    }
    else
    {
        $userId = test_input(strtoupper($_POST["userId"]));
        $userIdValidated = true;
    }

    // Name Validation
    if (empty($_POST["name"])) {
        $nameErr = "Name field is required!";
    }
    elseif (!preg_match("/^[a-zA-Z-' ]*$/", $_POST["name"]))
    {
        $nameErr = "Only letters and white spaces are allowed!";
    }
    else
    {
        $name = test_input(strtoupper($_POST["name"]));
        $nameValidated = true;
    }

    // Surname Validation
    if (empty($_POST["surname"]))
    {
        $surnameErr = "Last name field is required!";
    } elseif (!preg_match("/^[a-zA-Z-' ]*$/", $_POST["surname"]))
    {
        $surnameErr = "Only letters and white spaces are allowed!";
    }
    else
    {
        $surname = test_input(strtoupper($_POST["surname"]));
        $surnameValidated = true;
    }

    // Phone Number Validation
    if (empty($_POST["phone"]))
    {
        $phoneErr = "Phone Number field is required!";
    }
    elseif (!preg_match('/^[0-9]{8}$/', $_POST["phone"]))
    {
        $phoneErr = "Phone number can only be 8 digits long and contain digits only.";
    }
    else
    {
        $phone = test_input($_POST["phone"]);
        $phoneValidated = true;
    }

    // Email Validation
    if (empty($_POST["email"]))
    {
        $emailErr = "Email is required!";
    }
    
    elseif (!filter_var($_POST["email"], FILTER_VALIDATE_EMAIL))
    {
        $emailErr = "Invalid email format!";
    }
    else
    {
        $email = test_input($_POST["email"]);
        $emailValidated = true;
    }

    // Type Validation
    if (empty($_POST["type"]))
    {
        $typeErr = "Condition field is required!";
    } else
    {
        $type = test_input($_POST["type"]);
        $typeValidated = true;
    }

    if ($_POST["type"] == "ADMIN")
    {
        if (isset($_POST["position"]) && !empty($_POST["position"]))
        {
            $position = test_input(strtoupper($_POST["position"]));
            $posValidated = true;
        }
        else
        {
            $posErr = "Position field is required!";
            $posValidated = false;
        }
    }
    else
    {
        $posValidated = true;
    }

}

    // Connect to Database
    if ($userIdValidated && $nameValidated && $surnameValidated && $phoneValidated && $emailValidated && $typeValidated && $posValidated == true)
    {

        $typeValidated = mysqli_real_escape_string($link, $type);
        $userId = mysqli_real_escape_string($link, $userId);
        $name = mysqli_real_escape_string($link, $name);
        $surname = mysqli_real_escape_string($link, $surname);
        $phone = mysqli_real_escape_string($link, $phone);
        $email = mysqli_real_escape_string($link, $email);
        $position = mysqli_real_escape_string($link, $position);
        

        $sql_if_prodId_exists = "SELECT * FROM user WHERE UserId = '$userId'";
        $result = mysqli_query($link, $sql_if_prodId_exists);
        $row = mysqli_fetch_array($result, MYSQLI_ASSOC);

        if (mysqli_num_rows($result) == 1)
        {
            $userIdErr = "User ID is already taken. Please try again.";
        }
        else
        {
            $sql = "INSERT INTO user (UserId, FirstName, LastName, Phone, Email, Type, Position) 
                        VALUES ('$userId', '$name', '$surname', '$phone', '$email', '$type', '$position')";

            if ($link->query($sql) === TRUE)
            {
                echo '
                    <div class="container mt-5">
                        <div class="alert alert-success mb-3 mt-3" id="successMsg" style="width: 75%;">
                        <strong>Success!</strong> You have been successfully registered. Please <a href="index.php" class="alert-link">Log in</a>.
                        </div>
                    </div>';
            } else {
                echo "Error: " . $sql . "<br>" . $link->error;
            }
        }
    }
    $link->close();

function test_input($data)
{
    $data = trim($data);
    $data = stripslashes($data);
    return htmlspecialchars($data);
}
?>
<script>
    function enable_position()
    {
        var type = document.getElementById("type").value;

        if (type == "ADMIN")
        {
            document.getElementById("position").disabled = false;
        }

        else document.getElementById("position").disabled = true;
    }

</script>
<div class="container mt-4" style="margin-left: 20%">
    <form class='form-wrapper mx-5' novalidate action="<?php echo $_SERVER["PHP_SELF"]; ?>"
          method='POST' style="width: 40%">
        <div>
            <a href="index.php" class="btn btn-primary">Return</a>
        </div>
        <h1 class="mt-3">User Details</h1>
        <div class="mt-3">
            <label for="type" class="form-label">Type: </label>
            <select class="form-select needs-validation" required id="type" name="type" onchange="enable_position()">
                <option selected disabled value="">
                    <-- Please Select -->
                </option>
                <option value="ADMIN">ADMINISTRATOR</option>
                <option value="EDUCATOR">EDUCATOR</option>
                <option value="STUDENT">STUDENT</option>
            </select>
        </div>
        <span class="error" style="color:red"><?php echo $typeErr ?></span>
        <div class="mt-3">
            <label for="userId" class="form-label">User ID: </label>
            <input type="text" class="form-control" id="userId" name="userId" maxlength="5"
                   placeholder="Format: AD123 for Admins, ED321 for Educators or ST123 for Students"
                   style="text-transform: uppercase" required>
        </div>
        <span class="error" style="color:red"><?php echo $userIdErr ?></span>
        <div class="mt-3">
            <label for="name" class="form-label">Name:</label>
            <input type="text" class="form-control" id="name" name="name" style="text-transform: uppercase" placeholder="Example: Shawn" required>
        </div>
        <span class="error" style="color:red"><?php echo $nameErr ?></span>
        <div class="mt-3">
            <label for="surname" class="form-label">Last Name: </label>
            <input type="text" class="form-control" id="surname" name="surname" style="text-transform: uppercase" placeholder="Example: Lim" required>
        </div>
        <span class="error" style="color:red"><?php echo $surnameErr ?></span>
        <div class="mt-3">
            <label for="phone" class="form-label">Phone Number: </label>
            <input type="text" maxlength="8" class="form-control" id="phone" name="phone" placeholder="Example: 97532468" required>
        </div>
        <span class="error" style="color:red"><?php echo $phoneErr ?></span>
        <div class="mt-3">
            <label for="email" class="form-label">Email: </label>
            <input type="email" class="form-control" id="email" name="email" placeholder="Example: seanisso@gmail.com"
                   required>
        </div>
        <span class="error" style="color:red"><?php echo $emailErr ?></span>
        <div class="mt-3">
            <label for="position" class="form-label">Position: </label>
            <input type="text" class="form-control" id="position" name="position" style="text-transform: uppercase" placeholder="For Admins">
        </div>
        <span class="error" style="color:red"><?php echo $posErr ?></span>
        <div class="mt-3">
            <input class="btn btn-primary" type="submit" name="submit" value="Register">
        </div>
    </form>
</div>

</body>
<?php include('footer.php') ?>
</html>