#!/bin/sh

echo "<?xml version=\"1.0\" encoding=\"utf-8\"?>" > Grids.xml
echo "<gridlist>" >> Grids.xml
for file in Grids/*.xml ; do
	name=`echo $file | cut -d '/' -f 2`
	echo "<grid>$name</grid>" >> Grids.xml
done
echo "</gridlist>" >> Grids.xml
