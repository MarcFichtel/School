/*
BACKEND TODOs:
* Clean up some code

DONE:
* Message time stamps formatted by client
* Use cookies to remember user nicknames for a session
* Indicate nicknames to users (i.e. at top of screen)
* Users are assigned unique nicknames
* Server logs last 200 messages in memory
* New users can see the whole chat log
* Show all currently connected users
* Message time stamps calculated by server
* Messages are shown with user nicknames
* Messages by users should be bold
* Support at least 5 concurrent users
* Update user list on nickname change
* Users can change nicknames with /nick <new_name>
* Nickname change fails if name is already taken
* Users can change nickname color with /nickcolor RRGGBB
* All subsequent messages are shown with new nickname color (no need to update old ones)
* Chat log has vertical scrollbar
* Chat log is bottom aligned
* Attractive
* Responsive
* App works in FF & GC
*/

// Import dependencies
var express = require('express');
var app = express();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var datetime = require('node-datetime');

// User setup
let idCounter = 0;              // id counter
let users = [];                 // array of sockets
let nicknames = [];             // array of users
let activeUsers = [];           // array of active users
let colors = [];                // array of user colors
let log = [];                   // chat log
const LOG_SIZE = 200;           // chat log size
const DEF_COLOR = '#dfe0de';    // default message color

// Send website to client
app.get('/', function(req, res){
    res.sendFile(__dirname + '/index.html');
});

// User connected
io.on('connection', function(socket){

    // This is a new user
    socket.on('new user', function() {

        // Create and assign a unique nickname and default color
        nicknames[idCounter] = generateNickname(idCounter + 1);
        users[idCounter] = socket;
        activeUsers[idCounter] = nicknames[idCounter];
        colors[idCounter] = DEF_COLOR;
        socket.emit('nickname', nicknames[idCounter]);

        // Send log and update user list
        sendLog(socket);
        sendMessage('connected', activeUsers[idCounter], colors[idCounter]);
        updateUsers();
        idCounter++;
    });

    // If user was previously connected, send existing nickname
    socket.on('old user', function(username) {

        // Add user back into system with existing nickname
        let userId;
        if (nicknames.includes(username)) {
            userId = nicknames.indexOf(username);
            users[userId] = socket;
            activeUsers[userId] = username;
        } else {
            userId = idCounter;
            nicknames[userId] = username;
            users[userId] = socket;
            activeUsers[userId] = nicknames[userId];
            colors[userId] = DEF_COLOR;
            idCounter++;
        }

        // Send log and update user list
        sendLog(socket);
        sendMessage('connected', username, colors[userId]);
        updateUsers();
    });

    // User sent a message
    socket.on('chat message', function(msg, username){
        let userId = getUserID(socket);

        // User wants to change their color
        if (msg.includes('/nickcolor')) {
            let newColor = msg.substr(11, msg.length - 11);
            colors[userId] = newColor;
            sendMessage(msg, username, colors[userId]);
        }

        // User wants to change their nickname
        else if (msg.includes('/nick')) {
            let newName = msg.substr(6, msg.length - 6);

            // If new name is not yet taken, change it, else report error
            if (!nicknames.includes(newName)) {
                nicknames[userId] = newName;
                activeUsers[userId] = newName;
                socket.emit('nickname', newName);
                updateUsers();
                sendMessage(msg, newName, colors[userId]);
                sendMessage(username + ' changed their name to ' + newName, newName, colors[userId]);
            } else if (nicknames.includes(newName) && nicknames[userId] === newName) {
                sendMessage(msg, username, colors[userId]);
                sendMessage('Yep, that is your name.', username, colors[userId]);
            } else {
                sendMessage(msg, username, colors[userId]);
                sendMessage(newName + ' is already taken.', username, colors[userId]);
            }
        }

        // Display error for unrecognized chat commands
        else if (msg.substr(0,1) === '/') {
            sendMessage(msg, username, colors[userId]);
            sendMessage('Command was not recognized. Try something else like /nick or /nickcolor.', username, colors[userId]);
        }

        // Just a plain chat message
        else {
            sendMessage(msg, username, colors[userId]);
        }

    });

    // User disconnected, update user list
    socket.on('disconnect', function(){
        let userId = getUserID(socket);
        sendMessage('disconnected', activeUsers[userId], colors[userId]);
        activeUsers[userId] = null;
        updateUsers();
    });
});

// Start server on localhost port 3000
http.listen(3000, function(){
    console.log('listening on *:3000');
});

// Get user id of a socket
function getUserID(socket) {
    return users.indexOf(socket);
}

// Get current date time
function getCurrentDateTime() {
    let dt = datetime.create();
    return dt.format('d.m.Y at H:M:S');
}

// Generate a unique username
function generateNickname(idCounter) {
    return 'User ' + idCounter;
}

// Send a message to everyone
function sendMessage(msg, user, col) {

    // Create message object
    let message = {
        text: convertEmoji(msg),
        date: getCurrentDateTime(),
        from: user,
        color: col
    };

    // If log is full, discard oldest message
    if (log.length >= LOG_SIZE) {
        log.shift();
    }

    // Send and log message
    io.emit('chat message', message);
    log.push(message);
}

// Convert smiley faces to code
// TODO add ALL the smileys
function convertEmoji(msg) {
    return msg
        .replace('=)', '<i class="em em-slightly_smiling_face"></i>')
        .replace('=(', '<i class="em em-frowning"></i>')
        .replace('>=(', '<i class="em em-angry"></i>')
        .replace('<=(', '<i class="em em-anguished"></i>')
        .replace('<=O', '<i class="em em-astonished"></i>')
        .replace('=/', '<i class="em em-confused"></i>')
        .replace('shit', '<i class="em em-hankey"></i>')    // You thought you could swear in MY app?!?
        .replace('=*(', '<i class="em em-cry"></i>')
        .replace('xD', '<i class="em em-laughing"></i>')
        .replace('8)', '<i class="em em-sunglasses"></i>');

}

// Send all logged messages to new users
function sendLog(socket) {
    for (let i = 0; i < log.length; i++) {
        socket.emit('chat message', log[i]);
    }
}

// Update user list
function updateUsers() {
    io.emit('user change', activeUsers);
}
