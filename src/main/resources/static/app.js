let roomId = 1; // Make sure roomId is assigned dynamically based on context
let userId = 'user123'; // Assign a valid userId
let userRole = 'admin'; // Assign the user's role
let messageContent = '';

const stompClient = new StompJs.Client({
  brokerURL: 'ws://localhost:8080/ws-stomp'
});

stompClient.onConnect = (frame) => {
  setConnected(true);
  console.log('Connected: ' + frame);
  stompClient.subscribe('/topic/room/' + roomId, (message) => {
    console.log("Message received: ", message.body);  // 메시지 수신 확인
    showGreeting(JSON.parse(message.body).content);
  });
};

stompClient.onWebSocketError = (error) => {
  console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
  console.error('Broker reported error: ' + frame.headers['message']);
  console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
  $("#connect").prop("disabled", connected);
  $("#disconnect").prop("disabled", !connected);
  if (connected) {
    $("#conversation").show();
  } else {
    $("#conversation").hide();
  }
  $("#greetings").html("");
  if (!connected) {
    console.error('Disconnected from STOMP broker.');
  }
}

function connect() {
  stompClient.activate();  // Activate the STOMP client
}

function disconnect() {
  stompClient.deactivate();
  setConnected(false);
  console.log("Disconnected");
}

function sendName() {
  messageContent = $("#message").val();  // 메시지 입력 필드에서 콘텐츠 가져오기
  if (stompClient.connected) {  // 연결이 되어 있는지 확인
    console.log("연결됨");
    stompClient.publish({
      destination: "/pub/chat.sendMessage",
      body: JSON.stringify({
        'room': {'id': roomId},
        'content': messageContent,
        'sender': userId,
        'role': userRole
      })
    });
    $("#message").val('');  // 메시지 전송 후 입력 필드 초기화
  } else {
    console.error('STOMP client is not connected.');
  }
}

function showGreeting(message) {
  console.log("Showing message: ", message);  // 메시지 표시 확인
  $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
  $("form").on('submit', (e) => e.preventDefault());
  $("#connect").click(() => connect());
  $("#disconnect").click(() => disconnect());
  $("#send").click(() => sendName());
});
