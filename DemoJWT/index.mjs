import express, { json } from 'express';
import jwt from 'jsonwebtoken';

const app = express();
app.use(json());

const JWT_SECRET = 'key';
const JWT_EXPIRES_IN = '20m';


// Database
const users = [
  {
    id: 1,
    username: 'tune',
    password: "12345"
  }
];

app.post('/login', async (req, res) => {
  if(req.body === undefined) {
    return  res.status(400).json({ message: 'Missing required data' });
  }

  const { username, password } = req.body;

  const user = users.find(u => u.username === username);
  if (!user) {
    return res.status(401).json({ message: 'Invalid credentials' });
  }

  const isValidPassword = password === user.password
  if (!isValidPassword) {
    return res.status(401).json({ message: 'Invalid credentials' });
  }

  const token = jwt.sign(
    { 
      userId: user.id, 
      username: user.username 
    },
    JWT_SECRET,
    { expiresIn: JWT_EXPIRES_IN }
  );

  res.json({
    message: 'Login successful',
    token,
    expiresIn: JWT_EXPIRES_IN
  });
});

const authenticateToken = (req, res, next) => {
  const authHeader = req.headers['authorization'];
  const token = authHeader && authHeader.split(' ')[1];

  if (!token) {
    return res.status(401).json({ message: 'Access token required' });
  }

  jwt.verify(token, JWT_SECRET, (err, decoded) => {
    if (err) {
      return res.status(403).json({ message: 'Invalid or expired token' });
    }
    req.user = decoded;
    next();
  });
};

app.get('/protected', authenticateToken, (req, res) => {
  res.json({
    message: 'Access granted to protected resource',
    user: req.user
  });
});

const PORT = 3000;
app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
});