const User = require('./Models/User.js');
const Room = require('./Models/Room.js');
const Ownership = require('./Models/Ownership.js');
const PowerArray = ['StopWatch', 'Steal', 'Stop'];

var arrayRooms = [];
var id = 0;

var io = require('socket.io')(process.env.PORT || 3000);
//var io = require('socket.io')(3000);
const shuffle = (array) => {
    for (let i = (array.length - 1); i > 0; i--) {
        const j = Math.floor(Math.random() * i)
        const temp = array[i]
        array[i] = array[j]
        array[j] = temp
    }
    return array;
}

const newArray = (array, id) => {
    const temp = new Array()
    for (let i = 0; i < array.length; i++) {
        //console.log("ID PARAMETRO: "+id+ " ID ARRAY: "+ array.socketId)
        if (id != array[i].socketId) {
            temp.push(array[i].question);
        }
    }
    return shuffle(temp);
}

const getUsernames = (usersArray, socketId) => {
    const temp = new Array();
    for (let i = 0; i < usersArray.length; i++) {
        if (usersArray[i].socket.id != socketId) {
            temp.push(usersArray[i].username);
        }
    }
    return temp;
}


io.on('connection', socket => {
    console.log(socket.id);
    socket.emit('validation', { hello: 'ca estamos' });
    socket.on('host', function (data) {
        const user = new User(socket, data.username, 0);
        //console.log(user);
        const room = new Room(id, user, data.numberQuestions);
        room.users.push(user);
        //console.log(room)
        arrayRooms.push(room);
        //  console.log(arrayRooms);
        socket.emit('roomId', { roomId: room.id });
        id = id + 1;
    });

    socket.on('join', function (data) {
        const user = new User(socket, data.username, 0);
        console.log(data.roomId);
        var room = arrayRooms.find(function (room) { return room.id == data.roomId });
        if (room != undefined) {
            if (room.users.findIndex(u => u.username === user.username) == -1) {
                room.users.push(user);
                socket.emit('joinResult', { bool: true })
                room.host.socket.emit('playerJoined');
            } else {
                socket.emit('joinResult', { bool: false })
            }
        }
        //Room não existe
    });
    socket.on('StartGame', function (data) {
        var room = arrayRooms.find(function (room) { return room.host.socket.id == socket.id });
        if (room != undefined) {
            for (let i = 0; i < room.users.length; i++) {
                room.users[i].socket.emit('MakeQuestions', data)
            }
        }
    });
    socket.on('Ready', function (data) {
        console.log(data.roomId);
        var room = arrayRooms.find(function (room) { return room.id == data.roomId });
        room.numberReady = room.numberReady + 1;
        console.log("Number Ready: " + room.numberReady);
        console.log("Length: " + room.users.length);
        if (room.numberReady == room.users.length) {
            for (let i = 0; i < room.users.length; i++) {
                room.users[i].socket.emit('GimmeQuestions');
            }

        }
    });
    socket.on('RemoveReady', function (data) {
        var room = arrayRooms.find(function (room) { return room.id == data.roomId });
        room.numberReady = room.numberReady - 1;
    });

    socket.on('Questions', function (data) {
        var room = arrayRooms.find(function (room) { return room.id == data.roomId });
        for (let i = 0; i < data.questions.length; i++) {
            room.questions.push(new Ownership(data.questions[i], socket.id))
            room.questionsAdded = room.questionsAdded + 1;
        }
        //console.log(room.questionsAdded)
        //console.log((room.numberQuestions * (room.users.length + 1)))
        if (room.questionsAdded == (room.numberQuestions * room.users.length)) {
            for (i = 0; i < room.users.length; i++) {
                let usernameArray = getUsernames(room.users, room.users[i].socket.id);
                let questionsArray = newArray(room.questions, room.users[i].socket.id);
                room.users[i].socket.emit('GameQuestions', { questions: questionsArray, usernameArray: usernameArray });
            }
        }
    });

    socket.on('EndGame', function (data) {
        var room = arrayRooms.find(function (room) { return room.id == data.roomId });
        /*for (let i = 0; i < room.users.length; i++) {
            if (room.users[i].socket.id == socket.id) {
                room.users[i].points = data.points;
                room.numberReady = room.numberReady - 1;
            }
        }
        console.log(room.numberReady);*/
        room.numberReady = room.numberReady - 1;
        //Todos enviaram pontos
        if (room.numberReady == 0) {
            //Ordernar array
            room.users.sort(function (a, b) {
                return b.points - a.points;
            });
            //Enviar para todos
            let top3 = new Array();
            for (let i = 0; i < 3; i++) {
                if (room.users[i] != undefined)
                    top3.push({ username: room.users[i].username, points: room.users[i].points });
            }
            for (i = 0; i < room.users.length; i++) {
                let place = room.users.findIndex(function (user) { return user.socket.id == room.users[i].socket.id });
                room.users[i].socket.emit('Top', { points: room.users[i].points, top3: top3, place: place + 1 });
            }
        }
    });


    socket.on('roundEnded', function (data) {
        var room = arrayRooms.find(function (room) { return room.id == data.roomId });
        var userResponsible = room.users.find(function (user) { return user.socket.id == socket.id });
    
        userResponsible.powerUpOnMe = userResponsible.powerUpOnMe.filter(powerUp => {
            return powerUp.round >= room.round;
        });
        if (data.pointsAdded > 0) {
            userResponsible.acertou = true;
            userResponsible.pointsToBeAdded=data.pointsAdded;
        }
        for (let i = 0; i < data.powerOnThem.length; i++) {
            var user = room.users.find(function (user) { return user.username == data.powerOnThem[i].username })
            user.powerUpOnMe.push({ usedBy: userResponsible.username, power: data.powerOnThem[i].power, round: room.round })
        }
        room.numberReady = room.numberReady - 1;

        if (room.numberReady == 0) {
            //Atribuir PowerUps e pontos
            for (let i = 0; i < room.users.length; i++) {
                let stolen =false;
                room.users[i].powerUpOnMe.forEach(powerUp => { 
                    if (powerUp.power === 'Steal') {
                        let findUser=room.users.find(function (user) { return user.username == powerUp.usedBy });
                        findUser.points += room.users[i].pointsToBeAdded ;
                        stolen = true
                    }
                });
                if(!stolen){
                    room.users[i].points+= room.users[i].pointsToBeAdded;
                }else{
                    console.log(room.users[i].username+" Got Stolen")
                    room.users[i].powerUpOnMe.push({usedBy: "",power: 'stolen',round:room.round});
                }
                room.users[i].pointsToBeAdded=0;
            }
            for (let i = 0; i < room.users.length; i++) {
                let powerUpIndex = Math.floor(Math.random() * 3);
                let receivedPower = undefined;
                if (room.users[i].acertou) {
                    receivedPower = PowerArray[powerUpIndex];
                } else {
                    if (Math.random() > 0.4) {//Ganha Power Up
                        receivedPower = PowerArray[powerUpIndex];
                    }
                }

                room.users[i].socket.emit('goNext', { powerUp: room.users[i].powerUpOnMe, points: room.users[i].points,receivedPower:receivedPower });
                room.users[i].acertou = false;
    
            }
            room.numberReady = room.users.length;
            room.round += 1;
        }

    });


    socket.on('disconect', (data) => {
        var room = arrayRooms.find(function (room) { return room.id == data.roomId });
        let index = room.users.findIndex(function (user) { return user.socket.id == socket.id });
        if (index > -1) {
            room.users.splice(index, 1);
        }
        socket.disconnect(true);
    })





});
