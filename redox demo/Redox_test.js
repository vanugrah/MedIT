var https = require('https');
var querystring = require('querystring');
var url = require('url');

var access_token = '';

lookUpPatient = function() {
  var patient_lookup_json = '{ \
       "Meta": { \
          "DataModel": "PatientSearch", \
          "EventType": "Query", \
          "EventDateTime": "2016-03-25T05:08:45.003Z", \
          "Test": true, \
          "Destinations": [ \
             { \
                "ID": "0f4bd1d1-451d-4351-8cfd-b767d1b488d6", \
                "Name": "Patient Search Endpoint" \
             } \
          ] \
       }, \
       "Patient": { \
          "Demographics": { \
             "FirstName": "Timothy", \
             "LastName": "Bixby", \
             "DOB": "2008-01-06", \
             "SSN": "101-01-0001" \
          } \
       } \
    }';

  var options = {
    host: 'api.redoxengine.com',
    path: '/query',
    method: 'POST',
    headers: {
      'Content-Type' : 'application/json',
      'Content-Length': Buffer.byteLength(patient_lookup_json),
      'Authorization' : 'Bearer ' + access_token
    }
  };

  var post_request = https.request(options, (response) => {
    var str = '';
    
    response.on('data', function(chunk) {
      str += chunk;
    });
    
    response.on('end', function() {
      var patient = JSON.parse(str).Patient;
      console.log('Patient info found for ' + patient.Demographics.FirstName + ' ' + patient.Demographics.LastName);
      console.log('Patient phone number: ' + patient.Demographics.PhoneNumber.Home);
    });
  });
  
  post_request.write(patient_lookup_json);
  post_request.end();
}

getAccessToken = function() {
  var post_data = querystring.stringify({
  'apiKey' : '903cec1c-9ef8-4af8-8854-1e937a9172ab',
  'secret' : 'gtsecret'
  });

  var options = {
    host: 'api.redoxengine.com',
    path: '/auth/authenticate',
    method: 'POST',
    headers: {
      'Content-Type' : 'application/x-www-form-urlencoded',
      'Content-Length': Buffer.byteLength(post_data)
    }
  };

  var post_request = https.request(options, (response) => {
    var str = '';
    
    response.on('data', function(chunk) {
      str += chunk;
    });
    
    response.on('end', function() {
      access_token = JSON.parse(str).accessToken;
      console.log('Access Token: ' + access_token);
      lookUpPatient();
    });
  });
  
  post_request.write(post_data);
  post_request.end();
}

getAccessToken();
