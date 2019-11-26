import os
import fnmatch
import time
import subprocess
import datetime
import sys
#import fire
import re
from bs4 import BeautifulSoup

for file in os.listdir('/etc/init.d/'):
    if fnmatch.fnmatch(file, 'tomcat*'):
        tomcatN = file

basedir = os.path.split(os.path.realpath(__file__))[0]
webappspath = '/var/lib/' + tomcatN + '/webapps/'
rsyncaccount = sys.argv[1]
CONSOLE = sys.argv[2]


def os_exec(cmd):
    print('run_cmd=', cmd)
    try:
        completed = subprocess.run(
            cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    except subprocess.CalledProcessError as err:
        print('ERROR:', err)
    else:
        print('returncode:', completed.returncode)
        print('STDOUT: {!r}'.format(len(completed.stdout)),
              completed.stdout.decode('utf-8'))
        print('STDERR: {!r}'.format(len(completed.stderr)),
              completed.stderr.decode('utf-8'))

# sudo find /ZeroJudge_CONSOLE -type d -exec chmod 777 {} \;

os_exec('sudo usermod -a -G '+ tomcatN + ' ' + rsyncaccount)
os_exec('sudo chown -R '+rsyncaccount + ':' + tomcatN + ' ' + CONSOLE)
os_exec('sudo find ' + CONSOLE + ' -type d -exec chmod 770 {} \;')
