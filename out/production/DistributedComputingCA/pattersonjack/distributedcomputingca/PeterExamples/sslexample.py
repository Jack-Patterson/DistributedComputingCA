from http.server import HTTPServer, BaseHTTPRequestHandler
import ssl


httpd = HTTPServer(('127.0.0.1', 4443), BaseHTTPRequestHandler)

httpd.socket = ssl.wrap_socket(httpd.socket, keyfile="key.pem", certfile="certificate.pem", server_side=True)

httpd.serve_forever()