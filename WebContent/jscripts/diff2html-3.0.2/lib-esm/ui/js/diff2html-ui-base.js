var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
import { nodeStream, mergeStreams } from './highlight.js-helpers';
import { html, defaultDiff2HtmlConfig } from '../../diff2html';
export var defaultDiff2HtmlUIConfig = __assign(__assign({}, defaultDiff2HtmlConfig), { synchronisedScroll: true, highlight: true, fileListToggle: true, fileListStartVisible: false, smartSelection: true });
var Diff2HtmlUI = (function () {
    function Diff2HtmlUI(target, diffInput, config, hljs) {
        if (config === void 0) { config = {}; }
        this.hljs = null;
        this.currentSelectionColumnId = -1;
        this.config = __assign(__assign({}, defaultDiff2HtmlUIConfig), config);
        this.diffHtml = diffInput !== undefined ? html(diffInput, this.config) : target.innerHTML;
        this.targetElement = target;
        if (hljs !== undefined)
            this.hljs = hljs;
    }
    Diff2HtmlUI.prototype.draw = function () {
        this.targetElement.innerHTML = this.diffHtml;
        if (this.config.smartSelection)
            this.smartSelection();
        if (this.config.synchronisedScroll)
            this.synchronisedScroll();
        if (this.config.highlight)
            this.highlightCode();
        if (this.config.fileListToggle)
            this.fileListToggle(this.config.fileListStartVisible);
    };
    Diff2HtmlUI.prototype.synchronisedScroll = function () {
        this.targetElement.querySelectorAll('.d2h-file-wrapper').forEach(function (wrapper) {
            var _a = Array().slice.call(wrapper.querySelectorAll('.d2h-file-side-diff')), left = _a[0], right = _a[1];
            if (left === undefined || right === undefined)
                return;
            var onScroll = function (event) {
                if (event === null || event.target === null)
                    return;
                if (event.target === left) {
                    right.scrollTop = left.scrollTop;
                    right.scrollLeft = left.scrollLeft;
                }
                else {
                    left.scrollTop = right.scrollTop;
                    left.scrollLeft = right.scrollLeft;
                }
            };
            left.addEventListener('scroll', onScroll);
            right.addEventListener('scroll', onScroll);
        });
    };
    Diff2HtmlUI.prototype.fileListToggle = function (startVisible) {
        var showBtn = this.targetElement.querySelector('d2h-show');
        var hideBtn = this.targetElement.querySelector('.d2h-hide');
        var fileList = this.targetElement.querySelector('.d2h-file-list');
        if (showBtn === null || hideBtn === null || fileList === null)
            return;
        var show = function () {
            showBtn.style.display = 'none';
            hideBtn.style.display = 'inline';
            fileList.style.display = 'block';
        };
        var hide = function () {
            showBtn.style.display = 'inline';
            hideBtn.style.display = 'none';
            fileList.style.display = 'none';
        };
        showBtn.addEventListener('click', function () { return show(); });
        hideBtn.addEventListener('click', function () { return hide(); });
        var hashTag = this.getHashTag();
        if (hashTag === 'files-summary-show')
            show();
        else if (hashTag === 'files-summary-hide')
            hide();
        else if (startVisible)
            show();
        else
            hide();
    };
    Diff2HtmlUI.prototype.highlightCode = function () {
        var _this = this;
        if (this.hljs === null) {
            throw new Error('Missing a `highlight.js` implementation. Please provide one when instantiating Diff2HtmlUI.');
        }
        var files = this.targetElement.querySelectorAll('.d2h-file-wrapper');
        files.forEach(function (file) {
            var oldLinesState;
            var newLinesState;
            var codeLines = file.querySelectorAll('.d2h-code-line-ctn');
            codeLines.forEach(function (line) {
                if (_this.hljs === null)
                    return;
                var text = line.textContent;
                var lineParent = line.parentNode;
                if (text === null || lineParent === null || !_this.isElement(lineParent))
                    return;
                var lineState = lineParent.classList.contains('d2h-del') ? oldLinesState : newLinesState;
                var language = file.getAttribute('data-lang');
                var result = language && _this.hljs.getLanguage(language)
                    ? _this.hljs.highlight(language, text, true, lineState)
                    : _this.hljs.highlightAuto(text);
                if (_this.instanceOfIHighlightResult(result)) {
                    if (lineParent.classList.contains('d2h-del')) {
                        oldLinesState = result.top;
                    }
                    else if (lineParent.classList.contains('d2h-ins')) {
                        newLinesState = result.top;
                    }
                    else {
                        oldLinesState = result.top;
                        newLinesState = result.top;
                    }
                }
                var originalStream = nodeStream(line);
                if (originalStream.length) {
                    var resultNode = document.createElementNS('http://www.w3.org/1999/xhtml', 'div');
                    resultNode.innerHTML = result.value;
                    result.value = mergeStreams(originalStream, nodeStream(resultNode), text);
                }
                line.classList.add('hljs');
                line.classList.add(result.language);
                line.innerHTML = result.value;
            });
        });
    };
    Diff2HtmlUI.prototype.smartSelection = function () {
        var _this = this;
        var body = document.getElementsByTagName('body')[0];
        var diffTable = body.getElementsByClassName('d2h-diff-table')[0];
        diffTable.addEventListener('mousedown', function (event) {
            if (event === null || !_this.isElement(event.target))
                return;
            var table = event.target.closest('.d2h-diff-table');
            if (table !== null) {
                if (event.target.closest('.d2h-code-line,.d2h-code-side-line') !== null) {
                    table.classList.remove('selecting-left');
                    table.classList.add('selecting-right');
                    _this.currentSelectionColumnId = 1;
                }
                else if (event.target.closest('.d2h-code-linenumber,.d2h-code-side-linenumber') !== null) {
                    table.classList.remove('selecting-right');
                    table.classList.add('selecting-left');
                    _this.currentSelectionColumnId = 0;
                }
            }
        });
        diffTable.addEventListener('copy', function (event) {
            if (!_this.isClipboardEvent(event))
                return;
            var clipboardData = event.clipboardData;
            var text = _this.getSelectedText();
            if (clipboardData === null || text === undefined)
                return;
            clipboardData.setData('text', text);
            event.preventDefault();
        });
    };
    Diff2HtmlUI.prototype.instanceOfIHighlightResult = function (object) {
        return 'top' in object;
    };
    Diff2HtmlUI.prototype.getHashTag = function () {
        var docUrl = document.URL;
        var hashTagIndex = docUrl.indexOf('#');
        var hashTag = null;
        if (hashTagIndex !== -1) {
            hashTag = docUrl.substr(hashTagIndex + 1);
        }
        return hashTag;
    };
    Diff2HtmlUI.prototype.getSelectedText = function () {
        var sel = window.getSelection();
        if (sel === null)
            return;
        var range = sel.getRangeAt(0);
        var doc = range.cloneContents();
        var nodes = doc.querySelectorAll('tr');
        var idx = this.currentSelectionColumnId;
        var text = '';
        if (nodes.length === 0) {
            text = doc.textContent || '';
        }
        else {
            nodes.forEach(function (tr, i) {
                var td = tr.cells[tr.cells.length === 1 ? 0 : idx];
                if (td === undefined || td.textContent === null)
                    return;
                text += (i ? '\n' : '') + td.textContent.replace(/\r\n|\r|\n/g, '');
            });
        }
        return text;
    };
    Diff2HtmlUI.prototype.isElement = function (arg) {
        var _a;
        return arg !== null && ((_a = arg) === null || _a === void 0 ? void 0 : _a.classList) !== undefined;
    };
    Diff2HtmlUI.prototype.isClipboardEvent = function (arg) {
        var _a;
        return arg !== null && ((_a = arg) === null || _a === void 0 ? void 0 : _a.clipboardData) !== undefined;
    };
    return Diff2HtmlUI;
}());
export { Diff2HtmlUI };
//# sourceMappingURL=diff2html-ui-base.js.map