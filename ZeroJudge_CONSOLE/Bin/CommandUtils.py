import os
import subprocess
import sys


def run(cmd):
    try:
        completed = subprocess.run(
            cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    except subprocess.CalledProcessError as err:
        print('ERROR:', err)
    else:
        STDOUT = completed.stdout.decode('utf-8').strip()
        STDERR = completed.stderr.decode('utf-8').strip()
        print(f'CommandUtils.py (return:{completed.returncode}) CMD:{cmd}')
        print(f'(len:{len(completed.stdout)}) STDOUT: {STDOUT}')
        print(f'(len:{len(completed.stderr)}) STDERR: {STDERR}')
        return completed.returncode, STDOUT, STDERR


def getTomcatGroup(zeroaccount="zero"):
    tomcatGroup = "tomcat?"
    returncode, STDOUT, STDERR = run('sudo -u '+zeroaccount+' groups')
    for s in STDOUT.split():
        if 'tomcat' in s:
            tomcatGroup = s
    return tomcatGroup


def getTomcatN():
    tomcatN = "tomcatN"
    for file in os.listdir('/var/lib/'):
        if 'tomcat' in file:
            tomcatN = file
    return tomcatN


def restartTomcat(zeroaccount='zero'):
    run('sudo systemctl restart ' + getTomcatGroup(zeroaccount))


def restartMysql():
    run('sudo systemctl restart mysql')


def reboot():
    run('sudo reboot')


def whoami():
    run('whoami')
