var server_address = "http://localhost:8082";

function connectToServer(ok) {
  if(ok) {
    server_address = "http://localhost:8082";
    console.log("server address changed to localhost");
  } else {
    server_address = "http://37.97.223.70:8082";
    console.log("server address changed to remote");
  }
}

function getServerAddress() {
  return server_address;
}

function storeJwtToken(token) {
  document.cookie = "token=" + token;
}

function getJwtToken() {
    var token = "token=";
    var ca = document.cookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') {
            c = c.substring(1);
        }
        if (c.indexOf(token) == 0) {
            return c.substring(token.length,c.length);
        }
    }
    return "";
}

function sendLogin(username, password) {
  //var data = "{\n\t\"username\": \"" + username + "\",\n\t\"password\": \"" + password + "\"\n}";

  var jsData = {}
  jsData['username'] = username;
  jsData['password'] = password;

  var data = JSON.stringify(jsData);

  $.ajax({
    url: server_address + "/auth",
    async: true,
    crossDomain: true,
    headers: {
        'content-type': 'application/json',
        'cache-control': 'no-cache'
    },
    method: 'POST',
    data: data,
    success: function(response) {
      if(response['success']) {
        // login success
        console.log("Login succes, token: " + response['token']);
        var jwt = response['token'];
        storeJwtToken(jwt);
        window.location.href = "index.html";
      } else {
        // login fail
        console.log("login fail, message: " + response['message']);
        alert(response['message']);
      }
    }
  });
}

function sendRegister(username, password, email) {
  //var data = "{\n\t\"username\": \"" + username + "\", \n\t\"password\": \"" + password + "\", \n\t\"email\": \"" + email + "\"\n}";

  var jsData = {}
  jsData['username'] = username;
  jsData['password'] = password;
  jsData['email'] = email;

  var data = JSON.stringify(jsData);

console.log(data);
  $.ajax({
    url: server_address + "/user/register",
    async: true,
    crossDomain: true,
    header: {
      'content-type': 'application/json',
      'cache-control': 'no-cache'
    },
    method: 'POST',
    data: data,
    success: function(response) {
      if(response['success']) {
        alert("A new user has been created. You can login now.");
        window.location.href = "login.html";
      } else {
        alert(response['message']);
      }

    }
  });
}
