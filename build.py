#!/usr/bin/python3
import os
import fnmatch
import time
import subprocess
import datetime
import sys
import fire
from bs4 import BeautifulSoup

for file in os.listdir('/etc/init.d/'):
    if fnmatch.fnmatch(file, 'tomcat*'):
        tomcatN = file

basedir = os.path.split(os.path.realpath(__file__))[0]
webappspath = '/var/lib/' + tomcatN + '/webapps/'


class ZeroJudgeBuild(object):
    ''' ZeroJudge Build
    搭配參數如下：
    Build: 建立與升級資料庫
    '''
    ##########################################################################

    def os_exec(self, cmd):
        print('cmd=' + cmd)
        os.system(cmd)

    def importSQL(self, dbname, dbuser):
        print("sqlfile 列表：")
        sqllist = []
        for sqlfile in os.listdir(basedir):
            if fnmatch.fnmatch(sqlfile, '*.sql'):
                sqllist.append(sqlfile)

        for sqlfile in sqllist:
            print(str(sqllist.index(sqlfile,)) + ". " + sqlfile)
        print(str(len(sqllist)) + ". exit")

        index = input("請選擇要匯入的資料庫檔案 *.sql？ ")
        if index == str(len(sqllist)):
            return False

        # dbname = input("準備匯入 " + sqllist[int(index)] + ", 請輸入資料庫名稱：")
        # cmd = 'cat ' + apptmpdir + '/' + sqlfile + ' | mysql -u root -p'
        # cmd = 'mysql -u root -p ' + dbname + ' < ' + apptmpdir + '/' + sqlfile
        # print(cmd)
        # os.system(cmd)

        print("匯入資料表到此資料庫: ")
        exec("mysql -u" + dbuser + " -p " +
             dbname + " < " + basedir + "/" + sqlfile)

        return True

    ##########################################################################
    ##########################################################################

    ''' while True:
        choose5 = input("準備建立資料庫，請注意，若選擇建立資料庫，會現有資料庫備份後，建立新資料庫。請問是否重建資料庫？(Y/n) ")
        if choose5 == 'Y':
            #dbname = appname.lower()
            dbname = 'zerojudge' # 已經在 build.py 部署，直接指定 dbname 了
            print("即將「備份」現存資料庫，是否有已存在的資料庫(" + dbname + ")")
            print("Y. 若已存在則備份後刪除。")
            print("n. 若新安裝的系統尚未有資料庫，則直接建立資料庫。")
            choose6 = input("是否存在 (" + dbname + ")？(Y/n) ")
            if choose6 == 'Y':
                # 如果有現存的資料庫，就備份下來。
                dbname_backup = dbname + "_" + datetime.datetime.now().strftime('%Y%m%d-%H%M%S')
                print("匯出原資料庫： ")
                os_exec("mysqldump -hlocalhost -uroot -p  " + dbname + "  > " + dbname_backup + ".sql")
                print("建立一個備份資料庫: ")
                os_exec("mysqladmin -u root -p create " + dbname_backup)
                print("匯入資料到此資料庫: ")
                os_exec("mysql -hlocalhost -uroot -p " +
                    dbname_backup + " < " + dbname_backup + ".sql")
                print("刪除原資料庫： ")
                os_exec("mysqladmin -u root -p drop " + dbname)

            # 開始刪除原資料庫
            print("建立一個資料庫: ")
            os_exec("mysqladmin -u root -p create " + dbname)

            while True:
                if not importSQL(dbname):
                    break
            break
        elif choose5 == 'n':
            print("放棄重建資料庫。")
            break
    '''

    def build(self, warname, dbpass, dbname="zerojudge", dbuser="root", SSL=False):
        self.os_exec('/etc/init.d/' + tomcatN + ' restart')
        self.os_exec('sudo rm -rf /var/lib/' + tomcatN +
                     '/webapps/' + warname + '/')
        self.os_exec('sudo cp ' + basedir + '/' + warname +
                     '.war ' + webappspath + warname + '.war')
        contextpath = webappspath + warname + '/META-INF/context.xml'
        #contextpath = os.path.join(webappspath, warname, '/META-INF/context.xml')
        print("Waiting", end="")
        while not os.path.isfile(contextpath):
            print(".", end="")
            time.sleep(1)
        print()

        if os.path.exists("/ZeroJudge_CONSOLE/"):
            #    CONSOLE_backup = "ZeroJudge_CONSOLE_" + datetime.datetime.now().strftime('%Y%m%d-%H%M%S')
            #    print("注意！！ 已經存在一份原來的 /ZeroJudge_CONSOLE/ 是否要備份到 " + CONSOLE_backup)
            #    print("Y. 備份下來後，換成新的沒有任何測資的 CONSOLE")
            print("n. ZeroJudge_CONSOLE 已存在。保留原來的 CONSOLE 不做任何動作。")
        #    choose4 = input("請選擇？(Y/n) ")
        #    if choose4 == 'Y':
        #        os.system("mv /ZeroJudge_CONSOLE/ /" + CONSOLE_backup)
        #        os.system('mv ' + path + '/CONSOLE /ZeroJudge_CONSOLE')
        else:
            self.os_exec('mv ' + basedir + '/ZeroJudge_CONSOLE/ /')
            self.os_exec('chown -R ' + tomcatN + ':' +
                         tomcatN + ' /ZeroJudge_CONSOLE')

        # check if war file 是否已經完全解開。
        #    break
        # if choose3 == 'n':
        #    sys.exit()

        # if dbpass == None:
        #    dbpass = input("輸入資料庫 root 密碼：")

        # returncode = -1
        # while int(returncode)!=0:
        #     dbpass = input("輸入資料庫 root 密碼：")
        #     returncode = subprocess.call("mysql -u root -p'"+dbpass+"' -e \"USE "+dbname+";\"", shell=True)
        while int(subprocess.call("mysql -u "+dbuser+" -p'"+dbpass+"' -e \"USE "+dbname+";\"", shell=True)) != 0:
            dbpass = input("輸入資料庫 "+dbuser+" 密碼：")

        print("建立一個新資料庫(" + dbname + "): ")
        self.os_exec("mysql -u "+dbuser+" -p'" + dbpass +
                     "' -e \"CREATE DATABASE IF NOT EXISTS " + dbname + ";\"")
        # importSQL(dbname)

        print("匯入  完整資料表到資料庫(" + dbname + "): ")
        schemasql = ""
        for filename in os.listdir(basedir):
            if fnmatch.fnmatch(filename, 'Schema_*.sql'):
                schemasql = filename
        if schemasql != "":
            self.os_exec("mysql -u"+dbuser+" -p'" + dbpass + "' " +
                         dbname + " < " + basedir + "/" + schemasql)

        schemaupdatesql = ""
        for filename in os.listdir(basedir):
            if fnmatch.fnmatch(filename, 'SchemaUpdate_*.sql'):
                schemaupdatesql = filename
        if schemaupdatesql != "":
            #choose = input("備份原資料庫嗎？ (Y/n) ")
            # if choose == 'Y':
            dbname_backup = dbname + "_" + datetime.datetime.now().strftime('%Y%m%d-%H%M%S')
            print("進行資料表備份：匯出原資料庫 到 '" + dbname_backup + "'： ")
            self.os_exec("mysqldump -hlocalhost -u"+dbuser+" -p'" + dbpass +
                         "'  " + dbname + "  > /tmp/" + dbname_backup + ".sql")
            #print("建立一個備份資料庫: ")
            #os_exec("mysqladmin -u root -p'"+dbpass+"' create " + dbname_backup)
            #print("匯入資料到此資料庫: ")
            #os_exec("mysql -hlocalhost -uroot -p'"+dbpass+"' " + dbname_backup + " < " + dbname_backup + ".sql")

            print("資料庫(" + dbname + ") 進行資料表升級: ")
            sql = open(basedir + "/" + schemaupdatesql, 'r', encoding='UTF-8')
            for line in sql.readlines():
                line = line.strip()
                line = line.replace('\"', '\'')
                line = line.replace('`', '\\`')
                if line == '':
                    continue
                self.os_exec("mysql -u "+dbuser+" -p'" + dbpass +
                             "' -e \"USE " + dbname + "; " + line + "\"")
            #os_exec("mysql -uroot -p'" + dbpass + "' " + dbname + " < " + apptmpdir + "/" + schemaupdatesql)

        #exec('python3 /var/lib/' + tomcatN + '/webapps/' + appname + '/Setup.py')

        print("開始設定資料庫到 context.xml")
        context = open(contextpath, 'r+', encoding='UTF-8')
        contextsoup = BeautifulSoup(context, 'xml')
        resource = contextsoup.find('Resource')
        store = contextsoup.find('Store')

        #username = input("請輸入資料庫帳號(" + resource['username'] + ")：")
        # if username != "":
        #    resource['username'] = username
        #password = input("請輸入資料庫密碼(" + resource['password'] + ")：")
        # if password != "":
        #    resource['password'] = password

        resource['username'] = dbuser
        resource['password'] = dbpass
        store['connectionName'] = dbuser
        store['connectionPassword'] = dbpass

        self.os_exec('true > ' + contextpath)
        context.seek(0, 0)
        context.write(str(contextsoup))
        context.close()

        if SSL:
            self.os_exec('sudo cp ' + webappspath + warname +
                         '/WEB-INF/web_https.xml ' + webappspath + warname + '/WEB-INF/web.xml ')
        else:
            self.os_exec('sudo cp ' + webappspath + warname +
                         '/WEB-INF/web_http.xml ' + webappspath + warname + '/WEB-INF/web.xml ')

        self.os_exec('/etc/init.d/' + tomcatN + ' restart')


if __name__ == '__main__':
    fire.Fire(ZeroJudgeBuild)
