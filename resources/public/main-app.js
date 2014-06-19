/**
 * Created by macquest on 6/20/14.
 */

var WS_URI = 'ws://localhost:3000/ws';
var BASE_TOPIC_URI = "http://localhost:3000";
var sess;

// Connect to WebSocket
ab.connect(
	WS_URI,
// Connection callback
	function (session) {
		sess = session;
		console.log("Connected to " + WS_URI, sess.sessionid());

		// Authenticate
		var username = 'guest';
		var password = 'secret-password';

	},
// Disconnection callback
	function (code, reason) {
		sess = null;
		if (code != 0) {  // ignore app disconnects
			console.log("Connection lost (" + reason + ")");
		}
	},
// Options
	{'maxRetries': 5, 'retryDelay': 300000}
);

function onChatEvent(topic, event) {
	console.log("event:chat RECEIVED", event);
	}

$('#chat-btn').click(function() {
	sess.publish("event:chat", "foo");
	console.log("event:chat SENT 'foo'");
	});

