"use strict";
var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
Object.defineProperty(exports, "__esModule", { value: true });
var highlight_js_slim_1 = require("./highlight.js-slim");
var diff2html_ui_base_1 = require("./diff2html-ui-base");
exports.defaultDiff2HtmlUIConfig = diff2html_ui_base_1.defaultDiff2HtmlUIConfig;
var Diff2HtmlUI = (function (_super) {
    __extends(Diff2HtmlUI, _super);
    function Diff2HtmlUI(target, diffInput, config) {
        if (config === void 0) { config = {}; }
        return _super.call(this, target, diffInput, config, highlight_js_slim_1.hljs) || this;
    }
    return Diff2HtmlUI;
}(diff2html_ui_base_1.Diff2HtmlUI));
exports.Diff2HtmlUI = Diff2HtmlUI;
//# sourceMappingURL=diff2html-ui-slim.js.map