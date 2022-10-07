#!/usr/bin/python3
import os
import subprocess
import sys

basedir = os.path.split(os.path.realpath(__file__))[0]
#webappspath = '/var/lib/' + tomcatN + '/webapps/'
rsyncaccount = sys.argv[1]
CONSOLE = sys.argv[2]


def run(cmd):
    print('run_cmd=', cmd)
    try:
        completed = subprocess.run(
            cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    except subprocess.CalledProcessError as err:
        print('ERROR:', err)
    else:
        STDOUT = completed.stdout.decode('utf-8').strip()
        STDERR = completed.stderr.decode('utf-8').strip()
        print(f'InitializedListener.py (return:{completed.returncode}) CMD:{cmd}')
        print(f'(len:{len(completed.stdout)}) STDOUT: {STDOUT}')
        print(f'(len:{len(completed.stderr)}) STDERR: {STDERR}')
        return completed.returncode, STDOUT, STDERR


def getTomcatGroup(zeroaccount="zero"):
    tomcatGroup = "tomcatGroup"
    returncode, STDOUT, STDERR = run(f'sudo -u {zeroaccount} groups')
    for s in STDOUT.split():
        if 'tomcat' in s:
            tomcatGroup = s
    return tomcatGroup


# sudo find /ZeroJudge_CONSOLE -type d -exec chmod 777 {} \;
tomcatGroup = getTomcatGroup()

run('whoami')
run('sudo usermod -a -G ' + tomcatGroup + ' ' + rsyncaccount)
run('sudo chown -R '+rsyncaccount + ':' + tomcatGroup + ' ' + CONSOLE)
run('sudo find ' + CONSOLE + ' -type d -exec chmod 770 {} \;')
# find /ZeroJudge_CONSOLE -type f -exec chmod 660 {} \;
