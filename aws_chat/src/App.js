import useWebSocket from 'react-use-websocket';
import { v4 as uuidv4 } from 'uuid';
import './App.css';
import { useEffect, useState } from 'react';

const WebSocket_url = "wss://2jda7c80xl.execute-api.ap-northeast-2.amazonaws.com/demo/";

function App() {

  const [socketUrl] = useState(WebSocket_url);

  const {sendMessage, lastMessage, readyState} = useWebSocket(socketUrl);
  // readyState : '0' - 연결 시도 중 / '1' - 연결됨(open) / '2' - 연결 종료 시도 중 / '3' - 연결 종료

  // 고유한 사용자 아이디를 생성
  const [userId, setUserID] = useState(uuidv4());
  useEffect(() => {
    if(readyState === 1) {
      console.log("연결되었음!");
    } else if (readyState === 0) {
      console.log("연결 시도 중...");
    } else if (readyState === 3) {
      console.log("연결 종료");
    }
  }, [readyState]);

  // 메시지를 저장하고 출력할 수 있는 변수
  const [message, setMessage] = useState('');

  // 웹 소켓 연결 상태
  const connectiosState = {
    0 : '연결 시도 중...',
    1 : '연결 되었음~!!',
    2 : '연결 종료 시도 중...',
    3 : '연결 종료됨'
  }[readyState];
  console.log("connectiosState : " + connectiosState);

  const handlerSendMessage = () => {
    if(message) {
      sendMessage(JSON.stringify({action:'sendMessage', message, userId}));
      setMessage('');   // 메시지 전송 후 입력 필드 지우기
    }
  }

  return (
    <div className="App">
      <h1>webSocket 연결 테스트</h1>
      <p> webSocket 상태 : {connectiosState}</p>
      <p>User ID : {userId}</p>
      <p> 마지막 메시지 : {lastMessage ? lastMessage.data : '메시지 없음'}</p>
      <input type="text" value={message} onChange={(e) => setMessage(e.target.value)} placeholder='메시지를 입력하세요'/>     
      
      {/* 웹 소켓이 연결되지 않았으면 버튼 비활성화. */}
      <button onClick={handlerSendMessage} disabled={readyState !== 1}>Send</button>

    </div>
  );
}

export default App;
