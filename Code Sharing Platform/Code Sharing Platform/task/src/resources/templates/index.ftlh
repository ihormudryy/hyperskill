<html>
<head>
    <title>${title}</title>
    <link rel="stylesheet"
          href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css">
    <script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js"></script>
    <link rel="stylesheet" href="css/styles.css">
    <script type="text/javascript" src="js/script.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
</head>
<body>
<#if timeRestriction == true>
    <span id="time_restriction" type="text">${code.time}</span>
    <br/>
</#if>
<#if viewRestriction == true>
    <span id="views_restriction" type="text">${code.views}</span>
    <br/>
</#if>
<span id="load_date">${code.date}</span>
<br/>
<pre id="code_snippet"><code>${code.code}</code></pre>
<br/>
</body>
</html>