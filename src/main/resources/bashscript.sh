
#!/bin/bash

mkdir -p /var/log/24hcodeOTA
logDir=/var/log/24hcodeOTA
DATE=`date '+%Y-%m-%d %H:%M:%S'`
echo "$DATE"
if [ -e "$logDir/naviOTA.log" ]; then
echo "in check log file"
    line=$(head -n 1 "$logDir/naviOTA.log")
    echo "$line"
    d=`echo ${line//#}`
    h=`echo $(date -d "$DATE" +%Y-%m-%d)`
    k=`echo $(date -d "$d" +%Y-%m-%d)`
echo "$d"
    if [[ "$h" > "$k" ]]; then
	for file in "$logDir"/*"-"*.log; do
#	   echo "compress $file"
	    printf 'compress %s\n' "$file"
	    if [ ! -f "$logDir/OTA-Log.zip" ]; then
		echo "create zip file"
		zip OTA-Log.zip "$file"
	   fi
	   echo "add log to zip file"
	    zip -g "$logDir/OTA-Log.zip" "$file"
	    rm  "$file"
	done
        echo "in compare"
        mv "$logDir/naviOTA.log" "$logDir/naviOTA-$k.log"
    	touch  "$logDir/naviOTA.log"
    fi
    echo "after compare"
fi

echo "###$DATE###" >> "$logDir/naviOTA.log"
cd /home/orangepi
 /usr/bin/java -jar /home/orangepi/24hcodeOTA.jar >> "$logDir/naviOTA.log"
echo "end of OTA" >> "$logDir/naviOTA.log"
