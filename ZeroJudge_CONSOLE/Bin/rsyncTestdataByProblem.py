#!/usr/bin/python3
import sys
import CommandUtils

rsyncAccount = sys.argv[1]
sshPort = sys.argv[2]
testdataPath = sys.argv[3]
isLoopback = sys.argv[4]
server_rsyncAccount = sys.argv[5]
serverHost = sys.argv[6]
server_TestdataPath = sys.argv[7]

cmd = ''
# cmd += f'sudo '
# cmd += f'sudo -u {rsyncAccount} '
cmd += f'rsync -av --delete -e "ssh -p {sshPort}" '
cmd += f'{testdataPath} '
if not isLoopback:
    cmd += f'{server_rsyncAccount}@{serverHost}:'
cmd += f'{server_TestdataPath}'

CommandUtils.run(cmd)
