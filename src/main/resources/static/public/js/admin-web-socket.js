(() => {
	const onReady = (fn) => {
	    if (document.readyState === 'loading') {
	      document.addEventListener('DOMContentLoaded', fn, { once: true });
	    } else {
	      fn();
	    }
	};
	
	onReady(() => {
		/* =======================
			CHAT PAGE WEBSOCKET 
		   ======================= */ 
		const chatBox = document.getElementById("chat-messages");
		const input = document.getElementById("chat-input");
		const sendBtn = document.getElementById("chat-send-btn");
		
		if (chatBox && input && typeof SockJS !== "undefined" && typeof Stomp !== "undefined") {
			
			const roomId = chatBox.dataset.roomId;
			const senderId = chatBox.dataset.userId;
			const receiverId = chatBox.dataset.peerId;
			
			let stompClient = null;
			
			const scrollToBottom = (smooth = true) => {
				chatBox.scrollTo({
					top: chatBox.scrollHeight,
					behavior: smooth ? "smooth" : "auto"	
				});
			};
			
			const isNearBottom = () => {
				const tolerance = 80;
				return chatBox.scrollHeight - chatBox.scrollTop - chatBox.clientHeight < tolerance;
			}
			
			const appendMessage = (msg, fromSelf = false) => {
				const wrap = document.createElement("div");
				wrap.className = "d-flex mb-3 " + (fromSelf ? "justify-content-end" : "align-items-start");
				
				const bubble = document.createElement("div");
				bubble.className = "p-3 rounded-3 shadow-sm";
				bubble.style.background = fromSelf ? "#002766" : "#6c757d";
				bubble.innerHTML = `<p class="mb-0">${msg.content}</p>`;
				
				wrap.appendChild(bubble);
				chatBox.appendChild(wrap);
				
				if(isNearBottom()) {
					scrollToBottom(true);
				}
			};
			
			const socket = new SockJS("/ws");
			stompClient = Stomp.over(socket);
			
			stompClient.connect({},() => {
				stompClient.subscribe(`/topic/chat.${roomId}`,(frame) => {
					const body = JSON.parse(frame.body);
					
					if(body.senderId === senderId) {
						return;
					}
					
					appendMessage(body,false);
				});
				scrollToBottom(false);
			});
			
			const sendMessage = () => {
				const text = input.value.trim();
				if(!text || !stompClient || !stompClient.connected) return;
				
				const payload = {
					roomId,
					senderId,
					receiverId,
					content: text
				};
				
				appendMessage(payload, true);
				
				stompClient.send("/app/chat.send", {}, JSON.stringify(payload));
				input.value = "";
			};
			
			sendBtn.addEventListener("click",sendMessage);
			input.addEventListener("keydown", (e) => {
				if(e.key === "Enter") {
					e.preventDefault();
					sendMessage();
				}
			});
			
			window.addEventListener("beforeunload", () => {
				if(stompClient && stompClient.connected) {
					stompClient.disconnect(() => console.log("Websocket closed on unload."));
				}
			});
		}	  
	});
})();