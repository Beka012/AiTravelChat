import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '/src/Auth.css'; 

function Auth() {
  const [formType, setFormType] = useState('login');
  const navigate = useNavigate();

  const [registerForm, setRegisterForm] = useState({
    fullName: '',
    email: '',
    password: '',
    confirmPassword: '',
    role: 'ROLE_USER',
  });

  const handleRegisterChange = (e) => {
    setRegisterForm({ ...registerForm, [e.target.name]: e.target.value });
  };

  const validateEmail = (email) =>
    /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);

  const validatePassword = (password) => password.length >= 6;

  const handleRegisterSubmit = async (e) => {
    e.preventDefault();
    const { fullName, email, password, confirmPassword, role } = registerForm;

    if (!validateEmail(email)) {
      alert('Invalid email format.');
      return;
    }

    if (!validatePassword(password)) {
      alert('Password must be at least 6 characters.');
      return;
    }

    if (password !== confirmPassword) {
      alert('Passwords do not match.');
      return;
    }

    try {
      const response = await fetch('http://localhost:8087/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ fullName, email, password, role }),
      });

      if (!response.ok) throw new Error('Registration failed');
      const message = await response.text();
      alert(message);
      setFormType('login');
    } catch (error) {
      alert('Error: ' + error.message);
    }
  };

  // Логин
  const [loginForm, setLoginForm] = useState({
    email: '',
    password: '',
  });

  const handleLoginChange = (e) => {
    setLoginForm({ ...loginForm, [e.target.name]: e.target.value });
  };

  const handleLoginSubmit = async (e) => {
    e.preventDefault();
    const { email, password } = loginForm;

    try {
      const response = await fetch('http://localhost:8087/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password }),
      });

      if (!response.ok) throw new Error('Login failed');
      const { accessToken, refreshToken } = await response.json();

      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('refreshToken', refreshToken);
      
      const userName = email.split('@')[0];
      localStorage.setItem('userName', userName);
      
      alert('Login successful!');
      
      navigate('/chat');
    } catch (err) {
      alert('Error: ' + err.message);
    }
  };

  return (
    <div className="auth-section">
      <div className="container">
        <div className="auth-container">
          <div className="auth-image">
            <h2>Welcome to Travel AI</h2>
            <p>Join thousands of travelers who are using AI to plan their perfect trips. Sign up today and enjoy:</p>
            <ul className="feature-list">
              <li><div className="feature-icon">✓</div>24/7 AI travel assistant</li>
              <li><div className="feature-icon">✓</div>Personalized travel recommendations</li>
              <li><div className="feature-icon">✓</div>Exclusive deals on flights and hotels</li>
              <li><div className="feature-icon">✓</div>Save your favorite destinations</li>
              <li><div className="feature-icon">✓</div>Access to premium travel guides</li>
            </ul>
          </div>

          <div className="auth-forms">
            <div className="tabs">
              <div className={`tab ${formType === 'login' ? 'active' : ''}`} onClick={() => setFormType('login')}>Sign In</div>
              <div className={`tab ${formType === 'register' ? 'active' : ''}`} onClick={() => setFormType('register')}>Register</div>
            </div>

            {formType === 'login' && (
              <form className="form-container active" onSubmit={handleLoginSubmit}>
                <div className="input-group">
                  <label htmlFor="login-email">Email</label>
                  <input
                    type="email"
                    id="login-email"
                    name="email"
                    value={loginForm.email}
                    onChange={handleLoginChange}
                    placeholder="Enter your email"
                    required
                  />
                </div>
                <div className="input-group">
                  <label htmlFor="login-password">Password</label>
                  <input
                    type="password"
                    id="login-password"
                    name="password"
                    value={loginForm.password}
                    onChange={handleLoginChange}
                    placeholder="Enter your password"
                    required
                  />
                </div>
                <button className="auth-button" type="submit">Sign In</button>
              </form>
            )}

            {formType === 'register' && (
              <form className="form-container active" onSubmit={handleRegisterSubmit}>
                <div className="input-group">
                  <label htmlFor="register-name">Full Name</label>
                  <input
                    type="text"
                    id="register-name"
                    name="fullName"
                    value={registerForm.fullName}
                    onChange={handleRegisterChange}
                    placeholder="Enter your full name"
                    required
                  />
                </div>
                <div className="input-group">
                  <label htmlFor="register-email">Email</label>
                  <input
                    type="email"
                    id="register-email"
                    name="email"
                    value={registerForm.email}
                    onChange={handleRegisterChange}
                    placeholder="Enter your email"
                    required
                  />
                </div>
                <div className="input-group">
                  <label htmlFor="register-password">Password</label>
                  <input
                    type="password"
                    id="register-password"
                    name="password"
                    value={registerForm.password}
                    onChange={handleRegisterChange}
                    placeholder="Create a password"
                    required
                  />
                </div>
                <div className="input-group">
                  <label htmlFor="register-confirm-password">Confirm Password</label>
                  <input
                    type="password"
                    id="register-confirm-password"
                    name="confirmPassword"
                    value={registerForm.confirmPassword}
                    onChange={handleRegisterChange}
                    placeholder="Confirm your password"
                    required
                  />
                </div>
                <button className="auth-button" type="submit">Create Account</button>
              </form>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default Auth;