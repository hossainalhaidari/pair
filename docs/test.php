<style>
.test{width: 50px;height: 50px;border: 1px solid #000}
.cont{float: left}
</style>
<?php 

$red = array("255,0,0","235,0,0","215,0,0","195,0,0","175,0,0","155,0,0","135,0,0","115,0,0","95,0,0","85,0,0");
$green = array("0,255,0","0,235,0","0,215,0","0,195,0","0,175,0","0,155,0","0,135,0","0,115,0","0,95,0","0,85,0");
$blue = array("0,0,255","0,0,235","0,0,215","0,0,195","0,0,175","0,0,155","0,0,135","0,0,115","0,0,95","0,0,80");
$yellow = array("255,255,0","235,235,0","215,215,0","195,195,0","175,175,0","155,155,0","135,135,0","115,115,0","95,95,0","85,85,0");
$orange = array("255,165,0","235,155,0","215,145,0","195,135,0","175,125,0","155,115,0","135,105,0","115,95,0","95,85,0","85,75,0");
$pink = array("255,192,203","245,182,193","235,172,183","225,162,173","215,152,163","205,142,153","195,132,143","185,122,133","175,112,123","165,102,113");
$purple = array("128,0,128","118,0,118","108,0,108","98,0,98","88,0,88","78,0,78","68,0,68","58,0,58","48,0,48","38,0,38");
$brown = array("165,40,40","155,38,38","145,36,36","135,34,34","125,32,32","115,30,30","105,28,28","95,26,26","85,24,24","75,22,22");
$white = array("255,255,255","235,235,235","215,215,215","195,195,195","175,175,175","155,155,155","135,135,135","115,115,115","95,95,95","85,85,85");

print_color($red);
print_color($green);
print_color($blue);
print_color($yellow);
print_color($orange);
print_color($pink);
print_color($purple);
print_color($brown);
print_color($white);

$test = array('#69B4FF','#6969FF','#B4B4FF',
			  '#B4FF69','#FFB469','#69FFB4','#B469FF','#FFFF69',
			  '#FFFFB4','#6969B4','#B4B469','#FF69FF','#FFB4FF',
			  '#69FF69','#69B469','#B4FFB4','#B469B4','#FFFFFF','#696969','#B4B4B4');

foreach ($test as $value) {
	$s = hex2rgb($value);
	print_color(array($s));
}

function print_color($colors)
{
	echo '<div class="cont">';
	foreach ($colors as $color) {
		$temp = explode(',', $color);
		$r = $temp[0];
		$g = $temp[1];
		$b = $temp[2];
		echo '<div class="test" style="background: rgb('.$r.', '.$g.', '.$b.');"></div>';
	} 
	echo '</div>';
}

function hex2rgb($hex) {
   $hex = str_replace("#", "", $hex);

   if(strlen($hex) == 3) {
      $r = hexdec(substr($hex,0,1).substr($hex,0,1));
      $g = hexdec(substr($hex,1,1).substr($hex,1,1));
      $b = hexdec(substr($hex,2,1).substr($hex,2,1));
   } else {
      $r = hexdec(substr($hex,0,2));
      $g = hexdec(substr($hex,2,2));
      $b = hexdec(substr($hex,4,2));
   }
   $rgb = array($r, $g, $b);
   return implode(",", $rgb); // returns the rgb values separated by commas
   return $rgb; // returns an array with the rgb values
}