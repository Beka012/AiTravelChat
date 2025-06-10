import '../Home.css';
import { Link } from 'react-router-dom';

function Home() {
  return (
    <>
      <header>
        <div className="container">
          <div className="logo">
            <div className="logo-icon">T</div>
            Travel AI
          </div>
          <p>Your AI-Powered Travel Companion</p>

          {/* Кнопка входа/регистрации */}
          <Link to="/auth">
            <button className="auth-button" style={{ marginTop: '20px' }}>Login / Register</button>
          </Link>
        </div>
      </header>

      <section className="about-section">
        <div className="container">
          <h2 className="section-title">About Travel AI</h2>
          <div className="about-content">
            <p>Travel AI is a revolutionary platform that combines cutting-edge artificial intelligence with comprehensive travel services to create the ultimate travel companion. Our mission is to make travel planning effortless, personalized, and enjoyable for everyone.</p>
            <p>Founded in 2024 by a team of travel enthusiasts and AI specialists, Travel AI was born from a simple idea: what if planning your perfect trip was as easy as having a conversation? Today, we're proud to serve thousands of travelers worldwide with our intelligent travel assistant.</p>
            <p>Our AI technology searches across hundreds of travel sites to find you the best deals on flights, hotels, and tour packages. But we're more than just a booking platform – we're your personal travel advisor, ready to answer questions about destinations, suggest activities based on your interests, and help you navigate local customs and traditions.</p>
          </div>
        </div>
      </section>

      <section className="benefits">
        <div className="container">
          <h2 className="section-title">Why Choose Travel AI?</h2>
          <div className="benefits-grid">
            <div className="benefit-card">
              <div className="benefit-icon">🔍</div>
              <h3>Smart Search</h3>
              <p>Our AI scans hundreds of travel sites in seconds to find you the best deals on flights, hotels, and packages, saving you both time and money.</p>
            </div>
            <div className="benefit-card">
              <div className="benefit-icon">💬</div>
              <h3>24/7 AI Assistant</h3>
              <p>Have questions about a destination? Need restaurant recommendations? Our AI assistant is available anytime to provide instant, knowledgeable responses.</p>
            </div>
            <div className="benefit-card">
              <div className="benefit-icon">🎯</div>
              <h3>Personalized Recommendations</h3>
              <p>The more you use Travel AI, the better it understands your preferences, providing increasingly tailored travel suggestions just for you.</p>
            </div>
            <div className="benefit-card">
              <div className="benefit-icon">💰</div>
              <h3>Best Price Guarantee</h3>
              <p>We're so confident in our AI's ability to find the best prices that we offer a price-match guarantee on all bookings.</p>
            </div>
            <div className="benefit-card">
              <div className="benefit-icon">🔒</div>
              <h3>Secure Booking</h3>
              <p>Book with confidence knowing that all your personal and payment information is protected by industry-leading security measures.</p>
            </div>
            <div className="benefit-card">
              <div className="benefit-icon">🌎</div>
              <h3>Global Coverage</h3>
              <p>Whether you're dreaming of popular destinations or off-the-beaten-path adventures, our database covers over 190 countries worldwide.</p>
            </div>
          </div>
        </div>
      </section>

      <section className="vision-section">
        <div className="container">
          <h2 className="section-title">Our Vision</h2>
          <div className="vision-content">
            <p>We envision a world where travel is accessible, stress-free, and enriching for everyone. By harnessing the power of artificial intelligence, we're creating a future where language barriers, uncertainty, and complicated planning processes no longer stand in the way of incredible travel experiences.</p>
            <p>Join us on our journey to revolutionize the way people discover and explore our beautiful world.</p>
          </div>
        </div>
      </section>

      <footer>
        <div className="container">
          <p className="copyright">
            © 2025 Travel AI. All rights reserved.
          </p>
        </div>
      </footer>
    </>
  );
}

export default Home;
