import React, { useState, useEffect, useRef } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Dimensions, Alert, Modal } from 'react-native';
import Colors from '../constants/colors';
import Answer from '../components/Answer';
import { FlatList } from 'react-native-gesture-handler';
import Tempo from '../assets/cronometro2.svg';
import Btn from '../components/Btn';
import Thief from '../assets/criminal.svg';
import Wrong from '../assets/wrong.svg';
import Cancel from '../assets/do-not-touch.svg';
import { withInAppNotification } from 'react-native-in-app-notification';
const defaultTimer = 15;
const defaultDelay = 1000;

function useInterval(callback, delay) {
    const savedCallback = useRef();

    // Remember the latest callback.
    useEffect(() => {
        savedCallback.current = callback;
    }, [callback]);

    // Set up the interval.
    useEffect(() => {
        function tick() {
            savedCallback.current();
        }
        if (delay !== null) {
            let id = setInterval(tick, delay);
            return () => clearInterval(id);
        }
    }, [delay]);
}

const GameScreen = props => {
    const [timer, setTimer] = useState(defaultTimer);
    const [isRunning, setIsRunning] = useState(true);
    const [delay, setDelay] = useState(defaultDelay);
    const [questionIndex, setQuestionIndex] = useState(0);
    const [userAnswer, setUserAnswer] = useState({ answer: -1, time: defaultTimer });
    const [points, setPoints] = useState(0);
    const [showModal, setShowModal] = useState(-1);
    const [myPowers, setMyPowers] = useState([0, 0, 0]);
    const [lockMode, setLockMode] = useState(false);

    const socket = props.navigation.getParam('socket');
    const roomId = props.navigation.getParam('roomId');
    const questions = props.navigation.getParam('questions');
    const usernameArray = props.navigation.getParam('usernameArray');

    const [powerOnThem, setPowerOnThem] = useState([]);

    const answerSelectedHandler = (index) => {
        if (isRunning && userAnswer.answer == -1 && !lockMode)
            setUserAnswer({ answer: index, time: timer });
        else
            console.log("Time ran out!")
    }

    const powerSelectedHandler = (index) => {
        if (!lockMode)
            setShowModal(index);
    }

    const cancelHandler = (showModal) => {
        setShowModal(-1);
    }

    const onSelectUsername = (username) => {
        var newPower = [...powerOnThem];
        var newMyPowers = [...myPowers];
        if (showModal == 0) {// Time 2x
            newPower.push({ power: 'StopWatch', username: username })
            newMyPowers[0] = 0;
        }
        else if (showModal == 1) {
            newPower.push({ power: 'Steal', username: username })
            newMyPowers[1] = 0;
        } else if (showModal == 2) {
            newPower.push({ power: 'Stop', username: username })
            newMyPowers[2] = 0;
        }
        setPowerOnThem(newPower);
        setMyPowers(newMyPowers);
        setShowModal(-1);

    }
    const renderUsernames = (itemData) => {
        return (
            <TouchableOpacity onPress={() => onSelectUsername(itemData.item)}>
                <View style={styles.userContainer}>
                    <Text style={styles.userText}>{itemData.item}</Text>
                </View>
            </TouchableOpacity>
        );
    }

    const HandlePowerUps = (array, receivedPower) => {
        let block = false;
        let stop = false;
        let stolen = false;
        for (let i = 0; i < array.length; i++) {
            if (array[i].power == 'Stop') {
                setLockMode(true);
                block = true;
            }
            if (array[i].power == 'StopWatch' && !block) {
                stop = true;
                setDelay(delay / 2);
            }
            if (array[i].power == 'stolen') {
                stolen = true;
            }

        }
        //Notificação
        if (block) {
            props.showNotification({
                title: 'Someone wants you mad!',
                message: 'You cant answer in this round!',
                icon: require('../assets/do-not-touch.png'),
                onPress: () => {
                    if (stolen) {
                        props.showNotification({
                            title: 'Get good kid!',
                            message: 'Someone stole your points!',
                            icon: require('../assets/criminal.png'),
                        });
                    }
                }
            });
        } else {
            if (stop) {
                props.showNotification({
                    title: 'Time is ticking!',
                    message: 'Time will go faster this round!',
                    icon: require('../assets/stopwatch.png'),
                    onPress: () => {
                        if (stolen) {
                            props.showNotification({
                                title: 'Get good kid!',
                                message: 'Someone stole your points!',
                                icon: require('../assets/criminal.png'),
                            });
                        }
                    }
                })
            }
            else {
                if (stolen) {
                    props.showNotification({
                        title: 'Get good kid!',
                        message: 'Someone stole your points!',
                        icon: require('../assets/criminal.png'),
                    });
                }

            }
        }

        switch (receivedPower) {
            case 'StopWatch':
                let novo0 = [...myPowers];
                novo0[0] = 1;
                setMyPowers(novo0)
                break;
            case 'Steal':
                let novo1 = [...myPowers];
                novo1[1] = 1;
                setMyPowers(novo1)
                break;
            case 'Stop':
                let novo2 = [...myPowers];
                novo2[2] = 1;
                setMyPowers(novo2);
                break;
            default:
                break;
        }


    }
    const nextQuestion = () => {
        let pointsAdded = 0;
        if (userAnswer.answer != -1 && questions[questionIndex].answers[userAnswer.answer].bool) {
            pointsAdded = 30 + userAnswer.time;
        }
        socket.emit('roundEnded', { pointsAdded, pointsAdded, roomId: roomId, powerOnThem: powerOnThem });
        socket.on('goNext', function (data) {
            setPoints(data.points);
            if (questionIndex + 1 < questions.length) {
                HandlePowerUps(data.powerUp, data.receivedPower);
                setPowerOnThem([]);
                setQuestionIndex(questionIndex + 1);
                setTimer(defaultTimer);
                setUserAnswer({ answer: -1, time: defaultTimer });
                setIsRunning(true);

            }
            else {
                console.log("Acabaram perguntas");
                socket.emit('EndGame', { roomId: roomId });
                socket.on('Top', function (data) {
                    console.log(data.top3);
                    console.log(data.place);
                    props.navigation.navigate({
                        routeName: 'GameOver',
                        params: {
                            socket: socket,
                            roomId: roomId,
                            top3: data.top3,
                            place: data.place,
                            points: data.points
                        }
                    });
                });
            }
            socket.removeListener('goNext');
        });
    }


    useInterval(() => {
        if (timer > 0)
            setTimer(timer - 1);
        else {
            setIsRunning(false);
            setDelay(defaultDelay);
            setLockMode(false);
            nextQuestion();
        }
    }, isRunning ? delay : null);



    return (
        <View style={styles.screen}>
            <Modal
                animationType="slide"
                transparent={true}
                visible={showModal != -1 ? true : false}
                onRequestClose={() => {
                    Alert.alert('Modal has been closed.');
                    setShowModal(-1);
                }}
            >
                <View style={{ flex: 0.5, alignItems: 'center', justifyContent: 'space-around', marginTop: 100, backgroundColor: Colors.bckcolor, borderWidth: 2, borderColor: 'black' }}>
                    <View style={{ flex: 1, justifyContent: 'center' }}>
                        {showModal == 0 && <Text style={styles.questionText}>Select target to reduce time:</Text>}
                        {showModal == 1 && <Text style={styles.questionText}>Select target to steal points:</Text>}
                        {showModal == 2 && <Text style={styles.questionText}>Select target to block answer:</Text>}
                    </View>

                    <View style={{ flex: 4, width: '100%', padding: 15 }}>
                        <FlatList
                            data={usernameArray}
                            keyExtractor={(item, index) => item.toString()}
                            renderItem={renderUsernames}
                            style={{ width: '100%' }}
                            contentContainerStyle={{ alignItems: 'center', width: '100%' }}
                        />
                    </View>

                    <View style={{ flex: 1, justifyContent: 'center' }}>
                        <Btn style={styles.btn} styleText={styles.btnText} onPress={cancelHandler}>Close</Btn>
                    </View>
                </View>
            </Modal>
            <View style={styles.headerContainer}>
                <View style={styles.sideHeaderContainer}>
                    <Text style={styles.headerText}>{questionIndex + 1}/{questions.length}</Text>
                    <Text style={styles.headerTitle}>questions</Text>
                </View>
                <View style={styles.tempo}>
                    <Text style={styles.timerText}>{timer}</Text>
                </View>
                <View style={styles.sideHeaderContainer}>
                    <Text style={styles.headerText}>{points}</Text>
                    <Text style={styles.headerTitle}>points</Text>
                </View>
            </View>
            <View style={styles.questionContainer}>
                <Text style={styles.questionText}>{questions[questionIndex].question}</Text>
            </View>
            <View style={[styles.answersContainer, { opacity: lockMode ? 0.4 : 1 }]} >
                <Answer animation={userAnswer.answer == -1 ? true : false} bool={userAnswer.answer == 0 ? true : false} answer={questions[questionIndex].answers[0]} answerSelected={() => answerSelectedHandler(0)} />
                <Answer animation={userAnswer.answer == -1 ? true : false} bool={userAnswer.answer == 1 ? true : false} answer={questions[questionIndex].answers[1]} answerSelected={() => answerSelectedHandler(1)} />
                <Answer animation={userAnswer.answer == -1 ? true : false} bool={userAnswer.answer == 2 ? true : false} answer={questions[questionIndex].answers[2]} answerSelected={() => answerSelectedHandler(2)} />
                <Answer animation={userAnswer.answer == -1 ? true : false} bool={userAnswer.answer == 3 ? true : false} answer={questions[questionIndex].answers[3]} answerSelected={() => answerSelectedHandler(3)} />
            </View>
            <Text style={styles.powerText}>Power-Ups</Text>
            <View style={[styles.powerupsContainer, { opacity: lockMode ? 0.4 : 1 }]}>
                <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
                    <View style={{ position: 'absolute' }}>
                        <TouchableOpacity onPress={() => { powerSelectedHandler(0) }} onLongPress={()=>{Alert.alert('Time reducer','This powerup allows you to reduce the target response time, next round',[{text: 'Close',style: 'destructive'}])}}>
                            <Tempo width={50} height={50} />
                        </TouchableOpacity>
                    </View>
                    {myPowers[0] == 0 &&
                        <View style={{ position: 'absolute' }}>
                            <TouchableOpacity onLongPress={()=>{Alert.alert('Time reducer','This powerup allows you to reduce the target response time, next round',[{text: 'Close',style: 'destructive'}])}} >
                                <Wrong width={50} height={50} />
                            </TouchableOpacity>
                        </View>}
                </View>
                <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
                    <View style={{ position: 'absolute' }}>
                        <TouchableOpacity  onPress={() => { powerSelectedHandler(1) }} onLongPress={()=>{Alert.alert('Thief','This powerup allows you to steal the points your target made this round',[{text: 'Close',style: 'destructive'}])}}>
                            <Thief width={50} height={50} />
                        </TouchableOpacity>
                    </View>
                    {myPowers[1] == 0 &&
                        <View style={{ position: 'absolute' }}>
                            <TouchableOpacity onLongPress={()=>{Alert.alert('Thief','This powerup allows you to steal the points your target made this round',[{text: 'Close',style: 'destructive'}])}}>
                                <Wrong width={50} height={50} />
                            </TouchableOpacity>
                        </View>}
                </View>
                <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
                    <View style={{ position: 'absolute' }}>
                        <TouchableOpacity  onPress={() => { powerSelectedHandler(2) }} onLongPress={()=>{Alert.alert('Block Answer','This powerup allows you to block the target from responding next round',[{text: 'Close',style: 'destructive'}])}}>
                            <Cancel width={50} height={50} />
                        </TouchableOpacity>
                    </View>
                    {myPowers[2] == 0 &&
                        <View style={{ position: 'absolute' }}>
                            <TouchableOpacity onLongPress={()=>{Alert.alert('Block Answer','This powerup allows you to block the target from responding next round',[{text: 'Close',style: 'destructive'}])}} >
                                <Wrong width={50} height={50} />
                            </TouchableOpacity>
                        </View>}
                </View>
            </View>
        </View>
    );
};


