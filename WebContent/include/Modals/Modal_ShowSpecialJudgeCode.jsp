<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="modal fade" id="Modal_ShowSpecialJudgeCode" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">Special Judge 參考程式碼</h4>
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="form-group">
						<div class="col-sm-12">
							<label for="tname"> 以 Python 為例
							</label>
							<pre id="code" class="language-python">import sys
from itertools import zip_longest

result = {"$JUDGE_RESULT":"", "$LINECOUNT":"","$USEROUT":"","$SYSTEMOUT":"","$MESSAGE":""}

def main(argv):
    if len(argv) != 3:
        print ('special.py [inputfile] [ansfile] [outputfile]')
        sys.exit(2)

    infile = open(argv[0], 'r', encoding='UTF-8')
    ansfile = open(argv[1], 'r', encoding='UTF-8')
    outfile = open(argv[2], 'r', encoding='UTF-8')

    for index, (out, ans) in enumerate(zip_longest(outfile.readlines(), ansfile.readlines()), 1):
        if ans is None :
            result["$JUDGE_RESULT"] = "OLE"
            result["$LINECOUNT"]= str(index)
            result["$USEROUT"]=out.strip()
            result["$MESSAGE"]="多餘的輸出。"
            return
        if out is None :
            result["$JUDGE_RESULT"]="WA"
            result["$LINECOUNT"]=str(index)
            result["$MESSAGE"]="沒有完整輸出答案。"
            return
        if out.strip() != ans.strip():
            result["$JUDGE_RESULT"]="WA"
            result["$LINECOUNT"]=str(index)
            result["$USEROUT"]=out.strip()
            result["$SYSTEMOUT"]=ans.strip()
            result["$MESSAGE"]="您的答案比對不符合。"
            return
    result["$JUDGE_RESULT"]="AC"

if __name__ == "__main__":
    main(sys.argv[1:])
    for key in result:
        print(key+"="+result[key])
														</pre>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
