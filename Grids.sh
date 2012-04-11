#!/bin/sh

echo "<?xml version=\"1.0\" encoding=\"utf-8\"?>" > Grids.xml
echo "<gridlist>" >> Grids.xml
for file in Grids/*.xml ; do
	echo "<grid>$file</grid>" >> Grids.xml
done
echo "</gridlist>" >> Grids.xml
