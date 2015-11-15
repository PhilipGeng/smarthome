<?
	$fd = fopen("status.txt","w+");
	echo fwrite($fd,"light on");
	fclose($fd);
?>