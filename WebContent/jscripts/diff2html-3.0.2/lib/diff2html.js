"use strict";
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
var __importStar = (this && this.__importStar) || function (mod) {
    if (mod && mod.__esModule) return mod;
    var result = {};
    if (mod != null) for (var k in mod) if (Object.hasOwnProperty.call(mod, k)) result[k] = mod[k];
    result["default"] = mod;
    return result;
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
var DiffParser = __importStar(require("./diff-parser"));
var fileListPrinter = __importStar(require("./file-list-renderer"));
var line_by_line_renderer_1 = __importStar(require("./line-by-line-renderer"));
var side_by_side_renderer_1 = __importStar(require("./side-by-side-renderer"));
var types_1 = require("./types");
var hoganjs_utils_1 = __importDefault(require("./hoganjs-utils"));
exports.defaultDiff2HtmlConfig = __assign(__assign(__assign({}, line_by_line_renderer_1.defaultLineByLineRendererConfig), side_by_side_renderer_1.defaultSideBySideRendererConfig), { outputFormat: types_1.OutputFormatType.LINE_BY_LINE, drawFileList: true });
function parse(diffInput, configuration) {
    if (configuration === void 0) { configuration = {}; }
    return DiffParser.parse(diffInput, __assign(__assign({}, exports.defaultDiff2HtmlConfig), configuration));
}
exports.parse = parse;
function html(diffInput, configuration) {
    if (configuration === void 0) { configuration = {}; }
    var config = __assign(__assign({}, exports.defaultDiff2HtmlConfig), configuration);
    var diffJson = typeof diffInput === 'string' ? DiffParser.parse(diffInput, config) : diffInput;
    var hoganUtils = new hoganjs_utils_1.default(config);
    var fileList = config.drawFileList ? fileListPrinter.render(diffJson, hoganUtils) : '';
    var diffOutput = config.outputFormat === 'side-by-side'
        ? new side_by_side_renderer_1.default(hoganUtils, config).render(diffJson)
        : new line_by_line_renderer_1.default(hoganUtils, config).render(diffJson);
    return fileList + diffOutput;
}
exports.html = html;
//# sourceMappingURL=diff2html.js.map