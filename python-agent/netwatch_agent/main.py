# import stomp
#
# conn = stomp.Connection([('localhost', 8080)])
# conn.connect(wait=True)
# conn.send(body='{}', destination='/netwatch-agent-event-listener')
# conn.disconnect()
import json
from random import random

import stomper
import websocket
from websocket import ABNF

ws: websocket._core.WebSocket = websocket.create_connection("ws://localhost:8080/netwatch-agent-event-listener")

data = {
    "name": "Test Agent",
    "host": "1.2.3.4",
    "lastSeenAt": 1715114647900,
    "connections": [
        {
            "name": "Google",
            "description": "Connection to Google",
            "type": "HTTP",
            "host": "google.com",
            "port": 80,
            "active": True, "lastCheckedAt": 1715114647695
        },
        {
            "name": "Facebook",
            "description": "Connection to Facebook",
            "type": "HTTP",
            "host": "facebook.com",
            "port": 80,
            "active": False, "lastCheckedAt": 1715114647747
        }
    ]
}
ws.send("CONNECT\naccept-version:1.0,1.1,2.0\n\n\x00\n")
# ws.send(payload=json.dumps(data), opcode=ABNF.OPCODE_TEXT)
# Subscribing to topic
client_id = '999'
sub = stomper.subscribe("/netwatch-agent-event-listener", client_id, ack='auto')
ws.send(sub)
