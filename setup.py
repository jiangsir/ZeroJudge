import sys
import os
import fnmatch
import json
import time
import fire
import subprocess
from bs4 import BeautifulSoup

basepath = os.path.dirname(os.path.realpath(__file__))


class Setup(object):
    ''' ZeroJudge Setup
    搭配參數如下：
    install: 直接安裝並進行必要設定
    '''

    def __init__(self, offset=1):
        self._offset = offset

    def _exec2(self, cmd):
        print(basepath+"/setup.py EXEC2= " + cmd, flush=True)
        return os.system(cmd)

    def _exec(self, cmd):
        try:
            completed = subprocess.run(
                cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        except subprocess.CalledProcessError as err:
            print('ERROR:', err)
        else:
            STDOUT = completed.stdout.decode('utf-8').strip()
            STDERR = completed.stderr.decode('utf-8').strip()
            print(f'setup.py (return:{completed.returncode}) CMD:{cmd}')
            print(f'setup.py (len:{len(completed.stdout)}) STDOUT: {STDOUT}')
            print(f'setup.py (len:{len(completed.stderr)}) STDERR: {STDERR}')
            return completed.returncode, STDOUT, STDERR

    def getTomcatGroup(self, zeroaccount="zero"):
        tomcatGroup = "tomcatGroup"
        STDOUT, STDERR = self._exec('sudo -u '+zeroaccount+' groups')
        for s in STDOUT.split():
            if 'tomcat' in s:
                tomcatGroup = s
        return tomcatGroup

    def getTomcatN(self):
        tomcatN = "tomcatN"
        for file in os.listdir('/var/lib/'):
            if 'tomcat' in file:
                tomcatN = file
        return tomcatN

    def restartTomcat(self):
        #self.os_exec('sudo systemctl restart ' + self.getTomcatGroup())
        #self._exec(f'sudo service {self.getTomcatN()} restart')
        self._exec(f'sudo systemctl restart {self.getTomcatN()}')

    def _pull_tags(self, tmpdir):
        cmd = f'git --git-dir={tmpdir}/.git --work-tree={tmpdir} tag '
        print("cmd=" + cmd)
        tags = subprocess.check_output(cmd.split()).decode(
            'utf-8').rstrip().split('\n')
        count = 0
        for tag in tags:
            print(str(count) + ". tag=" + tag)
            count = count + 1
        tagindex = input("請問要取出哪一個 tag 版本？ ")
        self._exec(
            f'git --git-dir={tmpdir}/.git --work-tree={tmpdir} checkout {tags[int(tagindex)]}')
        open(tmpdir + '/WebContent/META-INF/Version.txt',
             mode='w', encoding='utf-8').write(tags[int(tagindex)])

    def _pull_latestversion(self, tmpdir):
        '''
        取得最新的 tag
        git describe --abbrev=0 --tags
        '''
        cmd = f'git --git-dir={tmpdir}/.git --work-tree={tmpdir} tag '
        code, stdout, stderr = self._exec(cmd)
        taglines = stdout.split('\n')
        print('type(taglines)=', type(taglines), taglines)

        latest_tag = []
        for tagline in taglines:
            tag = [int(x) for x in tagline.split('.')]
            if len(latest_tag) == 0 or latest_tag < tag:
                latest_tag = tag

        latest_tag_line = '.'.join(str(x) for x in latest_tag)
        # cmd = 'git --git-dir=' + tmpdir + '/.git --work-tree=' + \
        #     tmpdir + ' describe --abbrev=0 --tags'
        # latest_tag = self._exec(cmd)

        self._exec(
            f'git --git-dir={tmpdir}/.git --work-tree={tmpdir} checkout -b branch_{latest_tag_line} {latest_tag_line}')
        open(tmpdir + '/WebContent/META-INF/Version.txt',
             mode='w', encoding='utf-8').write(latest_tag_line)

    def install(self, dbpass, dbuser='root', githost='github.com', appname='ZeroJudge', version="latestversion", clean=True, SSL=False, NOPASS=False):
        gitpath = appname + "_gitclone"

        # # 從遠端 pull 回來
        if NOPASS:
            gituri = f"ssh://git@{githost}:/jiangsir/{appname}"
        else:
            gituri = f"https://{githost}/jiangsir/{appname}"

        # # 放棄本地修改 git --git-dir=ZeroJudge/.git --work-tree=ZeroJudge checkout .
        # self._exec('git --git-dir='+gitpath +
        #            '/.git --work-tree='+gitpath+' checkout .')
        # git --git-dir=ZeroJudge/.git --work-tree=ZeroJudge pull --allow-unrelated-histories https://github.com/jiangsir/ZeroJUdge
        # self._exec('git --git-dir='+gitpath+'/.git --work-tree=' +
        #            gitpath+' pull --allow-unrelated-histories ' + gituri)

        self._exec('rm -rf '+gitpath)
        result = self._exec2(f'git clone {gituri} {gitpath} --branch master')
        if result == 0:
            self._exec('rm -rf '+basepath)
            base = basepath[:basepath.rfind('/')]
            self._exec(f'mv {base}/{gitpath} {base}/{appname}')
            self._exec2(f"sudo python3 {basepath}/setup.py upgradeZeroJudge --dbuser '{dbuser}' --dbpass '{dbpass}' \
                    --githost '{githost}' --version '{version}' --clean {clean} --SSL {SSL} --NOPASS {NOPASS}")
        else:
            print('執行 git clone 失敗！')

    def upgradeZeroJudge(self, dbpass, warname=None, dbuser='root', githost='github.com', version="latestversion", clean=True, SSL=False, NOPASS=False):
        ''' 安裝/設定 ZeroJudge 系統 
        '''
        # appname = input("請輸入 git host 上的應用程式名稱: ")  # ex: ZeroJudge
        appname = "ZeroJudge"
        servername = appname+'_Server'
        dbname = appname.lower()

        apptmpdir = os.path.join("/tmp", appname)
        servertmpdir = os.path.join("/tmp", servername)

        self._exec('rm -rf ' + apptmpdir)
        self._exec('mkdir ' + apptmpdir)
        self._exec('rm -rf ' + servertmpdir)
        self._exec('mkdir ' + servertmpdir)

        if NOPASS:
            gituri = f"ssh://git@{githost}:/jiangsir/{appname}"
            servergituri = f"ssh://git@{githost}/jiangsir/{servername}"
        else:
            gituri = f"https://{githost}/jiangsir/{appname}"
            servergituri = f"https://{githost}/jiangsir/{servername}"

        #gituri = f"git@{githost}:/jiangsir/{appname}"
        self._exec(f'git clone {gituri} {apptmpdir}')

        #servergituri = f"git@{githost}:/jiangsir/{servername}"
        self._exec(f'git clone {servergituri} {servertmpdir}')

        #choose4 = input("["+appname+"] 請問要取出 1.tag 或者 2. branch：(1, 2) ")
        if version == "latestversion":
            '''
            自動取出最新版
            '''
            self._pull_latestversion(apptmpdir)
            self._pull_latestversion(servertmpdir)

            # self._exec('git --git-dir=' + servertmpdir + '/.git --work-tree=' +
            #            servertmpdir + ' checkout -b branch_'+latest_tag + ' ' + latest_tag)
            # open(servertmpdir + '/WebContent/META-INF/Version.txt',
            #      mode='w', encoding='utf-8').write(latest_tag)

        elif version == "tag":
            self._pull_tags(apptmpdir)
            self._pull_tags(servertmpdir)

            # self._exec('git --git-dir=' + servertmpdir + '/.git --work-tree=' +
            #            servertmpdir + ' checkout ' + tags[int(tagindex)])
            # open(servertmpdir + '/WebContent/META-INF/Version.txt',
            #      mode='w', encoding='utf-8').write(tags[int(tagindex)])
        elif version == "branch":
            cmd = f'git --git-dir={apptmpdir}/.git --work-tree={apptmpdir} branch -a --sort=-committerdate'
            print("cmd=" + cmd)
            branchs = subprocess.check_output(
                cmd.split()).decode('utf-8').rstrip().split('\n')
            count = 0
            for branch in branchs:
                print(str(count) + ". " + branch)
                count = count + 1
            index = int(input("請問要取出哪一個 app branch？ "))
            # branchname = branchs[index].split('/')[-1]
            branchname = branchs[index].replace('remotes/origin/', '')
            self._exec(
                f'git --git-dir={apptmpdir}/.git --work-tree={apptmpdir} checkout {branchname}')
            cmd = f'git --git-dir={apptmpdir}/.git --work-tree={apptmpdir} show-branch -g'
            message = subprocess.check_output(cmd.split()).decode('utf-8')
            print('message= ' + message)
            open(apptmpdir + '/WebContent/META-INF/Version.txt',
                 mode='w', encoding='utf-8').write(message)
            # ZeroJudge_Server
            cmd = f'git --git-dir={servertmpdir}/.git --work-tree={servertmpdir} branch -a --sort=-committerdate'
            print("cmd=" + cmd)
            branchs = subprocess.check_output(
                cmd.split()).decode('utf-8').rstrip().split('\n')
            count = 0
            for branch in branchs:
                print(str(count) + ". " + branch)
                count = count + 1
            index = int(input("請問要取出哪一個 server branch？ "))
            # branchname = branchs[int(index)].split(
            #     '/')[len(branchs[int(index)].split('/')) - 1]
            branchname = branchs[index].replace('remotes/origin/', '')

            self._exec(
                f'git --git-dir={servertmpdir}/.git --work-tree={servertmpdir} checkout {branchname}')
            cmd = f'git --git-dir={servertmpdir}/.git --work-tree={servertmpdir} show-branch -g'
            message = subprocess.check_output(cmd.split()).decode('utf-8')
            print('message= ' + message)
            open(servertmpdir + '/WebContent/META-INF/Version.txt',
                 mode='w', encoding='utf-8').write(message)

        else:
            print('version 的參數只能為 ("latestversion", "branch", "tag") ')
            sys.exit()

        # 清除所有的 BOM
        for root, dirs, files in os.walk(apptmpdir + "/src/"):
            for file in files:
                if file.endswith(".java"):
                    # print(os.path.join(root, file))
                    s = open(os.path.join(root, file), mode='r',
                             encoding='utf-8-sig').read()
                    open(os.path.join(root, file), mode='w',
                         encoding='utf-8').write(s)

        if warname == None:
            warname = 'ROOT'
        else:
            warname = input(
                "開始打包 war, 請輸入 所要使用的 App Name 。(不輸入則預設為 ROOT.war): ")
            if warname == '':
                warname = 'ROOT'

        if clean == True:
            target = 'clean makewar callpy'
        else:
            target = 'makewar callpy'

        self._exec(
            f'ant -f {apptmpdir}/build.xml {target} -Dappname={warname} -DTOMCAT_HOME=/usr/share/{self.getTomcatN()}/')
        self._exec(
            f'ant -f {servertmpdir}/build.xml -Dappname={servername} -DTOMCAT_HOME=/usr/share/{self.getTomcatN()}/')
        # self._exec("clear")

        # while int(subprocess.call(f"mysql -u {dbuser} -p'{dbpass}' -e \"USE {dbname};\"", shell=True)) != 0:
        #     dbpass = input("輸入資料庫 "+dbuser+" 密碼：")

        self._exec(
            f"python3 {apptmpdir}/build.py build --warname '{warname}' --dbuser '{dbuser}' --dbpass '{dbpass}' --SSL {SSL} --tomcatN '{self.getTomcatN()}' ")
        self._exec(
            f"python3 {servertmpdir}/build.py build --warname '{servername}' --tomcatN '{self.getTomcatN()}'")

        self._exec(f'sudo systemctl restart mysql')
        #self._exec(f'sudo systemctl restart {tomcatN}')
        #self._exec(f'sudo systemctl restart {self.getTomcatGroup()}')
        self.restartTomcat()

    def setContextxml(self, dbuser='root', dbpass='!@34ZeroJudge'):
        webappspath = f'/var/lib/{self.getTomcatN()}/webapps'
        contextpath = f'{webappspath}/ROOT/META-INF/context.xml'

        print(f"準備設定資料庫到 {contextpath}")
        with open(contextpath, 'r+', encoding='UTF-8') as context:
            print(f"開啟 {contextpath}")
            contextsoup = BeautifulSoup(context, 'xml')
            resource = contextsoup.find('Resource')
            store = contextsoup.find('Store')

            resource['username'] = dbuser
            resource['password'] = dbpass
            store['connectionName'] = dbuser
            store['connectionPassword'] = dbpass

            self._exec('true > ' + contextpath)
            context.seek(0, 0)
            context.write(str(contextsoup))

    def config(self, dbname='zerojudge', dbuser='root', dbpass='!@34ZeroJudge'):
        '''
        設定 AppConfig 通常用來開啟管理者 IP
        python3 ZeroJudge/setup.py config
        修正完畢之後必須 restartTomcat() 才行。
        '''
        while True:
            code, stdout, stderr = self._exec(
                f"export MYSQL_PWD='{dbpass}'; mysql -u{dbuser} -s -N -e \"USE {dbname}; SELECT manager_ip FROM appconfigs\"")
            if code != 0:
                dbpass = input('資料庫密碼錯誤，請輸入正確的資料庫密碼:') or dbpass
            else:
                break
        manager_ip = input(
            f'請設定 manager_ip(使用CIDR表示法，多個區段以,分隔) [default: {stdout}]:') or stdout
        self._exec(
            f"export MYSQL_PWD='{dbpass}'; mysql -u{dbuser} -e \"USE {dbname}; UPDATE appconfigs SET manager_ip='{manager_ip}'\"")

        code, stdout, stderr = self._exec(
            f"export MYSQL_PWD='{dbpass}'; mysql -u{dbuser} -s -N -e \"USE {dbname}; SELECT bannedipset FROM appconfigs\"")
        bannedipset = input(
            f'請設定 bannedipset [default: {stdout}]:') or stdout
        self._exec(
            f"export MYSQL_PWD='{dbpass}'; mysql -u{dbuser} -e \"USE {dbname}; UPDATE appconfigs SET bannedipset='{bannedipset}'\"")

        dbpass = input(f'修改 Web 的資料庫密碼設定 [default: {dbpass}]:') or dbpass
        self.setContextxml(dbuser, dbpass)

        self.restartTomcat()


if __name__ == '__main__':
    fire.Fire(Setup)
