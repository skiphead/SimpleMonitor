# SimpleMonitor

A simple monitor is a program for monitoring the status of a HTTP protocol and other network ports at  URLs or IP addresses.
To start working with the program, it is enough to edit the configuration file in the format Json - config/config.json:

{
  "http": [
    "http://site.com",
    "http://site2.com"
  ],
  "https": [
    "https://site.com"
  ],
  "manual":[
    "ip://smtp.site.com:25"
    "ip://10.0.0.1:1234"
  ],
  settings: {
    "wwwData": "www/test.json"
  }
}
