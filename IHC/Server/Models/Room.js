class Room{
    constructor(id,host,numberQuestions){
        this.id=id;
        this.host=host;
        this.users=new Array();
        this.numberQuestions=numberQuestions;
        this.numberReady=0;
        this.questionsAdded=0;
        this.questions=new Array();
        this.round=0;   
    }
};

module.exports = Room;