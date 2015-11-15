<?
	$fd = fopen("status.txt","w+");
	echo fwrite($fd,"light off");
	fclose($fd);
?>