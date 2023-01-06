import CommandUtils

returncode, STDOUT, STDERR = CommandUtils.run("uptime | egrep -o 'load average[s]*: [0-9,\. ]+' | awk -F',' '{print $1$2$3}' | awk -F' ' '{print $3,$4,$5}'")
