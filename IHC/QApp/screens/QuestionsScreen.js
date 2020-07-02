import React, { useState } from 'react';
import { View, Text, StyleSheet, FlatList, Switch, Dimensions, Alert } from 'react-native';
import Btn from '../components/Btn';
import QuestionItem from '../components/QuestionItem'
import { useSelector } from 'react-redux';
import DefaultStyles from '../constants/default-styles';
import Answer from '../models/answer'

const QuestionScreen = props => {
    const [ready, setReady] = useState(false);

    const questions = useSelector(state => state.game.questionsAdded);
    const numberQuestions = props.navigation.getParam('numberQuestions');
    const socket = props.navigation.getParam('socket');
    const roomId = props.navigation.getParam('roomId');

    const selectQuestionHandler = (itemData) => {
        if(ready){
            setReady(false)
            socket.emit('RemoveReady', { roomId: roomId });
        }
            props.navigation.navigate({
            routeName: 'AddQuestion', params: {
                questionHeader: itemData.item.question,
                arrayAnswers: itemData.item.answers,
                saved: true,
                index: itemData.index,
            }
        });
    }

    const renderQuestion = itemData => {
        return (
            <QuestionItem onSelectQuestion={() => { selectQuestionHandler(itemData) }} question={itemData.item} ></QuestionItem>
        );
    }

    const switchHandler = (ready) => {
        if (questions.length < numberQuestions)
            Alert.alert('You can\'t set ready', 'Please do all questions first!', [{ text: 'Okay', style: 'destructive' }]);
        else {
            setReady(ready);
            console.log(ready);
            if (ready) {
                socket.emit('Ready', { roomId: roomId });
                socket.on('GimmeQuestions', function (data) {
                    socket.emit('Questions', { questions: questions, roomId: roomId })
                    socket.on('GameQuestions', function (data) {
                        props.navigation.navigate({
                            routeName: 'GameScreen',
                            params: {
                                socket: socket,
                                roomId: roomId,
                                questions: data.questions,
                                usernameArray: data.usernameArray
                            }
                        });
                    })
                });
            }
            else{
                socket.emit('RemoveReady', { roomId: roomId });
            }
        }
    }

    const addButtonHandler = () => {
        props.navigation.navigate({
            routeName: 'AddQuestion', params: {
                questionHeader: "",
                arrayAnswers: ([new Answer('', false), new Answer('', false), new Answer('', false), new Answer('', false)]),
                saved: false,
                index: questions.length,
            }
        })
    }

    return (
        <View style={DefaultStyles.screen}>
            <View style={styles.btnsContainer}>
                <View style={styles.leftContainer}>
                    {questions.length < numberQuestions &&
                        <Btn onPress={addButtonHandler}>Add Question</Btn>}
                    {questions.length == numberQuestions &&
                        <Text style={styles.title}>Ready</Text>}
                    {questions.length == numberQuestions &&
                        <Switch trackColor={{ false: 'red', true: 'green' }}
                            onValueChange={(value) => switchHandler(value)}
                            value={ready}
                        />}
                </View>
                <View style={styles.rightContainer}>
                    <Text style={styles.title}>
                        {questions.length}/{numberQuestions}
                    </Text>
                </View>
            </View>
            <View style={styles.listContainer}>
                <FlatList
                    data={questions}
                    keyExtractor={(item, index) => index.toString()}
                    renderItem={renderQuestion}
                    style={{ width: '100%' }}
                />
            </View>
        </View>
    )
};

QuestionScreen.navigationOptions = {
    headerTitle: 'Questions',
};

const styles = StyleSheet.create({
    btnsContainer: {
        flex: 1,
        flexDirection: 'row',
        alignItems: 'center'
    },
    rightContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center'
    },
    listContainer: {
        flex: 4,
        width: '100%',
    },
    leftContainer: {
        flex: 2.5,
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
    },
    title: {
        fontFamily: 'archivo-bold',
        fontSize: Dimensions.get("window").height * 0.04,
        color: 'white'
    }
});

export default QuestionScreen;