GameScreen.navigationOptions = {
    headerShown: false,
};


const styles = StyleSheet.create({
    screen: {
        flex: 1,
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        paddingTop: 50,
        backgroundColor: Colors.bckcolor
    },
    tempo: {
        flex: 1,
        borderWidth: 2,
        borderColor: Colors.bordercolor,
        borderRadius: 50,
        height: 100,
        width: 100,
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: Colors.btncolor
    },
    powerupsContainer: {
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'space-around',
        width: '100%',
        alignItems: 'center'
    },
    headerContainer: {
        flex: 1,
        flexDirection: 'row',
        alignItems: 'center',
        width: '100%',
    },
    powerText: {
        fontFamily: 'archivo-regular',
        fontSize: Dimensions.get("window").height * 0.035,
        color: "white",
    },
    headerText: {
        fontFamily: 'archivo-bold',
        fontSize: Dimensions.get("window").height * 0.06,
        color: "white",
    },
    timerText: {
        fontFamily: 'archivo-bold',
        fontSize: Dimensions.get("window").height * 0.07,
        color: "white",
    },
    sideHeaderContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center'
    },
    questionContainer: {
        flex: 1,
        backgroundColor: Colors.btncolor,
        padding: 15,
        justifyContent: 'center',
        width: '100%',
        marginTop: 25
    },
    questionText: {
        color: 'white',
        fontFamily: 'archivo-bold',
        fontSize: Dimensions.get('window').height * 0.025
    },
    answersContainer: {
        flex: 3,
        justifyContent: 'space-around',
        width: '100%'
    },
    headerTitle: {
        fontFamily: 'archivo-regular',
        fontSize: Dimensions.get('window').height * 0.025,
        color: 'white'
    },
    btnText: {
        fontSize: Dimensions.get('window').width * 0.5 * 0.12,
        fontFamily: 'archivo-regular'
    },
    btn: {
        width: Dimensions.get('window').width * 0.5,
        height: Dimensions.get('window').height * 0.05
    },
    userContainer: {
        borderColor: Colors.bordercolor,
        borderWidth: 2,
        marginVertical: 5,
        justifyContent: 'center',
        width: 300,
        alignItems: 'center',
        borderRadius: 2
    },
    userText: {
        color: 'white',
        fontFamily: 'archivo-bold',
        fontSize: Dimensions.get('window').height * 0.035,
        textAlign: 'center'
    },
});



export default withInAppNotification(GameScreen);