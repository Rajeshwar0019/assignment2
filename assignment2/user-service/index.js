const express = require('express');
const app = express();
const port = 7001;  // Container listens on port 7001

app.get('/', (req, res) => {
  res.send('User Service is running on port ' + port);
});

app.listen(port, () => {
  console.log(`User service listening on port ${port}`);
});
