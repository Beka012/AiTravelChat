import { useState, useEffect, useRef } from 'react';

function Chat() {
  const [message, setMessage] = useState('');
  const [messages, setMessages] = useState([
    {
      id: 1,
      type: 'ai',
      responseType: 'text',
      text: `Welcome to Travel AI – Your Intelligent Travel Companion!

Hello there, traveler! I'm your personal Travel AI assistant, ready to help you discover amazing destinations, find the best flight deals, and uncover perfect tour packages tailored just for you.

Whether you're planning a weekend getaway, a business trip, or your dream vacation, I'm here to make your travel planning experience smooth and enjoyable. Simply tell me where you'd like to go, your travel dates, or the type of experience you're looking for, and I'll handle the rest!

I can:
- Search for the best flight deals across multiple airlines
- Find hotel accommodations that match your preferences and budget  
- Recommend personalized tour packages based on your interests
- Provide travel tips and destination information
- Help you create the perfect itinerary

Your next adventure begins with a simple conversation. What can I help you discover today?`,
      time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
      user: { name: 'Travel AI', avatar: 'T' }
    }
  ]);
  const [isTyping, setIsTyping] = useState(false);
  const [currentUser] = useState({
    name: 'Guest',
    avatar: 'G'
  });
  const messagesEndRef = useRef(null);
  const inputRef = useRef(null);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('ru-RU', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    }) + ' ' + date.toLocaleTimeString('ru-RU', {
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const formatSimpleDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('ru-RU', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    });
  };

  const formatDuration = (minutes) => {
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return `${hours}ч ${mins}м`;
  };

  const formatPrice = (price) => {
    return new Intl.NumberFormat('ru-RU').format(price);
  };

  const TicketCard = ({ ticket }) => (
    <div className="ticket-card">
      <div className="ticket-airline">
        {ticket.source}
      </div>

      <div className="ticket-route">
        <div className="ticket-city">
          <div className="city-name">{ticket.fromCity}</div>
          <div className="city-time">{formatDate(ticket.fromDate)}</div>
        </div>

        <div className="ticket-arrow">
          <div className="arrow-line"></div>
          <div className="arrow-head">→</div>
          <div className="flight-duration">{formatDuration(ticket.durationMinutes)}</div>
        </div>

        <div className="ticket-city">
          <div className="city-name">{ticket.toCity}</div>
          <div className="city-time">{formatDate(ticket.toDate)}</div>
        </div>
      </div>

      <div className="ticket-price">
        {ticket.price} {ticket.currency}
      </div>
    </div>
  );

  // Компонент для отображения тура
   const TourCard = ({ tour }) => {
    const handleTourClick = () => {
      if (tour.link) {
        window.open(tour.link, '_blank', 'noopener,noreferrer');
      }
    };

    return (
      <div className="tour-card" onClick={handleTourClick} style={{ cursor: tour.link ? 'pointer' : 'default' }}>
        <div className="tour-hotel-header">
          <div className="hotel-name">{tour.hotelName}</div>
          <div className="hotel-stars">
            {Array.from({ length: tour.tourInfo[0]?.star || 0 }, (_, i) => (
              <span key={i} className="star">★</span>
            ))}
          </div>
          {tour.link && (
            <div className="tour-link-icon">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                <path d="M19 19H5V5h7V3H5c-1.11 0-2 .9-2 2v14c0 1.1.89 2 2 2h14c1.11 0 2-.9 2-2v-7h-2v7zM14 3v2h3.59l-9.83 9.83 1.41 1.41L19 6.41V10h2V3h-7z"/>
              </svg>
            </div>
          )}
        </div>

        <div className="tour-options">
          {tour.tourInfo.map((option, index) => (
            <div key={index} className="tour-option">
              <div className="tour-main-info">
                <div className="tour-location">
                  <div className="country-name">{option.countryName}</div>
                  <div className="city-name">{option.cityName}</div>
                </div>
                
                <div className="tour-details">
                  <div className="tour-dates">
                    <div className="departure-date">
                      <span className="label">Дата выезда:</span>
                      <span className="value">{formatSimpleDate(option.departureDate)}</span>
                    </div>
                    <div className="nights">
                      <span className="label">Продолжительность:</span>
                      <span className="value">{option.numberOfNight}</span>
                    </div>
                  </div>
                  
                  <div className="tour-meal">
                    <span className="meal-type">{option.meal}</span>
                  </div>
                </div>
              </div>

              <div className="tour-price">
                <div className="price-amount">{formatPrice(option.price)} ₸</div>
                <div className="price-label">за тур</div>
              </div>
            </div>
          ))}
        </div>
        
        {tour.link && (
          <div className="tour-click-hint">
            <span>Нажмите для перехода к бронированию</span>
          </div>
        )}
      </div>
    );
  };

  const handleSendMessage = async (e) => {
    e?.preventDefault?.();
    if (!message.trim()) return;

    const userMessage = {
      id: messages.length + 1,
      type: 'user',
      text: message,
      time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
      user: currentUser
    };

    setMessages(prev => [...prev, userMessage]);
    setMessage('');
    setIsTyping(true);

    try {
      const response = await fetch('http://localhost:8090/api/chat/answer', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ prompt: message })
      });

      if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

      const data = await response.json();

      const aiMessage = {
        id: messages.length + 2,
        type: 'ai',
        responseType: data.type,
        data: data.type === 'ticket' || data.type === 'tours' ? data.data : null,
        text: data.type === 'text' ? (data.response || data.text || data.data || 'Ответ получен') : null,
        time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
        user: { name: 'Travel AI', avatar: 'T' }
      };

      setMessages(prev => [...prev, aiMessage]);
    } catch (error) {
      const errorMessage = {
        id: messages.length + 2,
        type: 'error',
        responseType: 'text',
        text: `Ошибка: ${error.message}`,
        time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
        user: { name: 'System', avatar: '⚠️' }
      };
      setMessages(prev => [...prev, errorMessage]);
    } finally {
      setIsTyping(false);
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSendMessage();
    }
  };

  return (
    <div className="chat-app">
      {/* Шапка */}
      <header className="chat-header">
        <div className="header-content">
          <div className="logo-section">
            <div className="logo-icon">T</div>
            <span className="logo-text">Travel AI</span>
          </div>
          <div className="user-avatar">
            {currentUser.avatar}
          </div>
        </div>
      </header>

      {/* Область чата */}
      <div className="chat-container">
        <div className="messages-area">
          {messages.map((msg) => (
            <div key={msg.id} className={`message-wrapper ${msg.type}`}>
              <div className="message-content">
                <div className="message-avatar">
                  {msg.user.avatar}
                </div>
                
                <div className="message-bubble">
                  {/* Отображение контента в зависимости от типа */}
                  {msg.responseType === 'ticket' && msg.data ? (
                    <div className="tickets-container">
                      {Array.isArray(msg.data) ? (
                        msg.data.map((ticket, index) => (
                          <TicketCard key={index} ticket={ticket} />
                        ))
                      ) : (
                        <TicketCard ticket={msg.data} />
                      )}
                    </div>
                  ) : msg.responseType === 'tours' && msg.data ? (
                    <div className="tours-container">
                      {Array.isArray(msg.data) ? (
                        msg.data.map((tour, index) => (
                          <TourCard key={index} tour={tour} />
                        ))
                      ) : (
                        <TourCard tour={msg.data} />
                      )}
                    </div>
                  ) : (
                    <div className="message-text">
                      {msg.text}
                    </div>
                  )}
                  
                  <div className="message-time">
                    {msg.time}
                  </div>
                </div>
              </div>
            </div>
          ))}

          {/* Индикатор печатания */}
          {isTyping && (
            <div className="message-wrapper ai">
              <div className="message-content">
                <div className="message-avatar">T</div>
                <div className="message-bubble typing">
                  <div className="typing-indicator">
                    <span>Travel AI печатает</span>
                    <div className="typing-dots">
                      <div className="dot"></div>
                      <div className="dot"></div>
                      <div className="dot"></div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          )}
          <div ref={messagesEndRef} />
        </div>

        {/* Поле ввода */}
        <div className="input-area">
          <div className="input-container">
            <textarea
              ref={inputRef}
              placeholder="Напишите ваше сообщение..."
              value={message}
              onChange={(e) => setMessage(e.target.value)}
              onKeyPress={handleKeyPress}
              className="message-input"
              rows="1"
            />
            <button
              onClick={handleSendMessage}
              className="send-button"
              disabled={!message.trim()}
            >
              <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                <path d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z"/>
              </svg>
            </button>
          </div>
        </div>
      </div>

      <style jsx>{`
        * {
          margin: 0;
          padding: 0;
          box-sizing: border-box;
        }

        .chat-app {
          font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Arial, sans-serif;
          height: 100vh;
          display: flex;
          flex-direction: column;
          background-color: #f0f8ff;
          overflow: hidden;
        }

        /* Шапка */
        .chat-header {
          background: linear-gradient(135deg, #0066cc, #00aaff);
          color: white;
          padding: 12px 20px;
          box-shadow: 0 2px 8px rgba(0,0,0,0.1);
          z-index: 100;
          flex-shrink: 0;
        }

        .header-content {
          display: flex;
          justify-content: space-between;
          align-items: center;
          max-width: 1200px;
          margin: 0 auto;
        }

        .logo-section {
          display: flex;
          align-items: center;
          gap: 12px;
        }

        .logo-icon {
          width: 42px;
          height: 42px;
          background-color: white;
          color: #0066cc;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-weight: bold;
          font-size: 20px;
          flex-shrink: 0;
        }

        .logo-text {
          font-size: 22px;
          font-weight: bold;
        }

        .user-avatar {
          width: 36px;
          height: 36px;
          background-color: rgba(255,255,255,0.3);
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-weight: bold;
          font-size: 16px;
          flex-shrink: 0;
        }

        /* Контейнер чата */
        .chat-container {
          flex: 1;
          display: flex;
          flex-direction: column;
          max-width: 1000px;
          margin: 0 auto;
          width: 100%;
          min-height: 0;
        }

        .messages-area {
          flex: 1;
          overflow-y: auto;
          padding: 20px;
          display: flex;
          flex-direction: column;
          gap: 16px;
          scroll-behavior: smooth;
        }

        /* Стили для скроллбара */
        .messages-area::-webkit-scrollbar {
          width: 6px;
        }

        .messages-area::-webkit-scrollbar-track {
          background: transparent;
        }

        .messages-area::-webkit-scrollbar-thumb {
          background: #ccc;
          border-radius: 3px;
        }

        .messages-area::-webkit-scrollbar-thumb:hover {
          background: #999;
        }

        /* Сообщения */
        .message-wrapper {
          display: flex;
          width: 100%;
          margin-bottom: 4px;
        }

        .message-wrapper.user {
          justify-content: flex-end;
        }

        .message-wrapper.ai {
          justify-content: flex-start;
        }

        .message-content {
          display: flex;
          max-width: 80%;
          gap: 10px;
          align-items: flex-start;
        }

        .message-wrapper.user .message-content {
          flex-direction: row-reverse;
        }

        .message-avatar {
          width: 36px;
          height: 36px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-weight: bold;
          font-size: 14px;
          color: white;
          flex-shrink: 0;
        }

        .message-wrapper.user .message-avatar {
          background-color: #0066cc;
        }

        .message-wrapper.ai .message-avatar {
          background-color: #00aaff;
        }

        .message-wrapper.error .message-avatar {
          background-color: #dc3545;
        }

        .message-bubble {
          border-radius: 18px;
          padding: 12px 16px;
          box-shadow: 0 1px 3px rgba(0,0,0,0.08);
          position: relative;
          max-width: 100%;
          word-wrap: break-word;
          overflow-wrap: break-word;
        }

        .message-wrapper.user .message-bubble {
          background-color: #0066cc;
          color: white;
          border-bottom-right-radius: 4px;
        }

        .message-wrapper.ai .message-bubble {
          background-color: #e6f2ff;
          color: #333;
          border-bottom-left-radius: 4px;
          border: 1px solid #e0e0e0;
        }

        .message-wrapper.error .message-bubble {
          background-color: #f8d7da;
          color: #721c24;
          border: 1px solid #f5c6cb;
        }

        .message-text {
          line-height: 1.5;
          white-space: pre-wrap;
          word-break: break-word;
        }

        .message-time {
          font-size: 11px;
          opacity: 0.7;
          margin-top: 6px;
        }

        .message-wrapper.user .message-time {
          text-align: right;
        }

        /* Билетные карточки */
        .tickets-container {
          max-width: 100%;
          display: flex;
          flex-direction: column;
          gap: 12px;
        }

        .ticket-card {
          border: 1px solid #f0f0f0;
          border-radius: 16px;
          padding: 20px;
          background: linear-gradient(135deg, #ffffff, #fafbfc);
          box-shadow: 0 2px 8px rgba(0,0,0,0.06);
          transition: transform 0.2s ease;
          min-width: 280px;
        }

        .ticket-card:hover {
          transform: translateY(-2px);
          box-shadow: 0 4px 16px rgba(0,102,204,0.1);
        }

        .ticket-airline {
          font-size: 14px;
          color: #6c757d;
          margin-bottom: 12px;
          font-weight: 500;
          text-transform: uppercase;
          letter-spacing: 0.5px;
        }

        .ticket-route {
          display: flex;
          align-items: center;
          justify-content: space-between;
          margin-bottom: 16px;
          gap: 10px;
        }

        .ticket-city {
          flex: 1;
          text-align: center;
          min-width: 0;
        }

        .city-name {
          font-size: 18px;
          font-weight: bold;
          color: #333;
          margin-bottom: 4px;
          overflow: hidden;
          text-overflow: ellipsis;
        }

        .city-time {
          font-size: 12px;
          color: #6c757d;
          word-break: break-word;
        }

        .ticket-arrow {
          flex: 0 0 auto;
          text-align: center;
          position: relative;
          margin: 0 15px;
          min-width: 60px;
        }

        .arrow-line {
          height: 2px;
          background: linear-gradient(to right, #0066cc, #00aaff);
          margin: 10px 0;
          position: relative;
        }

        .arrow-head {
          color: #0066cc;
          font-size: 18px;
          font-weight: bold;
          position: absolute;
          right: -10px;
          top: -15px;
        }

        .flight-duration {
          font-size: 11px;
          color: #6c757d;
          margin-top: 8px;
          white-space: nowrap;
        }

        .ticket-price {
          text-align: center;
          padding: 12px;
          background: linear-gradient(135deg, #0066cc, #00aaff);
          border-radius: 12px;
          color: white;
          font-size: 20px;
          font-weight: bold;
        }

        /* Туры */
        .tours-container {
          max-width: 100%;
          display: flex;
          flex-direction: column;
          gap: 16px;
        }

        .tour-card {
  border: 1px solid #e8f4f8;
  border-radius: 20px;
  overflow: hidden;
  background: linear-gradient(135deg, #ffffff, #f8fcff);
  box-shadow: 0 4px 16px rgba(0,102,204,0.08);
  transition: all 0.3s ease;
  position: relative;
}

.tour-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 32px rgba(0,102,204,0.15);
}

.tour-card[style*="cursor: pointer"]:hover {
  border-color: #0066cc;
}

.tour-card[style*="cursor: pointer"]:active {
  transform: translateY(-2px);
}

        .tour-hotel-header {
  background: linear-gradient(135deg, #0066cc, #00aaff);
  color: white;
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
  position: relative;
}

.tour-link-icon {
  position: absolute;
  top: 15px;
  right: 15px;
  opacity: 0.8;
  transition: opacity 0.2s ease;
}

.tour-card:hover .tour-link-icon {
  opacity: 1;
}

.tour-click-hint {
  background: linear-gradient(135deg, #f0f9ff, #e0f2fe);
  color: #0066cc;
  text-align: center;
  padding: 12px;
  font-size: 13px;
  font-weight: 500;
  border-top: 1px solid #e8f4f8;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.tour-card:hover .tour-click-hint {
  opacity: 1;
}

        .hotel-name {
          font-size: 20px;
          font-weight: bold;
          flex: 1;
          min-width: 200px;
        }

        .hotel-stars {
          display: flex;
          gap: 2px;
        }

        .star {
          color: #ffd700;
          font-size: 18px;
          text-shadow: 0 0 3px rgba(255, 215, 0, 0.5);
        }

        .tour-options {
          padding: 0;
        }

        .tour-option {
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding: 20px;
          border-bottom: 1px solid #f0f8ff;
          transition: background-color 0.2s ease;
          gap: 20px;
        }

        .tour-option:last-child {
          border-bottom: none;
        }

        .tour-option:hover {
          background-color: #f8fcff;
        }

        .tour-main-info {
          flex: 1;
          display: flex;
          flex-direction: column;
          gap: 12px;
        }

        .tour-location {
          display: flex;
          flex-direction: column;
          gap: 4px;
        }

        .country-name {
          font-size: 18px;
          font-weight: bold;
          color: #0066cc;
        }

        .tour-location .city-name {
          font-size: 14px;
          color: #666;
          line-height: 1.4;
        }

        .tour-details {
          display: flex;
          flex-wrap: wrap;
          gap: 16px;
          align-items: center;
        }

        .tour-dates {
          display: flex;
          flex-direction: column;
          gap: 6px;
        }

        .departure-date,
        .nights {
          display: flex;
          gap: 8px;
          align-items: center;
        }

        .label {
          font-size: 12px;
          color: #666;
          font-weight: 500;
          min-width: 120px;
        }

        .value {
          font-size: 14px;
          color: #333;
          font-weight: 600;
        }

        .tour-meal {
          display: flex;
          align-items: center;
        }

        .meal-type {
          background: linear-gradient(135deg, #e8f4f8, #f0f9ff);
          color: #0066cc;
          padding: 6px 12px;
          border-radius: 20px;
          font-size: 12px;
          font-weight: 600;
          border: 1px solid #e0f2fe;
        }

        .tour-price {
          text-align: center;
          background: linear-gradient(135deg, #0066cc, #00aaff);
          color: white;
          padding: 16px 20px;
          border-radius: 16px;
          min-width: 140px;
          box-shadow: 0 2px 8px rgba(0,102,204,0.2);
        }

        .price-amount {
          font-size: 22px;
          font-weight: bold;
          margin-bottom: 4px;
        }

        .price-label {
          font-size: 12px;
          opacity: 0.9;
          text-transform: uppercase;
          letter-spacing: 0.5px;
        }

        /* Индикатор печатания */
        .typing {
          background-color: #fafbfc !important;
          color: #333 !important;
          border: 1px solid #f0f0f0 !important;
        }

        .typing-indicator {
          display: flex;
          align-items: center;
          gap: 8px;
        }

        .typing-dots {
          display: flex;
          gap: 3px;
        }

        .dot {
          width: 6px;
          height: 6px;
          border-radius: 50%;
          background-color: #999;
          animation: pulse 1.4s infinite;
        }

        .dot:nth-child(2) {
          animation-delay: 0.2s;
        }

        .dot:nth-child(3) {
          animation-delay: 0.4s;
        }

        @keyframes pulse {
          0%, 60%, 100% {
            transform: scale(1);
            opacity: 0.5;
          }
          30% {
            transform: scale(1.2);
            opacity: 1;
          }
        }

        /* Поле ввода */
        .input-area {
          padding: 16px 20px;
          background-color: #fafbfc;
          border-top: 1px solid #f0f0f0;
          flex-shrink: 0;
        }

        .input-container {
          display: flex;
          gap: 12px;
          align-items: flex-end;
          max-width: 100%;
        }

        .message-input {
          flex: 1;
          padding: 12px 16px;
          border: 1px solid #e8e8e8;
          border-radius: 20px;
          font-size: 14px;
          font-family: inherit;
          outline: none;
          transition: all 0.2s ease;
          resize: none;
          min-height: 44px;
          max-height: 120px;
          line-height: 1.4;
          overflow-y: auto;
          color: #333;
          background-color: white;
        }

        .message-input:focus {
          border-color: #0066cc;
          box-shadow: 0 0 0 3px rgba(0,102,204,0.1);
          background-color: white;
        }

        .message-input::placeholder {
          color: #6c757d;
        }

        .send-button {
          width: 44px;
          height: 44px;
          border-radius: 50%;
          background: linear-gradient(135deg, #0066cc, #00aaff);
          color: white;
          border: none;
          cursor: pointer;
          display: flex;
          align-items: center;
          justify-content: center;
          transition: all 0.2s ease;
          box-shadow: 0 2px 8px rgba(0,102,204,0.25);
          flex-shrink: 0;
        }

        .send-button:hover:not(:disabled) {
          transform: translateY(-1px);
          box-shadow: 0 4px 12px rgba(0,102,204,0.35);
        }

        .send-button:active {
          transform: translateY(0);
        }

        .send-button:disabled {
          background: #d3d3d3;
          cursor: not-allowed;
          box-shadow: none;
        }

        /* Адаптивность для планшетов */
        @media (max-width: 768px) {
          .chat-header {
            padding: 10px 15px;
          }
          
          .logo-icon {
            width: 36px;
            height: 36px;
            font-size: 18px;
          }
          
          .logo-text {
            font-size: 20px;
          }
          
          .user-avatar {
            width: 32px;
            height: 32px;
            font-size: 14px;
          }
          
          .messages-area {
            padding: 15px;
            gap: 12px;
          }
          
          .message-content {
            max-width: 85%;
          }
          
          .ticket-card {
            padding: 16px;
            min-width: 240px;
          }
          
          .city-name {
            font-size: 16px;
          }
          
          .ticket-price {
            font-size: 18px;
          }

          .tour-option {
            flex-direction: column;
            align-items: stretch;
            gap: 16px;
          }

          .tour-price {
            align-self: center;
          }

          .hotel-name {
            font-size: 18px;
            min-width: auto;
          }
          
          .input-area {
            padding: 12px 15px;
          }
        }

        /* Адаптивность для мобильных устройств */
        @media (max-width: 480px) {
          .chat-header {
            padding: 8px 12px;
          }
          
          .logo-icon {
            width: 32px;
            height: 32px;
            font-size: 16px;
          }
          
          .logo-text {
            font-size: 18px;
          }
          
          
        .user-avatar {
          width: 36px;
          height: 36px;
          background-color: rgba(255,255,255,0.3);
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-weight: bold;
          font-size: 16px;
          flex-shrink: 0;
        }

        /* Контейнер чата */
        .chat-container {
          flex: 1;
          display: flex;
          flex-direction: column;
          max-width: 1000px;
          margin: 0 auto;
          width: 100%;
          min-height: 0;
        }

        .messages-area {
          flex: 1;
          overflow-y: auto;
          padding: 20px;
          display: flex;
          flex-direction: column;
          gap: 16px;
          scroll-behavior: smooth;
        }

        /* Стили для скроллбара */
        .messages-area::-webkit-scrollbar {
          width: 6px;
        }

        .messages-area::-webkit-scrollbar-track {
          background: transparent;
        }

        .messages-area::-webkit-scrollbar-thumb {
          background: #ccc;
          border-radius: 3px;
        }

        .messages-area::-webkit-scrollbar-thumb:hover {
          background: #999;
        }

        /* Сообщения */
        .message-wrapper {
          display: flex;
          width: 100%;
          margin-bottom: 4px;
        }

        .message-wrapper.user {
          justify-content: flex-end;
        }

        .message-wrapper.ai {
          justify-content: flex-start;
        }

        .message-content {
          display: flex;
          max-width: 80%;
          gap: 10px;
          align-items: flex-start;
        }

        .message-wrapper.user .message-content {
          flex-direction: row-reverse;
        }

        .message-avatar {
          width: 36px;
          height: 36px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-weight: bold;
          font-size: 14px;
          color: white;
          flex-shrink: 0;
        }

        .message-wrapper.user .message-avatar {
          background-color: #0066cc;
        }

        .message-wrapper.ai .message-avatar {
          background-color: #00aaff;
        }

        .message-wrapper.error .message-avatar {
          background-color: #dc3545;
        }

        .message-bubble {
          border-radius: 18px;
          padding: 12px 16px;
          box-shadow: 0 1px 3px rgba(0,0,0,0.08);
          position: relative;
          max-width: 100%;
          word-wrap: break-word;
          overflow-wrap: break-word;
        }

        .message-wrapper.user .message-bubble {
          background-color: #0066cc;
          color: white;
          border-bottom-right-radius: 4px;
        }

        .message-wrapper.ai .message-bubble {
          background-color: #e6f2ff;
          color: #333;
          border-bottom-left-radius: 4px;
          border: 1px solid #e0e0e0;
        }

        .message-wrapper.error .message-bubble {
          background-color: #f8d7da;
          color: #721c24;
          border: 1px solid #f5c6cb;
        }

        .message-text {
          line-height: 1.5;
          white-space: pre-wrap;
          word-break: break-word;
        }

        .message-time {
          font-size: 11px;
          opacity: 0.7;
          margin-top: 6px;
        }

        .message-wrapper.user .message-time {
          text-align: right;
        }

        /* Билетные карточки */
        .tickets-container {
          max-width: 100%;
          display: flex;
          flex-direction: column;
          gap: 12px;
        }

        .ticket-card {
          border: 1px solid #f0f0f0;
          border-radius: 16px;
          padding: 20px;
          background: linear-gradient(135deg, #ffffff, #fafbfc);
          box-shadow: 0 2px 8px rgba(0,0,0,0.06);
          transition: transform 0.2s ease;
          min-width: 280px;
        }

        .ticket-card:hover {
          transform: translateY(-2px);
          box-shadow: 0 4px 16px rgba(0,102,204,0.1);
        }

        .ticket-airline {
          font-size: 14px;
          color: #6c757d;
          margin-bottom: 12px;
          font-weight: 500;
          text-transform: uppercase;
          letter-spacing: 0.5px;
        }

        .ticket-route {
          display: flex;
          align-items: center;
          justify-content: space-between;
          margin-bottom: 16px;
          gap: 10px;
        }

        .ticket-city {
          flex: 1;
          text-align: center;
          min-width: 0;
        }

        .city-name {
          font-size: 18px;
          font-weight: bold;
          color: #333;
          margin-bottom: 4px;
          overflow: hidden;
          text-overflow: ellipsis;
        }

        .city-time {
          font-size: 12px;
          color: #6c757d;
          word-break: break-word;
        }

        .ticket-arrow {
          flex: 0 0 auto;
          text-align: center;
          position: relative;
          margin: 0 15px;
          min-width: 60px;
        }

        .arrow-line {
          height: 2px;
          background: linear-gradient(to right, #0066cc, #00aaff);
          margin: 10px 0;
          position: relative;
        }

        .arrow-head {
          color: #0066cc;
          font-size: 18px;
          font-weight: bold;
          position: absolute;
          right: -10px;
          top: -15px;
        }

        .flight-duration {
          font-size: 11px;
          color: #6c757d;
          margin-top: 8px;
          white-space: nowrap;
        }

        .ticket-price {
          text-align: center;
          padding: 12px;
          background: linear-gradient(135deg, #0066cc, #00aaff);
          border-radius: 12px;
          color: white;
          font-size: 20px;
          font-weight: bold;
        }

        /* Индикатор печатания */
        .typing {
          background-color: #fafbfc !important;
          color: #333 !important;
          border: 1px solid #f0f0f0 !important;
        }

        .typing-indicator {
          display: flex;
          align-items: center;
          gap: 8px;
        }

        .typing-dots {
          display: flex;
          gap: 3px;
        }

        .dot {
          width: 6px;
          height: 6px;
          border-radius: 50%;
          background-color: #999;
          animation: pulse 1.4s infinite;
        }

        .dot:nth-child(2) {
          animation-delay: 0.2s;
        }

        .dot:nth-child(3) {
          animation-delay: 0.4s;
        }

        @keyframes pulse {
          0%, 60%, 100% {
            transform: scale(1);
            opacity: 0.5;
          }
          30% {
            transform: scale(1.2);
            opacity: 1;
          }
        }

        /* Поле ввода */
        .input-area {
          padding: 16px 20px;
          background-color: #fafbfc;
          border-top: 1px solid #f0f0f0;
          flex-shrink: 0;
        }

        .input-container {
          display: flex;
          gap: 12px;
          align-items: flex-end;
          max-width: 100%;
        }

        .message-input {
          flex: 1;
          padding: 12px 16px;
          border: 1px solid #e8e8e8;
          border-radius: 20px;
          font-size: 14px;
          font-family: inherit;
          outline: none;
          transition: all 0.2s ease;
          resize: none;
          min-height: 44px;
          max-height: 120px;
          line-height: 1.4;
          overflow-y: auto;
          color: #333;
          background-color: white;
        }

        .message-input:focus {
          border-color: #0066cc;
          box-shadow: 0 0 0 3px rgba(0,102,204,0.1);
          background-color: white;
        }

        .message-input::placeholder {
          color: #6c757d;
        }

        .send-button {
          width: 44px;
          height: 44px;
          border-radius: 50%;
          background: linear-gradient(135deg, #0066cc, #00aaff);
          color: white;
          border: none;
          cursor: pointer;
          display: flex;
          align-items: center;
          justify-content: center;
          transition: all 0.2s ease;
          box-shadow: 0 2px 8px rgba(0,102,204,0.25);
          flex-shrink: 0;
        }

        .send-button:hover:not(:disabled) {
          transform: translateY(-1px);
          box-shadow: 0 4px 12px rgba(0,102,204,0.35);
        }

        .send-button:active {
          transform: translateY(0);
        }

        .send-button:disabled {
          background: #d3d3d3;
          cursor: not-allowed;
          box-shadow: none;
        }

        /* Адаптивность для планшетов */
        @media (max-width: 768px) {
          .chat-header {
            padding: 10px 15px;
          }
          
          .logo-icon {
            width: 36px;
            height: 36px;
            font-size: 18px;
          }
          
          .logo-text {
            font-size: 20px;
          }
          
          .user-avatar {
            width: 32px;
            height: 32px;
            font-size: 14px;
          }
          
          .messages-area {
            padding: 15px;
            gap: 12px;
          }
          
          .message-content {
            max-width: 85%;
          }
          
          .ticket-card {
            padding: 16px;
            min-width: 240px;
          }
          
          .city-name {
            font-size: 16px;
          }
          
          .ticket-price {
            font-size: 18px;
          }
          
          .input-area {
            padding: 12px 15px;
          }
        }

        /* Адаптивность для мобильных устройств */
        @media (max-width: 480px) {
          .chat-header {
            padding: 8px 12px;
          }
          
          .logo-icon {
            width: 32px;
            height: 32px;
            font-size: 16px;
          }
          
          .logo-text {
            font-size: 18px;
          }
          
          .user-avatar {
            width: 28px;
            height: 28px;
            font-size: 12px;
          }
          
          .messages-area {
            padding: 12px;
          }
          
          .message-content {
            max-width: 95%;
          }
          
          .message-avatar {
            width: 32px;
            height: 32px;
            font-size: 12px;
          }
          
          .message-bubble {
            padding: 10px 12px;
            border-radius: 16px;
          }
          
          .ticket-card {
            padding: 12px;
            min-width: 200px;
            margin: 0;
          }
          
          .ticket-route {
            flex-direction: column;
            gap: 8px;
            text-align: center;
          }
          
          .ticket-arrow {
            transform: rotate(90deg);
            margin: 8px 0;
          }
          
          .city-name {
            font-size: 14px;
          }
          
          .city-time {
            font-size: 11px;
          }
          
          .ticket-price {
            font-size: 16px;
            padding: 10px;
          }
          
          .input-area {
            padding: 10px 12px;
          }
          
          .input-container {
            gap: 8px;
          }
          
          .message-input {
            padding: 10px 12px;
            font-size: 16px; /* Предотвращает зум на iOS */
            border-radius: 18px;
          }
          
          .send-button {
            width: 40px;
            height: 40px;
          }
        }

        /* Для очень маленьких экранов */
        @media (max-width: 320px) {
          .logo-text {
            display: none;
          }
          
          .ticket-card {
            min-width: 180px;
          }
          
          .city-name {
            font-size: 13px;
          }
          
          .ticket-price {
            font-size: 14px;
          }
        }

        /* Улучшения для сенсорных устройств */
        @media (hover: none) and (pointer: coarse) {
          .send-button:hover {
            transform: none;
          }
          
          .ticket-card:hover {
            transform: none;
          }
          
          .message-input {
            font-size: 16px; /* Предотвращает зум */
          }
        }

        /* Темная тема для устройств с предпочтением темной темы */
        @media (prefers-color-scheme: dark) {
          .chat-app {
            background-color: #f0f8ff;
          }
          
          .message-wrapper.ai .message-bubble {
            background-color: #e6f2ff;
            color: #333;
            border-color: #e0e0e0;
          }
          
          .input-area {
            background-color: #fafbfc;
            border-top-color: #f0f0f0;
          }
          
          .message-input {
            background-color: white;
            color: #333;
            border-color: #e8e8e8;
          }
          
          .message-input:focus {
            background-color: white;
          }
          
          .message-input::placeholder {
            color: #999;
          }
        }
      `}</style>
    </div>
  );
}

export default Chat;