function getData() {
  let xhr = new XMLHttpRequest();
  xhr.open("GET", '/code/source', true);
  xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
  xhr.send();
  xhr.onload = function (e) {
    let data = JSON.parse(e.currentTarget.responseText);
    document.getElementById("code_snippet").innerText = data["code"];
    document.getElementById("load_date").innerText = data["date"];
  }
  xhr.onerror = function () {
    console.error("Data file is not found.");
  }
}

function send() {
  let object = {
    "code": document.getElementById("code_snippet").value
  };

  let json = JSON.stringify(object);

  let xhr = new XMLHttpRequest();
  xhr.open("POST", '/api/code/new', false)
  xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
  xhr.send(json);

  if (xhr.status == 200) {
    alert("Success!");
  }
}