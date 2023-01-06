#!/usr/bin/python3
import sys
import CommandUtils

rsyncAccount = sys.argv[1]
sshPort = sys.argv[2]
specialPath = sys.argv[3]
isloopback = sys.argv[4]
server_account = sys.argv[5]
server_host = sys.argv[6]
server_specialPath = sys.argv[7]

# rsync += RunCommand.Command.rsync + " --chmod=Du=rwx,Dg=rwx,Fu=rw,Fg=rw -av --delete -e \""
# 				+ RunCommand.Command.ssh + " -p " + appConfig.getServerConfig().getSshport() + "\" "
# 				+ appConfig.getSpecialPath(problem.getProblemid()) + " ";

cmd = f'sudo -u {rsyncAccount} '
cmd += f'rsync --chmod=Du=rwx,Dg=rwx,Fu=rw,Fg=rw -av --delete -e "ssh -p {sshPort}" {specialPath} '
if not bool(isloopback):
    cmd += f'{server_account}@{server_host}:'
cmd += server_specialPath

CommandUtils.run(cmd)
