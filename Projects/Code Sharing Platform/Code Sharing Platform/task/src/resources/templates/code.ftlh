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
<#list codes as code>
    <div>
        <br/>
        <span class="load_dates" id="load_date">${code.date}</span>
        <br/>
        <input id="time_restriction" type="text"/>
        <br/>
        <input id="views_restriction" type="text"/>
        <br/>
        <pre class="code_snippets" id="code_snippet"><code>${code.code}</code></pre>
    </div>
</#list>
</body>
</html>