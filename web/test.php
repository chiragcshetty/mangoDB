<?php
//Step1
 $db = mysqli_connect('localhost','root','mangoDB','pets')
 or die('Error connecting to MySQL server.');
?>

<html>
 <head>
 </head>
 <body>
 <h1>PHP connect to MySQL</h1>


<?php
//Step2
$query = "SELECT * FROM cats";
mysqli_query($db, $query) or die('Error querying database.');

$result = mysqli_query($db, $query);
//$row = mysqli_fetch_array($result);

while ($row = mysqli_fetch_array($result)) {
 echo $row['name'] . ' ' . $row['owner'] . ': ' . $row['birth'] . ' ' .'<br />';
}

mysqli_close($db);
?>

</body>
</html>