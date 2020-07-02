class User {
    constructor(socket, username) {
        this.socket=socket;
        this.username=username;
        this.points=0;
        this.powerUpOnMe=[];      
        this.acertou=false;
        this.pointsToBeAdded=0;
    }
};

module.exports = User;